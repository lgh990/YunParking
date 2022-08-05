package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingComponentDataBiz;
import com.smart.iot.parking.entity.ParkingComponent;
import com.smart.iot.parking.entity.ParkingComponentData;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("parkingComponentData")
@CheckClientToken
@CheckUserToken
@Api(tags = "组件数据管理")
public class ParkingComponentDataController extends BaseController<ParkingComponentDataBiz,ParkingComponentData,String> {
    @Autowired ParkingComponentDataBiz parkingComponentDataBiz;
    @RequestMapping(value = "addComponentData",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个组件对象")
    public ObjectRestResponse<ParkingComponentData> addComponentData(@RequestBody ParkingComponentData parkingComponentData){
        boolean flag =  parkingComponentDataBiz.to_change_component_to_map(parkingComponentData.getX(), parkingComponentData.getY(),parkingComponentData.getParkingAreaId(), parkingComponentData.getParkingId());
        if (flag) {
            throw new BusinessException("添加失败，该位置已存在部件");//"该位置已存在部件";
        }
        parkingComponentData.setComponentDataId(StringUtil.uuid());
        baseBiz.insertSelective(parkingComponentData);
        return new ObjectRestResponse<ParkingComponentData>().data(parkingComponentData);
    }

    @Override
    public ObjectRestResponse<ParkingComponentData> add(@RequestBody ParkingComponentData parkingComponentData){
        boolean flag =  parkingComponentDataBiz.to_change_component_to_map(parkingComponentData.getX(), parkingComponentData.getY(),parkingComponentData.getParkingAreaId(), parkingComponentData.getParkingId());
        if (flag) {
            throw new BusinessException("添加失败，该位置已存在部件");//"该位置已存在部件";
        }
        parkingComponentData.setComponentDataId(StringUtil.uuid());
        baseBiz.insertSelective(parkingComponentData);
        return new ObjectRestResponse<ParkingComponentData>().data(parkingComponentData);
    }

}
