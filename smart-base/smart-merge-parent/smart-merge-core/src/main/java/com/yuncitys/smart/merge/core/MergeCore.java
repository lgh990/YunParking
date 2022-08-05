package com.yuncitys.smart.merge.core;

import com.yuncitys.smart.merge.annonation.MergeField;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.configuration.MergeProperties;
import com.yuncitys.smart.merge.facade.DefaultMergeResultParser;
import com.yuncitys.smart.merge.facade.IMergeResultParser;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author smart
 * @create 2018/2/2.
 */
@Slf4j
public class MergeCore {

    private Map<String, MergeField> mergeFieldMap;
    private ListeningExecutorService backgroundRefreshPools;
    private LoadingCache<String, Map<String, String>> caches;

    public MergeCore(MergeProperties mergeProperties) {
        this.backgroundRefreshPools =
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(mergeProperties.getGuavaCacheRefreshThreadPoolSize()));
        this.mergeFieldMap = new HashMap<String, MergeField>();
        this.caches = CacheBuilder.newBuilder()
                .maximumSize(mergeProperties.getGuavaCacheNumMaxSize())
                .refreshAfterWrite(mergeProperties.getGuavaCacheRefreshWriteTime(), TimeUnit.MINUTES)
                .build(new CacheLoader<String, Map<String, String>>() {
                    @Override
                    public Map<String, String> load(String key) throws Exception {
                        log.debug("首次读取缓存: " + key);
                        MergeField mergeField = mergeFieldMap.get(key);
                        Object bean = BeanFactoryUtils.getBean(mergeField.feign());
                        Method method = mergeField.feign().getMethod(mergeField.method(), String.class);
                        Map<String, String> invoke = (Map<String, String>) method.invoke(bean, mergeField.key());
                        return invoke;
                    }

                    @Override
                    public ListenableFuture<Map<String, String>> reload(final String key,
                                                                        Map<String, String> oldValue) throws Exception {
                        return backgroundRefreshPools.submit(() -> {
                            log.debug("异步刷新缓存: " + key);
                            MergeField mergeField = mergeFieldMap.get(key);
                            Object bean = BeanFactoryUtils.getBean(mergeField.feign());
                            Method method = mergeField.feign().getMethod(mergeField.method(), String.class);
                            Map<String, String> invoke = (Map<String, String>) method.invoke(bean, mergeField.key());
                            return invoke;
                        });
                    }
                });

    }


    /**
     * aop方式加工
     *
     * @param pjp
     * @param anno
     * @return
     * @throws Throwable
     */
    public Object mergeData(ProceedingJoinPoint pjp, MergeResult anno) throws Throwable {
        Object proceed = pjp.proceed();
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method m = signature.getMethod();
            ParameterizedType parameterizedType = (ParameterizedType) m.getGenericReturnType();
            Type rawType = parameterizedType.getRawType();
            List<?> result = null;
            // 获取当前方法的返回值
            Type[] types = parameterizedType.getActualTypeArguments();
            Class clazz = ((Class) types[0]);
            // 非list直接返回
            if (anno.resultParser().equals(DefaultMergeResultParser.class) && ((Class) rawType).isAssignableFrom(List.class)) {
                result = (List<?>) proceed;
                mergeResult(clazz, result);
                return result;
            } else {
                IMergeResultParser bean = BeanFactoryUtils.getBean(anno.resultParser());
                result = bean.parser(proceed);
                mergeResult(clazz, result);
                return proceed;
            }
        } catch (Exception e) {
            log.error("某属性数据聚合失败", e);
            return proceed;
        }

    }

    /**
     * 手动调用进行配置合并
     *
     * @param clazz
     * @param result
     * @throws ExecutionException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void mergeResult(Class clazz, List<?> result) throws ExecutionException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (null != result && result.size()>0){
            Field[] fields = clazz.getDeclaredFields();
            List<Field> mergeFields = new ArrayList<Field>();
            Map<String, Map<String, String>> invokes = new HashMap<>();
            String className = clazz.getName();
            // 获取属性
            for (Field field : fields) {
                MergeField annotation = field.getAnnotation(MergeField.class);
                if (annotation != null) {
                    mergeFields.add(field);
                    String args = annotation.key();
                    // 表示该属性需要将值聚合成条件远程查询
                    if (annotation.isValueNeedMerge()) {
                        StringBuffer sb = new StringBuffer("");
                        Set<String> ids = new HashSet<>();
                        result.stream().forEach(obj -> {
                            field.setAccessible(true);
                            Object o = null;
                            try {
                                o = field.get(obj);
                                if (o != null) {
                                    if (!ids.contains(o)) {
                                        ids.add(o.toString());
                                        sb.append(o.toString()).append(",");
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                log.error("数据属性加工失败:" + field, e);
                                throw new RuntimeException("数据属性加工失败:" + field, e);
                            }

                        });
                        if(sb.length() != 0) {
                            args = sb.substring(0, sb.length() - 1);
                        }
                    } else {
                        String key = className + field.getName();
                        mergeFieldMap.put(key, annotation);
                        // 从缓存获取
                        Map<String, String> value =
                                (Map<String, String>) caches.get(key);
                        if (value != null) {
                            invokes.put(field.getName(), value);
                            continue;
                        }
                    }
                    Object bean = BeanFactoryUtils.getBean(annotation.feign());
                    Method method = annotation.feign().getMethod(annotation.method(), String.class);
                    Map<String, String> value = (Map<String, String>) method.invoke(bean, args);
                    invokes.put(field.getName(), value);
                }
            }
            result.stream().forEach(obj -> {
                mergeObjFieldValue(obj, mergeFields, invokes);
            });
        }
    }

    /**
     * 手动对单个结果进行配置合并
     *
     * @param clazz
     * @param mergeObj
     * @throws ExecutionException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void mergeOne(Class clazz, Object mergeObj) throws ExecutionException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> mergeFields = new ArrayList<Field>();
        Map<String, Map<String, String>> invokes = new HashMap<>();
        String className = clazz.getName();
        // 获取属性
        for (Field field : fields) {
            MergeField annotation = field.getAnnotation(MergeField.class);
            if (annotation != null) {
                mergeFields.add(field);
                String args = annotation.key();
                // 表示该属性需要将值聚合成条件远程查询
                if (annotation.isValueNeedMerge()) {
                    field.setAccessible(true);
                    Object o = null;
                    try {
                        o = field.get(mergeObj);
                    } catch (IllegalAccessException e) {
                        log.error("数据属性加工失败:" + field, e);
                        throw new RuntimeException("数据属性加工失败:" + field, e);
                    }
                    if (o != null) {
                        args = o.toString();
                    }
                } else {
                    String key = className + field.getName();
                    mergeFieldMap.put(key, annotation);
                    // 从缓存获取
                    Map<String, String> value =
                            (Map<String, String>) caches.get(key);
                    if (value != null) {
                        invokes.put(field.getName(), value);
                        continue;
                    }
                }
                Object bean = BeanFactoryUtils.getBean(annotation.feign());
                Method method = annotation.feign().getMethod(annotation.method(), String.class);
                Map<String, String> value = (Map<String, String>) method.invoke(bean, args);
                invokes.put(field.getName(), value);
            }
        }
        mergeObjFieldValue(mergeObj, mergeFields, invokes);
    }

    /**
     * 合并对象属性值
     * @param mergeObj
     * @param mergeFields
     * @param invokes
     */
    private void mergeObjFieldValue(Object mergeObj, List<Field> mergeFields, Map<String, Map<String, String>> invokes) {
        for (Field field : mergeFields) {
            field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(mergeObj);
                if (o != null && invokes.get(field.getName()).containsKey(String.valueOf(o))) {
                    field.set(mergeObj, invokes.get(field.getName()).get(o.toString()));
                }
            } catch (IllegalAccessException e) {
                log.error("数据属性加工失败:" + field, e);
                throw new RuntimeException("数据属性加工失败:" + field, e);
            }
        }
    }
}
