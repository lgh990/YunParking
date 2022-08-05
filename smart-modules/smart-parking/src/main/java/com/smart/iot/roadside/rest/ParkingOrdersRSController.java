package com.smart.iot.roadside.rest;

import com.alibaba.fastjson.JSON;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.smart.iot.roadside.biz.ParkingOrdersRSBiz;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingOrdersRS")
@CheckClientToken
@CheckUserToken
@Api(tags = "室外订单")
public class ParkingOrdersRSController extends BaseController<ParkingOrdersRSBiz,ParkingOrders,String> {
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRsBiz;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingOrdersMapper parkingOrdersMapper;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;

    @Override
    public TableResultResponse list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        String beginDate="";
        String endDate="";
        if(params.get("beginTime")!=null && params.get("endTime")!=null) {
            beginDate = String.valueOf(params.get("beginTime"));
            endDate=String.valueOf(params.get("endTime"));
        }
        params.remove("beginTime");
        params.remove("endTime");
        String userID = BaseContextHandler.getUserID();
        List<String> parkingIdList=userParkingBiz.queryParkingIdList(userID);
        Query query = new Query(params);
        Example example = new Example(ParkingOrders.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            criteria.andBetween("crtTime", beginDate, endDate);
        }
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        List<ParkingOrders> list=new ArrayList<>();
        if(parkingIdList!=null && parkingIdList.size()>0) {
            criteria.andIn("parkingId", parkingIdList);
        }else{
            return new TableResultResponse<ParkingOrders>(0, list);
        }
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        list = parkingOrdersBiz.selectByExample(example);
        if(params.get("orderStatus").equals("running")) {
            for (ParkingOrders parkingOrder : list) {
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
                    ObjectRestResponse<BigDecimal> objectRestResponse = chargeRulesTypeFeign.queryCostByIdAndParkingId(map);
                    parkingOrder.setRealMoney(objectRestResponse.getData());
                    parkingOrder.setAttr1(DateUtil.format(new Date(), null));//系统时间,用于app倒计时
                }
            }
        }
        try {
            mergeCore.mergeResult(ParkingOrders.class,list);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<ParkingOrdersVo> parkingOrdersVoList=parkingOrdersRsBiz.parkingOrderListMap(list);
        return new TableResultResponse(result.getTotal(), parkingOrdersVoList);
    }




    @ApiOperation("app订单")
    @RequestMapping(value = "/appOrderPage",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<ParkingOrdersVo> appPageOrder(@RequestParam Map<String, Object> params) {
        //查询列表数据
        String beginDate="";
        String endDate="";
        if(params.get("beginTime")!=null && params.get("endTime")!=null) {
            beginDate = String.valueOf(params.get("beginTime"));
            endDate=String.valueOf(params.get("endTime"));
        }
        params.remove("beginTime");
        params.remove("endTime");
        Query query = new Query(params);
        Example example = new Example(ParkingOrders.class);
        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            example.createCriteria().andBetween("crtTime", beginDate, endDate);
        }
        Example.Criteria criteria = example.createCriteria();
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<ParkingOrders>  list = parkingOrdersBiz.selectByExample(example);
        for (ParkingOrders parkingOrder : list) {
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
                ObjectRestResponse<BigDecimal> objectRestResponse = chargeRulesTypeFeign.queryCostByIdAndParkingId(map);
                parkingOrder.setRealMoney(objectRestResponse.getData());
                parkingOrder.setAttr1(DateUtil.format(new Date(),null));//系统时间,用于app倒计时
            }
        }
        try {
            mergeCore.mergeResult(ParkingOrders.class,list);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<ParkingOrdersVo> parkingOrdersVoList=baseBiz.parkingOrderListMap(list);
        return new TableResultResponse<ParkingOrdersVo>(result.getTotal(), parkingOrdersVoList);
    }

    @Override
    public ObjectRestResponse<ParkingOrders> get(@PathVariable String id){
        ObjectRestResponse<ParkingOrders> entityObjectRestResponse = new ObjectRestResponse<>();
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("orderId",id);
        Query query = new Query(params);
        TableResultPageResponse<ParkingOrders> tableResultPageResponse = baseBiz.selectByPageQuery(query,null,null);
        entityObjectRestResponse.data((ParkingOrders)tableResultPageResponse.getData().getRows().get(0));
        return entityObjectRestResponse;
    }

    @ApiOperation("手持机app室外下单接口")
    @RequestMapping(value = "/placeOrder",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse placeOrder(@RequestBody ParkingOrders parkingOrders) throws Exception {
        return parkingOrdersRsBiz.placeOrder(parkingOrders);
    }


    @RequestMapping(value = "/querySpaceInfoByUserId",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据用户id获取车位信息")
    public ObjectRestResponse<Object> querySpaceInfoByUserId(String id){
        return parkingOrdersRsBiz.querySpaceInfoByUserId(id);
    }
    @ApiOperation("根据车牌号码分页获取欠费数据")
    @RequestMapping(value = "/querArrearOrdersByCarnum",method = RequestMethod.POST)
    @ResponseBody
    public TableResultPageResponse<ParkingOrders> querArrearOrdersByCarnum(@RequestBody ParkingOrders parkingOrders){
        Plate plateEx = JSON.parseObject(parkingOrders.getPlaId(),Plate.class);
        Plate plate = plateBiz.selectOne(plateEx);
        HashMap<String,Object> params = new  HashMap<String,Object>();
        params.put("plaId",plate.getPlaId());
        params.put("orderStatus",BaseConstants.OrderStatus.unpay);
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectByPageQuery(query,null,null);
    }

    @ApiOperation("根据车牌号码分页获取欠费数据")
    @RequestMapping(value = "/queryRealMoneyByUserId",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Object> queryRealMoneyByUserId(@RequestBody ParkingOrders parkingOrders){
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        BigDecimal money = parkingOrdersMapper.queryRealMoneyByUserId(parkingOrders.getChargeId(),parkingOrders.getChargeDate().substring(0,10));
        int count = parkingOrdersMapper.queryOrderCountByUserId(parkingOrders.getChargeId(),parkingOrders.getChargeDate().substring(0,10));
        hashMap.put("money",money);
        hashMap.put("orderSum",count);
        return new ObjectRestResponse<>().data(hashMap);
    }
    @ApiOperation("人工支付")
    @RequestMapping(value = "/manualPayment",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ParkingOrders> manualPayment(@RequestBody ParkingOrders parkingOrders){
        return parkingOrdersRsBiz.manualPayment(parkingOrders);
    }
    @ApiOperation("app余额补缴订单")
    @RequestMapping(value = "/appBanlacePayment",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ParkingOrders> appBanlacePayment(@RequestBody ParkingOrders parkingOrders){
        return parkingOrdersRsBiz.appBanlacePayment(parkingOrders);
    }

}
