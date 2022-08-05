package com.smart.iot.onsite.rest;

import com.smart.iot.onsite.biz.OSDevDateHandleBiz;
import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.biz.UserParkingSpaceBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.UserParkingSpace;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/22 0022.
 */
@RestController
@RequestMapping("dev")
@CheckClientToken
@CheckUserToken
@Api(tags = "操作设备")
public class OperateDevController {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public OSDevDateHandleBiz osDevDateHandleBiz;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;

    @ApiOperation("操作车位锁")
    @RequestMapping(value = "/oprateCarLock",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ParkingSpace> oprateCarLock(@RequestParam Map<String, Object> params){
        String lockStatus = String.valueOf(params.get("lockStatus"));
        String spaceId = String.valueOf(params.get("spaceId"));
        String userId = String.valueOf(params.get("userId"));
        UserParkingSpace userParkingSpaceEx = new UserParkingSpace();
        userParkingSpaceEx.setUserId(userId);
        userParkingSpaceEx.setSpaceId(spaceId);
        UserParkingSpace userParkingSpace = userParkingSpaceBiz.selectOne(userParkingSpaceEx);
        //判断是否存在进行中订单
        ParkingOrders parkingOrders = new ParkingOrders();
        parkingOrders.setSpaceId(spaceId);
        parkingOrders.setOrderStatus(BaseConstants.OrderStatus.running);
        ParkingOrders parkingOrder = parkingOrdersOSBiz.selectOne(parkingOrders);

        //判断是否业主操作车位锁
        if(userParkingSpace != null)
        {   //存在进行中订单或当前处于出租时段不允许开锁
            if(userParkingSpace!=null && !StringUtil.isEmpty(userParkingSpace.getUserId())&& parkingOrder!=null && !userParkingSpace.getUserId().equals(parkingOrder.getUserId())) {
                if (parkingOrdersOSBiz.DetermWhetherOperatLock(userParkingSpace) || parkingOrder != null) {
                    return new ObjectRestResponse<ParkingSpace>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_UNLOCK_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_UNLOCK_MSG);
                }
            }
        }else
        {
            //不能操作车位锁
            if(parkingOrder == null)
            {
                return new ObjectRestResponse<ParkingSpace>().BaseResponse(BaseConstants.StateConstates.CANT_OPERATE_CARLOCK_CODE,BaseConstants.StateConstates.CANT_OPERATE_CARLOCK_MSG);
            }
        }
        String code = parkingOrdersOSBiz.CarLockCannel(lockStatus,spaceId);
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(spaceId);
        if("200".equals(code))
        {
            parkingSpace.setLockl(lockStatus);
            parkingSpaceBiz.saveOrUpdate(parkingSpace);
            return new ObjectRestResponse<ParkingSpace>().data(parkingSpace);
        }else
        {
            return new ObjectRestResponse<ParkingSpace>().data(parkingSpace).BaseResponse(Integer.valueOf(code),"操作失败");

        }
    }



    @ApiOperation("获取所有设备")
    @PostMapping(value = "/queryDeviceAttr")
    public void queryDeviceAttr() throws Exception {
        osDevDateHandleBiz.queryDeviceArrr();
    }

}
