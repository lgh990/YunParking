package com.smart.iot.pay.rest;

import com.smart.iot.pay.util.CommonUtil;
import com.smart.iot.pay.server.WeChatPayServer;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("weChatPay")
@Api(tags = "wechatPay接口")
public class WeChatPayController {

    @Autowired
    private WeChatPayServer weChatPayServer;

    /**
     * 微信下单接口
     * @param params
     * desc 商品描述
     * orderNum 订单号
     * money 订单金额
     * ip 服务ip
     * @return
     */
    @ApiOperation("微信下单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "desc", value = "商品描述", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="body", name = "orderNum", value = "订单号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "money", value = "订单金额", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "ip", value = "服务ip",required = true, dataType = "String")
    })
    @PostMapping(value = "/wechatPay")
    public Map<String, Object> wechatPay(Map<String,Object> params){
        return weChatPayServer.wechatPay(params);
    }

    /**
     * 微信结果通知，被微信端回调接口
     * @param request
     * result_code
     * out_trade_no
     * @return
     */
    @ApiOperation("微信结果通知")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "result_code", value = "结果码", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="body", name = "out_trade_no", value = "订单号", required = true, dataType = "String")
    })
    @PostMapping(value = "/notify")
    @IgnoreUserToken
    public String wechatNotify(HttpServletRequest request){
        // 读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in;
        try {
            inputStream = request.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null)
            {
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            log.info("=================微信回调参数获取错误！");
            e.printStackTrace();
        }
        // 解析xml成map
        Map<String, String> m = CommonUtil.doXMLParse(sb.toString());
        Iterator it = m.keySet().iterator();
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap();
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
        Map<String, String> params = new HashMap();
        CommonUtil.paramsParse(request.getParameterMap(),params);
        return weChatPayServer.wechatNotify(params);
    }

    /**
     * 微信退款
     * @param params
     * orderNum 订单号
     * money 订单金额
     * @return
     * @throws Exception
     */
    @ApiOperation("微信退款")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "orderNum", value = "订单号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "money", value = "订单金额", required = true, dataType = "String")
    })
    @PostMapping(value = "/wechatRefund")
    public BaseResponse wechatRefund(Map<String,String> params){
        return weChatPayServer.wechatRefund(params);
    }

    /**
     * 微信账单数据拉取
     * @param date
     * date 账单日期
     * @return
     */
    @ApiOperation("微信退款")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "date", value = "账单日期", required = true, dataType = "String")
    })
    @PostMapping(value = "/getWechatBill")
    @IgnoreUserToken
    public String getWechatBill(String date) {
        return weChatPayServer.wechatBill(date);
    }

}
