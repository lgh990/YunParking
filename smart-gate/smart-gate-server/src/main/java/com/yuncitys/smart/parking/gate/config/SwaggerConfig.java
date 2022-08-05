package com.yuncitys.smart.parking.gate.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smart
 * @create 2018/2/7.
 */
@Component
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("管理服务", "/api/admin/v2/api-docs", "2.0"));
        resources.add(swaggerResource("字典服务", "/api/dict/v2/api-docs", "2.0"));
        resources.add(swaggerResource("鉴权服务", "/api/auth/v2/api-docs", "2.0"));
        resources.add(swaggerResource("体验馆服务", "/api/iotexper/v2/api-docs", "2.0"));
        resources.add(swaggerResource("体验馆APP服务", "/api/iotexperapp/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
