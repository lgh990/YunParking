package com.smart.iot.parking.rest;

import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("freezeAuditRecords")
@CheckClientToken
@CheckUserToken
@Api(tags = "冻结审核表")
public class FreezeAuditRecordsController extends BaseController<FreezeAuditRecordsBiz,FreezeAuditRecords,String> {

    @Autowired
    public UserParkingSpaceBiz userParkingSpacebiz;
    @Autowired
    public ParkingOrdersOSBiz parkingOrdersOSBiz;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public SharingRuleRecordsBiz sharingRuleRecordsBiz;
    @ApiOperation("添加冻结审核")
    @RequestMapping(value = "/addFreezeAudit",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "userSpaceId", value = "共享车位id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "spaceId", value = "车位编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "parkingId", value = "停车场编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "enabledFlag", value = "关闭冻结=n，开启冻结=y", required = true, dataType = "String")
    })
    public ObjectRestResponse<FreezeAuditRecords> addFreezeAudit(@RequestBody Map<String, Object> param){
        String userSpaceId= String.valueOf(param.get("userSpaceId"));
        String spaceId=String.valueOf(param.get("spaceId"));
        String userId=String.valueOf(param.get("userId"));
        String parkingId=String.valueOf(param.get("parkingId"));
        String enabledFlag=String.valueOf(param.get("enabledFlag"));
        ParkingOrders paramsOrder=new ParkingOrders();
        UserParkingSpace userParkingSpace=userParkingSpacebiz.selectById(userSpaceId);
        FreezeAuditRecords freezeAuditRecords = new FreezeAuditRecords();
        if(enabledFlag.equals(BaseConstants.enabledFlag.n)) {
            paramsOrder.setSpaceId(userParkingSpace.getSpaceId());
            paramsOrder.setOrderStatus(BaseConstants.OrderStatus.running);
            List<ParkingOrders> ordersList = parkingOrdersOSBiz.selectList(paramsOrder);
            if (ordersList != null && ordersList.size() > 0) {
                return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_MSG);
            }
            List<SharingRuleRecords> sharingRuleRecordsList = sharingRuleRecordsBiz.selectListAll();
            SharingRuleRecords sharingRuleRecords = new SharingRuleRecords();
            if (sharingRuleRecordsList != null && sharingRuleRecordsList.size() > 0) {
                sharingRuleRecords = sharingRuleRecordsList.get(0);
            }
            FreezeAuditRecords freezeAuditRecords1 = new FreezeAuditRecords();
            freezeAuditRecords1.setUserSpaceId(userSpaceId);
            freezeAuditRecords1.setStatus("0");
            Calendar calendar = Calendar.getInstance();
            freezeAuditRecords1.setAttr1(String.valueOf(calendar.get(Calendar.MONTH)));
            Long count = baseBiz.selectCount(freezeAuditRecords1);
            if (sharingRuleRecords != null) {
                if (count >= Integer.valueOf(sharingRuleRecords.getFreezingTimes())) {
                    return new ObjectRestResponse<FreezeAuditRecords>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, "冻结次数已超过本月使用次数，请下月进行申请！");
                }
            }
            AppUser appUser = appUserBiz.selectById(userId);
            Parking parking = parkingBiz.selectById(parkingId);
            ParkingSpace space = parkingSpaceBiz.selectById(spaceId);

            freezeAuditRecords.setId(StringUtil.uuid());
            freezeAuditRecords.setParkingName(parking.getParkingName());
            freezeAuditRecords.setSpaceNum(space.getSpaceNum());
            freezeAuditRecords.setStatus("0");
            freezeAuditRecords.setUserSpaceId(userSpaceId);
            freezeAuditRecords.setTelephone(appUser.getMobile());
            baseBiz.insertSelective(freezeAuditRecords);
        }else{
            FreezeAuditRecords prams=new FreezeAuditRecords();
            prams.setStatus("1");
            prams.setEnabledFlag(BaseConstants.enabledFlag.y);
            prams.setUserSpaceId(userSpaceId);
            freezeAuditRecords=baseBiz.selectOne(prams);
            userParkingSpace.setEnabledFlag(BaseConstants.enabledFlag.y);
            userParkingSpacebiz.updateById(userParkingSpace);
            if(freezeAuditRecords!=null) {
                freezeAuditRecords.setEnabledFlag(BaseConstants.enabledFlag.n);
                baseBiz.updateById(freezeAuditRecords);
            }
        }
        return new ObjectRestResponse<FreezeAuditRecords>().data(freezeAuditRecords);
    }
    @ApiOperation("冻结审核")
    @RequestMapping(value = "/freezeAudit",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<FreezeAuditRecords> freezeAudit(@RequestBody Map<String, Object> param){
        String userSpaceId= String.valueOf(param.get("userSpaceId"));
        String status= String.valueOf(param.get("status"));
        String remake=String.valueOf(param.get("remake"));
        String id=String.valueOf(param.get("id"));
        ParkingOrders paramsOrder=new ParkingOrders();
        UserParkingSpace userParkingSpace=userParkingSpacebiz.selectById(userSpaceId);
        paramsOrder.setSpaceId(userParkingSpace.getSpaceId());
        paramsOrder.setOrderStatus(BaseConstants.OrderStatus.running);
        List<ParkingOrders> ordersList=parkingOrdersOSBiz.selectList(paramsOrder);
        if(ordersList!=null && ordersList.size()>0){
            return new ObjectRestResponse<FreezeAuditRecords>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_MSG);
        }
        FreezeAuditRecords freezeAuditRecords=baseBiz.selectById(id);
        freezeAuditRecords.setRemake(remake);
        freezeAuditRecords.setStatus(status);
        if("1".equals(status)){
            List<SharingRuleRecords> sharingRuleRecordsList=sharingRuleRecordsBiz.selectListAll();
            SharingRuleRecords sharingRuleRecords=new SharingRuleRecords();
            if(sharingRuleRecordsList!=null && sharingRuleRecordsList.size()>0){
                sharingRuleRecords=sharingRuleRecordsList.get(0);
            }
            userParkingSpace.setEnabledFlag(BaseConstants.enabledFlag.n);
            userParkingSpacebiz.updateById(userParkingSpace);
            Date date = DateUtil.parse(DateUtil.format(new Date()), DateUtil.YYYY_MM_DD);
            date.setDate(date.getDate()+Integer.valueOf(sharingRuleRecords.getFreezingTimes()));
            freezeAuditRecords.setFreezeTime(date);
            freezeAuditRecords.setAttr1(String.valueOf(new Date().getMonth()+1));
        }
        baseBiz.updateSelectiveById(freezeAuditRecords);
        return new ObjectRestResponse<FreezeAuditRecords>().data(freezeAuditRecords);
    }



}
