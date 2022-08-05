package com.yuncitys.smart.parking.app.oauth.service;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.app.biz.AppUserBiz;
import com.yuncitys.smart.parking.app.entity.AppUser;
import com.yuncitys.smart.parking.app.oauth.bean.OauthUser;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.constant.SMSConstants;
import com.yuncitys.smart.parking.common.constant.iotexper.SysConstant;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.parking.core.GrantedAuthority;
import org.springframework.parking.core.authority.SimpleGrantedAuthority;
import org.springframework.parking.core.userdetails.UserDetails;
import org.springframework.parking.core.userdetails.UserDetailsService;
import org.springframework.parking.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by smart on 2017/8/11.
 */
@Component
@Slf4j
public class OauthUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserBiz userService;
    /*@Autowired
    private MessageBiz messageBiz;
    @Autowired
    private IOTExperServerFeign iOTExperServerFeign;*/
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();

    /**
     * APP短信验证
     *
     * @param paramMap
     * @return
     * @throws UsernameNotFoundException
     */
    public void verificationCodeClient(Map<String, String> paramMap) {
        String mobile = paramMap.get("username");
        String password = paramMap.get("password");
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException("手机号为空!");
        }
        //登陆类型 sms：短信登陆
        String authType = paramMap.get("auth_type");
        AppUser appUser = userService.getUserByMobile(mobile);
        if(authType.equals(SysConstant.AUTH_TYPE.PWD_AUTH_TYPE) && appUser==null){
            throw new UsernameNotFoundException("用户不存在，请进行注册!");
        }
        if(authType.equals(SysConstant.AUTH_TYPE.PWD_AUTH_TYPE)  && !appUser.getPassword().equals(encoder.encode(password))){
            throw new UsernameNotFoundException("密码有误，请重新输入!");
        }
        if (authType.equals(SysConstant.AUTH_TYPE.SMS_AUTH_TYPE)) {
            if (StringUtils.isBlank(password)) {
                throw new BusinessException("验证码为空!");
            }
            //验证验证码
            String smsCodeRedis = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY + "_" + SMSConstants.TEMPLATECODE.LOGIN + "_" + mobile);
            if (StringUtils.isBlank(smsCodeRedis)) {
                throw new BusinessException("无效验证码!");
            } else if (!smsCodeRedis.equals(password)) {
                throw new BusinessException("验证码有误!");
            }
            //创建一个用户
            if (appUser == null) {
                appUser = new AppUser();
                appUser.setAuthType(SysConstant.AUTH_TYPE.SMS_AUTH_TYPE);
                appUser.setName(mobile);
                appUser.setMobile(mobile);
                appUser.setPassword(password);
                appUser.setUserType(SysConstant.user_type.common);
                appUser.setIsSettingPwd(SysConstant.IS_SETTING_PWD.NO);
                userService.insertSelective(appUser);
                redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_PWD_KEY + mobile, password);
            }
        }
        if(null == appUser){
            appUser = new AppUser();
        }
        appUser.setIsSettingPwd(SysConstant.IS_SETTING_PWD.YES);
        appUser.setAuthType(authType);
        userService.updateSelectiveById(appUser);

    }


    @Override
    public UserDetails loadUserByUsername(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException("手机号为空!");
        }
        AppUser appUser = userService.getUserByMobile(mobile);
        if (appUser == null) {
            throw new BusinessException("手机号不存在!");
        }
        if (appUser.getAuthType().equals(SysConstant.AUTH_TYPE.SMS_AUTH_TYPE)) {
            String pwdR = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_PWD_KEY + mobile);
            if (StringUtils.isNotEmpty(pwdR)) {
                appUser.setPassword(encoder.encode(pwdR));
            } else {
                throw new BusinessException("密码为空，请重新设置!");
            }
        }
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("guest"));
        OauthUser ou = new OauthUser(appUser.getId().toString(), appUser.getMobile(), appUser.getPassword(), appUser.getName(), appUser.getTenantId(), appUser.getIsSettingPwd(), appUser.getUserType(), authorities);
        if (null != ou) {
            redisTemplate.delete(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY + mobile);
        }
        //消息推送
        //sendMsg(mobile);
        return ou;
    }

    /**
     * 消息推送
     * @param mobile
     */
   /* @Async
    public void sendMsg(String mobile){
        messageBiz.processMqttMsg(SysConstant.PUSH_MSG_CONTENT.LOGIN_MSG_TYPE, mobile);
    }*/
}
