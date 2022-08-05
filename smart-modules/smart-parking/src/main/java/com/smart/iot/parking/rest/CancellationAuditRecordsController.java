package com.smart.iot.parking.rest;

import com.smart.iot.onsite.biz.ParkingOrdersOSBiz;
import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cancellationAuditRecords")
@CheckClientToken
@CheckUserToken
@Api(tags = "注销审核记录表")
public class CancellationAuditRecordsController extends BaseController<CancellationAuditRecordsBiz,CancellationAuditRecords,String> {

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

    @ApiOperation("添加注销审核")
    @RequestMapping(value = "/addCancellationAuditRecords",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "userSpaceId", value = "共享车位id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType="body", name = "spaceId", value = "车位编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "parkingId", value = "停车场编号", required = true, dataType = "String")
    })
    public ObjectRestResponse<CancellationAuditRecords> addCancellationAuditRecords(@RequestBody Map<String, Object> param){
        String userSpaceId= String.valueOf(param.get("userSpaceId"));
        String spaceId=String.valueOf(param.get("spaceId"));
        String userId=String.valueOf(param.get("userId"));
        String parkingId=String.valueOf(param.get("parkingId"));
        ParkingOrders paramsOrder=new ParkingOrders();
        UserParkingSpace userParkingSpace=userParkingSpacebiz.selectById(userSpaceId);
        paramsOrder.setSpaceId(userParkingSpace.getSpaceId());
        paramsOrder.setOrderStatus(BaseConstants.OrderStatus.running);
        List<ParkingOrders> ordersList=parkingOrdersOSBiz.selectList(paramsOrder);
        if(ordersList!=null && ordersList.size()>0){
            return new ObjectRestResponse<UserParkingSpace>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_MSG);
        }
        List<SharingRuleRecords> sharingRuleRecordsList=sharingRuleRecordsBiz.selectListAll();
        SharingRuleRecords sharingRuleRecords=new SharingRuleRecords();
        if(sharingRuleRecordsList!=null && sharingRuleRecordsList.size()>0){
            sharingRuleRecords=sharingRuleRecordsList.get(0);
        }
        CancellationAuditRecords cancellationAuditRecords=new CancellationAuditRecords();
        cancellationAuditRecords.setUserSpaceId(userSpaceId);
        cancellationAuditRecords.setStatus("0");
        Calendar calendar = Calendar.getInstance();
        cancellationAuditRecords.setAttr1(String.valueOf(calendar.get(Calendar.YEAR)));
        Long count=baseBiz.selectCount(cancellationAuditRecords);
        if(sharingRuleRecords!=null){
            if(count>=Integer.valueOf(sharingRuleRecords.getFreezingTimes())){
                return new ObjectRestResponse<CancellationAuditRecords>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, "冻结次数已超过本月使用次数，请下月进行申请！");
            }
        }
        AppUser appUser=appUserBiz.selectById(userId);
        Parking parking=parkingBiz.selectById(parkingId);
        ParkingSpace space=parkingSpaceBiz.selectById(spaceId);
        CancellationAuditRecords cancellationAuditRecords1=new CancellationAuditRecords();
        cancellationAuditRecords1.setId(StringUtil.uuid());
        cancellationAuditRecords1.setParkingName(parking.getParkingName());
        cancellationAuditRecords1.setSpaceNum(space.getSpaceNum());
        cancellationAuditRecords1.setStatus("0");
        cancellationAuditRecords1.setUserSpaceId(userSpaceId);
        cancellationAuditRecords1.setTelephone(appUser.getMobile());
        baseBiz.insertSelective(cancellationAuditRecords1);
        return new ObjectRestResponse<CancellationAuditRecords>().data(cancellationAuditRecords1);
    }


    @ApiOperation("注销审核")
    @RequestMapping(value = "/CancellationAudit",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "userSpaceId", value = "共享车位id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "status", value = "注销状态（0=审核中，1=通过，2=拒绝）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "remake", value = "注销备注", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="body", name = "id", value = "注销审核记录编号", required = true, dataType = "String"),
    })
    public ObjectRestResponse<CancellationAuditRecords> CancellationAudit(@RequestBody Map<String, Object> param){
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
            return new ObjectRestResponse<CancellationAuditRecords>().BaseResponse(BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_CODE, BaseConstants.StateConstates.EXIST_ORDERS_NOTALLOW_MSG);
        }
        CancellationAuditRecords cancellationAuditRecords=baseBiz.selectById(id);
        cancellationAuditRecords.setRemake(remake);
        cancellationAuditRecords.setStatus(status);
        if("1".equals(status)){
            List<SharingRuleRecords> sharingRuleRecordsList=sharingRuleRecordsBiz.selectListAll();
            SharingRuleRecords sharingRuleRecords=new SharingRuleRecords();
            if(sharingRuleRecordsList!=null && sharingRuleRecordsList.size()>0){
                sharingRuleRecords=sharingRuleRecordsList.get(0);
            }
            //注销成功执行业务
            /*userParkingSpace.setEnabledFlag(BaseConstants.enabledFlag.n);
            userParkingSpacebiz.updateById(userParkingSpace);*/
            Calendar calendar = Calendar.getInstance();
            cancellationAuditRecords.setAttr1(String.valueOf(calendar.get(Calendar.YEAR)));
            userParkingSpace.setAttr1("n");
            userParkingSpacebiz.updateById(userParkingSpace);
            ParkingSpace parkingSpace=parkingSpaceBiz.selectById(userParkingSpace.getSpaceId());
            parkingSpace.setAbscissa("0");
            parkingSpace.setOrdinate("0");
            parkingSpaceBiz.updateById(parkingSpace);
        }
        baseBiz.updateSelectiveById(cancellationAuditRecords);
        return new ObjectRestResponse<CancellationAuditRecords>().data(cancellationAuditRecords);
    }
}
