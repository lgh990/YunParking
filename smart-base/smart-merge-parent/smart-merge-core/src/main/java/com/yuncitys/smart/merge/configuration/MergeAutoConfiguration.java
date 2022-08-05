package com.yuncitys.smart.merge.configuration;

import com.yuncitys.smart.merge.core.BeanFactoryUtils;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.merge.facade.DefaultMergeResultParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author smart
 * @create 2018/2/3.
 */
@Configuration
@ComponentScan("com.yuncitys.smart.merge.aspect")
@ConditionalOnProperty(name = "merge.enabled", matchIfMissing = false)
public class MergeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MergeProperties mergeProperties() {
        return new MergeProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanFactoryUtils beanFactoryUtils() {
        return new BeanFactoryUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public MergeCore mergeCore() {
        return new MergeCore(mergeProperties());
    }

    @Bean
    public DefaultMergeResultParser defaultMergeResultParser() {
        return new DefaultMergeResultParser();
    }
}
