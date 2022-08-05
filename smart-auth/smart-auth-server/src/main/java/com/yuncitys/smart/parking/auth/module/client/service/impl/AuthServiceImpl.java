package com.yuncitys.smart.parking.auth.module.client.service.impl;

import com.yuncitys.ag.core.constants.CommonConstants;
import com.yuncitys.smart.parking.auth.common.util.jwt.IJWTInfo;
import com.yuncitys.smart.parking.auth.common.util.jwt.JWTInfo;
import com.yuncitys.smart.parking.auth.feign.IUserService;
import com.yuncitys.smart.parking.auth.jwt.user.JwtTokenUtil;
import com.yuncitys.smart.parking.auth.module.client.service.AuthService;
import com.yuncitys.smart.parking.common.exception.auth.UserInvalidException;
import com.yuncitys.smart.parking.common.util.RedisKeyUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author smart
 */
@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(String username, String password) throws Exception {
        Map<String, String> data = userService.validate(username, password).getData();
        String token = "";
        if (!StringUtils.isEmpty(data.get("id"))) {
            Map<String, String> map = new HashMap<>();
            map.put(CommonConstants.JWT_KEY_TENANT_ID, String.valueOf(data.get("tenantId")));
            map.put(CommonConstants.JWT_KEY_DEPART_ID, String.valueOf(data.get("departId")));
            JWTInfo jwtInfo = new JWTInfo(data.get("username"), data.get("id"), data.get("name"));
            Date expireTime = DateTime.now().plusSeconds(jwtTokenUtil.getExpire()).toDate();
            token = jwtTokenUtil.generateToken(jwtInfo, map, expireTime);
            redisTemplate.opsForValue().set(RedisKeyUtil.buildUserAbleKey(data.get("id"), expireTime), "1");
            return token;
        }
        throw new UserInvalidException("用户不存在或账户密码错误!");
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public Boolean invalid(String token) throws Exception {
        IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
        redisTemplate.delete(RedisKeyUtil.buildUserAbleKey(infoFromToken.getId(), infoFromToken.getExpireTime()));
        redisTemplate.opsForValue().set(RedisKeyUtil.buildUserDisableKey(infoFromToken.getId(), infoFromToken.getExpireTime()), "1");
        return true;
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(oldToken);
        invalid(oldToken);
        Date expireTime = DateTime.now().plusSeconds(jwtTokenUtil.getExpire()).toDate();
        redisTemplate.opsForValue().set(RedisKeyUtil.buildUserAbleKey(infoFromToken.getId(), expireTime), "1");
        return jwtTokenUtil.generateToken(infoFromToken, infoFromToken.getOtherInfo(), expireTime);
    }
}
