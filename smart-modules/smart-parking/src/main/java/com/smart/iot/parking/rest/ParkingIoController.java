package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingIoBiz;
import com.smart.iot.parking.entity.ParkingIo;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.vo.ParkingSpaceVo;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingIo")
@CheckClientToken
@CheckUserToken
@Api(tags = "出入口管理")
public class ParkingIoController extends BaseController<ParkingIoBiz,ParkingIo,String> {
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Override
    public ObjectRestResponse<ParkingIo> add(@RequestBody ParkingIo entity){
        entity.setParkingIoId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<ParkingIo>().data(entity);
    }

    @RequestMapping(value = "/mergeOne/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询单个对象" )
    public ObjectRestResponse<Object> mergeOne(@RequestParam Map<String, Object> params){
        List<ParkingIo> objects = new ArrayList<ParkingIo>();
        ParkingIo o = baseBiz.selectById(params.get("spaceId"));
        objects.add(o);
        List<ParkingIo> list = (List<ParkingIo>) objects;
        ParkingIo parkingIo=null;
        if(list.size() != 0) {
            //List<ParkingSpaceVo> spaceList=parkingIoBiz.(list);
            parkingIo = list.get(0);
        }
        return new ObjectRestResponse<Object>().data(parkingIo);
    }
}
