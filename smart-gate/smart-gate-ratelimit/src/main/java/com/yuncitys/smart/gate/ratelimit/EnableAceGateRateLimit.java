package com.yuncitys.smart.gate.ratelimit;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by smart on 2017/9/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RateLimitAutoConfiguration.class)
@Documented
@Inherited
public @interface EnableAceGateRateLimit {
}
