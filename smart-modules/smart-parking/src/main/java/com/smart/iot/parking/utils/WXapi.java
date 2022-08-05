package com.smart.iot.parking.utils;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingOrdersBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.Plate;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ssl.SSLContexts;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URL;
import java.parking.KeyStore;
import java.text.DecimalFormat;
import java.util.*;

@Component
@Slf4j
public class WXapi
{
    @Value("${wcpay.wc_pda_appid}")
    private   String wc_pda_appid;
    @Value("${wcpay.wc_pda_mchid}")
    private   String wc_pda_mchid;
    @Value("${wcpay.wc_pda_secret}")
    private   String wc_pda_secret;
    @Value("${wcpay.wc_pda_apikey}")
    private   String wc_pda_apikey;
    @Value("${wcpay.wc_pda_notify_url}")
    private  String wc_pda_notify_url;
    @Value("${wcpay.wc_pda_program_id}")
    private  String wc_pda_program_id;

    public static String wc_app_appid;
    public static String wc_app_mchid;
    public static String wc_app_apikey;
    public static String wc_app_notify_url;
    public static String wc_cert_path;
    @Value("${wcpay.wc_app_appid}")
    public void setWcAppAppId(String wcAppAppId) {
        WXapi.wc_app_appid = wcAppAppId;
    }

    @Value("${wcpay.wc_app_mchid}")
    public void setWc_app_mchid(String wc_app_mchid) {
        WXapi.wc_app_mchid = wc_app_mchid;
    }

    @Value("${wcpay.wc_app_apikey}")
    public void setWc_app_apikey(String wc_app_apikey){WXapi.wc_app_apikey=wc_app_apikey;}

    @Value("${wcpay.wc_app_notify_url}")
    public void setWc_app_notify_url(String wc_app_notify_url){WXapi.wc_app_notify_url=wc_app_notify_url;}

    @Value("${wcpay.wc_cert_path}")
    public void setWc_cert_path(String wc_cert_path){WXapi.wc_cert_path=wc_cert_path;}

    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingBiz parkingBiz;

    @Value("${ssl_enabled}")
    private  String ssl_enabled;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    public  Map<String, Object> weixiPay(String sn, BigDecimal totalAmount, String description) throws IOException, JDOMException {
        SortedMap<Object, Object> parameterMap = new TreeMap<Object, Object>();
        parameterMap.put("appid", wc_app_appid);
        parameterMap.put("mch_id",wc_app_mchid);
        parameterMap.put("nonce_str", PayCommonUtil.getGUID());
        parameterMap.put("body",
                StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("fee_type", "CNY");
        System.out.println("jiner");
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));
        System.out.println("jiner2");
        parameterMap.put("spbill_create_ip", "192.168.1.25");
        /*String localAddr= request.getLocalAddr();
        int localPort= request.getLocalPort();*/
        parameterMap.put("notify_url",wc_app_notify_url);
        parameterMap.put("trade_type", "APP");
        String sign = createSign("UTF-8", parameterMap);
        System.out.println("jiner2");
        parameterMap.put("sign", sign);
        System.out.println(parameterMap.toString());
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println(requestXML);
        String result = httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        System.out.println(result);
        Map<String, Object> map = null;
        map = PayCommonUtil.doXMLParse(result);
        map.put("partnerid",wc_app_mchid);
        SortedMap<Object, Object> creaSignMap  = weixinPrePay(map);

        map.put("timestamp", (Long) creaSignMap.get("timestamp"));
        map.put("sign", creaSignMap.get("sign"));

        return map;
    }

    public  SortedMap<Object, Object> weixinPrePay(Map<String, Object> map)
    {
        SortedMap<Object, Object> parameterMap = new TreeMap<Object, Object>();
        parameterMap.put("appid",wc_app_appid);
        parameterMap.put("partnerid",wc_app_mchid);
        parameterMap.put("prepayid", map.get("prepay_id"));
        parameterMap.put("noncestr", map.get("nonce_str"));
        parameterMap.put("package", "Sign=WXPay");
        parameterMap.put("timestamp", System.currentTimeMillis()/1000);

        String sign = createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        return parameterMap;
    }
    // 生成签名
    public  String createSign(String characterEncoding, SortedMap<Object, Object> parameters)
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wc_app_apikey);
        System.out.println(sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    // 请求方法
    public  String httpsRequest(String requestUrl, String requestMethod, String outputStr)
    {
        try
        {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if(ssl_enabled!=null && "Y".equals(ssl_enabled)){
                setCert(conn);
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr)
            {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        }
        catch (ConnectException ce)
        {
            System.out.println("连接超时：{}" + ce);
        }
        catch (Exception e)
        {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }

    /**
 * 给HttpsURLConnection设置数字证书
 * @param connection
 * @throws IOException
 */
private  void setCert(HttpsURLConnection connection) throws IOException{
    FileInputStream instream = null;
    try {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //读取本机存放的PKCS12证书文件
        instream = new FileInputStream(new File(wc_cert_path)); //certPath:数字证书路径            System.out.println("=============================================");
        //指定PKCS12的密码(商户ID)
        keyStore.load(instream, "1488614362".toCharArray());
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,  "1488614362".toCharArray()).build();
        //指定TLS版本
        SSLSocketFactory ssf = sslcontext.getSocketFactory();
        connection.setSSLSocketFactory(ssf);
    } catch (Exception e){
        e.printStackTrace();
    }finally {
        instream.close();
    }
}
    public  void query_openId(String code, HttpServletResponse response, HttpServletRequest request, String state) throws Exception {
        String appid = wc_pda_appid;//公众号appid
        String appsecret = wc_pda_secret;//公众号秘钥
        //微信返回的code
        String ordernum = state.split(",")[0];
        //获取openId和access_token（获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder）
        String openId = "";
        String access_token = "";
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";

        JSONObject jsonObject = JSONObject.parseObject(httpsRequest(URL, "GET", null));
        log.info(String.valueOf(jsonObject));

        if (null != jsonObject) {
            openId = jsonObject.getString("openid");
            access_token = jsonObject.getString("access_token");
        }
        //openId="oNUAR0-2Mq91lt0-WrtZapzvg0EU";
        log.info("---------------openId:"+openId);
        request.getSession().setAttribute("openId", openId);

        //获取用户信息
        String nickname = "";
        String headimgurl = "";
        String userURL = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"";
        JSONObject userJson = JSONObject.parseObject(httpsRequest(userURL, "GET", null));
        if (null != jsonObject) {
            nickname = userJson.getString("nickname");
            headimgurl = userJson.getString("headimgurl");
        }
        request.getSession().setAttribute("nickname", nickname);
        request.getSession().setAttribute("headimgurl", headimgurl);
        response.setCharacterEncoding("utf-8");
        response.sendRedirect("https://www.parking.yuncitys.com.com/api/parking/wxPay.html?ordernum="+ordernum+"&openId="+openId);//跳转到支付页面
        log.info("==================获取openid"+openId);
    }

    public void pda_weixin_notify(HttpServletRequest request, HttpServletResponse response) throws JDOMException {
        log.info("==========获取微信支付成功回调") ;
        // 读取参数
        InputStream inputStream = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            inputStream = request.getInputStream();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s;
        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            while ((s = in.readLine()) != null)
            {
                sb.append(s);
            }
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try
        {
            in.close();
            inputStream.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        try {
            m = XMLUtil.doXMLParse(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext())
        {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue)
            {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        // 账号信息
        String key = wc_pda_apikey; // key

        // 判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key))
        {
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code")))
            {
                // 这里是支付成功
                // ////////执行自己的业务逻辑////////////////
                String mch_id = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String is_subscribe = (String) packageParams.get("is_subscribe");
                String out_trade_no =  packageParams.get("out_trade_no").toString();
                String total_fee = (String) packageParams.get("total_fee");

                log.info("mch_id:" + mch_id);
                log.info("openid:" + openid);
                log.info("is_subscribe:" + is_subscribe);
                log.info("out_trade_no:" + out_trade_no);
                log.info("total_fee:" + total_fee);
                ParkingOrders parkingOrderEx = new ParkingOrders();
                parkingOrderEx.setOrderNum(out_trade_no);
                ParkingOrders parkingOrder = parkingOrdersBiz.selectOne(parkingOrderEx);
                if (parkingOrder != null)
                {
                    log.info("==========开始处理微信订单结束与取消支付宝订单") ;
/*
                    WeixinPayUtil.closeAliPayOrder(parkingOrder.getOrderNum());
                    UserInfo userInfo=userInfoRepository.findByUserId(Integer.valueOf(chargeId));
                    log.info("==========推送人员:"+userInfo.getUserName()+"");
                    JPush.buildPushObject_all_alias(String.valueOf(userInfo.getUserId()), "1", parkingOrder.getParkingId());
*/
                    parkingOrder.setPayStatus(BaseConstants.status.success);
                    parkingOrder.setChargeDate(DateUtil.getDateTime());
/*
                    parkingOrder.setChargeId(Integer.valueOf(chargeId));
*/
                    if(parkingOrder.getPosition().equals(BaseConstants.Position.leave))
                    {
                        parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
                    }else
                    {
                        //把订单数据推进延迟队列
                        parkingOrdersBiz.PutOrderIntoDelayQueue(parkingOrder);
                    }
                    parkingOrder.setPayType(BaseConstants.payType.wechat);
                    parkingOrdersBiz.saveOrUpdate(parkingOrder);

                }
                // ////////执行自己的业务逻辑////////////////

                log.info("支付成功");
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                log.info("==========结束处理微信订单结束与取消支付宝订单");
            }
            else
            {
                log.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
            BufferedOutputStream out = null;
            try
            {
                out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        else
        {
            log.info("通知签名验证失败");
        }
    }
    public ObjectRestResponse topayServlet(HttpServletResponse response, HttpServletRequest request, JSONObject paramsMap, ParkingOrders parkingOrder) throws Exception {
        request.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        Plate plate = plateBiz.selectById(parkingOrder.getPlaId());
        Parking parking = parkingBiz.selectById(parkingOrder.getParkingId());
        BigDecimal total =  parkingOrdersBiz.queryCurrCostMoney(DateUtil.getDateTime(),parking,plate,parkingOrder);
        parkingOrder.setRealMoney(total);
        parkingOrdersBiz.saveOrUpdate(parkingOrder);
        total = total.multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        String money = df.format(total);
        String openId = String.valueOf(paramsMap.get("openId"));
        String ordernum = parkingOrder.getOrderNum();
        String appid = wc_pda_appid;//公众号appid
        String appsecret =  wc_pda_secret;//公众号秘钥

        String partner = wc_pda_mchid;//商户号
        String partnerkey = wc_pda_apikey;//商户API秘钥



        //商户号
        String mch_id = partner;

        //随机数
        String nonce_str = StringUtil.uuid();

        //商品描述
        String body = "停车";

        //附加数据
        String attach = "云创智城科技有限公司";

        //商户订单号
        String out_trade_no = ordernum;

        //订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();

        //支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等：http://*/weChatpay/notifyServlet
        String notify_url = wc_pda_notify_url;

        String trade_type = "JSAPI";

        String openid = openId;

        //非必输
//				String product_id = "";

        //获取sign（统一下单接口签名）
        //第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），
        //使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
        //第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，
        //再将得到的字符串所有字符转换为大写，得到sign值signValue。
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("attach", attach);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", money);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        packageParams.put("openid", openid);
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(appid, appsecret, partnerkey);
        String sign = reqHandler.createSign(packageParams);

        //统一下单接口携带参数（xml格式），接口地址https://api.mch.weixin.qq.com/pay/unifiedorder
        String xml="<xml>"+
                "<appid>"+appid+"</appid>"+
                "<mch_id>"+mch_id+"</mch_id>"+
                "<nonce_str>"+nonce_str+"</nonce_str>"+
                "<sign>"+sign+"</sign>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<attach>"+attach+"</attach>"+
                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
                //金额，这里写的1 分到时修改，测试用
//						"<total_fee>"+1+"</total_fee>"+
                "<total_fee>"+money+"</total_fee>"+
                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<notify_url>"+notify_url+"</notify_url>"+
                "<trade_type>"+trade_type+"</trade_type>"+
                "<openid>"+openid+"</openid>"+
                "</xml>";
        System.out.println(xml);

        String allParameters = "";//没用
        try {
            allParameters =  reqHandler.genPackage(packageParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //请求微信统一下单接口，成功后返回预支付交易会话标识prepay_id
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = "";
        try {
            prepay_id =GetWxOrderno.getPayNo(createOrderURL, xml);
            if("".equals(prepay_id)){
                request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
                response.sendRedirect("error.jsp");
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //生成H5调起微信支付API相关参数（前端页面js的配置参数）
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String timestamp = String.valueOf(DateUtil.getCurrTimestamp());//当前时间的时间戳
        String packages = "prepay_id="+prepay_id;;//订单详情扩展字符串
        finalpackage.put("appId", appid);//公众号appid
        finalpackage.put("timeStamp", timestamp);
        finalpackage.put("nonceStr", nonce_str); //随机数
        finalpackage.put("package", packages);
        finalpackage.put("signType", "MD5");//签名方式
        String finalsign = reqHandler.createSign(finalpackage);//签名

        json.put("appId", appid);
        json.put("timeStamp", timestamp);
        json.put("nonceStr", nonce_str);
        json.put("packages", packages);
        json.put("sign", finalsign);
        ObjectRestResponse objectRestResponse=new ObjectRestResponse<Object>().data(json);
        log.info("========"+objectRestResponse.toString());
        return objectRestResponse;

    }




    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<Object, Object> packageParams, String AppKey) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + AppKey);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * @Author: HONGLINCHEN
     * @Description:微信退款
     * @param out_trade_no
     * @param total_fee
     * @Date: 2017-9-11 14:35
     * @return:
     */
    public  String wxPayRefund(String out_trade_no,String total_fee) {
        StringBuffer xml = new StringBuffer();
        String data = null;
        try {
            String currTime = PayCommonUtil.getCurrTime();
            String strTime = currTime.substring(8, currTime.length());
            String strRandom = PayCommonUtil.buildRandom(4) + "";
            String nonce_str = strTime + strRandom;
            String nonceStr = nonce_str;
            xml.append("</xml>");
            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
            parameters.put("appid", wc_app_appid);
            parameters.put("mch_id", wc_app_mchid);
            parameters.put("nonce_str", nonceStr);
            parameters.put("out_trade_no", out_trade_no);
            //parameters.put("transaction_id", transaction_id);
            parameters.put("out_refund_no", nonceStr);
            parameters.put("fee_type", "CNY");
            parameters.put("total_fee", total_fee);
            parameters.put("refund_fee", total_fee);
            parameters.put("op_user_id",wc_app_mchid);
            parameters.put("sign", createSign(parameters,wc_app_apikey));
            data  = PayCommonUtil.getRequestXml(parameters);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return data;
    }



}
