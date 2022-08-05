package com.smart.iot.parking.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class SendMsg{

    //T产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    static final String autograph = "云创智城停车";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI3OZEAx06INin";
    static final String accessKeySecret = "4SzfbebI24IuN1d3Lr8FlM7ReU3wXW";


    public static SendSmsResponse sendSms() throws ClientException {

        IAcsClient acsClient = setSystem();

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers("13636102641");
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(autograph);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_78705054");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"123\"}");


        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }





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

    public static IAcsClient singlecall_setSystem() throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");


        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dyvmsapi", "dyvmsapi.aliyuncs.com");
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return acsClient;
    }



    public static void main(String[] args) throws ClientException, InterruptedException {

        //发短信
        SingleCallByTtsRequest response = singlecall("18503028505","粤A12345");
      /*  System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }*/

    }


    /**
     * 语音通知挪车
     *
     * @param phone 手机
     * @param platenum 车牌号
     * */

    public static SingleCallByTtsRequest singlecall(String phone, String platenum) throws ClientException {
        IAcsClient acsClient = null;
        try {
            acsClient = singlecall_setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber("075536567796");
        //必填-被叫号码
        request.setCalledNumber(phone);
        //必填-Tts模板ID
        request.setTtsCode("TTS_109425466");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam("{\"platenum\":\""+platenum+"\"}");
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
        if(singleCallByTtsResponse.getCode() != null && "OK".equals(singleCallByTtsResponse.getCode())) {
            //请求成功
            System.out.println("语音文本外呼---------------");
            System.out.println("RequestId=" + singleCallByTtsResponse.getRequestId());
            System.out.println("Code=" + singleCallByTtsResponse.getCode());
            System.out.println("Message=" + singleCallByTtsResponse.getMessage());
            System.out.println("CallId=" + singleCallByTtsResponse.getCallId());
        }
        return request;
    }


    /**
     * 快捷登录
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String reg(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78705052");
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    /**
     * 快捷登录
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String quick_login(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78705054");
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }


    /**
     * 免密支付
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String no_pay(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_111795532");
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
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
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78705051");
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }
    /**
     * 车子开走给用户的短信提示
     *
     * @param phone 手机号
     * @param spacenum 车位编号
     * @param time 停车时长
     * @param rprice 实际金额
     */
    public static String can_order_MSG(String phone, String spacenum, String time, String rprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_127163796");
        request.setTemplateParam("{spacenum:'" + spacenum + "',time:'" + time  + "',rprice:'"
                + rprice + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("车子开走短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }
    /**
     * 车子开走给用户的短信提示
     *
     * @param phone 手机号
     * @param spacenum 车位编号
     * @param time 停车时长
     * @param price 预交金额
     * @param rprice 实际金额
     * @param tprice 退费金额
     */
    public static String can_order_MSG(String phone, String spacenum, String time, String price, String rprice,
                                       String tprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78565062");
        request.setTemplateParam("{spacenum:'" + spacenum + "',time:'" + time + "',price:'" + price + "',rprice:'"
                + rprice + "',tprice:'" + tprice + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("车子开走短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 时长不足15分钟
     *
     * @param phone 手机号码
     */
    public static String time_defi(String phone, String spacenum)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78660046");
        request.setTemplateParam("{spacenum:'" + spacenum + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 停车时长已完
     *
     * @param phone 手机号码
     */
    public static String Order_end(String phone, String spacenum)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78555042");
        request.setTemplateParam("{spacenum:'" + spacenum + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
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
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78705050");
        request.setTemplateParam("{code:'" + code + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 设置支付密码
     *
     * @param phone 手机号码
     * @param code 验证码
     */
    public static String set_paypwd(String phone, String code)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_78705056");
        request.setTemplateParam("{code:'" + code + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }
    /**
     * 下订单成功（先下单后缴费）
     * @param phone 手机号码
     * @param space_number 车位号
     * @param beginDate  开始时间
     * @return
     */
    public static String recver_order_success(String phone, String space_number, String beginDate)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_127152689");
        request.setTemplateParam("{space_number:'" + space_number + "',beginDate:'" + beginDate + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    /**
     * 下订单成功
     * @param phone 手机号码
     * @param space_number 车位号
     * @param beginDate  开始时间
     * @param minute 预定时长
     * @param money 金额
     * @return
     */
    public static String recver_order_success(String phone, String space_number, String beginDate, String minute, String money)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_110235004");
        request.setTemplateParam("{spacenumber:'" + space_number + "',begindate:'" + beginDate + "',minute:'" + minute + "',money:'" + money + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

    /**
     * 续费成功
     * @param phone 手机号码
     * @param spacenum 车位号
     * @param times 预定时长
     * @param price 金额
     * @return
     */
    public static String renew_success(String phone, String spacenum, String times, String price)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_109555235");
        request.setTemplateParam("{spacenum:'" + spacenum + "',times:'" + times + "',price:'" + price + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 次日续时
     * @param phone 手机号码
     * @param spacenum 车位号
     * @param times 预定时长
     * @param rprice 金额
     * @return
     */
    public static String submit_delay(String phone, String spacenum, String times,String beginDate, String rprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_110210010");
        request.setTemplateParam("{spacenum:'" + spacenum + "',times:'" + times + "',beginDate:'" + beginDate + "',rprice:'" + rprice + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 取消次日续时
     * @param phone 手机号码
     * @param spacenum 车位号
     * @param rprice 金额
     * @return
     */
    public static String cancel_delay(String phone, String spacenum, String rprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_109385242");
        request.setTemplateParam("{spacenum:'" + spacenum + "',rprice:'" + rprice + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 补缴成功
     * @param phone 手机号码
     * @param spacenum 车位号
     * @param rprice 金额
     * @return
     */
    public static String submit_ticket(String phone, String spacenum, String rprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_110230015");
        request.setTemplateParam("{spacenum:'" + spacenum + "',price:'" + rprice + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 申诉
     * @param phone 手机号码
     * @param time 车位号
     * @param price 消费金额
     * @return
     * 您的申诉已受理成功，实际停车时长${time}，消费${price}元，剩余${realprice}元已退还至你的账户。
     */
    public static String appeal(String phone, String time, String price)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_120376720");
        request.setTemplateParam("{time:'" + time + "',price:'" + price +"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 异常免单
     * @param phone 手机号码
     * @param price 金额
     * @return
     * 您的订单发生异常，已将订单金额${price}元全部退还至您的账户。
     */
    public static String free_single(String phone, String price)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172208283");
        request.setTemplateParam("{price:'" + price + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 自动续时提示
     * @param phone 手机号码
     * @param spaceNum 车位号
     * @return
     * 您停留在{spaceNum}车位号的预定订单结束时间已到，系统已自动续时
     */
    public static String auto_continuation(String phone, String spaceNum)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_117455035");
        request.setTemplateParam("{spaceNum:'" + spaceNum + "'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 欠费提示
     * @param phone 手机号码
     * @param spaceNum 车位号
     * @param time 停车时长
     * @param price 预付金额
     * @param rprice 实际消费金额
     * @param tprice 欠费金额
     * @return
     * 您在泊位编号${spacenum}停车时长${time}，预付${price}元，实际消费${rprice}元，欠费${tprice}元，请尽早补缴。
     */
    public static String auto_continuation(String phone, String spaceNum,String time,String price,String rprice,String tprice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_117455039");
        request.setTemplateParam("{spacenum:'" + spaceNum + "',time:'" + time + "',price:'" + price + "',rprice:'" + rprice + "',tprice:'"+tprice+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }


    /**
     * 充值
     * @param phone 手机号码
     * @return
     *    尊敬的${userName}用户您好，您在${time}，成功充值${price}元，可用余额${userPrice}元，赠送余额${givePrice}元 。
     */
    public static String recharge(String phone,String time,double price,double userPrice,double givePrice)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172208292");
        request.setTemplateParam("{userName:'" + phone + "',time:'"+time+"',price:'"+price+"',userPrice:'"+userPrice+"',givePrice:'"+givePrice+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }


    /**
     * 车牌通过提示
     * @param phone 手机号码
     * @return
     *    尊敬的${userName}用户，您好！您申请的{carNumber}已通过审核。。
     */
    public static String carNumberSusses(String phone,String carNumber)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172208292");
        request.setTemplateParam("{userName:'" + phone + "',carNumber:'"+carNumber+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 车牌审核提示（未通过）
     * @param phone 手机号码
     * @return
     *    尊敬的${userName}用户，您好！您申请的{carNumber}未通过审核，请核实信息再次申请。。
     */
    public static String carNumberFail(String phone,String carNumber)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172208289");
        request.setTemplateParam("{userName:'" + phone + "',carNumber:'"+carNumber+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * vip超时
     * @param phone 手机号码
     * @return
     *    尊敬的{userName}用户您好，您在{parkingName}停车场预定{spaceNumber}车位，停车时长已超过1小时，超出部分按累加方式计费， 祝你停车愉快！
     */
    public static String vipTimeout(String phone,String parkingName,String spaceNumber)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172223259");
        request.setTemplateParam("{userName:'" + phone + "',parkingName:'"+parkingName+"',spaceNumber:'"+spaceNumber+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

    /**
     * 出场缴费
     * @param phone 手机号码
     * @return
     *    尊敬的${userName}用户您好，您在${parkingName}停车场，本次缴费${price}元 祝你停车愉快！
     */
    public static String payment(String phone,String parkingName,double price)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_146616235");
        request.setTemplateParam("{userName:'" + phone + "',parkingName:'"+parkingName+"',price:'"+price+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }


    /**
     * 预定成功
     * @param phone 手机号码
     * @return
     *    尊敬的${userName}用户您好，您在${parkingName}停车场预订${spaceNumber}车位，请在${date}之前入场，若未入场车位自动取消。 祝你停车愉快！
     */
    public static String scheduledTimeout(String phone,String parkingName,String spaceNumber,String date)
    {
        IAcsClient acsClient = null;
        try {
            acsClient = setSystem();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(autograph);
        request.setTemplateCode("SMS_172223262");
        request.setTemplateParam("{userName:'" + phone + "',parkingName:'"+parkingName+"',spaceNumber:'"+spaceNumber+"',date:'"+date+"'}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信接口返回的数据----------------");
            log.info("Code=" + sendSmsResponse.getCode());
            log.info("Message=" + sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();

    }

}
