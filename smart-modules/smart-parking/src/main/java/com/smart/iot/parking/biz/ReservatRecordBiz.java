package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.feign.DictFeign;
import com.smart.iot.parking.mapper.ReservatRecordMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.utils.MD5;
import com.smart.iot.parking.utils.SendMsg;
import com.smart.iot.parking.vo.ReservatRecordVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class ReservatRecordBiz extends BusinessBiz<ReservatRecordMapper,ReservatRecord> {
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public DictFeign dictFeign;

    public void reservatOverdueTask(JSONObject jsonObject)
    {
        String spaceId = (String) jsonObject.get("spaceId");
        String userId = (String) jsonObject.get("userId");

        String reservatState = (String) jsonObject.get("reservatState");
        ReservatRecord example = new ReservatRecord();
        example.setSpaceId(spaceId);
        example.setUserId(userId);
        example.setReservatState(reservatState);
        List<ReservatRecord> reservatRecordList = this.selectList(example);
        if(reservatRecordList!=null && reservatRecordList.size()>0) {
            publishMsgBiz.publishReserverOvertime(userId);
        }
        for(ReservatRecord reservatRecord:reservatRecordList){
            reservatRecord.setEndDate(DateUtil.getDateTime());
            reservatRecord.setReservatState(BaseConstants.proceStatus.complete);
            this.saveOrUpdate(reservatRecord);
        }
        //结束预定但未进场vip预定订单
        ParkingOrders orderExample = new ParkingOrders();
        orderExample.setUserId(userId);
        orderExample.setSpaceId(spaceId);
        orderExample.setOrderStatus(BaseConstants.OrderStatus.running);
        orderExample.setPosition(BaseConstants.Position.unapproach);
        List<ParkingOrders> parkingOrderList = parkingOrdersBiz.selectList(orderExample);
        for(ParkingOrders parkingOrders:parkingOrderList) {
            if (parkingOrders != null) {
                parkingOrders.setPosition(BaseConstants.Position.leave);
                parkingOrders.setOrderStatus(BaseConstants.OrderStatus.complete);
                parkingOrders.setEndDate(DateUtil.getDateTime());
                parkingOrdersBiz.saveOrUpdate(parkingOrders);

            }
        }

    }

    public void ordersOverdueTask(JSONObject object) {
        ParkingOrders parkingOrders = JSON.parseObject(String.valueOf(object),ParkingOrders.class);
        ParkingOrders orders = parkingOrdersBiz.selectById(parkingOrders.getOrderId());
        if(orders!=null) {
            if (orders.getOrderStatus().equals(BaseConstants.OrderStatus.running) && orders.getPayStatus().equals(BaseConstants.status.success) && orders.getPosition().equals(BaseConstants.Position.admission)) {
                long time = DateUtil.getDiffTimeStamp(orders.getChargeDate(), DateUtil.getDateTime());
                //超过15分钟重新创建缴费订单
                Map<String, String> dictMap =dictFeign.getDictValues("parking_config");
                long expCommon=Long.valueOf(dictMap.get("exp_common"));
                if (time >= expCommon/1000) {
                    orders.setEndDate(orders.getChargeDate());
                    orders.setPosition(BaseConstants.Position.leave);
                    orders.setOrderStatus(BaseConstants.OrderStatus.complete);
                    parkingOrdersBiz.saveOrUpdate(orders);
                    ParkingOrders newOrder = new ParkingOrders();
                    newOrder.setOrderId(StringUtil.uuid());
                    newOrder.setBeginDate(orders.getChargeDate());
                    newOrder.setSpaceId(orders.getSpaceId());
                    newOrder.setLmId(orders.getLmId());
                    newOrder.setOrderStatus(BaseConstants.OrderStatus.running);
                    newOrder.setOrderType(orders.getOrderType());
                    newOrder.setParkingId(orders.getParkingId());
                    newOrder.setPosition(BaseConstants.Position.admission);
                    String orderNum = DateUtil.getOrderNum();
                    newOrder.setOrderNum(orderNum);
                    newOrder.setOrderNumMd5(MD5.MD5(orderNum));
                    newOrder.setUserId(orders.getUserId());
                    newOrder.setChargeRulesTypeId(orders.getChargeRulesTypeId());
                    newOrder.setPlaId(orders.getPlaId());
                    newOrder.setPayStatus(BaseConstants.status.fail);
                    newOrder.setUserId(orders.getUserId());
                    newOrder.setPlatenumImage(orders.getPlatenumImage());
                    newOrder.setCreateOrderType(BaseConstants.createOrderType.artificial);
                    newOrder.setParentId(orders.getOrderId());
                    parkingOrdersBiz.saveOrUpdate(newOrder);
                    //室外手持机推送
                    if (newOrder.getOrderType().equals(BaseConstants.OrderType.handset)) {
                        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(newOrder.getSpaceId());
                        publishMsgBiz.publishPdaAndWebMsg(orders.getParkingId(), parkingSpace);
                        AppUser appUser=appUserBiz.selectById(orders.getUserId());
                        ParkingSpace space=parkingSpaceBiz.selectById(orders.getSpaceId());
                        Parking parking=parkingBiz.selectById(orders.getParkingId());
                        if(appUser!=null) {
                            SendMsg.vipTimeout(appUser.getMobile(), parking.getParkingName(), space.getSpaceNum());
                        }
                    }
                }
            }
        }
    }
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<ReservatRecord> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

    public List<ReservatRecordVo> reservatRecordToReservatRecordVo(List<ReservatRecord> list){
        List<String> parkingIdList=new ArrayList<>();
        List<String> plaIdList=new ArrayList<>();
        List<String> appUserIdList=new ArrayList<>();
        List<String> spaceIdList=new ArrayList<>();
        for(ReservatRecord reservatRecord:list){
            parkingIdList.add(reservatRecord.getParkingId());
            plaIdList.add(reservatRecord.getPlaId());
            appUserIdList.add(reservatRecord.getUserId());
            spaceIdList.add(reservatRecord.getSpaceId());
        }
        List<ReservatRecordVo> list1=new ArrayList();
        List<Parking> parkingList=parkingBiz.parkingIdInList(parkingIdList);
        List<Plate> plateList=plateBiz.PlaIdInList(plaIdList);
        List<AppUser> appUserList=appUserBiz.userIdInList(appUserIdList);
        List<ParkingSpace> spaceList=parkingSpaceBiz.spaceIdInList(spaceIdList);
        for(ReservatRecord reservatRecord:list) {
            ReservatRecordVo reservatRecordVo = new ReservatRecordVo(reservatRecord);
            for (Parking parking : parkingList) {
                if (parking.getParkingId().equals(reservatRecord.getParkingId())) {
                    reservatRecordVo.setParking(parking);
                }
            }
            for (Plate plate : plateList) {
                if (plate.getPlaId().equals(reservatRecord.getPlaId())) {
                    reservatRecordVo.setPlate(plate);
                }
            }
            for (AppUser appUser : appUserList) {
                if (appUser.getId().equals(reservatRecord.getUserId())) {
                    reservatRecordVo.setAppUser(appUser);
                }
            }
            for (ParkingSpace space : spaceList) {
                if (space.getSpaceId().equals(reservatRecord.getSpaceId())) {
                    reservatRecordVo.setParkingSpace(space);
                }
            }
            list1.add(reservatRecordVo);
        }
        return list1;
    }

}
