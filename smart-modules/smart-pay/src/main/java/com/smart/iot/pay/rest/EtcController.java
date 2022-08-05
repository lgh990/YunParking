package com.smart.iot.pay.rest;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.smart.iot.pay.entity.AlipayRefund;
import com.smart.iot.pay.netty.NettyClient;
import com.smart.iot.pay.server.AliPayServer;
import com.smart.iot.pay.util.CommonUtil;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("etc")
@Api(tags = "alipay接口")
public class EtcController {

    @RequestMapping("test")
    public String test(){
        String ip = "";
        int port = 0;
        NettyClient nettyClient = new NettyClient();
        try {
            nettyClient.connect(ip,port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }



}
