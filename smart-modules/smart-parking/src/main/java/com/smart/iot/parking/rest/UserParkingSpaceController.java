package com.smart.iot.parking.rest;

import com.alibaba.fastjson.JSON;
import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.biz.UserParkingSpaceBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingOrders;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.UserParkingSpace;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("userParkingSpace")
@CheckClientToken
@CheckUserToken
@Api(tags = "共享车位用户-停车场")
public class UserParkingSpaceController extends BaseController<UserParkingSpaceBiz,UserParkingSpace,String> {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public UserParkingSpaceBiz userParkingSpacebiz;
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Override
    public ObjectRestResponse<UserParkingSpace> add(@RequestBody UserParkingSpace userParkingSpace){
        if(userParkingSpace.getUserId() == null) {
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.PARAM_ERROR_ID, BaseConstants.StateConstates.PARAM_ERROR_MSG);
        }
        ParkingSpace parkingSpace = parkingSpaceBiz.selectById(userParkingSpace.getSpaceId());
        if(!parkingSpace.getSpaceType().equals(BaseConstants.SpaceType.private1)) {
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.NO_PRAVITE_SPACE_CODE, BaseConstants.StateConstates.NO_PRAVITE_SPACE_MSG);
        }
        baseBiz.saveOrUpdate(userParkingSpace);
        return new ObjectRestResponse<UserParkingSpace>().data(userParkingSpace);
    }
    @RequestMapping(value = "/queryParkingByUserId",method = RequestMethod.POST)
    @ResponseBody
    public TableResultResponse queryParkingByUserId(@RequestBody UserParkingSpace userParkingSpace){
        //查询列表数据
       return userParkingSpacebiz.queryParkingByUserId(userParkingSpace.getUserId(),"");
    }
    @Override
    public ObjectRestResponse<UserParkingSpace> update(@RequestBody UserParkingSpace entity){
        if(entity.getUserId() == null) {
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.PARAM_ERROR_ID, BaseConstants.StateConstates.PARAM_ERROR_MSG);
        }

        ParkingSpace space = parkingSpaceBiz.selectById(entity.getSpaceId());
        ParkingOrders paramsOrder=new ParkingOrders();
        paramsOrder.setSpaceId(space.getSpaceId());
        paramsOrder.setOrderStatus(BaseConstants.OrderStatus.running);
        List<ParkingOrders> ordersList=parkingOrdersOSBiz.selectList(paramsOrder);
        if(ordersList!=null && ordersList.size()>0){
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_MSG);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd "+entity.getBeginDate()+":ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd "+entity.getEndDate()+":ss");

        String beginDate=sdf.format(new Date());
        String endDate=sdf1.format(new Date());
        long time=DateUtil.parse(endDate,DateUtil.YYYY_MM_DD_HH_MM_SS).getTime()-DateUtil.parse(beginDate,DateUtil.YYYY_MM_DD_HH_MM_SS).getTime();
        long dd=time/1000/24/60;
        if(dd<2){
            //租赁规则===车位出租时段不得少于2个小时
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE,"开放时段少于2小时无法进行修改");
        }
        space.setVersion(space.getVersion());
        parkingSpaceBiz.updateById(space);
        entity.setSpaceId(space.getSpaceId());
        entity.setParkingId(entity.getParkingId());
        entity.setEnabledFlag(BaseConstants.enabledFlag.y);
        baseBiz.updateSelectiveById(entity);
        return new ObjectRestResponse<UserParkingSpace>().data(entity);
    }

}
