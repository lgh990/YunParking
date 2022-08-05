package com.smart.iot.start.biz;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.ReservatRecordMapper;
import com.smart.iot.parking.rabbitmq.ExpirationMessagePostProcessor;
import com.smart.iot.parking.rabbitmq.QueueConfig;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导航记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-15 10:08:42
 */
@Transactional
@Service
@Slf4j
public class ReservatRecordOSBiz extends BusinessBiz<ReservatRecordMapper,ReservatRecord> {
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public UserMoncardsBiz userMoncardsBiz;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ReservatRecordBiz reservatRecordBiz;
    @Autowired
    public DictFeign dictFeign;

    public ObjectRestResponse<ReservatRecord> add(ReservatRecord reservatRecord) {
        //室外普通车位预定
        ReservatRecord example = new ReservatRecord();
        example.setSpaceId(reservatRecord.getSpaceId());
        Plate plate = new Plate();
        plate.setUserId(reservatRecord.getUserId());
        plate.setEnabledFlag("Y");
        plate = plateBiz.selectOne(plate);
        if(plate == null)
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.PLATE_NOT_EXIST_CODE,BaseConstants.StateConstates.PLATE_NOT_EXIST_MSG);
        }
        ReservatRecord record=new ReservatRecord();
        record.setUserId(reservatRecord.getUserId());
        record.setReservatState(BaseConstants.proceStatus.running);
        ReservatRecord record1=reservatRecordBiz.selectOne(record);
        if(record1 != null){
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_USER_BOOKED_CODE,BaseConstants.StateConstates.SPACE_USER_BOOKED_MSG);
        }
        example.setReservatState(BaseConstants.proceStatus.running);
        reservatRecord.setBeginDate(DateUtil.getDateTime());
        //判断是否存在预定记录
        ReservatRecord reservatRecord1 = this.selectOne(example);
        if(reservatRecord1 != null)
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_BOOKED_CODE,BaseConstants.StateConstates.SPACE_BOOKED_MSG);
        }
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(reservatRecord.getSpaceId());
        if(parkingSpace!=null) {
            if (parkingSpace.getEnabledFlag().equals(BaseConstants.enabledFlag.n)) {
                return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_NUMBER_ERRER_ID, BaseConstants.StateConstates.SPACE_NUMBER_ERRER_CODE);
            }
        }else{
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.IS_NO_RESERVER_ERROR_CODE, BaseConstants.StateConstates.IS_NO_RESERVER_ERROR_MSG);
        }
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.vip) || parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.temporary) || !parkingSpace.getSpaceStatus().equals(BaseConstants.errorType.normal) )
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.RESERVER_FAIL_CODE,BaseConstants.StateConstates.RESERVER_FAIL_MSG);
        }
        if(parkingSpace.getLotType().equals(BaseConstants.enabledFlag.y))
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.SPACE_BOOKED_CODE,BaseConstants.StateConstates.SPACE_BOOKED_MSG);
        }
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.private1))
        {
            UserParkingSpace userParkingSpaceEx = new UserParkingSpace();
            userParkingSpaceEx.setSpaceId(reservatRecord.getSpaceId());
            UserParkingSpace userParkingSpace = userParkingSpaceBiz.selectOne(userParkingSpaceEx);
            //判断车位是否在可预定时间段内
            if(userParkingSpace!=null && !StringUtil.isEmpty(userParkingSpace.getRentalPeriod())&& !parkingOrdersOSBiz.DetermWhetherOperatLock(userParkingSpace) )
            {
                return new ObjectRestResponse<ParkingSpace>().BaseResponse(BaseConstants.StateConstates.NOTALLOW_REWSERVAT_CODE,BaseConstants.StateConstates.NOTALLOW_REWSERVAT_MSG);
            }
            if(userParkingSpace!=null && userParkingSpace.getEnabledFlag().equals(BaseConstants.enabledFlag.n)){
                return new ObjectRestResponse<ParkingSpace>().BaseResponse(BaseConstants.StateConstates.NOTALLOW_REWSERVAT_CODE,BaseConstants.StateConstates.NOTALLOW_REWSERVAT_MSG);
            }
        }
        Example nvExample = new Example(UserMoncards.class);
        nvExample.createCriteria().andEqualTo("parkingId", parkingSpace.getParkingId()).andEqualTo("plateId", reservatRecord.getPlaId()).andGreaterThan("endDate",DateUtil.getDateTime());
        List<UserMoncards> userMoncards = userMoncardsBiz.selectByExample(nvExample);
        //月卡用户不允许普通预定
        if(userMoncards.size() != 0 && parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.common))
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.NO_ALLOW_RESERVER_CODE,BaseConstants.StateConstates.NO_ALLOW_RESERVER_MSG);
        }
        //判断用户是否存在进行中订单
        ParkingOrders odExample =  new ParkingOrders();
        odExample.setSpaceId(reservatRecord.getSpaceId());
        odExample.setOrderStatus(BaseConstants.OrderStatus.running);
        ParkingOrders parkingOrders = parkingOrdersBiz.selectOne(odExample);
        //vip先下单再导航
        if(parkingOrders != null && !parkingOrders.getOrderType().equals(BaseConstants.OrderType.vip))
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.APP_ORDER_ID,BaseConstants.StateConstates.APP_ORDER_MSG);
        }
        //判断用户是否存在进行中订单
        ParkingOrders od1Example =  new ParkingOrders();
        od1Example.setUserId(reservatRecord.getUserId());
        od1Example.setOrderStatus(BaseConstants.OrderStatus.running);
        ParkingOrders parkingOrders1 = parkingOrdersBiz.selectOne(od1Example);
        if(parkingOrders1 != null)
        {
            return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.CARNUM_IN_ORDER_CODE,BaseConstants.StateConstates.CARNUM_IN_ORDER_MSG);
        }

        String dateTime = DateUtil.getDateTime();
        reservatRecord.setReservatState(BaseConstants.proceStatus.running);
        reservatRecord.setParkingId(parkingSpace.getParkingId());
        reservatRecord.setId(StringUtil.uuid());
        this.insertSelective(reservatRecord);
        //把数据推进延迟队列
        long time = 0;
        Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
        long expCommon=Long.valueOf(dictMap.get("exp_common"));
        long expPrivate1=Long.valueOf(dictMap.get("exp_private1"));
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.common))
        {
            time = expCommon;
        }
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.private1))
        {
            time = expPrivate1;
        }
        if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.vip))
        {
            //vip订单的停车记录结束时间以预定结束时间为准
            if(parkingOrders != null) {
                time = DateUtil.getDiffTimeStamp(dateTime,parkingOrders.getReverseDate())*1000 ;
            }
        }
        HashMap<String,Object> reservatRecordMap = new  HashMap<String,Object>();
        reservatRecordMap.put("type","reservatRecord");
        reservatRecordMap.put("object",reservatRecord);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(reservatRecordMap);
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME,
                (Object) (jsonObject.toString()), new ExpirationMessagePostProcessor(time));
        log.info("--------------------预定倒计时:"+time);
        reservatRecord.setAttr1(String.valueOf(time));
        return new ObjectRestResponse<ReservatRecord>().data(reservatRecord);
    }

    public ObjectRestResponse<ReservatRecord> update(ReservatRecord reservatRecord) {
        ReservatRecord reservatRecord1 = reservatRecordBiz.selectById(reservatRecord.getId());
        if(reservatRecord.getReservatState().equals(BaseConstants.proceStatus.cancel))
        {
            ParkingSpace parkingSpace = parkingSpaceBiz.selectById(reservatRecord.getSpaceId());
            if(parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.vip))
            {
                ParkingOrders odExample =  new ParkingOrders();
                odExample.setSpaceId(reservatRecord.getSpaceId());
                odExample.setOrderStatus(BaseConstants.OrderStatus.running);
                ParkingOrders parkingOrders = parkingOrdersBiz.selectOne(odExample);
                long time = DateUtil.getDiffTimeStamp(DateUtil.getDateTime(),parkingOrders.getReverseDate())*1000 ;

                Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
                long expVipCancel=Long.valueOf(dictMap.get("exp_vip_cancel"));
                //vip订单到预定结束时间前半个小时内不允许取消预定
                if(time <=  expVipCancel)
                {
                    log.info("===============超时不进行退款");
                    reservatRecord1.setReservatState(BaseConstants.proceStatus.cancel);
                    parkingOrders.setOrderStatus(BaseConstants.OrderStatus.cancel);
                    parkingOrdersBiz.saveOrUpdate(parkingOrders);
                    //return new ObjectRestResponse<AppUser>().BaseResponse(BaseConstants.StateConstates.NO_CANCEL_ALLOW_CODE,BaseConstants.StateConstates.NO_CANCEL_ALLOW_MSG);
                }else
                {
                    //退款操作
                    AppUser user = appUserBiz.selectById(parkingOrders.getUserId());
                    user.setMoney(user.getMoney().add(parkingOrders.getRealMoney()));
                    Parking parking = parkingBiz.selectById(parkingOrders.getParkingId());
                    parking.setParkingRevenue(parking.getParkingRevenue().subtract(parkingOrders.getRealMoney()));
                    reservatRecord1.setReservatState(BaseConstants.proceStatus.cancel);
                    reservatRecordBiz.saveOrUpdate(reservatRecord1);
                    parkingOrders.setRealMoney(new BigDecimal(0));
                    parkingOrders.setOrderStatus(BaseConstants.OrderStatus.cancel);
                    parkingOrdersBiz.saveOrUpdate(parkingOrders);
                    appUserBiz.saveOrUpdate(user);
                }
            }else{
                reservatRecord1.setReservatState(BaseConstants.proceStatus.cancel);
            }
        }
        this.updateSelectiveById(reservatRecord1);
        return new ObjectRestResponse<ReservatRecord>().data(reservatRecord1);

    }
}
