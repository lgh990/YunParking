package com.yuncitys.smart.parking.gate.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.codingapi.tx.datasource.relational.LCNTransactionDataSource;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 代理数据源
 * @author smart
 *@version 2022/1/18.
 */
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@Configuration
public class TxConfiguration {

    @Bean("lcnTransactionDataSource")
    @ConfigurationProperties("spring.datasource.druid")
    @Primary
    public LCNTransactionDataSource dataSource(Environment env) {
        LCNTransactionDataSource dataSourceProxy = new LCNTransactionDataSource();
        dataSourceProxy.setDataSource(createDataSource(env));
        dataSourceProxy.setMaxCount(10);
        return dataSourceProxy;
    }

    public DataSource createDataSource(Environment env) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        if(dataSource.getUsername() == null) {
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
        }

        if(dataSource.getPassword() == null) {
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
        }

        if(dataSource.getUrl() == null) {
            dataSource.setUrl(env.getProperty("spring.datasource.url"));
        }

        if(dataSource.getDriverClassName() == null) {
            dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        }

        if(!"false".equals(env.getProperty("spring.datasource.druid.StatViewServlet.enabled"))) {
            try {
                dataSource.setFilters("stat");
            } catch (SQLException var4) {
                var4.printStackTrace();
            }
        }
        return dataSource;
    }
}
