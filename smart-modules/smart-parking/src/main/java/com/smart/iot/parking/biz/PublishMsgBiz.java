package com.smart.iot.parking.biz;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.ParkingMapper;
import com.smart.iot.parking.mapper.ParkingSpaceMapper;
import com.smart.iot.parking.mqtt.MqttClient;
import com.smart.iot.parking.utils.HttpUtil;
import com.smart.iot.parking.utils.SendMsg;
import com.smart.iot.parking.vo.ParkingSpaceVo;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * MQTT日志表
 *
 * @author Mr.AG
 *@version 2022-08-23 11:36:21
 * @email
 */
@Service
@Slf4j
@Transactional
public class PublishMsgBiz {

    @Value("${mqtt.webTopic}")
    public String webTopic;
    @Value("${mqtt.appTopic}")
    public String appTopic;
    @Value("${mqtt.pdaTopic}")
    public String pdaTopic;
    @Value("${mqtt.qoslevel}")
    public int qoslevel;
    @Autowired
    ParkingBiz parkingBiz;
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public UserMessageBiz userMessageBiz;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Autowired
    public ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public ParkingMapper parkingMapper;
    @Autowired
    public ParkingAreaBiz parkingAreaBiz;
    @Autowired
    public HttpUtil httpUtil;
    @Value("${baiduMap.routematrixUrl}")
    public String routematrixUrl;
    @Value("${baiduMap.ak}")
    public String ak;
    @Autowired
    public AppUserBiz appUserBiz;

    public void publishBalanceIsNotEnough(String userId) {
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setMsgType(BaseConstants.msgType.balance_is_not_enough);
        userMessage.setTitle(BaseConstants.msgType.balance_is_not_enough_title);
        userMessage.setContent(BaseConstants.msgType.balance_is_not_enough_content);
        userMessage.setUserId(userId);
        userMessageBiz.saveOrUpdate(userMessage);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, content);
    }

    public void publishReserverOvertime(String userId) {
        UserMessage userMessage = new UserMessage();
        userMessage.setMsgType(BaseConstants.msgType.reserver_overtime);
        userMessage.setTitle(BaseConstants.msgType.reserver_overtime_title);
        userMessage.setUserId(userId);
        userMessage.setId(StringUtil.uuid());
        userMessageBiz.saveOrUpdate(userMessage);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, content);
    }


    public void publishParkingSpacesOccupiedInformationLot(ParkingSpace parkingSpace, String userId) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setMsgType(BaseConstants.msgType.parking_spaces_lot_occupied);
        userMessage.setTitle(BaseConstants.msgType.parking_spaces_lot_occupied_title);
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("previousSpace",parkingSpace);
        String spaceContent = JSONObject.toJSONString(map);
        userMessage.setContent(spaceContent);
        String userMessageString = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, userMessageString);
    }

    public void publishParkingSpacesOccupiedInformation(ParkingSpace parkingSpace, String userId) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        String parkingId = parkingSpace.getParkingId();
        String areaId=parkingSpace.getAreaId();
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setMsgType(BaseConstants.msgType.parking_spaces_occupied);
        userMessage.setTitle(BaseConstants.msgType.parking_spaces_occupied__title);
        HashMap<String,Object> map = new HashMap<String,Object>();
        ParkingSpaceVo parkingSpaceVo=parkingSpaceBiz.spaceToParkingSpaceVo(parkingSpace);
        map.put("previousSpace",parkingSpaceVo);
        String longitude = parkingSpace.getLongitude();
        String latitude = parkingSpace.getLatitude();
        Parking parking=parkingSpaceVo.getParking();
       if(longitude == null || latitude == null)
        {
            longitude = parking.getPointLng();
            latitude = parking.getPointLat();
        }
        //查询本停车场最近车位
        Double addlng = Double.parseDouble(longitude) + BaseConstants.Coordinate.lng;
        Double reducelng = Double.parseDouble(longitude) - BaseConstants.Coordinate.lng;
        Double  addlat= Double.parseDouble(latitude) + BaseConstants.Coordinate.lat;
        Double  reducelat = Double.parseDouble(latitude) - BaseConstants.Coordinate.lat;
        List<ParkingSpace> spaceList = parkingSpaceMapper.queryRandUlotParkingSpaceByParkingId(areaId,BaseConstants.SpaceType.common, BaseConstants.enabledFlag.n, parkingSpace.getChargePile(),parkingSpace.getSpaceId());
        if(spaceList !=null && spaceList.size() != 0) {
            parkingSpace=spaceList.get(0);
        }else{
            Example example=new Example(ParkingSpace.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("spaceType",BaseConstants.SpaceType.common);
            criteria.andEqualTo("lotType",BaseConstants.enabledFlag.n);
            criteria.andEqualTo("chargePile",parkingSpace.getChargePile());
            criteria.andEqualTo("parkingId",parkingId);
            criteria.andEqualTo("spaceStatus","normal");
             criteria.andNotEqualTo("spaceId",parkingSpace.getSpaceId());
            spaceList=parkingSpaceBiz.selectByExample(example);
            if(spaceList!=null && spaceList.size() != 0) {
                parkingSpace=spaceList.get(0);
            }else{
                parking=parkingMapper.queryNearParking(addlat,reducelat,addlng,reducelng,parkingId);
                criteria.andEqualTo("spaceType",BaseConstants.SpaceType.common);
                criteria.andEqualTo("lotType",BaseConstants.enabledFlag.n);
                criteria.andEqualTo("chargePile",parkingSpace.getChargePile());
                criteria.andEqualTo("parkingId",parking.getParkingId());
                criteria.andEqualTo("spaceStatus","normal");
                criteria.andNotEqualTo("spaceId",parkingSpace.getSpaceId());
                spaceList=parkingSpaceBiz.selectByExample(example);
                if(spaceList!=null && spaceList.size()>0){
                    parkingSpace=spaceList.get(0);
                }
                userMessage.setMsgType(BaseConstants.msgType.parking_occupied);
                userMessage.setTitle(BaseConstants.msgType.parking_occupied__title);
            }
        }
        parkingSpaceVo=parkingSpaceBiz.spaceToParkingSpaceVo(parkingSpace);
        map.put("lastSpace", parkingSpaceVo);
        String spaceContent = JSONObject.toJSONString(map);
        userMessage.setContent(spaceContent);
        String userMessageString = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, userMessageString);
    }


    public void publishConsumptionInformation(String userId, BigDecimal money, String longTime,Parking parking) {
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setMsgType(BaseConstants.msgType.appearance);
        userMessage.setTitle(BaseConstants.msgType.appearance_title);
        userMessage.setContent("本次消费:" + money + "元" + "停车时长" + longTime);
        userMessage.setUserId(userId);
        userMessageBiz.saveOrUpdate(userMessage);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, content);
        AppUser appUser=appUserBiz.selectById(userId);
        SendMsg.payment(appUser.getMobile(),parking.getParkingName(),money.doubleValue());
    }

    public void publishConsumptionInformationfree(String userId, BigDecimal money, String longTime,Parking parking) {
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setMsgType(BaseConstants.msgType.appearance);
        userMessage.setTitle(BaseConstants.msgType.appearance_title);
        userMessage.setContent("本次消费:" + money + "元" + "停车时长" + longTime+"，已为你免费订单");
        userMessage.setUserId(userId);
        userMessageBiz.saveOrUpdate(userMessage);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, content);
        AppUser appUser=appUserBiz.selectById(userId);
        SendMsg.payment(appUser.getMobile(),parking.getParkingName(),money.doubleValue());
    }



    public void publishExitInformation(String userId,ParkingOrders orders,Plate plate,IoRecord ioRecord,ParkingIo parkingIo){
        Map map=new HashMap();
        map.put("plate",plate);
        try {
            mergeCore.mergeOne(ParkingOrders.class, orders);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        map.put("parkingIo",parkingIo);
        map.put("parkingOrders",orders);
        map.put("ioRecord",ioRecord);
        if (StringUtil.isEmpty(plate.getUserId())) {
            map.put("userType", "临时用户");
        } else {
            map.put("userType", "注册用户");
        }
        String spaceContent = JSONObject.toJSONString(map);
        publishPdapMsg(userId,spaceContent);
    }
    //入口推送1 刷新列表
    public void entrancePush(String userId){
        String spaceContent = "1";
        publishPdapMsg(userId,spaceContent);
    }

    public void publishIoBusinessInformation(Plate plate, ParkingSpace space, ParkingIo parkingIo, ParkingOrders orders) {
        //推送
        if(plate==null || StringUtil.isEmpty(plate.getUserId())){
            return;
        }
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setMsgType(BaseConstants.msgType.entranceIo);
        userMessage.setTitle(BaseConstants.msgType.entranceIo_title);
        userMessage.setUserId(plate.getUserId());
        userMessageBiz.saveOrUpdate(userMessage);
       try {
            mergeCore.mergeOne(ParkingOrders.class,orders);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Map map=new HashMap();
        map.put("space",space);
        map.put("parkingIo",parkingIo);
        map.put("orders",orders);
        String contentMap = JSONObject.toJSONString(map);
        userMessage.setContent(contentMap);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(plate.getUserId(), content);
    }

    public void publishSpaceErrorInformation(String userId) {
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setContent(BaseConstants.msgType.space_error_content);
        userMessage.setMsgType(BaseConstants.msgType.space_error);
        userMessage.setTitle(BaseConstants.msgType.space_error_title);
        userMessage.setUserId(userId);
        userMessageBiz.saveOrUpdate(userMessage);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(userId, content);
    }

    public void publishLeaveOvertimeSpace(String parkingId, String spaceNum) {
        //推送
        UserMessage userMessage = new UserMessage();
        userMessage.setMsgType(BaseConstants.msgType.leave_overtime);
        userMessage.setTitle(spaceNum + BaseConstants.msgType.leave_overtime_title);
        String content = JSONObject.toJSONString(userMessage);
        publishAppMsg(parkingId, content);

    }

    public void publishAppMsg(String userId, String msg) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        // 设置消息的服务质量
        message.setQos(qoslevel);
        try {
            log.info("主题:" + appTopic + userId + "内容：" + message);
            MqttClient.mqttClient.publish(appTopic + userId, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishPdapMsg(String parkingId, String msg) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        // 设置消息的服务质量
        message.setQos(qoslevel);
        Example example = new Example(UserParking.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("parkingId", parkingId);
        List<UserParking> userParkingLis = userParkingBiz.selectByExample(example);
        for (UserParking userParking : userParkingLis) {
            try {
                log.info("主题:" + pdaTopic + userParking.getUserId() + "内容：" + message);
                MqttClient.mqttClient.publish(pdaTopic + userParking.getUserId(), message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    //月卡信息
    public void publishMoncardMsg(String userId) {
        UserMessage publishMsg = new UserMessage();
        publishMsg.setMsgType(BaseConstants.msgType.month_card_info);
        publishMsg.setContent(BaseConstants.msgType.month_card_title);
        publishMsg.setUserId(userId);
        publishMsg.setId(StringUtil.uuid());
        userMessageBiz.saveOrUpdate(publishMsg);
        String appContent = JSONObject.toJSONString(publishMsg);
        MqttMessage message = new MqttMessage(appContent.getBytes());
        // 设置消息的服务质量
        message.setQos(qoslevel);
        try {
            log.info("主题:" + appTopic + userId + "内容：" + message);
            MqttClient.mqttClient.publish(appTopic + userId, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishWebMsg(ParkingSpace space, String msg) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        // 设置消息的服务质量
        message.setQos(qoslevel);
        try {
            log.info("主题:" + webTopic + space.getParkingId() + "内容：" + message);
            MqttClient.mqttClient.publish(webTopic + space.getParkingId(), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //遍历所有手持机人员
    public void publishPdaMsg(String parkingId, String msg) {
        MqttMessage message = new MqttMessage(msg.getBytes());
        // 设置消息的服务质量
        message.setQos(qoslevel);
        Example example = new Example(UserParking.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("parkingId", parkingId);
        List<UserParking> userParkingLis = userParkingBiz.selectByExample(example);
        for (UserParking userParking : userParkingLis) {
            try {
                log.info("主题:" + appTopic + userParking.getUserId() + "内容：" + message);
                MqttClient.mqttClient.publish(appTopic + userParking.getUserId(), message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    //发送车位改变信息
    public void publishPdaAndWebMsg(String parkingId, ParkingSpace space) {
        //推送
        ObjectRestResponse<Map<String, Object>> entityObjectRestResponse = new ObjectRestResponse<>();
        entityObjectRestResponse = parkingBiz.queryMapInfo(space.getAreaId());
        /**************************************************/
        List<ParkingSpace> spaceList= (List<ParkingSpace>) entityObjectRestResponse.getData().get("parking_space_list");
        List<ParkingSpace> spaceLists=new ArrayList<>();
        for(ParkingSpace parkingSpace:spaceList){
            if(parkingSpace.getSpaceId().equals(space.getSpaceId())){
                parkingSpace.setLotType(space.getLotType());
            }
            spaceLists.add(parkingSpace);
        }
        entityObjectRestResponse.getData().put("parking_space_list",spaceLists);
        /**************************************************/
        // 发布消息
        String webContent = JSONObject.toJSONString(entityObjectRestResponse);
        UserMessage publishMsg = new UserMessage();
        publishMsg.setMsgType(BaseConstants.msgType.parking_space_info);
        ParkingOrders orderEx = new ParkingOrders();
        orderEx.setOrderStatus(BaseConstants.OrderStatus.running);
        orderEx.setSpaceId(space.getSpaceId());
        ParkingOrders parkingOrders = parkingOrdersBiz.selectOne(orderEx);
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if(parkingOrders != null) {
                mergeCore.mergeOne(ParkingOrders.class, parkingOrders);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        map.put("order",parkingOrders);
        map.put("space", space);
        String spaceContent = JSONObject.toJSONString(map);
        publishMsg.setContent(spaceContent);
        String appContent = JSONObject.toJSONString(publishMsg);
        this.publishWebMsg(space, webContent);
        this.publishPdaMsg(parkingId, appContent);
    }


    public void publishChargeSuccessMsg(AppUser userInfo, BigDecimal realMoney) {
        String content = "恭喜" + userInfo.getName() + "用户成功，金额为" + realMoney + "元。";
        String title = "【充值提醒】";
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setContent(content);
        userMessage.setMsgType(BaseConstants.msgType.charge);
        userMessage.setTitle(title);
        userMessage.setUserId(userInfo.getId());
        userMessageBiz.saveOrUpdate(userMessage);
        publishAppMsg(userInfo.getId(), content);

    }

    public void publishRechargeDepositSuccessMsg(AppUser userInfo, BigDecimal realMoney) {
        String content = "恭喜" + userInfo.getName() + "用户充值押金成功，金额为" + realMoney + "元。";
        UserMessage userMessage = new UserMessage();
        userMessage.setId(StringUtil.uuid());
        userMessage.setContent(content);
        userMessage.setMsgType(BaseConstants.msgType.recharge_deposit_success);
        userMessage.setTitle(BaseConstants.msgType.recharge_deposit_success_title);
        userMessage.setUserId(userInfo.getId());
        userMessageBiz.saveOrUpdate(userMessage);
        publishAppMsg(userInfo.getId(), content);

    }


}
