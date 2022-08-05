package com.smart.iot.parking.rest;

import com.alipay.api.AlipayApiException;
import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.Plate;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.smart.iot.roadside.biz.ParkingOrdersRSBiz;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.PublicUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingOrders")
@CheckClientToken
@CheckUserToken
@Api(tags = "订单管理")
public class ParkingOrdersController extends BaseController<ParkingOrdersBiz, ParkingOrders, String> {
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public DictFeign dictFeign;
    @Autowired
    public CombinePayBiz combinePayBiz;
    @Autowired
    public ParkingOrdersMapper parkingOrdersMapper;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    @Autowired
    public MergeCore mergeCore;
    @ApiOperation("充值或交押金")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse recharge(@RequestBody ParkingOrders parkingOrders) throws Exception {
        String userId = parkingOrders.getUserId();
        String money = String.valueOf(parkingOrders.getRealMoney());
        String payType = parkingOrders.getPayType();
        String orderType = parkingOrders.getOrderType();
        return parkingOrdersBiz.recharge(userId, money, payType, orderType);
    }

    @ApiOperation("查询订单状态")
    @RequestMapping(value = "/queryByOrderStatus", method = RequestMethod.GET)
    @ResponseBody
    public TableResultPageResponse queryByOrderStatusList(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        //查询列表数据
        //查询列表数据
        Query query = new Query(params);
        String orderType=String.valueOf(params.get("orderType"));
        TableResultPageResponse tableResultPageResponse=parkingOrdersBiz.selectByPageQuery2(query,null,null);
        List<ParkingOrders> parkingOrdersList=tableResultPageResponse.getData().getRows();
        List<ParkingOrders> list=new ArrayList<>();

        for (ParkingOrders parkingOrder : parkingOrdersList) {
            if(StringUtil.isEmpty(orderType) && parkingOrder.getOrderType().equals(BaseConstants.OrderType.recharge)){
                list.add(parkingOrder);
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            Plate plate = plateBiz.selectById(parkingOrder.getPlaId());
            Parking parking = parkingBiz.selectById(parkingOrder.getParkingId());
            if (parking != null && plate != null) {
                map.put("parkingId", parking.getParkingId());
                /*String carType="";
                if(plate.getCarType().equals("2")){
                    carType="auto";
                }else if(plate.getCarType().equals("1")){
                    carType="truck";
                }*/
                map.put("carType", plate.getCarType());
                map.put("chargeRuleType", parking.getChargeRuleId());
                map.put("beginDate", parkingOrder.getBeginDate());
                map.put("orderNum", parkingOrder.getOrderNum());
                if (parkingOrder.getOrderStatus().equals(BaseConstants.OrderStatus.running)) {
                    if (!parkingOrder.getPosition().equals(BaseConstants.Position.unapproach)) {
                        ObjectRestResponse<BigDecimal> objectRestResponse = chargeRulesTypeFeign.queryCostByIdAndParkingId(map);
                        parkingOrder.setRealMoney(objectRestResponse.getData());
                        String date=DateUtil.format(new Date(),null);
                        parkingOrder.setAttr1(date);//系统时间,用于app
                    }
                }

            }
        }
        for(ParkingOrders orders:list){
            parkingOrdersList.remove(orders);
        }
        try {
            mergeCore.mergeResult(ParkingOrders.class,parkingOrdersList);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<ParkingOrdersVo> parkingOrdersVoList=parkingOrdersRSBiz.parkingOrderListMap(parkingOrdersList);
        tableResultPageResponse.getData().setRows(parkingOrdersVoList);
        return tableResultPageResponse;
    }
/*    public TableResultPageResponse<OrderTree> queryByOrderStatusList(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        //查询列表数据
        Query query = new Query(params);
        TableResultPageResponse<ParkingOrders> parkingOrdersTableResultPageResponse = parkingOrdersBiz.selectByPageQuery2(query, null, null);
        List<ParkingOrders> parkingOrders = parkingOrdersTableResultPageResponse.getData().getRows();
        String[] idList = new String[parkingOrders.size()];
        for (int i = 0; i < idList.length; i++) {
            idList[i] = parkingOrders.get(i).getOrderId();
        }
        TreeUtil treeUtil = new TreeUtil();
        List<ParkingOrders> orders = parkingOrdersMapper.selectByParenIds(idList);
        mergeCore.mergeResult(ParkingOrders.class, orders);
        parkingOrders.addAll(orders);
        List<OrderTree> trees = new ArrayList<>();
        parkingOrders.forEach(o -> {
            trees.add(new OrderTree(o.getOrderId(), o.getBeginDate(), o.getEndDate(), o.getReverseDate(), o.getChargeRulesTypeId(), o.getLmId(), o.getLpId(), o.getOrderStatus(), o.getOrderType(), o.getPrivateUserId(), o.getParkingBusType(), o.getParkingId(), o.getParkingLongTime(), o.getPlaId(), o.getRealMoney(), o.getOrderNum(), o.getOrderNumMd5(), o.getSpaceId(), o.getUdId(), o.getUserId(), o.getChargeId(), o.getChargeDate(), o.getPlatenumImage(), o.getPayStatus(), o.getPayType(), o.getPosition(), o.getCreateOrderType(), o.getErrorType(), o.getMonthCardPrice(), o.getMonthCount(), o.getParentId()));
        });
        List<OrderTree> tree = treeUtil.bulid(trees, "-1", null);
*//*
        mergerDictParams(parkingOrdersTableResultPageResponse.getData().getRows(),"order_status");
*//*
        TableResultPageResponse<OrderTree> treeTableResultPageResponse = new TableResultPageResponse<OrderTree>();
        treeTableResultPageResponse.getData().setRows(tree);
        treeTableResultPageResponse.getData().setLimit(parkingOrdersTableResultPageResponse.getData().getLimit());
        treeTableResultPageResponse.getData().setOffset(parkingOrdersTableResultPageResponse.getData().getOffset());
        treeTableResultPageResponse.getData().setTotal(parkingOrdersTableResultPageResponse.getData().getTotal());
        return treeTableResultPageResponse;
    }*/
    public List<ParkingOrders> mergerDictParams(List<ParkingOrders> ordersList, String code) {
        Map<String, String> dictMap = dictFeign.getDictValues(code);
        for (int i = 0; i < ordersList.size(); i++) {
            for (String key : dictMap.keySet()) {
                if (ordersList.get(i).getOrderStatus().equals(key)) {
                    ordersList.get(i).setOrderStatus(dictMap.get(key));
                }
            }
        }
        return ordersList;
    }

    /**
     * 微信退款订单接口
     */
    @RequestMapping(value = "/vip_refund")
    public BaseResponse vip_refund(@RequestBody ParkingOrders parkingOrders) throws JDOMException, IOException, IllegalAccessException, AlipayApiException {
        AppUser appUser = appUserBiz.selectById(parkingOrders.getUserId());
        if (appUser.getUserType().equals(BaseConstants.user_type.common)) {
            return new BaseResponse(BaseConstants.StateConstates.MESSAGE_FAIL_CODE, BaseConstants.StateConstates.MESSAGE_FAIL_MSG);
        }
        ParkingOrders orderEx = new ParkingOrders();
        orderEx.setOrderNum(appUser.getVipOrderNum());
        ParkingOrders order = parkingOrdersBiz.selectOne(orderEx);
        if (order != null && StringUtil.isNotNull(order.getPayType()) && order.getPayType().equals(BaseConstants.payType.wechat)) {
            return combinePayBiz.wc_refund(order);
        } else {
            return combinePayBiz.ali_refund(order);
        }
    }

    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/queryParentOrderList/{parentId}")
    public List queryParentOrderList(@PathVariable("parentId") String parentId) throws JDOMException, IOException, IllegalAccessException, AlipayApiException {
        Example example=new Example(ParkingOrders.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        example.orderBy("beginDate");
        List<ParkingOrders> parkingOrdersList=parkingOrdersBiz.selectByExample(example);
        ParkingOrders orders=parkingOrdersBiz.selectById(parentId);
        List list=new ArrayList();
        for(ParkingOrders parkingOrders1:parkingOrdersList){
            Map<String, Object> result = PublicUtil.object2Map(parkingOrders1);
            list.add(result);
        }
        list.add(PublicUtil.object2Map(orders));
        return list;
    }


    @IgnoreClientToken
    @IgnoreUserToken
    @RequestMapping(value = "/queryOrder/{orderNum}",method = RequestMethod.GET)
    public Map<String,Object> queryOrder(@PathVariable("orderNum") String orderNum){
        ObjectRestResponse entityObjectRestResponse = new ObjectRestResponse<>();
        ParkingOrders orders=new ParkingOrders();
        orders.setOrderNum(orderNum);
        Object o = baseBiz.selectOne(orders);
        Map<String, Object> result = PublicUtil.object2Map(o);
        return result;
    }

    @ApiOperation("总览")
    @RequestMapping(value = "/pandect",method = RequestMethod.POST)
    public Map pandect(Map params){
        return baseBiz.pandect(params);
    }

    @ApiOperation("出入场订单数")
    @RequestMapping(value = "/countOrders")
    public Map countOrders(@RequestParam Map params){
        return baseBiz.countOrders(params);
    }

    @ApiOperation("当日每时订单额")
    @RequestMapping(value = "/sumMoneyByEnd")
    public List sumMoneyByEnd(@RequestParam Map params){
        return baseBiz.sumMoneyByEnd(params);
    }

    @ApiOperation("查询订单总数")
    @RequestMapping(value = "/queryOrdersAllCount",method = RequestMethod.POST)
    public Object queryOrdersAllCount(@RequestParam Map params){
        return baseBiz.queryOrdersAllCount(params);
    }
    @ApiOperation("订单列表查询")
    @RequestMapping("/queryOrdersByPage")
    public TableResultPageResponse<Object> queryOrdersByPage(@RequestParam Map params){
        return baseBiz.queryOrdersByPage(params);
    }

    @ApiOperation("按照时间查询订单图表数据")
    @RequestMapping(value = "/queryOrdersCountByTime")
    public Object queryOrdersCountByTime(@RequestParam Map params){
        return baseBiz.queryOrdersCountByTime(params);
    }

    @ApiOperation("用户活跃度")
    @RequestMapping(value = "/userActiveTimes")
    public Object userActiveTimes(){
        return baseBiz.userActiveTimes();
    }

    @ApiOperation("停车平均时长")
    @RequestMapping(value = "/queryOrdersTimeByTime")
    public Object queryOrdersTimeByTime(@RequestParam Map params){
        return baseBiz.queryOrdersTimeByTime(params);
    }

    @ApiOperation("营收同比")
    @RequestMapping(value = "/queryMonthCount")
    public Map queryMonthCount(@RequestParam Map params){
        Map primary = Maps.newHashMap();
        Map secondary = Maps.newHashMap();
        primary.put("year",params.get("primaryYear"));
        secondary.put("year",params.get("secondaryYear"));
        if(params.get("address")!=null){
            primary.put("address",params.get("address"));
            secondary.put("address",params.get("address"));
        }
        if(params.get("parkingId")!=null){
            primary.put("parkingId",params.get("parkingId"));
            secondary.put("parkingId",params.get("parkingId"));
        }
        String primaryQueryMonth = new String(params.get("primaryMonth").toString());
        String secondaryQueryMonth = new String(params.get("secondaryMonth").toString());
        if(primaryQueryMonth.contains(",")){
            String[] primaryMonths = primaryQueryMonth.split(",");
            String[] secondaryMonths = secondaryQueryMonth.split(",");
            if(primaryMonths.length>=3){
                primary.put("firstMonth",primaryMonths[0]);
                primary.put("secondMonth",primaryMonths[1]);
                primary.put("thirdMonth",primaryMonths[2]);
            }
            if(secondaryMonths.length>=3){
                secondary.put("firstMonth",secondaryMonths[0]);
                secondary.put("secondMonth",secondaryMonths[1]);
                secondary.put("thirdMonth",secondaryMonths[2]);
            }
        }else{
            primary.put("month",primaryQueryMonth);
            secondary.put("month",secondaryQueryMonth);
        }
        return baseBiz.queryMonthCount(primary,secondary);
    }

    @ApiOperation("总营收")
    @RequestMapping(value = "/totalRevenueCount")
    public Object totalRevenueCount(@RequestParam Map params){
        return baseBiz.totalRevenueCount(params);
    }

    @ApiOperation("支付类型")
    @RequestMapping(value = "/payTypeChart")
    public Object payTypeChart(@RequestParam Map params){
        return baseBiz.payTypeChart(params);
    }


}
