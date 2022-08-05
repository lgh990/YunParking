package com.smart.iot.parking.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.smart.iot.parking.biz.CombinePayBiz;
import com.smart.iot.parking.biz.ParkingOrdersBiz;
import com.smart.iot.parking.biz.PlateBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.utils.WXapi;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/pay")
@Slf4j
@IgnoreUserToken
@Api(tags = "支付管理")
public class CombinePayController {
    @Autowired
    private CombinePayBiz callbackBiz;
    @Autowired
    private WXapi wXapi;
    @Autowired
    private ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    private PlateBiz plateBiz;
    @Autowired
    private MergeCore mergeCore;

    @RequestMapping(value = "/get_alipay_config")
    @IgnoreUserToken
    @ApiOperation("支付宝二维码支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "WIDout_trade_no", value = "商户订单号", required = true, dataType = "String")
    })
    public void get_web_alipay_config(HttpServletResponse response, HttpServletRequest request) throws Exception {
        log.info("==================进入支付宝二维码支付");
        callbackBiz.get_alipay_config(request, response);
    }

    @RequestMapping(value = "/queryOrderStatus")
    @IgnoreUserToken
    @ApiOperation("查询订单是否支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "params", value = "params{ordernum:'订单号'}", required = true, dataType = "String")
    })
    public BaseResponse queryOrderStatus(HttpServletResponse response, HttpServletRequest request, String params) throws Exception {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(params);
        JSONObject map = (JSONObject) jsonObject.get("params");
        request.setCharacterEncoding("UTF-8");
        String ordernum = String.valueOf(map.get("ordernum"));
        ParkingOrders parkingOrderEx = new ParkingOrders();
        parkingOrderEx.setOrderNum(ordernum);
        ParkingOrders parkingOrders = parkingOrdersBiz.selectOne(parkingOrderEx);
        if (parkingOrders != null && StringUtil.isNotNull(parkingOrders.getOrderStatus())) {
            if (parkingOrders.getPayStatus().equals(BaseConstants.status.fail)) {
                return new BaseResponse();
            }
        }
        return new BaseResponse(100, "");

    }


    /**
     * 获取openid
     * <p>
     * code
     */
    @RequestMapping(value = "/query_opnId")
    @IgnoreUserToken
    @ApiOperation("获取openid")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "code", value = "微信code", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "state", value = "微信状态", required = true, dataType = "String")
    })
    public void jsCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        wXapi.query_openId(code, response, request, state);
    }

    /**
     * 微信回调
     *
     * @param request
     * @param response
     */
    @ApiOperation("微信回调")
    @PostMapping(value = "/pda_weixin_notify")
    public void pda_weixin_notify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, JDOMException {
        wXapi.pda_weixin_notify(request, response);
    }

    /**
     * 获取openid
     * <p>
     * code
     */
    @RequestMapping(value = "/topayServlet")
    @IgnoreUserToken
    public ObjectRestResponse topayServlet(HttpServletResponse response, HttpServletRequest request, HttpSession session, String params) throws Exception {
        log.info("APP接口请求参数:" + params);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(params);
        JSONObject map = (JSONObject) jsonObject.get("params");
        request.setCharacterEncoding("UTF-8");
        String ordernum = String.valueOf(map.get("ordernum"));
        ParkingOrders parkingOrderEx = new ParkingOrders();
        parkingOrderEx.setOrderNum(ordernum);
        ParkingOrders parkingOrders = parkingOrdersBiz.selectOne(parkingOrderEx);
        return wXapi.topayServlet(response, request, map, parkingOrders);
    }

    /**
     * 支付宝支付结果
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/alipay_notify")
    public String alipay_notify(HttpServletRequest request) throws Exception {
        return callbackBiz.alipay_notify(request);
    }

    /**
     * 微信支付结果
     *
     * @param request
     * @param response
     */
    @PostMapping(value = "/wechat_notify")
    public void wechat_notify(HttpServletRequest request, HttpServletResponse response) {
        callbackBiz.wechat_notify(request, response);
    }

    /*@RequestMapping(value = "/queryPlateList")
    @IgnoreUserToken
    public ObjectRestResponse queryPlateList(HttpServletResponse response,HttpServletRequest request,String params) throws Exception
    {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(params);
        JSONObject map = (JSONObject) jsonObject.get("params");
        request.setCharacterEncoding("UTF-8");
        String carNumber = String.valueOf(map.get("carNumber"));
        Example example = new Example(Plate.class);
        example.createCriteria().andLike("carNumber","%"+carNumber+"%");
        List<Plate> plateList=plateBiz.selectByExample(example);
        return new ObjectRestResponse().data(plateList);
    }*/

    @RequestMapping(value = "/queryOrderList")
    @IgnoreUserToken
    public ObjectRestResponse queryOrderList(HttpServletResponse response, HttpServletRequest request, String params) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(params);
        JSONObject map = (JSONObject) jsonObject.get("params");
        request.setCharacterEncoding("UTF-8");
        String carNumber = String.valueOf(map.get("carNumber"));
        Plate plateParams = new Plate();
        plateParams.setCarNumber(carNumber);
        plateParams.setEnabledFlag(BaseConstants.enabledFlag.y);
        Plate plate = plateBiz.selectOne(plateParams);
        List<ParkingOrders> ordersList = new ArrayList<>();
        if (plate != null) {
            Example example = new Example(ParkingOrders.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotEqualTo("payStatus", BaseConstants.status.success)
                    .andEqualTo("plaId", plate.getPlaId())
                    .andNotEqualTo("orderType", BaseConstants.OrderType.recharge)
                    .andNotEqualTo("orderType", BaseConstants.OrderType.monthCard);
            ordersList = parkingOrdersBiz.selectByExample(example);
            mergeCore.mergeResult(ParkingOrders.class,ordersList);
        }
        for(ParkingOrders parkingOrder:ordersList)
        {
            if(parkingOrder.getOrderStatus().equals(BaseConstants.OrderStatus.running))
            {
                Parking parking = JSON.parseObject(parkingOrder.getParkingId(), Parking.class);
                BigDecimal money = parkingOrdersBiz.queryCurrCostMoney(null, parking, plate, parkingOrder);
                parkingOrder.setRealMoney(money);
                int r=money.compareTo(new BigDecimal(0));
                if(r==-1 || r==0){
                    ordersList.remove(parkingOrder);
                    if(ordersList.size()==0){
                        break;
                    }
                }
            }
        }
        return new ObjectRestResponse().data(ordersList);
    }


}
