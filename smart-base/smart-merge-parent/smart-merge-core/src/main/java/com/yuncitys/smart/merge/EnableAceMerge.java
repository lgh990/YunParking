package com.yuncitys.smart.merge;

import com.yuncitys.smart.merge.configuration.MergeAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author smart
 * @create 2018/2/3.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MergeAutoConfiguration.class})
public @interface EnableAceMerge {
}
