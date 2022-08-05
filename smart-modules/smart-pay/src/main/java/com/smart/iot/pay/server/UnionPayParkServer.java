package com.smart.iot.pay.server;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.pay.connect.HttpRequest;
import com.smart.iot.pay.feign.ParkingOrderFeign;
import com.smart.iot.pay.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UnionPayParkServer {

    @Autowired
    private HttpRequest httpRequest;

    @Autowired
    private Map<String,String> requestProperty;

    @Autowired                      
    private ParkingOrderFeign parkingOrderFeign;

    /**
     * 银联支付统配连接银联服务
     * @return
     */
    public String pushMsg(Map<String,String> params){
        Map<String,String> lowerMap = caseToLowerMap(params);
        String lowerStr = caseToString(lowerMap);
        String sign = CommonUtil.makeEncrypt(lowerStr,"SHA-256");
        log.info("====sign:"+sign);
        params.put("Sign",sign);
        params.remove("Key");
        String paramStr = caseToString(params);
        log.info(paramStr);
        return httpRequest.request(requestProperty.get("unionpayUrl"),"POST",paramStr);
    }

    /**
     * 银联推送新加签约
     * @param params
     * @return
     */
    public JSONObject signContract(Map<String,String> params){
        Set keys = params.keySet();
        Iterator iterator = keys.iterator();
        for (;iterator.hasNext();){
            String key = (String) iterator.next();
            log.info("============================"+key + " : " + params.get(key));
        }
        //调用业务服务，通知该客户签约银联闪付
//        parkingOrderFeign.pushNewContract(params); 
        return getResponseObject("40702");
    }

    /**
     * 银联查询停车状态
     * @param params
     * @return
     */
    public JSONObject parkingStatus(Map<String,String> params){
        Set keys = params.keySet();
        Iterator iterator = keys.iterator();
        for (;iterator.hasNext();){
            String key = (String) iterator.next();
            log.info("============================"+key + " : " + params.get(key));
        }
        JSONObject responseObject = getResponseObject("40802");
        //调用业务服务，获取停车状态后拼装报文
//        Map parkingMap = parkingOrderFeign.queryStatus(params);
//        if(parkingMap==null){
//            responseObject.put("ResultCode","07");
//            responseObject.put("ResultMsg","没找到停车信息");
//        }else {
//            responseObject.put("Status",parkingMap.get("Status"));
//            responseObject.put("ExitTime",parkingMap.get("ExitTime"));
//        }
        responseObject.put("Status","1");
        responseObject.put("ExitTime","");
        return responseObject;
    }

    /**
     * 将参数转为key1=value1&key2=value2的串
     * @param params
     * @return
     */
    private String caseToString(Map<String,String> params){
        Set keys = params.keySet();
        StringBuffer bufferString = new StringBuffer();
        for (Iterator it = keys.iterator();it.hasNext();){
            String key = (String) it.next();
            bufferString.append(key+"="+params.get(key));
            if(it.hasNext()){
                bufferString.append("&");
            }
        }
        log.info("map转url串"+bufferString.toString());
        return bufferString.toString();
    }

    /**
     *
     * @param params
     * @return
     */
    private Map<String,String> caseToLowerMap(Map<String,String> params){
        Set keys = params.keySet();
        SortedMap<String,String> map = new TreeMap();
        for (Iterator it = keys.iterator();it.hasNext();){
            String key = (String) it.next();
            String lowerkey = key.toLowerCase();
            map.put(lowerkey,params.get(key));
        }
        return map;
    }

    private JSONObject getResponseObject(String transId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TransId",transId);
        jsonObject.put("ResultCode","00");
        jsonObject.put("ResultMsg","");
        jsonObject.put("Remark","");
        return jsonObject;
    }

}
