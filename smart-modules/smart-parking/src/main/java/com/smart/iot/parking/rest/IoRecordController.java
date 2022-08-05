package com.smart.iot.parking.rest;

import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.onsite.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.OnerankdevMapper;
import com.smart.iot.parking.utils.SpiderGateWay;
import com.smart.iot.parking.vo.IoRecordVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.omg.IOP.IOR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("ioRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "进出场管理")
public class IoRecordController extends BaseController<IoRecordBiz,IoRecord,Integer> {
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;
    @Autowired
    public OnerankdevMapper onerankdevMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public IoRecordBiz ioRecordBiz;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;

    SpiderGateWay c_spd = new SpiderGateWay();
    @Override
    public TableResultResponse list(@RequestParam Map<String, Object> params) {
        String beginTime = String.valueOf(params.get("beginTime"));
        String endTime = String.valueOf(params.get("endTime"));
        params.remove("beginTime");
        params.remove("endTime");
        String details = String.valueOf(params.get("details"));
        List<String> parkingIdList = new ArrayList<>();
        List<String> plateIdList = new ArrayList<>();
        if (!StringUtil.isEmpty(details)) {
            Example plateExample = new Example(Plate.class);
            plateExample.createCriteria().andLike("carNumber", "%" + details + "%");
            List<Plate> plateList = plateBiz.selectByExample(plateExample);
            for (Plate plate : plateList) {
                plateIdList.add(plate.getPlaId());
            }

            Example parkingExample = new Example(Parking.class);
            parkingExample.createCriteria().andLike("parkingName", "%" + details + "%");
            List<Parking> parkingList = parkingBiz.selectByExample(parkingExample);
            for (Parking parking : parkingList) {
                parkingIdList.add(parking.getParkingId());
            }
        }
        if (parkingIdList.size() == 0 && plateIdList.size() == 0 && !StringUtil.isEmpty(details)) {
            parkingIdList.add("0");
            plateIdList.add("0");
        }
        //查询列表数据
        Query query = new Query(params);
        Example example = new Example(IoRecord.class);
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            example.createCriteria().andBetween("crtTime", beginTime, endTime);
        }
        if (parkingIdList != null && parkingIdList.size() > 0) {
            example.createCriteria().orIn("parkingId", parkingIdList);
        }
        if (plateIdList != null && plateIdList.size() > 0) {
            example.createCriteria().orIn("plateId", plateIdList);
        }
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<IoRecord> list = ioRecordBiz.selectByExample(example);
        List<IoRecordVo> ioRecordVoList=ioRecordBiz.ioRecordListToIoRecordVo(list);
        return new TableResultResponse(result.getTotal(), ioRecordVoList);
    }


    @ApiOperation("操作道闸")
    @RequestMapping(value = "/operationGate", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "parkingIoId", value = "摄像头id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "operationType", value = "操作类型(1=开，2=关)", required = true, dataType = "int")
    })
    public ObjectRestResponse operationGate(@RequestParam Map<String, Object> param) {
        String parkingIoId = String.valueOf(param.get("parkingIoId"));//摄像头id
        int operationType = Integer.valueOf(String.valueOf(param.get("operationType")));//操作类型
        byte[] bt = new byte[0];
        String parseCode = "";
        List<Onerankdev> onerankdevList = onerankdevMapper.queryBindOnerankdevByIoidAndDevType(parkingIoId, null);
        for (Onerankdev onerankdev : onerankdevList) {
            if (onerankdev.getOnerankdevType().equals(BaseConstants.devType.ROAD_GATE)) {
                bt = c_spd.OperateRoadGate1(operationType);
                parseCode = parkingOrdersOSBiz.operateDevByPost(onerankdev, bt);
            }
        }
        ObjectRestResponse objectRestResponse=new ObjectRestResponse();
        objectRestResponse.setStatus(Integer.valueOf(parseCode));
        if("200".equals(parseCode)) {
            objectRestResponse.setData("操作成功！");
        }
        return objectRestResponse;
    }

    @ApiOperation("现金缴费")
    @RequestMapping(value = "/cashPayment", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "orderId", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "parkingIoId", value = "停车场摄像头", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "money", value = "优惠金额", required = true, dataType = "String")
    })
    public ObjectRestResponse cashPayment(@RequestParam Map<String, Object> param) throws Exception {
        String orderId = String.valueOf(param.get("orderId"));//订单编号
        String parkingIoId = String.valueOf(param.get("parkingIoId"));//停车场摄像头
        String money = String.valueOf(param.get("money"));//优惠金额

        ParkingOrders parkingOrdersParams = new ParkingOrders();
        parkingOrdersParams.setOrderId(orderId);
        parkingOrdersParams.setPayStatus(BaseConstants.status.fail);
        ParkingOrders parkingOrders = parkingOrdersOSBiz.selectOne(parkingOrdersParams);
        ObjectRestResponse objectRestResponse=new ObjectRestResponse();
        if (parkingOrders == null) {
            objectRestResponse.setStatus(500);
            objectRestResponse.setData("暂无订单信息！！");
            return objectRestResponse;
        } else if (parkingOrders.getOrderType().equals(BaseConstants.OrderType.vip) || parkingOrders.getOrderType().equals(BaseConstants.OrderType.monthCard)) {
            objectRestResponse.setStatus(500);
            objectRestResponse.setData("订单为月卡与vip预定无需缴费现金！！");
            return objectRestResponse;
        }

        byte[] bt = new byte[0];
        String parseCode = "";
        List<Onerankdev> onerankdevList = onerankdevMapper.queryBindOnerankdevBy(parkingIoId);
        for (Onerankdev onerankdev : onerankdevList) {
            if(onerankdev.getOnerankdevType().equals(BaseConstants.devType.ROAD_GATE)) {
                bt = c_spd.OperateRoadGate1(1);
                parseCode = parkingOrdersOSBiz.operateDevByPost(onerankdev, bt);
            }
        }
        objectRestResponse.setStatus(Integer.valueOf(parseCode));
        if("200".equals(parseCode)) {
            IoRecord ioRecord=ioRecordBiz.selectById(parkingOrders.getLpId());
            if (ioRecord != null) {
                ioRecord.setGououtDate(DateUtil.getDateTime());
                ioRecordBiz.saveOrUpdate(ioRecord);
            }
            if(!StringUtil.isEmpty(money)){
                //优惠价格
                String attr1=String.valueOf(parkingOrders.getRealMoney().subtract(BigDecimal.valueOf(Double.valueOf(money))));
                parkingOrders.setAttr1(attr1);
                parkingOrders.setAttr2("优惠"+money+"元");
            }
            parkingOrders.setPayType(BaseConstants.payType.cash);
            parkingOrders.setPosition(BaseConstants.Position.leave);
            parkingOrders.setPayStatus(BaseConstants.status.success);
            parkingOrders.setOrderStatus(BaseConstants.OrderStatus.complete);
            parkingOrdersOSBiz.updateById(parkingOrders);
            objectRestResponse.setData("操作成功！");
            parkingOrdersOSBiz.CarnelBootScreen(parkingOrders.getParkingId());
        }
        Plate plate=plateBiz.selectById(parkingOrders.getPlaId());
        Parking parking=parkingBiz.selectById(parkingOrders.getParkingId());
        if(plate.getUserId() != null) {
            publishMsgBiz.publishConsumptionInformationfree(plate.getUserId(), parkingOrders.getRealMoney(), parkingOrders.getParkingLongTime(),parking);
        }
        return objectRestResponse;
    }

    @ApiOperation("主页入口终端机列表")
    @RequestMapping(value = "/terminalList", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "条数", required = true, dataType = "int")
    })
    public TableResultResponse terminalList(@RequestParam Map<String, Object> param) {
        String userId = String.valueOf(param.get("userId"));
        UserParking userParking = new UserParking();
        userParking.setUserId(userId);
        List<UserParking> userParkingList = userParkingBiz.selectList(userParking);
        if (userParkingList != null && userParkingList.size() == 1) {
            userParking = userParkingList.get(0);
        } else {
            throw new BusinessException("用户绑定多个停车场异常或未绑定停车场!");
        }
        Query query = new Query(param);
        Example example = new Example(IoRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parkingId", userParking.getParkingId());
        criteria.andIsNull("exitId");
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<IoRecord> list = ioRecordBiz.selectByExample(example);
        List<IoRecordVo> ioRecordVoList=ioRecordBiz.ioRecordListToIoRecordVo(list);
        return new TableResultResponse(result.getTotal(), ioRecordVoList);
    }

    @ApiOperation("修改车牌列表")
    @RequestMapping(value = "/updatePlateList", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "carNumber", value = "车牌（模糊查询）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "条数", required = true, dataType = "int")
    })
    public TableResultResponse updatePlateList(@RequestParam Map<String, Object> param) {
        String userId = String.valueOf(param.get("userId"));
        String carNumber= String.valueOf(param.get("carNumber"));
        UserParking userParking = new UserParking();
        userParking.setUserId(userId);
        List<UserParking> userParkingList = userParkingBiz.selectList(userParking);
        if (userParkingList != null && userParkingList.size() == 1) {
            userParking = userParkingList.get(0);
        } else {
            throw new BusinessException("用户绑定多个停车场异常或未绑定停车场!");
        }
        List<String> plateIdList=new ArrayList<>();
        Example plateExample = new Example(Plate.class);
        plateExample.createCriteria().andLike("carNumber", "%" + carNumber + "%");
        List<Plate> platesList = plateBiz.selectByExample(plateExample);
        for (Plate plate : platesList) {
            plateIdList.add(plate.getPlaId());
        }
        Query query = new Query(param);
        Example example = new Example(IoRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parkingId", userParking.getParkingId());
        criteria.andIn("plateId",plateIdList);
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<IoRecord> list = ioRecordBiz.selectByExample(example);
        List<IoRecordVo> ioRecordVoList=ioRecordBiz.ioRecordListToIoRecordVo(list);
        return new TableResultResponse(result.getTotal(), ioRecordVoList);
    }
    @ApiOperation("修改车牌")
    @RequestMapping(value = "/updateOrderPlate", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "orderId", value = "订单号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "lrId", value = "停车记录编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "carNumber", value = "新车牌", required = true, dataType = "String")
    })
    public ObjectRestResponse updateOrderPlate(@RequestParam Map<String, Object> param) {
        String orderId = String.valueOf(param.get("orderId"));
        String lrId = String.valueOf(param.get("lrId"));
        String carNumber = String.valueOf(param.get("carNumber"));
        Plate aPlate = new Plate();
        aPlate.setCarNumber(carNumber);
        Plate plate = plateBiz.selectOne(aPlate);
        String plaId="";
        if(plate==null){
            plate=new Plate();
            plaId =StringUtil.uuid();
            plate.setPlaId(plaId);
            plate.setCarNumber(carNumber);
            plateBiz.saveOrUpdate(plate);
        }else{
            plaId=plate.getPlaId();
        }
        //修改订单信息-车牌
        ParkingOrders parkingOrders=parkingOrdersOSBiz.selectById(orderId);
        parkingOrders.setPlaId(plaId);
        parkingOrdersOSBiz.saveOrUpdate(parkingOrders);
        //修改出入信息订单
        IoRecord ioRecord=ioRecordBiz.selectById(lrId);
        ioRecord.setPlateId(plaId);
        ioRecordBiz.saveOrUpdate(ioRecord);
        ObjectRestResponse objectRestResponse=new ObjectRestResponse();
        objectRestResponse.setStatus(200);
        objectRestResponse.setData("操作成功！");
        return objectRestResponse;
    }


    @ApiOperation("查询出入记录")
    @RequestMapping(value = "/ioRecordList", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "carNumber", value = "车牌号（模糊查询）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "beginTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "endTime", value = "结束时间", required = false, dataType = "String")
    })
    public TableResultResponse ioRecordList(@RequestParam Map<String, Object> param) {
        String userId = String.valueOf(param.get("userId"));
        String carNumber= String.valueOf(param.get("carNumber"));
        String beginTime = String.valueOf(param.get("beginTime"));
        String endTime = String.valueOf(param.get("endTime"));
        param.remove("beginTime");
        param.remove("endTime");
        UserParking userParking = new UserParking();
        userParking.setUserId(userId);
        List<UserParking> userParkingList = userParkingBiz.selectList(userParking);
        if (userParkingList != null && userParkingList.size() == 1) {
            userParking = userParkingList.get(0);
        } else {
            throw new BusinessException("用户绑定多个停车场异常或未绑定停车场!");
        }
        List<String> plateIdList=new ArrayList<>();
        Example plateExample = new Example(Plate.class);
        plateExample.createCriteria().andLike("carNumber", "%" + carNumber + "%");
        List<Plate> platesList = plateBiz.selectByExample(plateExample);
        for (Plate plate : platesList) {
            plateIdList.add(plate.getPlaId());
        }
        Query query = new Query(param);
        Example example = new Example(IoRecord.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            criteria.andBetween("crtTime", beginTime, endTime);
        }
        criteria.andEqualTo("parkingId", userParking.getParkingId());
        if(plateIdList!=null && plateIdList.size()>0) {
            criteria.andIn("plateId", plateIdList);
        }
        criteria.andIsNotNull("exitId");
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<IoRecord> list = ioRecordBiz.selectByExample(example);
        List<IoRecordVo> ioRecordVoList=ioRecordBiz.ioRecordListToIoRecordVo(list);
        return new TableResultResponse(result.getTotal(), ioRecordVoList);
    }

    @ApiOperation("免费开闸")
    @RequestMapping(value = "/freeGate", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "orderId", value = "订单编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "parkingIoId", value = "出入口编号", required = true, dataType = "String")
    })
    public ObjectRestResponse freeGate(@RequestParam Map<String, Object> param) {
        String orderId = String.valueOf(param.get("orderId"));//订单编号
        String parkingIoId = String.valueOf(param.get("parkingIoId"));//停车场摄像头
        ParkingOrders parkingOrdersParams = new ParkingOrders();
        parkingOrdersParams.setOrderId(orderId);
        parkingOrdersParams.setPayStatus(BaseConstants.status.fail);
        ParkingOrders parkingOrders = parkingOrdersOSBiz.selectOne(parkingOrdersParams);
        ObjectRestResponse objectRestResponse=new ObjectRestResponse();
        if (parkingOrders == null) {
            objectRestResponse.setStatus(500);
            objectRestResponse.setData("暂无订单信息！！");
            return objectRestResponse;
        } else if (parkingOrders.getOrderType().equals(BaseConstants.OrderType.vip) || parkingOrders.getOrderType().equals(BaseConstants.OrderType.monthCard)) {
            objectRestResponse.setStatus(500);
            objectRestResponse.setData("订单为月卡与vip预定无需缴费现金！！");
            return objectRestResponse;
        }
        byte[] bt = new byte[0];
        String parseCode = "";
        //下发命令，道闸放行
        List<Onerankdev> onerankdevList = onerankdevMapper.queryBindOnerankdevBy(parkingIoId);
        for (Onerankdev onerankdev : onerankdevList) {
            if(onerankdev.getOnerankdevType().equals(BaseConstants.devType.ROAD_GATE)) {
                bt = c_spd.OperateRoadGate1(1);
                parseCode = parkingOrdersOSBiz.operateDevByPost(onerankdev, bt);
            }
        }
        objectRestResponse.setStatus(Integer.valueOf(parseCode));
        if("200".equals(parseCode)) {
            IoRecord ioRecord=ioRecordBiz.selectById(parkingOrders.getLpId());
            if (ioRecord != null) {
                ioRecord.setGououtDate(DateUtil.getDateTime());
                ioRecordBiz.saveOrUpdate(ioRecord);
            }
            parkingOrders.setAttr1("0");
            parkingOrders.setOrderType(BaseConstants.OrderType.free_admission);
            parkingOrders.setOrderStatus(BaseConstants.OrderStatus.complete);
            parkingOrders.setPayType(BaseConstants.payType.free);
            parkingOrders.setPosition(BaseConstants.Position.leave);
            parkingOrders.setPayStatus(BaseConstants.status.success);
            parkingOrders.setCreateOrderType(BaseConstants.createOrderType.free_labor);
            parkingOrdersOSBiz.updateById(parkingOrders);
            objectRestResponse.setData("操作成功！");
            try {
                parkingOrdersOSBiz.CarnelBootScreen(parkingOrders.getParkingId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Plate plate=plateBiz.selectById(parkingOrders.getPlaId());
        Parking parking=parkingBiz.selectById(parkingOrders.getParkingId());
        if(plate.getUserId() != null) {
            publishMsgBiz.publishConsumptionInformationfree(plate.getUserId(), parkingOrders.getRealMoney(), parkingOrders.getParkingLongTime(),parking);
        }
        return objectRestResponse;
    }
}
