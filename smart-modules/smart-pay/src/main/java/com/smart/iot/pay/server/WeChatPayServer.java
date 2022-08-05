package com.smart.iot.pay.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.pay.connect.HttpRequest;
import com.smart.iot.pay.feign.ParkingOrderFeign;
import com.smart.iot.pay.util.CommonUtil;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class WeChatPayServer {

    //第三方请求配置参数中心
    @Autowired
    private Map<String, String> requestProperty;

    @Autowired
    private ParkingOrderFeign parkingOrderFeign;

    @Autowired
    private HttpRequest httpRequest;

    /**
     * 获取openId,token,nickname,headimgurl
     *
     * @param params
     * @return
     */
    public Map<String, String> getOpenId(Map<String, String> params) {
        Map returnMap = Maps.newHashMap();
        String appid = requestProperty.get("wcPdaAppId");//公众号appid
        String appsecret = requestProperty.get("wcPdaSecret");//公众号秘钥
        String code = params.get("code");
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
        JSONObject tokenObject = JSONObject.parseObject(httpRequest.request(URL, "GET", null));
        String userURL = "https://api.weixin.qq.com/sns/userinfo?access_token=" + tokenObject.getString("access_token") + "&openid=" + tokenObject.getString("openid") + "";
        JSONObject userJson = JSONObject.parseObject(httpRequest.request(userURL, "GET", null));
        returnMap.put("openId", tokenObject.getString("openid"));
        returnMap.put("accessToken", tokenObject.getString("access_token"));
        returnMap.put("nickname", userJson.getString("nickname"));
        returnMap.put("headimgurl", userJson.getString("headimgurl"));
        return returnMap;
    }

    /**
     * 微信支付下单接口
     *
     * @return
     */
    public Map<String, Object> wechatPay(Map<String, Object> params) {
        SortedMap<Object, Object> parameterMap = new TreeMap();
        parameterMap.put("appid", requestProperty.get("wcAppId"));
        parameterMap.put("mch_id", requestProperty.get("wcMchId"));
        parameterMap.put("nonce_str", CommonUtil.getGUID());
        parameterMap.put("body",
                StringUtils.abbreviate(params.get("desc").toString().replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        parameterMap.put("out_trade_no", params.get("orderNum"));
        parameterMap.put("fee_type", "CNY");
        parameterMap.put("total_fee", params.get("money"));
        parameterMap.put("spbill_create_ip", params.get("ip"));
        parameterMap.put("notify_url", requestProperty.get("wcNotifyUrl"));
        parameterMap.put("trade_type", "APP");

        String sign = createSign(requestProperty.get("charset"), parameterMap);
        parameterMap.put("sign", sign);

        String requestXML = CommonUtil.mapToXml(parameterMap);

        String result = httpRequest.request(requestProperty.get("wechatPayUrl"), "POST", requestXML);
        Map<String, Object> map = CommonUtil.doXMLParse(result);

        map.put("partnerid", requestProperty.get("wcMchId"));
        SortedMap<Object, Object> creaSignMap = new TreeMap();
        creaSignMap.put("appid", requestProperty.get("wcAppId"));
        creaSignMap.put("partnerid", requestProperty.get("wcMchId"));
        creaSignMap.put("prepayid", map.get("prepay_id"));
        creaSignMap.put("noncestr", map.get("nonce_str"));
        creaSignMap.put("package", "Sign=WXPay");
        creaSignMap.put("timestamp", System.currentTimeMillis() / 1000);
        creaSignMap.put("sign", createSign(requestProperty.get("charset"), parameterMap));

        map.put("timestamp", creaSignMap.get("timestamp"));
        map.put("sign", creaSignMap.get("sign"));

        return map;
    }

    /**
     * 微信回调
     *
     * @param notifyParams
     * @return
     */
    public String wechatNotify(Map<String, String> notifyParams) {
        String tradeStatus = notifyParams.get("result_code");
        if (isTenpaySign(requestProperty.get("charset"), notifyParams, requestProperty.get("wcPdaApikey"))) {
            String tradeNo = notifyParams.get("out_trade_no").toString();
            Map<String, String> map = Maps.newHashMap();
            map.put("tradeStatus", tradeStatus);
            map.put("tradeNo", tradeNo);
            parkingOrderFeign.notify(map);
        }
        return wechatResponseXml(tradeStatus);
    }

    /**
     * 微信退款
     *
     * @param params
     * @return
     */
    public BaseResponse wechatRefund(Map<String, String> params) {
        //业务层调用传递参数
        String orderNum = params.get("orderNum");
        String money = params.get("money");
        //当前时间
        String currTime = CommonUtil.getCurrTime();
        //退款单号
        String nonceStr = currTime.substring(8, currTime.length()) + (CommonUtil.buildRandom(4) + "");
        //拼装微信参数集合
        SortedMap<Object, Object> parameters = new TreeMap();
        parameters.put("appid", requestProperty.get("wcAppId"));
        parameters.put("mch_id", requestProperty.get("wcMchId"));
        parameters.put("nonce_str", nonceStr);
        parameters.put("out_trade_no", orderNum);
        parameters.put("out_refund_no", nonceStr);
        parameters.put("fee_type", "CNY");
        parameters.put("total_fee", money);
        parameters.put("refund_fee", money);
        parameters.put("op_user_id", requestProperty.get("wcMchId"));
        parameters.put("sign", createSign(requestProperty.get("charset"), parameters));

        CloseableHttpResponse response = null;

        //默认返回的错误response
        BaseResponse returnResponse = new BaseResponse();
        returnResponse.setStatus(500);
        returnResponse.setMessage("未请求到微信端或报文解析失败");

        try {
            //ssl加密https方式请求微信端接口
            response = httpRequest.sslRequest(requestProperty.get("wcAppId"), requestProperty.get("wechatRefundUrl"), CommonUtil.mapToXml(parameters));
        } catch (Exception e) {
            log.error("========================请求微信端失败，退款不成功===========================");
        }

        try {
            //微信返回报文解析
            if (response != null) {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(response.getEntity().getContent());
                Element rootElt = document.getRootElement();
                String resultCode = rootElt.elementText("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    returnResponse.setStatus(200);
                    returnResponse.setMessage("退款成功");
                } else {
                    returnResponse.setStatus(500);
                    returnResponse.setMessage(rootElt.elementText("err_code_des"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("xml报文解析失败");
        }

        return returnResponse;
    }

    /**
     * 微信账单数据拉取
     *
     * @param date 如20190609
     * @return
     */
    public String wechatBill(String date) {
        SortedMap<Object, Object> parameterMap = new TreeMap();
        parameterMap.put("appid", requestProperty.get("wcAppId"));
        parameterMap.put("mch_id", requestProperty.get("wcAppMchId"));
//        parameterMap.put("appid","wx8c1c3b7654729679");
//        parameterMap.put("mch_id","1514119501");
        parameterMap.put("nonce_str", CommonUtil.getGUID());
        parameterMap.put("bill_type", "ALL");
//        parameterMap.put("tar_type","GZIP");
        parameterMap.put("bill_date", date);
//        if(requestParams.containsKey("type")){
//            parameterMap.put("bill_type",requestParams.get("type"));
//        }

        parameterMap.put("sign", createSign(requestProperty.get("charset"), parameterMap));
//        parameterMap.put("sign",createSign("UTF-8",parameterMap));
        String requestXML = CommonUtil.mapToXml(parameterMap);

        String result = httpRequest.request(requestProperty.get("wechatBillUrl"), "POST", requestXML);
//        String result = httpRequest.request("https://api.mch.weixin.qq.com/pay/downloadbill", "POST", requestXML);

        //账单结果集每行转为一个数组元素
        String[] resultArray = result.split("\n");
        //取出最后两行为总计的数据
        String countTitle = new String(resultArray[resultArray.length-2]);
        String countBill = new String(resultArray[resultArray.length-1]);
        String[] countTitles = countTitle.split(",");
        String[] countBills = countBill.split(",");
        JSONObject count = new JSONObject();
        for (int k =0;k<countTitles.length;k++){
            count.put(countTitles[k],countBills[k]);
        }
        log.info(countTitle);
        log.info(countBill);
        //转换出一个纯账单明细的数据集合

        String[] middle = ArrayUtils.remove(resultArray,resultArray.length-1);
        String[] ordersbill = ArrayUtils.remove(middle,middle.length-1);

        //字段名数组
        String[] billname = null;
        JSONArray bills = new JSONArray();
        //遍历每一行数据
        for (int i = 0; i < ordersbill.length; i++) {
            //拿出一个行中的所有数据单元
            String[] s = ordersbill[i].split(",");
            JSONObject bill = new JSONObject();
            if (i == 0) {
                //第一行为数据字段名
                billname = ordersbill[i].split(",");
            }else {
                //遍历每行中的每个数据，成对放入jsonobject中
                for (int j = 0; j < s.length; j++) {
                    String str = s[j].replace("`","");
                    bill.put(billname[j], str);
                }
                bills.add(bill);
            }
        }
        JSONObject wechatBill = new JSONObject();
        wechatBill.put("count",count);
        wechatBill.put("detail",bills);
        return wechatBill.toJSONString();
    }


    /**
     * 微信生成sign签名
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    private String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.keySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Object k = it.next();
            String v = parameters.get(k).toString();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
//        sb.append("key=" + requestProperty.get("wcApiKey"));
        sb.append("key=" + "yuncitysingiioweofl66255uowenfnosdj4");
        System.out.println(sb.toString());
        String sign = CommonUtil.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 生成wechat回调返回报文
     *
     * @param payStatus
     * @return
     */
    private String wechatResponseXml(String payStatus) {
        String msg;
        if (payStatus.equals("SUCCESS")) {
            msg = "OK";
        } else {
            msg = "报文为空";
        }
        return "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[" + msg + "]]></return_msg>" + "</xml> ";
    }

    /**
     * 验证微信回调sign是否正确
     * <p>
     * 重复地方，parking原支付逻辑中
     *
     * @return boolean
     */
    private boolean isTenpaySign(String characterEncoding, Map<String, String> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + API_KEY);

        // 算出摘要
        String mysign = CommonUtil.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = (packageParams.get("sign")).toLowerCase();

        // System.out.println(tenpaySign + "    " + mysign);
        return tenpaySign.equals(mysign);
    }

}
