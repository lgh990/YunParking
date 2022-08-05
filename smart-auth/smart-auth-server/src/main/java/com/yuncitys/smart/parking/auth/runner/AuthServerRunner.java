package com.yuncitys.smart.parking.auth.runner;

import com.alibaba.fastjson.JSON;
import com.yuncitys.ag.core.util.RsaKeyHelper;
import com.yuncitys.smart.parking.auth.configuration.KeyConfiguration;
import com.yuncitys.smart.parking.auth.jwt.AECUtil;
import com.yuncitys.smart.parking.auth.module.client.biz.GatewayRouteBiz;
import com.yuncitys.smart.parking.auth.module.client.entity.GatewayRoute;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author smart
 * @version 2022/12/17.
 */
@Configuration
@Slf4j
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private RsaKeyHelper rsaKeyHelper;

    @Autowired
    private GatewayRouteBiz gatewayRouteBiz;

    @Override
    public void run(String... args) throws Exception {
        boolean flag = false;
        if (redisTemplate.hasKey(RedisKeyConstants.REDIS_USER_PRI_KEY)&&redisTemplate.hasKey(RedisKeyConstants.REDIS_USER_PUB_KEY)){
            try {
                keyConfiguration.setUserPriKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_USER_PRI_KEY).toString()));
                keyConfiguration.setUserPubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_USER_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化用户公钥/密钥异常...",e);
                flag = true;
            }
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_USER_PRI_KEY, rsaKeyHelper.toHexString(keyMap.get("pri")));
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成用户公钥/密钥的初始化...");
        flag = false;
        if (redisTemplate.hasKey(RedisKeyConstants.REDIS_SERVICE_PRI_KEY)&&redisTemplate.hasKey(RedisKeyConstants.REDIS_SERVICE_PUB_KEY)) {
            try {
                keyConfiguration.setServicePriKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_SERVICE_PRI_KEY).toString()));
                keyConfiguration.setServicePubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_SERVICE_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化服务公钥/密钥异常...",e);
                flag = true;
            }
        } else {
            flag = true;
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getServiceSecret());
            keyConfiguration.setServicePriKey(keyMap.get("pri"));
            keyConfiguration.setServicePubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_SERVICE_PRI_KEY, rsaKeyHelper.toHexString(keyMap.get("pri")));
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_SERVICE_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成服务公钥/密钥的初始化...");
        List<GatewayRoute> gatewayRoutes = gatewayRouteBiz.selectListAll();
        redisTemplate.opsForValue().set(RedisKeyConstants.ZUUL_ROUTE_KEY, JSON.toJSONString(gatewayRoutes));
        log.info("完成网关路由的更新...");
    }
}
