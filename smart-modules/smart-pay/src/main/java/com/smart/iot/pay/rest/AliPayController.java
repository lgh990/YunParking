package com.smart.iot.pay.rest;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.smart.iot.pay.entity.AlipayRefund;
import com.smart.iot.pay.util.CommonUtil;
import com.smart.iot.pay.server.AliPayServer;
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
@RequestMapping("aliPay")
@Api(tags = "alipay接口")
public class AliPayController {

    @Autowired
    private AliPayServer aliPayServer;

    @RequestMapping("test")
    public String test(){
        return "success";
    }

    /**
     * 支付宝支付订单接口，返回支付订单
     * @param params
     * tradeNo 订单号
     * subject 订单名称
     * money 订单金额
     * desc 描述
     * timeout 超时时间
     * productCode 销售产品码
     * @return
     */
    @ApiOperation("支付宝下单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "tradeNo", value = "订单号", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="body", name = "subject", value = "订单名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "money", value = "订单金额", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "desc", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "timeout", value = "超时时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "productCode", value = "销售产品码", required = true, dataType = "String")
    })
    @PostMapping(value = "/getAliPayForm")
    public String getAliPayForm(Map<String,String> params){
        return aliPayServer.getAliPayForm(params);
    }

    /**
     * 支付宝支付结果回调，支付宝调用该接口
     * @param request
     * @return
     */
    @PostMapping(value = "/notify")
    @IgnoreUserToken
    public String notify(HttpServletRequest request){
        Map<String, String> params = new HashMap();
        CommonUtil.paramsParse(request.getParameterMap(),params);
        return aliPayServer.notify(params);
    }

    /**
     * alipay退款服务
     * @param alipayRefund
     * @return
     */
    @ApiOperation("alipay退款服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "out_trade_no", value = "商户订单号", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="body", name = "trade_no", value = "支付宝交易号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "refund_amount", value = "退款金额", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "refund_reason", value = "退款的原因说明", dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "out_request_no", value = "标识一次退款请求", dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "operator_id", value = "商户的操作员编号", dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "store_id", value = "商户的门店编号", dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "terminal_id", value = "商户的终端编号", dataType = "String")
    })
    @PostMapping(value = "/aliRefund")
    public AlipayTradeRefundResponse aliRefund(AlipayRefund alipayRefund){
        return aliPayServer.aliRefund(alipayRefund);
    }

    /**
     * alipay账单数据
     * @param date
     * @return
     */
    @ApiOperation("alipay账单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "date", value = "账单日期", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/getAliPayBill")
    @IgnoreUserToken
    public String getAliPayBill(String date){
        return aliPayServer.getAliPayBill(date);
    }

}
