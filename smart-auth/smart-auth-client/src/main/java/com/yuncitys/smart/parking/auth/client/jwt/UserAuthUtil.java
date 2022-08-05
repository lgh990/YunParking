package com.yuncitys.smart.parking.auth.client.jwt;

import com.alibaba.fastjson.JSON;
import com.yuncitys.smart.parking.auth.client.config.UserAuthConfig;
import com.yuncitys.smart.parking.auth.common.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.auth.common.util.jwt.JWTHelper;
import com.yuncitys.smart.parking.common.exception.auth.NonLoginException;
import com.yuncitys.smart.parking.common.util.RedisKeyUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by smart on 2017/9/15.
 */
@Configuration
public class UserAuthUtil {

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JWTHelper jwtHelper;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        IJWTInfo infoFromToken=null;
        try {

            try {
                infoFromToken = jwtHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
            }catch (Exception ex){
                 return infoFromToken;
                //throw new NonLoginException("用户会话已过期!");
            }
            if(redisTemplate.hasKey(RedisKeyUtil.buildUserDisableKey(infoFromToken.getId(),infoFromToken.getExpireTime()))){
                return infoFromToken;
                //throw new NonLoginException("User token is invalid!");
            }
            if(new DateTime(infoFromToken.getExpireTime()).plusMinutes(userAuthConfig.getTokenLimitExpire()).isBeforeNow()){
                redisTemplate.opsForValue().set(RedisKeyUtil.buildUserDisableKey(infoFromToken.getId(),infoFromToken.getExpireTime()), "1");
                redisTemplate.delete(RedisKeyUtil.buildUserAbleKey(infoFromToken.getId(), infoFromToken.getExpireTime()));
                throw new NonLoginException("用户会话已过期!");
            }
            return infoFromToken;
        } catch (ExpiredJwtException ex) {
            return infoFromToken;
            //throw new NonLoginException("用户会话已过期!");
        } catch (SignatureException ex) {
            return infoFromToken;
            //throw new NonLoginException("User token signature error!");
        } catch (IllegalArgumentException ex) {
            return infoFromToken;
            //throw new NonLoginException("User token is null or empty!");
        }
    }


}
