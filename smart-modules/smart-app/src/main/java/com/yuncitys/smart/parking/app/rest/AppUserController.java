package com.yuncitys.smart.parking.app.rest;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.app.biz.AppUserBiz;
import com.yuncitys.smart.parking.app.entity.AppUser;
import com.yuncitys.smart.parking.app.feign.ToolServerFeign;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.constant.MqttTopicConstants;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.constant.SMSConstants;
import com.yuncitys.smart.parking.common.constant.iotexper.SysConstant;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.app.sms.SendMsg;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
@RequestMapping("appUser")
@CheckClientToken
@CheckUserToken
@Api(tags = "APP用户注册与登陆")
public class AppUserController {
    @Autowired
    private AppUserBiz appUserBiz;
    @Autowired
    private ToolServerFeign toolServerFeign;
   /* @Autowired
    private MessageBiz messageBiz;
    @Autowired
    private IOTExperServerFeign iOTExperServerFeign;*/
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();
    private final static String REG_PHONE = "^1[0-9]{10}$";

    /**
     * 注册
     */
    @PostMapping("register")
    @IgnoreUserToken
    public ObjectRestResponse register(String mobile, String password) {
        boolean flgphone = Pattern.matches(REG_PHONE, mobile);
        if (!flgphone) {
            throw new BusinessException("手机号不合法!");
        }
        AppUser userByMobile = this.appUserBiz.getUserByMobile(mobile);
        if (userByMobile != null) {
            throw new BusinessException("手机号已经存在!");
        }
        AppUser appuser = new AppUser();
        appuser.setMobile(mobile);
        appuser.setPassword(password);
        appuser.setAuthType(SysConstant.AUTH_TYPE.PWD_AUTH_TYPE);
        appuser.setUserType(SysConstant.user_type.common);
        appuser.setIsSettingPwd(SysConstant.IS_SETTING_PWD.YES);
        appUserBiz.insertSelective(appuser);
        redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_PWD_KEY + mobile, password);
        //消息推送
        //sendMsg(mobile);
        return ObjectRestResponse.ok(appuser);
    }

    /**
     * 获取短信验证码 接口：getSmsCode
     *
     * @param mobile 手机
     * @param type   发送类型
     * @return
     * @throws Exception
     */
    @PostMapping("getSmsCode")
    @IgnoreUserToken
    public ObjectRestResponse getSmsCode(String mobile, String type) throws Exception {
        System.out.println("==================进入app模块");
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException("手机号码为空!");
        }else if(!Pattern.matches(REG_PHONE, mobile)){
            throw new BusinessException("手机号不合法!");
        }
        if (StringUtils.isEmpty(type)) {
            throw new BusinessException("短信模版类型为空!");
        }
        int code = (int) (Math.random() * 9000 + 1000);
        /**
         * 注册手机短信
         */
        ObjectRestResponse codeMessage = SendMsg.send(mobile, String.valueOf(code), type);
        if (codeMessage.getData().equals("OK")) {

            //存放值到redis
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY + "_" + SMSConstants.getTempCode(type) + "_" + mobile, String.valueOf(code), 10, TimeUnit.MINUTES);
        }
        return new ObjectRestResponse();
    }

    /**
     * 获取用户信息
     */
    @PostMapping("info")
    public ObjectRestResponse info() {
        String mobile = BaseContextHandler.getName();
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException("用户信息获取失败，请重新登陆!");
        }
        AppUser userByMobile = appUserBiz.getUserByMobile(BaseContextHandler.getName());
        return ObjectRestResponse.ok(userByMobile);
    }

    /**
     * 修改密码
     */
    @PostMapping("changePassword")
    public ObjectRestResponse changePassword(String firstPassword, String secondPassword, String smsCode) {
        if (StringUtils.isEmpty(smsCode)) {
            throw new BusinessException("验证码为空!");
        }
        String smsCodeRedis = redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY + "_" + SMSConstants.TEMPLATECODE.RESETTING_PWD + "_" + BaseContextHandler.getUsername());
        if (StringUtils.isEmpty(smsCodeRedis)) {
            throw new BusinessException("验证码失效!");
        }
        if (!smsCodeRedis.equals(smsCode)) {
            throw new BusinessException("验证码不匹配!");
        }
        if (!firstPassword.equals(secondPassword)) {
            throw new BusinessException("两次密码输入不匹配!");
        }
        Boolean rs = appUserBiz.changePassword(secondPassword);
        if (rs) {
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_PWD_KEY + BaseContextHandler.getUsername(), secondPassword);
        } else {
            throw new BusinessException("修改失败!");
        }
        //消息推送
        //sendMsg(BaseContextHandler.getUsername());
        return ObjectRestResponse.ok(rs);
    }

    /**
     * 设置密码
     */
    @PostMapping("settingPassword")
    public ObjectRestResponse settingPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new BusinessException("密码不能设置为空!");
        }
        Boolean rs = appUserBiz.changePassword(password);
        if (rs) {
            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_APP_USER_PWD_KEY + BaseContextHandler.getUsername(), password);
        } else {
            throw new BusinessException("修改失败!");
        }
        //消息通知
        //sendMsg(BaseContextHandler.getUsername());
        return ObjectRestResponse.ok(rs);
    }

    /**
     * 设置密码
     */
    @PostMapping("changeMobile")
    public ObjectRestResponse changeMobile(String mobile,String smsCode,String type) {
        AppUser params=new AppUser();
        params.setMobile(mobile);
        AppUser appUser=appUserBiz.selectOne(params);
        if (appUser!=null) {
            throw new BusinessException("用户已存在，无法进行修改!");
        }
        AppUser user = appUserBiz.selectById(BaseContextHandler.getUserID());
        String redisCode=redisTemplate.opsForValue().get(RedisKeyConstants.REDIS_APP_USER_SMS_CODE_KEY + "_" + SMSConstants.getTempCode(type) + "_" + user.getMobile());
        if (!smsCode.equals(redisCode)) {
            throw new BusinessException("验证码错误!");
        }
        user.setMobile(mobile);
        user.setName(mobile);
        appUserBiz.updateById(user);
        return ObjectRestResponse.ok(user);
    }


}
