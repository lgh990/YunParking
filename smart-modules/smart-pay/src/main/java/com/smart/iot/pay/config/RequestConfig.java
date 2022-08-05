package com.smart.iot.pay.config;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

/**
 * 第三方配置信息中心
 */
@Component
public class RequestConfig {

    private Map<String,String> map;

    /**
     * requestConfig map 初始化
     */
    @PostConstruct
    private void init(){
        //配置参数map集合
        map = Maps.newHashMap();

        //alipay配置参数
        map.put("aliPartner",aliPartner);
        map.put("aliSellerId",aliSellerId);
        map.put("aliNotifyUrl",aliNotifyUrl);
        map.put("aliAppId",aliAppId);
        map.put("aliPrivateSign",aliPrivateSign);
        map.put("aliPublicKey",aliPublicKey);
        map.put("aliPublicKeys",aliPublicKeys);
        map.put("aliPayGateUrl",aliPayGateUrl);
        map.put("aliSignType",aliSignType);

        //rpc配置参数
        map.put("rpcApiUrl",rpcApiUrl);
        map.put("rpcUrl",rpcUrl);
        map.put("rpcDevparamsUrl",rpcDevparamsUrl);
        map.put("rpcTokenUrl",rpcTokenUrl);
        map.put("username",rpcUsername);
        map.put("password",rpcPassword);

        //wechat配置参数
        map.put("wcAppId",wcAppId);
        map.put("wcAppMchId",wcAppMchId);
        map.put("wcAppApiKey",wcAppApiKey);
        map.put("wcAppNotifyUrl",wcAppNotifyUrl);
        map.put("wcCertPath",wcCertPath);
        map.put("wechatPayUrl",wechatPayUrl);
        map.put("wechatRefundUrl",wechatRefundUrl);
        map.put("wcPdaAppId",wcPdaAppId);
        map.put("wcPdaMchId",wcPdaMchId);
        map.put("wcPdaSecret",wcPdaSecret);
        map.put("wcPdaApikey",wcPdaApikey);
        map.put("wcPdaNotifyUrl",wcPdaNotifyUrl);
        map.put("wcPdaProgramId",wcPdaProgramId);
        map.put("wechatBillUrl",wechatBillUrl);

        //开关ssl标识
        map.put("sslEnabled",sslEnabled);
        map.put("sslUsername",sslUsername);
        map.put("sslPassword",sslPassword);

        //公共参数
        map.put("timeout",timeout);
        map.put("charset",charset);
        map.put("format",format);

        map.put("unionpayUrl",unionpayUrl);
    }

    /**
     * ali请求配置参数
     */
    @Value("${alipay.ali_app_partner}")
    private String aliPartner;

    @Value("${alipay.ali_app_seller_id}")
    private  String aliSellerId;
    //回调地址
    @Value("${alipay.ali_notify_url}")
    private  String aliNotifyUrl;
    //
    @Value("${alipay.ali_app_id}")
    private  String aliAppId;
    //私钥 pkcs8格式
    @Value("${alipay.ali_private_sign}")
    private  String aliPrivateSign;
    //阿里公钥
    @Value("${alipay.alipay_public_key}")
    private  String aliPublicKey;
    //阿里公钥集
    @Value("${alipay.alipay_public_keys}")
    private  String aliPublicKeys;
    //支付网关
    private  String aliPayGateUrl = "https://openapi.alipay.com/gateway.do";
    // 签名
    private String aliSignType = "RSA2";


    /**
     *RPC请求配置参数
     */
    @Value("${rpc.api_url}")
    private String rpcApiUrl;
    @Value("${rpc.rpc_url}")
    private  String rpcUrl;
    @Value("${rpc.get_devparams_url}")
    private  String rpcDevparamsUrl;
    @Value("${rpc.get_token_url}")
    private  String rpcTokenUrl;
    @Value("${rpc.username}")
    private  String rpcUsername;
    @Value("${rpc.password}")
    private  String rpcPassword;

    /**
     * wechat请求配置参数
     */
    @Value("${wcpay.wc_app_appid}")
    private String wcAppId;
    @Value("${wcpay.wc_app_mchid}")
    private String wcAppMchId;
    @Value("${wcpay.wc_app_apikey}")
    private String wcAppApiKey;
    @Value("${wcpay.wc_app_notify_url}")
    private String wcAppNotifyUrl;

    @Value("${wcpay.wc_cert_path}")
    private String wcCertPath;

    @Value("${wcpay.wc_pda_appid}")
    private String wcPdaAppId;
    @Value("${wcpay.wc_pda_mchid}")
    private String wcPdaMchId;
    @Value("${wcpay.wc_pda_secret}")
    private String wcPdaSecret;
    @Value("${wcpay.wc_pda_apikey}")
    private String wcPdaApikey;
    @Value("${wcpay.wc_pda_notify_url}")
    private String wcPdaNotifyUrl;
    @Value("${wcpay.wc_pda_program_id}")
    private String wcPdaProgramId;
    //微信支付url
    private String wechatPayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信退款url
    private String wechatRefundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //微信账单url
    private String wechatBillUrl = "https://api.mch.weixin.qq.com/pay/downloadbill";

    /**
     * 银联支付请求配置参数
     */
    @Value("${unionpay.unionpay_url}")
    private String unionpayUrl;

    /**
     * ssl配置
     */
    @Value("${ssl_enabled}")
    private String sslEnabled;
    private String sslUsername="PKCS12";
    private String sslPassword="1488614362";

    /**
     * 公共参数
     */
    // 超时时间
    private String timeout = "2m";
    // 编码
    private String charset = "UTF-8";
    // 返回格式
    private String format = "json";

    @Bean
    public Map<String,String> requestProperty(){
        return map;
    }

    /**
     * 运行时修改内存中requestProperty的参数，重启失效
     * @param key
     * @param value
     */
    public void editRequestProperty(String key,String value){
        Set keySet = map.keySet();
        if(keySet.contains(key)){
            map.remove(key);
            map.put(key,value);
        }else{
            map.put(key,value);
        }


    }

}
