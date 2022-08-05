package com.smart.iot.pay.util;




import com.yuncitys.smart.parking.common.util.DateUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class testUtil {
    private static final long serialVersionUID = 1L;
    public static Map<String,String> orderResult; //用来存储订单的交易状态(key:订单号，value:状态(0:未支付，1：已支付))  ---- 这里可以根据需要存储在数据库中
    public static int orderStatus=0;
public  static void main(String[] args) {

            HashMap<String, String> map = new HashMap<>();

                    //XmlUtils.getParameterMap(req);

            map.put("mch_id","102516501294");
            map.put("notify_url", "https://www.parking.yx-sz.cn/api/pay/codePay/payResultSerlet");
            map.put("nonce_str", String.valueOf(new Date().getTime()));
            map.put("out_trade_no", DateUtil.getOrderNum());
             map.put("total_fee","1");
             map.put("sign_type","RSA_1_256");
             map.put("service","unified.trade.native");
             map.put("body","焱鑫停车");
             map.put("mch_create_ip","192.168.0.109");
             map.put("nonce_str","12sdasd1231212321");
            Map<String, String> params = SignUtils.paraFilter(map);
            System.out.println(params);
            StringBuilder buf = new StringBuilder(
                    (params.size() + 1) * 10);
            SignUtils.buildPayParams(buf, params, false);
            String preStr = buf.toString();
            String sign_type = map.get("sign_type");

            map.put("sign", SignUtil.getSign(sign_type, preStr));


            String reqUrl = "https://pay.swiftpass.cn/pay/gateway";
            System.out.println("reqUrl：" + reqUrl);

            System.out.println("reqParams:" + XmlUtils.parseXML(map));
            CloseableHttpResponse response = null;
            CloseableHttpClient client = null;
            String res = null;
            String reSign = null;
            try {
                HttpPost httpPost = new HttpPost(reqUrl);
                StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
                httpPost.setEntity(entityParams);
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                if (response != null && response.getEntity() != null) {

                    Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");

                    reSign = resultMap.get("sign");
                    sign_type = resultMap.get("sign_type");
                    res = XmlUtils.toXml(resultMap);
                    System.out.println("签名方式" + sign_type);
                    if (resultMap.containsKey("sign")) {
                        if (SignUtil.verifySign(reSign, sign_type, resultMap)) {
                            System.out.println("返回参数验证签名通过。。。");
                            if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
                                if (orderResult == null) {
                                    orderResult = new HashMap<String, String>();
                                }
                                orderResult.put(map.get("out_trade_no"), "0");//初始状态

                                String code_img_url = resultMap.get("code_img_url");
                                System.out.println(code_img_url);
                            } else {
                            }
                        }


                    }
                } else {
                    res = "操作失败";
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = "系统异常";
            } finally {
                if (response != null) {
                    System.out.println("成功");
                }
                if (client != null) {
                    System.out.println("成功");
                }
            }
            System.out.println(res);
        }

}
