package com.yuncitys.smart.parking.common.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author smart
 * @create 2018/2/11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface Depart {
    String departField() default "depart_id";
    String userField() default "crt_user_id";
}
