package com.yuncitys.smart.oss.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yuncitys.smart.oss.cloud.OSSFactory;
import com.yuncitys.smart.oss.sms.SendMsg;
import com.yuncitys.smart.parking.common.constant.SMSConstants;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * 短信
 *
 * @author cyd
 */
@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {
    @Autowired
    private SendMsg sendMsg;
    //T产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI3OZEAx06INin";
    static final String accessKeySecret = "4SzfbebI24IuN1d3Lr8FlM7ReU3wXW";

    /**
     * 发送
     * @param phone 手机号码
     * @param code  验证码
     * @param templateType  短信模版类型
     */
    @RequestMapping("/send")
    public ObjectRestResponse send(@RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("templateType") String templateType) throws Exception {
        return ObjectRestResponse.ok(sendSms(phone, code, templateType));
    }

    /**
     * @param phone 手机号码
     * @param code  验证码
     * @param templateType  短信模版类型
     */
    public static String sendSms(String phone, String code, String templateType) {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(SMSConstants.SMS_SIGNNAME);
        request.setTemplateCode(SMSConstants.getTempCode(templateType));
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------"+code);
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    public static IAcsClient setSystem() throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return acsClient;
    }
}
