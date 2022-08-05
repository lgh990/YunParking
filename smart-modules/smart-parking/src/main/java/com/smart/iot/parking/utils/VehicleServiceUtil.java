package com.smart.iot.parking.utils;



import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class VehicleServiceUtil {

    @Autowired
    private static RedisTemplate<String, String> redisTemplate;
    public static  String host = "https://jisuclwhxx.market.alicloudapi.com";
    public static  String method = "GET";
    public static String appcode = "3589bc0b6ef84acba739f3579bd77832";
    public static long time = 24 * 60 * 60 * 1000*7 ;
    /**
     * 根据车牌号和发动机号校验车辆信息
     * @param params
     * @return
     */
    public static String VerificationVehicleInfo(Map<String,String> params) {
        String host = "http://cheliang.market.alicloudapi.com";
        String path = "/clouds/verify/carInfoCheck";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "{\"carNo\":\""+params.get("carNo")+"\",\"enginNo\":\""+params.get("enginNo")+"\",\"type\":\""+params.get("type")+"\"}";
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
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
     * 获取城市接口
     * @return
     */
    public static String queryCity() {
        String path = "/vehiclelimit/city";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            log.info(response.toString());
            //获取response的body
            log.info(response.getEntity().toString());
            String msg=EntityUtils.toString(response.getEntity());
             return msg;
             } catch (Exception e) {
                e.printStackTrace();
            }
        return "";
    }

    /**
     * 城市限行查询接口
     * @param map
     * @return
     */
    public static String qureyCityLimitRow(Map<String,String> map) {
        String path = "/vehiclelimit/query";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("city", map.get("city"));
        querys.put("date", map.get("date"));
        log.info(querys.toString());
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            log.info(response.toString());
            //获取response的body
            log.info(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 车架号VIN查询车辆信息
     * @param params
     * @return
     */
    public static String queryVinVehicleInfo(Map<String,String> params) {
        String host = "https://ali-vin.showapi.com";
        String path = "/vin";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("vin", params.get("frameno"));
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            log.info(response.toString());
            //获取response的body
            log.info(response.getEntity().toString());
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 驾驶证扣分查询
     * @param params
     * @return
     */
    public static String scoreDeductionInquiry(Map<String,String> params) {
        String host = "https://jisujszkf.market.alicloudapi.com";
        String path = "/driverlicense/query";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("licenseid", params.get("licenseid"));
        querys.put("licensenumber", params.get("licensenumber"));
            try {
                HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
                log.info(response.toString());
                //获取response的body
                log.info(response.getEntity().toString());
                return EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "";
    }

    /**
     * 全国车辆违章记录查询 （支持新能源车、大型货车查询）
     * @param params
     * @return
     */
    public static String recordsOfViolations(Map<String,String> params){
        String host = "https://carwz.shumaidata.com";
        String path = "/post/carwz";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
            try {
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, params);
                log.info(response.toString());
                //获取response的body
                log.info(response.getEntity().toString());
                return EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "";
    }
}
