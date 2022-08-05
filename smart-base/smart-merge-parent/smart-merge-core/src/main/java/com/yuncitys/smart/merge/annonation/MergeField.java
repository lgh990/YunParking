package com.yuncitys.smart.merge.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author smart
 * @create 2018/2/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
public @interface MergeField {
    /**
     * 查询值
     * @return
     */
    String key() default "";

    /**
     * 目标类
     * @return
     */
    Class<? extends Object> feign() default Object.class;

    /**
     * 调用方法
     * @return
     */
    String method() default "";

    /**
     * 是否以属性值合并作为查询值
     * @return
     */
    boolean isValueNeedMerge() default false;
}
