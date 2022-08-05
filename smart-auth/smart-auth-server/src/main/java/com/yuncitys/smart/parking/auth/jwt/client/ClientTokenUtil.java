package com.yuncitys.smart.parking.auth.jwt.client;

import com.yuncitys.smart.parking.auth.common.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.auth.common.util.jwt.JWTHelper;
import com.yuncitys.smart.parking.auth.configuration.KeyConfiguration;
import com.yuncitys.smart.parking.common.exception.auth.ClientTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * Created by smart on 2017/9/10.
 */
@Configuration
public class ClientTokenUtil {
    private Logger logger = LoggerFactory.getLogger(ClientTokenUtil.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${client.expire}")
    private int expire;
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private KeyConfiguration keyConfiguration;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return jwtHelper.generateToken(jwtInfo, keyConfiguration.getServicePriKey()+"", expire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        IJWTInfo infoFromToken = jwtHelper.getInfoFromToken(token, keyConfiguration.getServicePubKey());
        Date current = infoFromToken.getExpireTime();
        if(new Date().after(current)){
            throw new ClientTokenException("Client token expired!");
        }
        return infoFromToken;
    }

}
