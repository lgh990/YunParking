package com.yuncitys.smart.parking.app.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yuncitys.smart.parking.common.constant.SMSConstants;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SendMsg {

    //T产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI3OZEAx06INin";
    static final String accessKeySecret = "4SzfbebI24IuN1d3Lr8FlM7ReU3wXW";

    private static Logger logger = Logger.getLogger(SendMsg.class);




    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("13636102641");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
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




    public static ObjectRestResponse send( String phone,String code, String templateType) throws Exception {
        return ObjectRestResponse.ok(sendSms(phone, code, templateType));
    }


    /**
     * 快捷登录
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String sendSms(String phone, String code,String TemplateCode)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(SMSConstants.SMS_SIGNNAME);
        request.setTemplateCode(SMSConstants.getTempCode(TemplateCode));
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    /**
     * 重置密码
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String upd(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("云创智城");
        request.setTemplateCode("SMS_78705051");
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }



    /**
     * 更换手机号码
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String upd_phonenum(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("云创智城");
        request.setTemplateCode("SMS_78705050");
        request.setTemplateParam("{code:'" + code + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }


}
