package com.smart.iot.pay.rest;

import com.smart.iot.pay.util.CommonUtil;
import com.smart.iot.pay.server.UnionPayParkServer;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("unionPayPark")
@Slf4j
@Api(tags = "银联支付接口")
public class UnionPayParkController {

    @Autowired
    private UnionPayParkServer unionPayParkServer;

    /**
     * 银联支付综合接口
     * @param params
     * @return
     */
    @RequestMapping("unionPay")
    public String pushParkingOrder(Map<String,String> params){
//        SortedMap<String,String> treeMap = Maps.newTreeMap();
//        CommonUtil.paramsParse(request.getParameterMap(),treeMap);
        return unionPayParkServer.pushMsg(params);
    }

    /**
     * 用户场内签约，银联调用接口
     * @param request
     * @return
     */
    @RequestMapping("signContract")
    @IgnoreUserToken
    public Object signContract(HttpServletRequest request){
        log.info("==========请求开始处理=========");
        Map<String, String> params = new HashMap();
        CommonUtil.paramsParse(request.getParameterMap(),params);
        return unionPayParkServer.signContract(params);
    }

    /**
     * 银联查询车辆场内状态接口
     * @param request
     * @return
     */
    @RequestMapping("parkingStatus")
    @IgnoreUserToken
    public Object parkingStatus(HttpServletRequest request){
        log.info("==========请求开始处理=========");
        Map<String, String> params = new HashMap();
        CommonUtil.paramsParse(request.getParameterMap(),params);
        return unionPayParkServer.parkingStatus(params);
    }

}
