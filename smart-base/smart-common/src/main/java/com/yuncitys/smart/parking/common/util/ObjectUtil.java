package com.yuncitys.smart.parking.common.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 * @说明 对象操纵高级方法
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class ObjectUtil {
    /**
     * 返回一个对象的属性和属性值
     */
    @SuppressWarnings("unchecked")
    public static Map<String,String> getProperty(Object entityName) {
        Map<String,String> map = new HashMap<String, String>();
        try {
            Class c = entityName.getClass();
            // 获得对象属性
            Field field[] = c.getDeclaredFields();
            for (Field f : field) {
                Object v = invokeMethod(entityName, f.getName(), null);
                map.put(f.getName(), v.toString());
            }
        } catch (Exception e) {
            map = null;
        }
        return map;
    }
    /**
     * 获得对象属性的值
     */
    @SuppressWarnings("unchecked")
    private static Object invokeMethod(Object owner, String methodName,
            Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        methodName = methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
        Method method = null;
        try {
            method = ownerClass.getMethod("get" + methodName);
        } catch (parkingException e) {
        } catch (NoSuchMethodException e) {
            return " can't find 'get" + methodName + "' method";
        }
        return method.invoke(owner);
    }
}
