package com.yuncitys.smart.parking.common.util;

/**
 * Created by Administrator on 2018/8/9 0009.
 */

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取到的List<Map>结果集转化为JavaBean工具类
 *
 * @author suny
 * @date 2017-7-4
 * <pre>
 *  desc:
 * </pre>
 */
public class BeanUtils2<T> {

    /**
     * 根据List<Map<String, Object>>数据转换为JavaBean数据
     *
     * @param datas
     * @param beanClass
     * @return
     * @throws CommonException
     */
    public List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass) throws CommonException {
        // 返回数据集合
        List<T> list = null;
        // 对象字段名称
        String fieldname = "";
        // 对象方法名称
        String methodname = "";
        // 对象方法需要赋的值
        Object methodsetvalue = "";
        try {
            list = new ArrayList<T>();
            // 得到对象所有字段
            Field fields[] = beanClass.getDeclaredFields();
            // 遍历数据
            for (Map<String, Object> mapdata : datas) {
                // 创建一个泛型类型实例
                T t = beanClass.newInstance();
                // 遍历所有字段，对应配置好的字段并赋值
                for (Field field : fields) {
                    if (null != field) {
                        boolean fieldHasAnno = field.isAnnotationPresent(Id.class);
                        // 全部转化为大写
                        String dbfieldname = field.getName();
                        if(!dbfieldname.equals("serialVersionUID")) {
                            // 获取字段名称
                            fieldname = field.getName();
                            // 拼接set方法
                            methodname = "set" + StrUtil.capitalize(fieldname);
                            // 获取data里的对应值
                            methodsetvalue = mapdata.get(dbfieldname);
                            // 赋值给字段
                            Method m = beanClass.getDeclaredMethod(methodname, field.getType());
                            String parameterTypes = m.getParameterTypes()[0].getName();
                            if (parameterTypes.equals("java.math.BigDecimal") && methodsetvalue != null) {
                                methodsetvalue = new BigDecimal(String.valueOf(methodsetvalue));
                            }
                            m.invoke(t, methodsetvalue);
                        }
                    }
                }
                // 存入返回列表
                list.add(t);
            }
        } catch (InstantiationException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (IllegalAccessException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (parkingException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (NoSuchMethodException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (IllegalArgumentException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        } catch (InvocationTargetException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        }
        // 返回
        return list;
    }

    /**
     * 根据获取主键值
     *
     * @param entity
     * @return
     * @throws CommonException
     */
    public String getEntityPrimaryKeyValue(T entity, Class<T> beanClass) throws IllegalAccessException, InstantiationException {
        // 得到对象所有字段
        Field fields[] = beanClass.getDeclaredFields();
        // 创建一个泛型类型实例
        String value = null;
        // 遍历所有字段，对应配置好的字段并赋值
        for (Field field : fields) {
            if (null != field) {
                boolean fieldHasAnno = field.isAnnotationPresent(Id.class);
                // 获取字段名称
                String fieldname = field.getName();
                if (fieldHasAnno) {
                    Object keyValue = PropertyUtil.invokeGet(entity, fieldname);
                    value = String.valueOf(keyValue);
                }
            }
        }
        return value;
    }

}
