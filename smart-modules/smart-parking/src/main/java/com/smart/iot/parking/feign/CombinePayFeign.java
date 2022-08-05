package com.smart.iot.parking.feign;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.smart.iot.parking.utils.AlipayRefund;
import com.yuncitys.smart.parking.auth.client.config.FeignApplyConfiguration;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付模块接口
 */
@FeignClient(value = "smart-pay-server", configuration = FeignApplyConfiguration.class)
public interface CombinePayFeign {

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
    @PostMapping(value = "/aliPay/getAliPayForm")
    String getAliPayForm(Map<String, String> params);

    /**
     * 支付宝支付结果回调，支付宝调用该接口
     * @param request
     * @return
     */
    @PostMapping(value = "/aliPay/notify")
    String notify(HttpServletRequest request);

    /**
     * alipay退款服务
     * @param alipayRefund
     * @return
     */
    @PostMapping(value = "/aliPay/aliRefund")
    AlipayTradeRefundResponse aliRefund(AlipayRefund alipayRefund);

    /**
     * 微信退款
     * @param params
     * orderNum 订单号
     * money 订单金额
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/weChatPay/wechatRefund")
    BaseResponse wechatRefund(Map<String, String> params);

    /**
     *微信结果通知，被微信端回调接口
     * @param request
     * result_code
     * out_trade_no
     * @return
     */
    @PostMapping(value = "/weChatPay/notify")
    String wechatNotify(HttpServletRequest request);

    /**
     * 微信下单接口
     * @param params
     * desc 商品描述
     * orderNum 订单号
     * money 订单金额
     * ip 服务ip
     * @return
     */
    @PostMapping(value = "/weChatPay/wechatPay")
    Map<String, Object> wechatPay(Map<String, Object> params);

    /**
     * 银联支付综合接口
     * @param params
     * @return
     */
    @PostMapping("/unionPayPark/unionPay")
    String pushMsg(Map<String, String> params);

}
