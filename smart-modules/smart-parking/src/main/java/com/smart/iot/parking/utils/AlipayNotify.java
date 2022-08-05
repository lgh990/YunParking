package com.smart.iot.parking.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/* *
 * 类名：AlipayNotify
 * 功能：支付宝通知处理类
 * 详细：处理支付宝各接口通知返回
 * 版本：1.1
 * 日期：2017-06-19
 * 说明：
 * 
 * ************************注意*************************
 * 调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify
{

    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * 
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params)
    {

        // 判断responsetTxt是否为true，isSign是否为true
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = "true";
        if (params.get("notify_id") != null)
        {
            String notify_id = params.get("notify_id");
            responseTxt = verifyResponse(notify_id);
        }
        String sign = "";
        if (params.get("sign") != null)
        {
            sign = params.get("sign");
        }
        boolean isSign = getRSASignVerify(params, sign);

        // 写日志记录（若要调试，请取消下面两行注释）
        // String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign +
        // "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
        // AlipayCore.logResult(sWord);

        if (isSign && "true".equals(responseTxt))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * 
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    private static boolean getRSASignVerify(Map<String, String> Params, String sign)
    {
        // 过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        // 获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        // 获得签名验证结果
        boolean isSign = false;
        try
        {
            isSign = AlipaySignature.rsaCheckContent(preSignStr, sign, RSA.publicKey, "GBK");
        }
        catch (AlipayApiException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isSign;
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     * 
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     *         验证结果集：
     *         invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     *         true 返回正确信息
     *         false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String verifyResponse(String notify_id)
    {
        // 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

        String partner = "";
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url);
    }

    /**
     * 获取远程服务器ATN结果
     * 
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     *         验证结果集：
     *         invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     *         true 返回正确信息
     *         false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String checkUrl(String urlvalue)
    {
        String inputLine = "";

        try
        {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            inputLine = "";
        }

        return inputLine;
    }
}
