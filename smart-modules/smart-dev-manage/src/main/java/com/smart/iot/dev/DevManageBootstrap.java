package com.smart.iot.dev;

import com.yuncitys.smart.merge.EnableAceMerge;
import com.yuncitys.smart.parking.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author smart
 * @version 2022/12/26.
 */
@EnableEurekaClient
// 开启事务
@EnableTransactionManagement
// 开启熔断监控
@EnableCircuitBreaker
// 开启服务鉴权
@EnableFeignClients({"com.yuncitys.smart.parking.auth.client.feign","com.smart.iot.dev.feign"})
@MapperScan("com.smart.iot.dev.mapper")
@SpringBootApplication(scanBasePackages = {"com.smart.iot.dev.*"})
@EnableAceAuthClient
@EnableSwagger2Doc
@EnableAceMerge

public class DevManageBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DevManageBootstrap.class).web(true).run(args);
    }

    /*@Bean
    public EmbeddedServletContainerFactory servletContainer() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                parkingConstraint parkingConstraint = new parkingConstraint();
                parkingConstraint.setUserConstraint("CONFIDENTIAL");
                parkingCollection collection = new parkingCollection();
                collection.addPattern("/*");
                parkingConstraint.addCollection(collection);
                context.addConstraint(parkingConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(80);
        connector.setRedirectPort(443);
        connector.setSecure(false);
        return connector;
    }*/
}
