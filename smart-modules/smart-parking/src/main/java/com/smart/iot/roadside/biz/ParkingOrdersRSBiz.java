package com.smart.iot.roadside.biz;

import com.alibaba.fastjson.JSON;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.mapper.ParkingOrdersMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.MD5;
import com.smart.iot.parking.utils.SendMsg;
import com.smart.iot.parking.vo.ParkingOrdersVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 订单表
 *
 * @author Mr.AG
 *@version 2022-08-07 14:53:11
 * @email
 */
@Service
@Transactional
@Slf4j
public class ParkingOrdersRSBiz extends BusinessBiz<ParkingOrdersMapper, ParkingOrders> {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingOrdersMapper parkingOrdersMapper;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public DictFeign dictFeign;

    @Value("${sysPath}")
    private String sysPath;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public IoRecordBiz ioRecordBiz;
    @Autowired
    public ParkingBusinessTypeBiz parkingBusinessTypeBiz;

    @Autowired
    public ReservatRecordBiz reservatRecordBiz;
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<ParkingOrders> selectByPageQuery(Query query, String beginDate, String endDate) {
        return super.selectByPageQuery(query, beginDate, endDate);
    }

    /**
     * 下单接口
     */
    public BaseResponse placeOrder(ParkingOrders parkingOrders) throws Exception {
        ParkingSpace parkingSpace = JSON.parseObject(parkingOrders.getSpaceId(), ParkingSpace.class);
        String spacesNum = parkingSpace.getSpaceNum();
        ParkingSpace params=new ParkingSpace();
        params.setSpaceNum(spacesNum);
        params.setEnabledFlag("y");
        parkingSpace = parkingSpaceBiz.selectOne(params);
        Parking parking = parkingBiz.selectById(parkingOrders.getParkingId());
        Plate plateEx = JSON.parseObject(parkingOrders.getPlaId(), Plate.class);
        Plate plateExample = new Plate();
        plateExample.setCarNumber(plateEx.getCarNumber());
        Plate plate = plateBiz.selectOne(plateExample);
        String userId = parkingOrders.getUserId();
        String cityId = parking.getCityId();
        String orderType = parkingOrders.getOrderType();
        String carNum = plateEx.getCarNumber();
        String imagePath = parkingOrders.getPlatenumImage();
        AppUser appUser = appUserBiz.selectById(userId);
        if (spacesNum == null || cityId == null) {
            return new BaseResponse(BaseConstants.StateConstates.PARAMETER_IS_EMPTY_ID, BaseConstants.StateConstates.PARAMETER_IS_EMPTY_MSG);
        }
        if (parkingSpace == null || parking.getParkingBusType().equals(BaseConstants.BusinessModule.ace_onsite_server)) {
            return new BaseResponse(BaseConstants.StateConstates.SPACE_NUMBER_ERRER_ID, BaseConstants.StateConstates.SPACE_NUMBER_ERRER_CODE);
        }
        String spaceStatus = parkingSpace.getSpaceStatus();
        if (!spaceStatus.equals(BaseConstants.errorType.normal)) { //异常
            return new BaseResponse(BaseConstants.StateConstates.SPACE_EXCEPTION_CODE, BaseConstants.StateConstates.SPACE_EXCEPTION_MSG);
        }
        String parkingId = parkingSpace.getParkingId();
        String spaceId = parkingSpace.getSpaceId();
        if (parkingSpace.getSpaceStatus().equals(BaseConstants.enabledFlag.n)) {
            return new BaseResponse(BaseConstants.StateConstates.NO_CAR_CODE, BaseConstants.StateConstates.NO_CAR_MSG);
        }
        LotMsg lm = lotMsgMapper.queryRunLotMsgBySpaceId(spaceId);
        if (lm == null) {
            return new BaseResponse(BaseConstants.StateConstates.NO_CAR_CODE, BaseConstants.StateConstates.NO_CAR_MSG);
        }
        ParkingOrders example = new ParkingOrders();
        example.setOrderStatus(BaseConstants.OrderStatus.running);
        example.setSpaceId(spaceId);
        ParkingOrders parkingOrder = this.selectOne(example);
        if (parkingOrder != null) {
            return new BaseResponse(BaseConstants.StateConstates.EXISTING_ORDER_CODE, BaseConstants.StateConstates.EXISTING_ORDER_MSG);
        }
        Plate plaExample = new Plate();
        plaExample.setCarNumber(carNum);
        plaExample.setEnabledFlag(BaseConstants.enabledFlag.y);
/*
        if(plate != null) {
            ParkingOrder order = orderRepository.findByPlaIdAndEnabledFlagAndOrderStatusAndAttribute2IsNull(plate.getPlaId(),"Y",OrderConstants.ONHAND_ORDER);
            if (order != null && order.getOrderId() != 0) {
                logger.info("---------------------------该车牌已存在一笔进行中的订单---------------------------------");
                return ResultUtil.state(StateConstates.CARNUM_IN_ORDER_CODE, StateConstates.CARNUM_IN_ORDER_MSG);
            }
        }
*/
        ParkingOrders ordersParams=new ParkingOrders();
        ordersParams.setUserId(userId);
        ordersParams.setOrderStatus(BaseConstants.OrderStatus.unpay);
        ordersParams.setOrderType(BaseConstants.OrderType.common);
        ParkingOrders orders1=parkingOrdersBiz.selectOne(ordersParams);
        if(orders1!=null){
            return new BaseResponse(127, "请支付待支付订单，在进行下订单操作！！");
        }
        //当app下单且车牌号为空的时候提示绑定车牌
       if (plate == null && orderType.equals(BaseConstants.OrderType.common)) {
            return new BaseResponse(BaseConstants.StateConstates.PLATE_NOT_EXIST_CODE, BaseConstants.StateConstates.PLATE_NOT_EXIST_MSG);
            //当视频桩是手持机下单不存在车牌时，则新建一个临时未绑定的车牌
        } else if (plate == null && orderType.equals(BaseConstants.OrderType.handset) || orderType.equals(BaseConstants.OrderType.videopile)) {
            plate = new Plate();
            plate.setPlaId(StringUtil.uuid());
            plate.setCarNumber(carNum);
            plate.setCarType(plateEx.getCarType());
            plateBiz.insertSelective(plate);
        } else if (plate != null && orderType.equals(BaseConstants.OrderType.common)) {
            if (plate.getUserId() == null) {
                return new BaseResponse(BaseConstants.StateConstates.PLATE_NOT_AUTH_CODE, BaseConstants.StateConstates.PLATE_NOT_AUTH_MSG);
            }
        }
        ParkingOrders orders = new ParkingOrders();
        //判断该停车记录是否存在两笔以上订单，当存在多笔订单则取上比单结束时间作为开始时间
        String begintime = parkingOrdersMapper.findMaxEndDateByLmId(lm.getLmId());
        if (!StringUtil.isBlank(begintime)) {
            orders.setBeginDate(begintime);
        } else {
            orders.setBeginDate(lm.getBeginDate());
        }
        orders.setSpaceId(spaceId);
        orders.setLmId(lm.getLmId());
        orders.setOrderStatus(BaseConstants.OrderStatus.running);
        orders.setOrderType(orderType);
        //判断是否
        if(plate.getUserId() != null ) {
            orders.setOrderType(BaseConstants.OrderType.common);
        }
        orders.setOrderId(StringUtil.uuid());
        orders.setParkingId(parkingId);
        orders.setPosition(BaseConstants.Position.admission);
        orders.setOrderNum(DateUtil.getOrderNum());
        orders.setOrderNumMd5(MD5.MD5(orders.getOrderNum()));
        orders.setUserId(userId);
        orders.setChargeRulesTypeId(parking.getChargeRuleId());
        orders.setPlaId(plate.getPlaId());
        orders.setPayStatus(BaseConstants.status.fail);
        //当车牌绑定用户时把订单变成app订单
        if (plate.getUserId() != null) {
            orders.setUserId(plate.getUserId());
        }
        if (orderType.equals(BaseConstants.OrderType.handset)) {
            String url = sysPath;
            url = url.substring(1, url.length());
            orders.setPlatenumImage(url + imagePath);
            orders.setCreateOrderType(BaseConstants.createOrderType.artificial);
        }
        this.saveOrUpdate(orders);
        ParkingSpace spaces = parkingSpaceBiz.selectById(spaceId);
        parking.setAttr3(StringUtil.uuid());
        parkingBiz.updateById(parking);
        if (plate.getUserId() != null && appUser!=null ) {
            SendMsg.recver_order_success(appUser.getName(), spaces.getSpaceNum(), orders.getBeginDate());
        }
        return new ObjectRestResponse<ParkingOrders>().data(orders);
    }

    private String getCarTypeByPlateColor(String plate) {
        if ("黄".equals(plate)) {
            return BaseConstants.plateType.truck;
        } else {
            return BaseConstants.plateType.auto;
        }
    }

    public void updateOrder(ParkingSpace space, String lotType, String date) {
        if (lotType.equals(BaseConstants.enabledFlag.y)) {//有车
            //更新停车记录
            lotMsgBiz.updateLotMsg(space.getSpaceId(), lotType, date);
            space.setLotType(lotType);

            log.info("=====执行判断是否存在预定记录");
            ReservatRecord example = new ReservatRecord();
            example.setSpaceId(space.getSpaceId());
            example.setReservatState(BaseConstants.proceStatus.running);
            //判断是否存在预定记录
            ReservatRecord reservatRecord = reservatRecordBiz.selectOne(example);
            if(reservatRecord != null) {
                try {
                    reservatRecord.setEndDate(DateUtil.getDateTime());
                    reservatRecord.setReservatState(BaseConstants.proceStatus.complete);
                    reservatRecordBiz.saveOrUpdate(reservatRecord);
                    publishMsgBiz.publishParkingSpacesOccupiedInformationLot(space,reservatRecord.getUserId());
                    log.info("=====路侧推送重新分配车位");
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

        } else if (lotType.equals(BaseConstants.enabledFlag.n)) {//无车
            space.setLotType(lotType);

            //查询所有该车位停车时间为空的记录
            LotMsg lotMsg = lotMsgMapper.queryRunLotMsgBySpaceId(space.getSpaceId());
            if (lotMsg != null) {
                //取第一条，如果结束时间在记录开始时间之前，那么判断此次车位异常，进行异常处理
                if (DateUtil.parse(date, null).before(DateUtil.parse(lotMsg.getBeginDate(), null))) {
                    ExceptionReportProcess(space, lotType, BaseConstants.errorType.geomagneticTime);
                } else {
                    //更新停车记录
                    lotMsgBiz.updateLotMsg(space.getSpaceId(), lotType, date);
                    //更新订单并有退款进行新建退款订单
                    updateOrderService(date, space);
                }

            }
        }
    }

    public void updateOrderService(String endTime, ParkingSpace space) {
        Example example = new Example(ParkingOrders.class);
        example.createCriteria().andEqualTo("orderStatus", BaseConstants.OrderStatus.running).andEqualTo("spaceId", space.getSpaceId());
        List<ParkingOrders> orderList = this.selectByExample(example);
        Parking parking = parkingBiz.selectById(space.getParkingId());
        for (ParkingOrders order : orderList) {
            AppUser user = appUserBiz.selectById(order.getUserId());
            Plate plate = plateBiz.selectById(order.getPlaId());
            BigDecimal money = parkingOrdersBiz.queryCurrCostMoney(DateUtil.getDateTime(), parking, plate, order);
            String longTime = null;
            if (order.getPayStatus().equals(BaseConstants.status.fail)) { //当订单未支付时
                if (order.getOrderType().equals(BaseConstants.OrderType.common)) {
                    //订单金额
                    order.setOrderStatus(BaseConstants.OrderStatus.unpay);
                    longTime = DateUtil.getDateDiff(order.getBeginDate(), endTime);
                    order.setParkingLongTime(longTime);
                    order.setEndDate(endTime);
                    feeDeduction(order, user, money, parking);
                } else if (order.getOrderType().equals(BaseConstants.OrderType.handset)) {
                    order.setPosition(BaseConstants.Position.leave);//设置车位已出场
                    order.setEndDate(endTime);
                    order.setRealMoney(money);
                    if(money.compareTo(new BigDecimal(0)) == 0)
                    {
                        order.setOrderStatus(BaseConstants.OrderStatus.complete);
                    }
                    longTime = DateUtil.getDateDiff(order.getBeginDate(), endTime);
                    order.setParkingLongTime(longTime);
                    this.saveOrUpdate(order);
                }
            }else { //当订单已支付
                long time = DateUtil.getDiffTimeStamp(order.getChargeDate(), DateUtil.getDateTime());
                //超过15分钟重新创建缴费订单
                Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
                long expCommon=Long.valueOf(dictMap.get("exp_common"));
                if (time > expCommon/1000) {
                    ParkingOrders orders = new ParkingOrders();
                    orders.setOrderId(StringUtil.uuid());
                    orders.setBeginDate(order.getChargeDate());
                    orders.setSpaceId(order.getSpaceId());
                    orders.setLmId(order.getLmId());
                    orders.setOrderStatus(BaseConstants.OrderStatus.running);
                    orders.setOrderType(order.getOrderType());
                    orders.setParkingId(order.getParkingId());
                    orders.setPosition(BaseConstants.Position.leave);
                    String orderNum = DateUtil.getOrderNum();
                    orders.setOrderNum(orderNum);
                    orders.setOrderNumMd5(MD5.MD5(orderNum));
                    orders.setUserId(order.getUserId());
                    orders.setPlaId(plate.getPlaId());
                    orders.setPayStatus(BaseConstants.status.fail);
                    orders.setParentId(order.getOrderId());
                    if (plate.getUserId() != null) {
                        orders.setUserId(plate.getUserId());
                    }
                    orders.setPlatenumImage(order.getPlatenumImage());
                    orders.setCreateOrderType(BaseConstants.createOrderType.artificial);
                    this.saveOrUpdate(orders);
                    publishMsgBiz.publishLeaveOvertimeSpace(orders.getParkingId(),space.getSpaceNum());
                }
                order.setEndDate(order.getChargeDate());
                order.setPosition(BaseConstants.Position.leave);
                order.setOrderStatus(BaseConstants.OrderStatus.complete);
                this.saveOrUpdate(order);
            }
            //当车牌是注册用户则推送信息给用户
            if (user != null) {
                publishMsgBiz.publishConsumptionInformation(user.getId(), money, longTime,parking);
            }
        }
    }

    public void feeDeduction(ParkingOrders parkingOrder, AppUser user, BigDecimal money, Parking parking) {
        //当用户存在余额时自动扣费，否则设置为未支付
        BigDecimal accoundBalance = new BigDecimal(0.00);
        if (user.getMoney() != null) {
            accoundBalance = user.getMoney();
        }
        if (accoundBalance.compareTo(money) < 0) {
            parkingOrder.setOrderStatus(BaseConstants.OrderStatus.unpay);
        } else if (money.compareTo(BigDecimal.ZERO) == 0) {
            parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
        } else {
            parkingOrder.setOrderStatus(BaseConstants.OrderStatus.complete);
            parkingOrder.setChargeDate(DateUtil.getDateTime());
            parkingOrder.setPayType(BaseConstants.payType.balance);
            BigDecimal parkingBalance = parking.getParkingRevenue().add(money);
            BigDecimal userBalance = user.getMoney().subtract(money);
            parking.setParkingRevenue(parkingBalance);
            user.setMoney(userBalance);
            parkingBiz.saveOrUpdate(parking);
            appUserBiz.saveOrUpdate(user);
        }
        parkingOrder.setPosition(BaseConstants.Position.leave);//设置车位已出场
        parkingOrder.setRealMoney(money);
        this.saveOrUpdate(parkingOrder);
    }

    public void ExceptionReportProcess(ParkingSpace space, String lotType, String errorType) {
        //处理异常，把订单跟车位使用记录截掉
        //查找异常时间区间内所有订单，时间区间内所有订单免费
        //结束预定但未进场vip预定订单
        ParkingOrders orderExample = new ParkingOrders();
        orderExample.setSpaceId(space.getSpaceId());
        orderExample.setOrderStatus(BaseConstants.OrderStatus.running);
        orderExample.setPosition(BaseConstants.Position.admission);
        ParkingOrders order = this.selectOne(orderExample);
        CloseErrOrder(order, errorType);
        if (!StringUtil.isBlank(lotType)) {
            //订单结束时间确实，异常状况下第一批上来的数据无法保证是否正确，只能提前结束订单，并且改变车位记录
            lotMsgBiz.updateLotMsg(space.getSpaceId(), lotType, null);
        }

    }

    public void CloseErrOrder(ParkingOrders order, String errType) {
        if (order != null) {
            String endDate = DateUtil.getDateTime();
            if (BaseConstants.OrderType.common.equals(order.getOrderType())) {
                AppUser appUser = appUserBiz.selectById(order.getUserId());
                order.setOrderStatus(BaseConstants.OrderStatus.exception);
                //实际消费金额
                String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                order.setRealMoney(BigDecimal.valueOf(0));
                order.setParkingLongTime(longTime);
                order.setEndDate(endDate);
                order.setErrorType(errType);
                SendMsg.free_single(appUser.getName(), String.valueOf(order.getRealMoney()));
                this.saveOrUpdate(order);
                //推送
                publishMsgBiz.publishSpaceErrorInformation(order.getUserId());
            } else if (BaseConstants.OrderType.handset.equals(order.getOrderType())) {
                order.setOrderStatus(BaseConstants.OrderStatus.exception);
                //实际消费金额*
                String longTime = DateUtil.getDateDiff(order.getBeginDate(), DateUtil.getDateTime());
                order.setRealMoney(BigDecimal.valueOf(0));
                order.setParkingLongTime(longTime);
                order.setEndDate(endDate);
                order.setErrorType(errType);
                this.saveOrUpdate(order);
            }
        }

    }

    public void closeOrder(String orderNum) {
        ParkingOrders parkingOrdersEx = new ParkingOrders();
        parkingOrdersEx.setOrderNum(orderNum);
        ParkingOrders order = this.selectOne(parkingOrdersEx);
        if (order != null) {
            order.setOrderStatus(BaseConstants.OrderStatus.exception);
            AppUser appUser = appUserBiz.selectById(order.getUserId());
            //已支付订单退还本金
            if (order.getPayType() != null) {
                appUser.setMoney(appUser.getMoney().add(order.getRealMoney()));
                appUserBiz.saveOrUpdate(appUser);
            }
            //实际消费金额
            order.setRealMoney(BigDecimal.valueOf(0));
            order.setParkingLongTime(DateUtil.getDateDiffs(order.getBeginDate(), DateUtil.getDateTime()));
            order.setErrorType(BaseConstants.errorType.handheld_machine);
            order.setEndDate(DateUtil.getDateTime());
            this.saveOrUpdate(order);
        }
        //推送
        publishMsgBiz.publishSpaceErrorInformation(order.getUserId());
    }


    public ObjectRestResponse<ParkingOrders> manualPayment(ParkingOrders parkingOrders) {
        String orderStatus = parkingOrders.getOrderStatus();
        String chargeId = parkingOrders.getChargeId();
        String payType = parkingOrders.getPayType();
        ParkingOrders orders = null;
        parkingOrders = parkingOrdersBiz.selectById(parkingOrders.getOrderId());
        if(parkingOrders.getPayStatus().equals(BaseConstants.status.success))
        {
            return new ObjectRestResponse<ParkingOrders>().BaseResponse(BaseConstants.StateConstates.ORDERS_HAVE_BEEN_PAID_CODE,BaseConstants.StateConstates.ORDERS_HAVE_BEEN_PAID_MSG);
        }
        //现金支付
        if(payType != null)
        {
            if(payType.equals(BaseConstants.payType.cash)) {
                String endDate=DateUtil.getDateTime();
                orders = this.selectById(parkingOrders.getOrderId());
                Parking parking = parkingBiz.selectById(orders.getParkingId());
                orders.setPayStatus(BaseConstants.status.success);
                orders.setPayType(BaseConstants.payType.cash);
                orders.setChargeDate(endDate);
                orders.setChargeId(chargeId);
                orders.setEndDate(endDate);
                orders.setParkingLongTime(DateUtil.getDateDiffs(orders.getBeginDate(), DateUtil.getDateTime()));
                Plate plate = plateBiz.selectById(orders.getPlaId());
                BigDecimal money = parkingOrdersBiz.queryCurrCostMoney(endDate, parking, plate, orders);
                orders.setRealMoney(money);
                if(orders.getPosition().equals(BaseConstants.Position.leave))
                {
                    orders.setOrderStatus(BaseConstants.OrderStatus.complete);
                }else
                {   //把订单数据推进延迟队列
                    parkingOrdersBiz.PutOrderIntoDelayQueue(orders);
                }
                parkingOrdersBiz.saveOrUpdate(orders);
            }
        }else if(orderStatus != null)
        {
            if(orderStatus.equals(BaseConstants.OrderStatus.unpay)) {
                orders = this.selectById(parkingOrders.getOrderId());
                if(orders.getPosition().equals(BaseConstants.Position.admission))
                {
                    return new ObjectRestResponse<Parking>().BaseResponse(BaseConstants.StateConstates.ORDERS_RUNING_CODE,BaseConstants.StateConstates.ORDERS_RUNING_MSG);
                }
                Parking parking = parkingBiz.selectById(orders.getParkingId());
                Plate plate = plateBiz.selectById(orders.getPlaId());
                BigDecimal money = parkingOrdersBiz.queryCurrCostMoney(DateUtil.getDateTime(), parking, plate, orders);
                orders.setRealMoney(money);
                orders.setOrderStatus(BaseConstants.OrderStatus.unpay);
                parkingOrdersBiz.saveOrUpdate(orders);
            }
        }
/*
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(orders.getSpaceId());
        publishMsgBiz.publishPdaAndWebMsg(parkingSpace.getParkingId(), parkingSpace);
*/
        return new ObjectRestResponse<ParkingOrders>().data(orders);

    }

    public ObjectRestResponse<Object> querySpaceInfoByUserId(String id) {
        HashMap<String,Object> params = new HashMap<String,Object>();
        UserParking userParking = new UserParking();
        userParking.setUserId(id);
        UserParking userParking1 = userParkingBiz.selectOne(userParking);
        params.put("orderStatus", BaseConstants.OrderStatus.running);
        params.put("parkingId",userParking1.getParkingId());
        //查询列表数据
        Query query = new Query(params);
        TableResultPageResponse<ParkingOrders> parkingOrders = this.selectByPageQuery(query,null,null);
        List<ParkingOrders> parkingOrders1 = parkingOrders.getData().getRows();
        try {
            mergeCore.mergeResult(ParkingOrders.class,parkingOrders1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Example example = new Example(ParkingOrders.class);
        example.createCriteria().andEqualTo("parkingId",userParking1.getParkingId());
        List<ParkingSpace> parkingSpaceList = parkingSpaceBiz.selectByExample(example);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("space",parkingSpaceList);
        result.put("order",parkingOrders1);
        return new ObjectRestResponse<Object>().data(result);
    }

    public ObjectRestResponse<ParkingOrders> appBanlacePayment(ParkingOrders parkingOrders) {
        parkingOrders = this.selectOne(parkingOrders);
        if(parkingOrders.getOrderStatus().equals(BaseConstants.OrderStatus.unpay))
        {
            AppUser appUser = appUserBiz.selectById(parkingOrders.getUserId());
            //用户余额不足
            if(appUser.getMoney().compareTo(parkingOrders.getRealMoney()) == -1)
            {
                return new ObjectRestResponse<ParkingOrders>().BaseResponse(BaseConstants.StateConstates.BALANCE_INSUFFICIENT_ID,BaseConstants.StateConstates.BALANCE_INSUFFICIENT_MSG);
            }
            parkingOrders.setOrderStatus(BaseConstants.OrderStatus.complete);
            this.saveOrUpdate(parkingOrders);
            appUser.setMoney(appUser.getMoney().subtract(parkingOrders.getRealMoney()));
            Parking parking = parkingBiz.selectById(parkingOrders.getParkingId());
            if(null == parking){
                parking = new Parking();
            }
            parking.setParkingRevenue(parking.getParkingRevenue().add(parkingOrders.getRealMoney()));
            appUserBiz.saveOrUpdate(appUser);
            parkingBiz.saveOrUpdate(parking);
        }
        return new ObjectRestResponse<ParkingOrders>().data(parkingOrders);
    }

    /**
     *  parkingOrder转Map
     * @param list
     * @param parkingIdList
     * @return
     */
    public List<ParkingOrdersVo> parkingOrderListMap(List<ParkingOrders> list){
        List<String> lpIdList=new ArrayList<>();
        List<String> parkingBusTypeIdList=new ArrayList<>();
        List<String> plaIdList=new ArrayList<>();
        List<String> spaceIdList=new ArrayList<>();
        List<String> parkingIdList=new ArrayList<>();
        for(ParkingOrders orders:list){
            lpIdList.add(orders.getLpId());
            parkingBusTypeIdList.add(orders.getParkingBusType());
            plaIdList.add(orders.getPlaId());
            spaceIdList.add(orders.getSpaceId());
            parkingIdList.add(orders.getParkingId());
        }
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<ParkingBusinessType> parkingBusinessTypeList=parkingBusinessTypeBiz.businessTypeInList(parkingBusTypeIdList);
        List<ParkingSpace> spaceList=new ArrayList<>();
        if(spaceIdList!=null && spaceIdList.size()>0) {
            spaceList= parkingSpaceBiz.spaceIdInList(spaceIdList);
        }
        List<Plate> plateList=new ArrayList<>();
        if(plaIdList!=null && plaIdList.size()>0) {
            plateList = plateBiz.PlaIdInList(plaIdList);
        }
        List<IoRecord> ioRecordList=new ArrayList<>();
        if(ioRecordList!=null && ioRecordList.size()>0) {
            ioRecordList = ioRecordBiz.lpIdInList(lpIdList);
        }
        List<ParkingOrdersVo> lists=new ArrayList();
        for(ParkingOrders orders:list){
            ParkingOrdersVo parkingOrdersVo=new ParkingOrdersVo(orders);
            for(Parking parking:parkingList){
                if(parking.getParkingId().equals(orders.getParkingId())){
                    parkingOrdersVo.setParking(parking);
                }
            }
            for(ParkingSpace space:spaceList){
                if(space.getSpaceId().equals(orders.getSpaceId())){
                    parkingOrdersVo.setParkingSpace(space);
                }
            }
            for(ParkingBusinessType parkingBusinessType:parkingBusinessTypeList){
                if(parkingBusinessType.getId().equals(orders.getParkingBusType())){
                    parkingOrdersVo.setParkingBusTypeEntity(parkingBusinessType);
                }
            }
            for(Plate plate:plateList){
                if(plate.getPlaId().equals(orders.getPlaId())){
                    parkingOrdersVo.setPlate(plate);
                }
            }
            for(IoRecord ioRecord:ioRecordList){
                if(ioRecord.getLrId().equals(orders.getLpId())){
                    parkingOrdersVo.setIoRecord(ioRecord);
                }
            }
            lists.add(parkingOrdersVo);
        }
        return lists;
    }

    public ParkingOrdersVo parkingOrderToParkingOrdersVo(ParkingOrders parkingOrders){
        ParkingOrdersVo parkingOrdersVo=new ParkingOrdersVo(parkingOrders);
        Plate plate=plateBiz.selectById(parkingOrders.getPlaId());
        Parking parking=parkingBiz.selectById(parkingOrders.getParkingId());
        ParkingBusinessType parkingBusinessType= parkingBusinessTypeBiz.selectById(parkingOrders.getParkingBusType());
        ParkingSpace space=parkingSpaceBiz.selectById(parkingOrders.getSpaceId());
        IoRecord ioRecord=ioRecordBiz.selectById(parkingOrders.getLpId());
        parkingOrdersVo.setPlate(plate);
        parkingOrdersVo.setParking(parking);
        parkingOrdersVo.setParkingBusTypeEntity(parkingBusinessType);
        parkingOrdersVo.setParkingSpace(space);
        parkingOrdersVo.setIoRecord(ioRecord);
        return parkingOrdersVo;
    }
}
