package com.yuncitys.smart.merge.annonation;

import com.yuncitys.smart.merge.facade.DefaultMergeResultParser;
import com.yuncitys.smart.merge.facade.IMergeResultParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author smart
 * @create 2018/2/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface MergeResult {
    Class<? extends IMergeResultParser> resultParser() default DefaultMergeResultParser.class;
}
