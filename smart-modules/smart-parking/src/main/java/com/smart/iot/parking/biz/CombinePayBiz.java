package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.UserFeign;
import com.smart.iot.parking.utils.*;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.parking.KeyStore;
import java.util.*;

import static com.smart.iot.parking.utils.PayCommonUtil.doXMLParse;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-13 11:40:54
 */
@Service
@Slf4j
public class CombinePayBiz {
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private AppUserBiz appUserBiz;
    @Value("${alipay.ali_app_partner}")
    private String ali_app_partner;
    @Value("${alipay.ali_app_seller_id}")
    private  String ali_app_seller_id;
    @Value("${alipay.ali_notify_url}")
    private  String ali_notify_url;
    @Value("${alipay.ali_app_id}")
    private  String ali_app_id;
    @Value("${alipay.ali_private_sign}")
    private  String ali_private_sign;
    @Value("${alipay.alipay_public_key}")
    private  String alipay_public_key;
    @Value("${alipay.alipay_public_keys}")
    private  String alipay_public_keys;


    @Value("${wcpay.wc_app_appid}")
    private   String wc_app_appid;
    @Value("${wcpay.wc_app_mchid}")
    private   String wc_app_mchid;
    @Value("${wcpay.wc_app_apikey}")
    private   String wc_app_apikey;
    @Value("${wcpay.wc_app_notify_url}")
    private   String wc_app_notify_url;

    @Value("${rpc.api_url}")
    private String api_url;
    @Value("${rpc.rpc_url}")
    private  String rpc_url;
    @Value("${rpc.get_devparams_url}")
    private  String get_devparams_url;
    @Value("${rpc.get_token_url}")
    private  String get_token_url;
    @Value("${wcpay.wc_cert_path}")
    private  String wc_cert_path;
    @Value("${rpc.username}")
    private  String username;
    @Value("${rpc.password}")
    private  String password;
    @Autowired
    private PublishMsgBiz publishMsgBiz;
    @Autowired
    private PlateBiz plateBiz;
    @Autowired
    private ParkingBiz parkingBiz;
    @Autowired
    private ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    private ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    private IoRecordBiz ioRecordBiz;
    @Autowired
    private ParkingIoBiz parkingIoBiz;
    /**
     * ?????????????????????
     *
     * @param request
     * @return
     */
    public String alipay_notify(HttpServletRequest request) throws Exception {
        log.info("==========???????????????POST??????????????????") ;
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();)
        {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++)
            {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        boolean flag = false;
        flag = AlipayNotify.verify(params);
        // ????????????
        String trade_status = null;
        try
        {
            trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (flag)
        {
            log.info("????????????");
        }
        ParkingOrders parkingOrders = new ParkingOrders();
        parkingOrders.setOrderNum(params.get("out_trade_no"));
        ParkingOrders parkingOrder = parkingOrdersBiz.selectOne(parkingOrders);
        if (!"TRADE_FINISHED".equals(trade_status) && !"TRADE_SUCCESS".equals(trade_status))
        {
            log.info("??????????????????????????????????????????trade_status=" + trade_status + "???");
        }
        else
        {
            parkingOrder.setPayType(BaseConstants.payType.alipay);
            cannelOrders(parkingOrder);
            send_msg(parkingOrder);
        }
        return "success";

    }

    // ????????????
    public  void send_msg(ParkingOrders parkingOrder)
    {
        log.info("==========????????????????????????") ;
        if(parkingOrder.getOrderType().equals(BaseConstants.OrderType.handset)){ //???????????????????????????
            HashMap<String,Object> userInfo= userFeign.getUserById(parkingOrder.getCrtUser());
            ParkingSpace parkingSpace = parkingSpaceBiz.selectById(parkingOrder.getSpaceId());
            log.info("==========????????????:"+userInfo.get("userName"));
            publishMsgBiz.publishPdaAndWebMsg(parkingOrder.getParkingId(),parkingSpace);
        }else if(parkingOrder.getOrderType().equals(BaseConstants.OrderType.recharge))//????????????????????????
        {
            AppUser userInfo = appUserBiz.selectById(parkingOrder.getUserId());
            userInfo.setMoney(userInfo.getMoney().add(parkingOrder.getRealMoney()));
            parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
            publishMsgBiz.publishChargeSuccessMsg(userInfo,parkingOrder.getRealMoney());
            String time= DateUtil.dateTimeToStr(new Date());
            //?????????${userName}?????????????????????${time}???????????????${price}??????????????????${userPrice}??????????????????${givePrice}??? ???
            SendMsg.recharge(userInfo.getName(),time,parkingOrder.getRealMoney().doubleValue(),userInfo.getMoney().doubleValue(),0.0);

        }else if(parkingOrder.getOrderType().equals(BaseConstants.OrderType.vip_charge))//????????????????????????
        {
            AppUser userInfo = appUserBiz.selectById(parkingOrder.getUserId());
            parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
            publishMsgBiz.publishRechargeDepositSuccessMsg(userInfo,parkingOrder.getRealMoney());
            String time= DateUtil.dateTimeToStr(new Date());
            //?????????${userName}?????????????????????${time}???????????????${price}??????????????????${userPrice}??????????????????${givePrice}??? ???
            SendMsg.recharge(userInfo.getName(),time,parkingOrder.getRealMoney().doubleValue(),userInfo.getMoney().doubleValue(),0.0);
        }else if(parkingOrder.getOrderType().equals(BaseConstants.OrderType.common)){
            log.info("????????????????????????????????????");
            IoRecord ioRecord=ioRecordBiz.selectById(parkingOrder.getLpId());
            ParkingIo parkingIo=parkingIoBiz.selectById(ioRecord.getExitId());
            ioRecord.setGououtDate(parkingOrder.getEndDate());
            ioRecordBiz.saveOrUpdate(ioRecord);
            Map map=new HashMap();
            map.put("payStatus","1");
            map.put("payNum",parkingOrder.getOrderNum());
            map.put("screenMsg","??????????????????????????????????????????????????????");
            map.put("invoiceURL","http://www.yx-sz.cn/");
            RobotUtil.payResult(map,parkingIo);
        }
    }    /**
     * ??????????????????
     *
     * @param request
     * @param response
     */
    public void wechat_notify(HttpServletRequest request, HttpServletResponse response) {
        // ????????????
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

        // ??????xml???map
        Map<String, String> m = new HashMap<String, String>();
        try
        {
            m = doXMLParse(sb.toString());
        }
        catch (JDOMException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ????????? ?????? TreeMap
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

        log.info(""+packageParams);
        // ????????????????????????
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, wc_app_apikey))
        {
            // ------------------------------
            // ??????????????????
            // ------------------------------
            String resXml = "";
            String out_trade_no = (String) packageParams.get("out_trade_no");
            ParkingOrders parkingOrders = new ParkingOrders();
            parkingOrders.setOrderNum(out_trade_no);
            ParkingOrders parkingOrder = parkingOrdersBiz.selectOne(parkingOrders);
            if ("SUCCESS".equals((String) packageParams.get("result_code")))
            {
                parkingOrder.setPayType(BaseConstants.payType.wechat);
                cannelOrders(parkingOrder);
                send_msg(parkingOrder);
                // ////////???????????????????????????////////////////
                log.info("????????????");
                // ????????????.??????????????????.??????.???????????????????????????.????????????????????????????????????.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
            else
            {
                log.info("????????????,???????????????" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[????????????]]></return_msg>" + "</xml> ";
            }
            log.info("????????????");
            // ------------------------------
            // ??????????????????
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
            log.info("????????????????????????");
        }

    }
    public void cannelOrders(ParkingOrders parkingOrder){
        parkingOrder.setChargeDate(DateUtil.getDateTime());
        parkingOrder.setPayStatus(BaseConstants.status.success);
        if (parkingOrder != null) {
            if (parkingOrder.getOrderType().equals(BaseConstants.OrderType.vip_charge) || parkingOrder.getOrderType().equals(BaseConstants.OrderType.recharge)) {
                if(BaseConstants.OrderStatus.unpay.equals( parkingOrder.getOrderStatus())) {
                    parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
                    parkingOrdersBiz.saveOrUpdate(parkingOrder);
                    if (parkingOrder.getOrderType().equals(BaseConstants.OrderType.recharge)) {
                        AppUser appUser = appUserBiz.selectById(parkingOrder.getUserId());
                        appUser.setMoney(appUser.getMoney().add(parkingOrder.getRealMoney()));
                        appUserBiz.saveOrUpdate(appUser);
                    } else if (parkingOrder.getOrderType().equals(BaseConstants.OrderType.vip_charge)) {
                        AppUser appUser = appUserBiz.selectById(parkingOrder.getUserId());
                        appUser.setUserType(BaseConstants.user_type.vip);
                        appUser.setVipOrderNum(parkingOrder.getOrderNum());
                        appUserBiz.saveOrUpdate(appUser);
                    }
                }
            }else if(parkingOrder.getOrderType().equals(BaseConstants.OrderType.common) && parkingOrder.getOrderStatus().equals(BaseConstants.OrderStatus.running)){
                log.info("???????????????????????????????????????????????????");
                parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
                parkingOrdersBiz.saveOrUpdate(parkingOrder);
            } else{
/*
                send_msg(parkingOrder);
*/

                if (parkingOrder.getPosition().equals(BaseConstants.Position.leave)) {
                    parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
                } else {
                    //?????????????????????????????????
                    parkingOrdersBiz.PutOrderIntoDelayQueue(parkingOrder);
                }

                //parkingOrder.setPayType(BaseConstants.payType.wechat);
                parkingOrdersBiz.saveOrUpdate(parkingOrder);
                log.info("==========????????????????????????");
            }
        }
    }
    public void get_alipay_config(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        if (request.getParameter("WIDout_trade_no") != null) {
            // ?????????????????????????????????????????????????????????????????????
            String out_trade_no = new String(request.getParameter("WIDout_trade_no"));
            log.info("-----"+out_trade_no);
            ParkingOrders parkingOrderEx = new ParkingOrders();
            parkingOrderEx.setOrderNum(out_trade_no);
            ParkingOrders parkingOrder = parkingOrdersBiz.selectOne(parkingOrderEx);
            Plate plate = plateBiz.selectById(parkingOrder.getPlaId());
            Parking parking = parkingBiz.selectById(parkingOrder.getParkingId());
            // ?????????????????????
            String subject ="????????????";
            BigDecimal money = parkingOrdersBiz.queryCurrCostMoney(DateUtil.getDateTime(),parking,plate,parkingOrder);
            parkingOrder.setRealMoney(money);
            parkingOrdersBiz.saveOrUpdate(parkingOrder);
            // ?????????????????????
            String total_amount = money.toString();
            // ?????????????????????
            String body = "????????????";
            // ???????????? ??????
            String timeout_express = "2m";
            // ??????????????? ??????
            String product_code = "QUICK_WAP_WAY";
            /**********************/
            // SDK ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            //??????RSA????????????
            // ??????appid
            String APPID = ali_app_id;
            // ?????? pkcs8?????????
            String RSA_PRIVATE_KEY = ali_private_sign;
            // ??????????????????
            String URL = "https://openapi.alipay.com/gateway.do";
            // ??????
            String CHARSET = "UTF-8";
            // ????????????
            String FORMAT = "json";
            // ???????????????
            String ALIPAY_PUBLIC_KEY = alipay_public_key;
            // ??????????????????
            String log_path = "/log";
            // RSA2
            String SIGNTYPE = "RSA2";
            AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY,FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

            // ????????????????????????
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            model.setTotalAmount(total_amount);
            model.setBody(body);
            model.setTimeoutExpress(timeout_express);
            model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // ????????????????????????
            alipay_request.setNotifyUrl(ali_notify_url);
            // form????????????
            String form = "";
            try {
                // ??????SDK????????????
                form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=" + CHARSET);
                response.getWriter().write(form);//????????????????????????html???????????????
                response.getWriter().flush();
                response.getWriter().close();
            } catch (AlipayApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public BaseResponse ali_refund(ParkingOrders order) throws AlipayApiException, IllegalAccessException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                ali_app_id, ali_private_sign, "json", "UTF-8",
                alipay_public_keys, "RSA2");
      /*String ali_app_id="2018082361123405";
        String ali_private_sign="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCG8CTx+YPEgc2Y+s+HiWlzs7tu1hMrCZViW4a8h8WbvKHTngDT4JQ1eXMKNKQi74zPCQFVENNpQKlEH3KUs6yWNoSnXPfvcfwN4sgUFwh7wnqII7xfL1o4V9/723ZTU1FznwPjQLUSyKVJ4D1raYTJF56xopwQGzt221IWhV6S4tG+bJv2e9oAdnu7GSrfLWfnB9zVO6tVK7iGgLb1ax9TkwioA4eXWi9Ogd5nhKv4gR57IpqI64IZClfSjqyykLHBnWS7wRkUM0mTMH+s063bWRCUoCZ5qRPu4XAJQaOg/DSbj6ScrmcKNZIN7zcbzH+pxHZ+HtM9slqNoLLDAbWVAgMBAAECggEAOhRC0qPCw2HbTG0nxf6vFM3Xbu1VcsC9O4iJhbwqoc2fGaZ4VLnlEN8lVM8zlpXk1XfayvJjDHp4YInol3h+YGk2z5KEZNt60b86vurrC9m5Q7+d5JpNCtNH5O+hDlxeQpW+5qGDToJuIk4SIZlZPfScqXiEE6qk/ZOI0Yi2HCJEb5k8ewFiNq50hvGmCKTnQtIMDJReTeRTwHgvScZvI3tSgk42LoE/r61dJndLu8DrAc9iv4M8qbmzBJe8x1FfhFhPnLAKYQA3cllxyGQm0c3oNYrWmCCGMY1YcPiDUTPe1E+NsNNtS7c0zlGBe0EBQIrLCmg8BS78R5kf7/UwAQKBgQDAFzkhlQKpc/6kFFTPHgWs9C6Nh76gf5H7udVfkujcMDOn+/RY0THCVa5KJgCOoHoFkjAbU6l8inALUcmFZAJ5AhxcGQi/4ds9CuRLW8U2fvUY8mayknheQk8sHV+E0Dj+tYjEW0PgrzG+4W9GWz/AnoohcirrdSJuaez2hU5VVQKBgQCz1RuN8flJmKjSFaYuysjpMxdSzNTLNpMwMg+JUXe5+QW7Ay1PCjFMukTekTHDJIJ2QNkwhA0OTApIc+TxAgxYNMKho3MFQe4dyexUiTKhRYzTJ8mIft309yz3H0clQvmpsxLo9uotyXoEyNSZE5XtD3mJkH8+7lyBf2yD9qXfQQKBgDQPlzpUym3mtCMAJ6QivOMNIvjUy4NwT75rtTq79ESJA9cJYEYaHGRRGHWcKxZ8w9Vys3sUh6DrXEaVGgr++fSV792+IzLuIZ8/rnQGIUMN0Zu7Rr4rGFhjnFWWeGbkkC3oQXMGUTU6Bj93ldL5tCOeVE2QRbWe/3/xUXnW004pAoGACXjkJqnkM5gy7D3vru9HfeEPDXF0k++f+R3p65W69LTFNICKVmt3BU+aaQ7EzD6UEJ6B0ihid6pRsFdKa9drxwmgZtsxJ8m9PRxxslI3tH7xV/30g5gLIOgUGkAvgyrv85xXFqZK13aRUscxS47YitAR2skdqrbrcHbBxIGtYwECgYAzonjqkvB9eN/wyaSZjJrsfG4EQ+NoEgTWGw5HX3J58uH9SFMgBmHwmHmmMfChWWMU0HtjG4TnptZVGVs/cu+7NVpBrT2i6GwlWO3pV+KQpCS7iIGJQdychMJoJP7L7GTjrHHHqZpj6Y7YBg35lrOJITBC08lbuYuBLpxLR6BAvA==";
        String alipay_public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlT1fu6kjwlSaww4Avfg1Uz4boKojVNMENc6RmM0mfUJznPgASjC4eHDMA7oADMq5Mf32l4OedkyvlzyVg+kDILbcMQhNiE6o4h2YEF4jtJ0WBvCS2w5AZPHvAs59R8JkuPa5ZGuCVdYh6Gt4c5dIF+sT80NtN2WUiWE5ZqffS1c/ztvcn5uN5onmEbjbMwpW+QuMh1jBHDccNI3BZJjyVMrD0Gyo0TRrSObCTB0yKYAqZJmnuBpOlVT15QBV/gzx+o7K3+gyDaR9yhkwQC5QOEDiBxneC1QrwT+oWgjnj8pYGBq9QReBu14HlCaqH0FKzn2+XpcbjYzhul1YuLtJqwIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                ali_app_id, ali_private_sign, "json", "UTF-8",
                alipay_public_key, "RSA2");*/
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayRefund alipayRefund = new AlipayRefund();
        if(order==null){
            BaseResponse baseResponse=new BaseResponse();
            baseResponse.setStatus(201);
            baseResponse.setMessage("??????????????????");
            return baseResponse;
        }
        alipayRefund.setOut_trade_no(order.getOrderNum());//???????????????????????????
        //alipayRefund.setTrade_no("2017091221001004040242043928");//??????????????????????????????
        alipayRefund.setRefund_amount(order.getRealMoney().toString());//????????????
        alipayRefund.setRefund_reason("????????????");//????????????
        alipayRefund.setOut_request_no("HZ01RF001");
        alipayRefund.setOperator_id("OP001");
        alipayRefund.setStore_id("NJ_S_001");
        alipayRefund.setTerminal_id("NJ_T_001");//request.setBizContent(JSONUtil.simpleObjectToJsonStr(alipayRefund));
        request.setBizContent(JSONObject.toJSONString(alipayRefund));//2???????????????????????????????????? ???????????????
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        BaseResponse baseResponse=new BaseResponse();
        if (response.isSuccess()) {
            refundOrder(order);
            System.out.println("????????????");
            baseResponse.setMessage("????????????");
        } else {
            System.out.println("????????????");
            baseResponse.setStatus(201);
            baseResponse.setMessage("????????????");
        }
        return baseResponse;
    }

    public void refundOrder(ParkingOrders order)
    {
        AppUser appUser = appUserBiz.selectById(order.getUserId());
        appUser.setUserType(BaseConstants.user_type.common);
        appUser.setVipOrderNum("");
        appUserBiz.saveOrUpdate(appUser);
        order.setOrderStatus(BaseConstants.OrderStatus.refund);
        parkingOrdersBiz.saveOrUpdate(order);

    }

    /**
     * @Author: HONGLINCHEN
     * @Description:????????????????????????   ??????????????????????????????????????? ???????????????X100 ??????int????????? ?????????????????????????????????
     * @Date: 2017-9-12 11:18
     */
    public BaseResponse wc_refund(ParkingOrders parkingOrders) {
        try{
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(wc_cert_path));
            try {
                keyStore.load(instream, wc_app_mchid.toCharArray());
            }finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,wc_app_mchid.toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf).build();
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            WXapi wXapi=new WXapi();
            String xml =wXapi.wxPayRefund(parkingOrders.getOrderNum(),String.valueOf((int)(parkingOrders.getRealMoney().doubleValue()*100)));

            try {
                StringEntity se = new StringEntity(xml);
                httppost.setEntity(se);
                System.out.println("executing request" + httppost.getRequestLine());
                CloseableHttpResponse responseEntry = httpclient.execute(httppost);
                try {
                    HttpEntity entity = responseEntry.getEntity();
                    System.out.println(responseEntry.getStatusLine());
                    if (entity != null) {
                        System.out.println("Response content length: "
                                + entity.getContentLength());
                        SAXReader saxReader = new SAXReader();
                        org.dom4j.Document document = saxReader.read(entity.getContent());
                        org.dom4j.Element rootElt = document.getRootElement();
                        System.out.println("????????????" + rootElt.getName());
                        System.out.println("==="+rootElt.elementText("result_code"));
                        System.out.println("==="+rootElt.elementText("return_msg"));
                        String resultCode = rootElt.elementText("result_code");
                        JSONObject result = new JSONObject();

                        Document documentXml = DocumentHelper.parseText(xml);
                        Element rootEltXml = documentXml.getRootElement();
                        BaseResponse baseResponse=new BaseResponse();
                        if("SUCCESS".equals(resultCode)){
                            System.out.println("=================prepay_id===================="+ rootElt.elementText("prepay_id"));
                            System.out.println("=================sign===================="+ rootEltXml.elementText("sign"));
                            result.put("weixinPayUrl", rootElt.elementText("code_url"));
                            result.put("prepayId", rootElt.elementText("prepay_id"));
                            result.put("status","success");
                            result.put("msg","success");
                            refundOrder(parkingOrders);
                            baseResponse.setMessage("????????????");

                        }else{
                            result.put("status","false");
                            result.put("msg",rootElt.elementText("err_code_des"));
                            baseResponse.setStatus(201);
                            baseResponse.setMessage(rootElt.elementText("err_code_des"));
                        }
                        return baseResponse;
                    }
                    EntityUtils.consume(entity);
                }
                finally {
                    responseEntry.close();
                }
            }
            finally {
                httpclient.close();
            }
            return null;
        }catch(Exception e){
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("status","error");
            result.put("msg",e.getMessage());
            BaseResponse baseResponse=new BaseResponse();
            baseResponse.setStatus(201);
            baseResponse.setMessage(e.getMessage());
            return baseResponse;
        }
    }


    /**
     * @Author: HONGLINCHEN
     * @Description:????????????????????????   ??????????????????????????????????????? ???????????????X100 ??????int????????? ?????????????????????????????????
     * @Date: 2017-9-12 11:18
     */
    public BaseResponse wc_refunds(ParkingOrders parkingOrders) {
        try{
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File("D:\\apiclient_cert.p12"));
            try {
                keyStore.load(instream, "1514119501".toCharArray());
            }finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,"1514119501".toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf).build();
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            WXapi wXapi=new WXapi();
            String xml =wXapi.wxPayRefund(parkingOrders.getOrderNum(),String.valueOf((int)(parkingOrders.getRealMoney().intValue()*100)));

            try {
                StringEntity se = new StringEntity(xml);
                httppost.setEntity(se);
                System.out.println("executing request" + httppost.getRequestLine());
                CloseableHttpResponse responseEntry = httpclient.execute(httppost);
                try {
                    HttpEntity entity = responseEntry.getEntity();
                    System.out.println(responseEntry.getStatusLine());
                    if (entity != null) {
                        System.out.println("Response content length: "
                                + entity.getContentLength());
                        SAXReader saxReader = new SAXReader();
                        org.dom4j.Document document = saxReader.read(entity.getContent());
                        org.dom4j.Element rootElt = document.getRootElement();
                        System.out.println("????????????" + rootElt.getName());
                        System.out.println("==="+rootElt.elementText("result_code"));
                        System.out.println("==="+rootElt.elementText("return_msg"));
                        String resultCode = rootElt.elementText("result_code");
                        JSONObject result = new JSONObject();

                        Document documentXml = DocumentHelper.parseText(xml);
                        Element rootEltXml = documentXml.getRootElement();
                        BaseResponse baseResponse=new BaseResponse();
                        if("SUCCESS".equals(resultCode)){
                            System.out.println("=================prepay_id===================="+ rootElt.elementText("prepay_id"));
                            System.out.println("=================sign===================="+ rootEltXml.elementText("sign"));
                            result.put("weixinPayUrl", rootElt.elementText("code_url"));
                            result.put("prepayId", rootElt.elementText("prepay_id"));
                            result.put("status","success");
                            result.put("msg","success");
                            refundOrder(parkingOrders);
                            baseResponse.setMessage("????????????");

                        }else{
                            result.put("status","false");
                            result.put("msg",rootElt.elementText("err_code_des"));
                            baseResponse.setStatus(201);
                            baseResponse.setMessage(rootElt.elementText("err_code_des"));
                        }
                        return baseResponse;
                    }
                    EntityUtils.consume(entity);
                }
                finally {
                    responseEntry.close();
                }
            }
            finally {
                httpclient.close();
            }
            return null;
        }catch(Exception e){
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("status","error");
            result.put("msg",e.getMessage());
            BaseResponse baseResponse=new BaseResponse();
            baseResponse.setStatus(201);
            baseResponse.setMessage(e.getMessage());
            return baseResponse;
        }
    }

    public  String qrCodeAliPay(ParkingOrders parkingOrders) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient
                ("https://openapi.alipay.com/gateway.do",
                        ali_app_id,ali_private_sign, "json", "UTF-8", alipay_public_keys, "RSA2");
        //??????????????????AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//??????API?????????request???
        request.setNotifyUrl(ali_notify_url);
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+parkingOrders.getOrderNum()+"\"," +//???????????????
                "    \"total_amount\":\""+parkingOrders.getRealMoney()+"\"," +
                "    \"subject\":\"????????????\"," +
                "    \"store_id\":\"????????????\"," +
                "    \"timeout_express\":\"90m\"}");//?????????????????????????????????
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        Map map=JsonUtil.JsonStringToMap(response.getBody());
        Map maps=(Map)map.get("alipay_trade_precreate_response");
        System.out.println(maps.get("qr_code"));
        return maps.get("qr_code").toString();
    }
}
