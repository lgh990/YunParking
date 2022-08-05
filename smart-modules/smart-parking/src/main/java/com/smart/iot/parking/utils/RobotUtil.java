package com.smart.iot.parking.utils;

import com.smart.iot.parking.entity.ParkingIo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class RobotUtil {

    /**
     * 语言
     * @param params
     * @return
     */
    public static String videoMessage(Map<String,String> params, ParkingIo parkingIo) {
        String path = "/localCenter/videoMessage";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        try {
            HttpResponse response = HttpUtils.doPost("http://"+parkingIo.getAttr2(), path, method, headers, params, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 屏显
     * @param params
     * @return
     */
    public static String screenMessage(Map<String,String> params, ParkingIo parkingIo) {
        String path = "/localCenter/screenMessage";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        try {
            HttpResponse response = HttpUtils.doPost("http://"+parkingIo.getAttr2(), path, method, headers, params, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 支付信息展示接口
     * payCode收费二维码文本信息 String Y 二维码信息 URL（用于生成二维码）
     * payNum 支付单号 String Y  订单号
     * payWay 运营性质字段 String Y  二维码上方提示信息（推荐使用 XXX）
     * txtMsg屏显内容四段格式 Sting Y
     *  屏显内容四段（车牌号、缴费金额、停车时长、车辆类型“临时车、月卡车”） 英文逗号隔开（txtMsg）
     * @param params
     * @return
     */
    public static String payMsg(Map<String,String> params, ParkingIo parkingIo) {
        String path = "/localCenter/payMsg";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        try {
            HttpResponse response = HttpUtils.doPost("http://"+parkingIo.getAttr2(), path, method, headers, params, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 支付结果展示接口
     payStatus 支付状态 String Y  支付状态成功为 1，其他为-1（只有状态为1 才会展示该接口对应的界面）
     payNum 支付单号 String Y  支付订单号，不影响界面展示
     screenMsg 运营型字段 String Y  提示性文字
     invoiceURL 二维码信息 String Y  用来生成二维码的信息
     * @param params
     * @return
     */
    public static String payResult(Map<String,String> params,ParkingIo parkingIo) {
        String path = "/localCenter/payResult";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        try {
            HttpResponse response = HttpUtils.doPost("http://"+parkingIo.getAttr2(), path, method, headers, params, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * postPrintMsg
     eadMsg 头部信息 String Y ● 小票的标题信息
     qrCodeMsg 二维码信息 String Y ● 小票的二维码信息
     bottomMsg 底部提示信息 String Y ● 小票的底部提示信息
     * @param params
     * @return
     */
    public static String postPrintMsg(Map<String,String> params,ParkingIo parkingIo) {
        String path = "/localCenter/postPrintMsg";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        try {
            HttpResponse response = HttpUtils.doPost("http://"+parkingIo.getAttr2(), path, method, headers, params, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws InterruptedException {
     /*  HashMap<String,String> params=new HashMap<>();
        params.put("msg","云创智城停车欢迎您,粤B88888,临时车");
        screenMessage(params);
        videoMessage(params);

       Thread.sleep(10000);
        Map<String,String> params2=new HashMap<>();
        params2.put("msg","云创智城停车 祝你一路顺风,粤B88888,临时车,请缴费4元");
        videoMessage(params2);

        //支付
        Map<String,String> params1=new HashMap<>();
        params1.put("payCode","https://www.hao123.com/");
        params1.put("payNum","201909271812087956");
        params1.put("payWay","推荐使用支付宝支付");
        params1.put("txtMsg","粤B88888,4元,0天4小时44分钟,临时车");
        payMsg(params1);
        Thread.sleep(10000);
        //支付结果
        Map<String,String> params4=new HashMap<>();
        params4.put("payStatus","1");
        params4.put("payNum","201909271812087956");
        params4.put("screenMsg","请扫描屏幕二维码根据提示领取发票");
        params4.put("invoiceURL","https://www.hao123.com/");
        payResult(params4);*/



        //打印小票
        /*Map<String,String> params3=new HashMap<>();
        params3.put("headMsg","微信或支付宝扫描电子红包找零");
        params3.put("qrCodeMsg","https://www.hao123.com");
        params3.put("bottomMsg","提示:小票请在24小时内领取");
        postPrintMsg(params3);*/
    }

}
