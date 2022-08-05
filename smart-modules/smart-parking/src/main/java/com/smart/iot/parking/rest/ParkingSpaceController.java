package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.*;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.mapper.ParkingMapper;
import com.smart.iot.parking.vo.LotMsgVo;
import com.smart.iot.parking.vo.ParkingSpaceVo;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingSpace")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位管理")
public class ParkingSpaceController extends BaseController<ParkingSpaceBiz,ParkingSpace,String> {
    @Autowired
    public ParkingMapper parkingMapper;
    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    @Autowired
    public PlateBiz plateBiz;
    @Autowired
    public ParkingOrdersBiz parkingOrdersBiz;
    @Override//一个城市内的车位编号是唯一的
    public ObjectRestResponse<ParkingSpace> add(@RequestBody ParkingSpace parkingSpace){
        return baseBiz.add(parkingSpace);
    }

    /**
     * caoyingde 核验代码添加的临时方法 2020/12/11
     * @param spaceNum
     * @return
     */
    @RequestMapping(value = "/queryRoadSpaceInfo",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询手持机室内临时车位和停车记录")
    public ObjectRestResponse<Object> queryRoadSpaceInfo(@RequestParam Map<String,String> params){
        ParkingSpace parkingSpace=new ParkingSpace();
        parkingSpace.setSpaceNum(params.get("spaceNumber"));
        List<ParkingSpace> spaceList = parkingSpaceBiz.selectList(parkingSpace);
        HashMap<String,Object> objectHashMap = new HashMap<String,Object>();
        objectHashMap.put("parkingSpace",spaceList);
        return new ObjectRestResponse<Object>().data(objectHashMap);
    }

    @RequestMapping(value = "/queryTemSpaceAndLotMsgByParkingId",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询手持机室内临时车位和停车记录")
    public ObjectRestResponse<Object> queryTemSpaceAndLotMsgByParkingId(@RequestBody Parking parking){
       ParkingSpace parkingSpace=new ParkingSpace();
        parkingSpace.setSpaceType(BaseConstants.SpaceType.temporary);
        parkingSpace.setParkingId(parking.getParkingId());
        List<ParkingSpace> spaceList = parkingSpaceBiz.selectList(parkingSpace);
        List<LotMsgVo> lotMsgs = lotMsgBiz.queryRunLotMsgByParkingIdAndSpaceType(parking.getParkingId());
        HashMap<String,Object> objectHashMap = new HashMap<String,Object>();
        objectHashMap.put("parkingSpace",spaceList);
        objectHashMap.put("lotMsg",lotMsgs);
        return new ObjectRestResponse<Object>().data(objectHashMap);
    }
    @Override
    public ObjectRestResponse<ParkingSpace> update(@RequestBody ParkingSpace parkingSpace){
        return baseBiz.update(parkingSpace);
    }
    @RequestMapping(value = "/mergeOne/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询单个对象" )
    public ObjectRestResponse<Object> mergeOne(@RequestParam Map<String, Object> params){
        List<ParkingSpace> objects = new ArrayList<ParkingSpace>();
        ParkingSpace o = baseBiz.selectById(params.get("spaceId"));
        objects.add(o);
        List<ParkingSpace> list = (List<ParkingSpace>) objects;
        ParkingSpaceVo space=null;
        try {
            if(list.size() != 0) {
                mergeCore.mergeResult(ParkingSpace.class, list);
                List<ParkingSpaceVo> spaceList=parkingSpaceBiz.parkingSpaceListToParkingSpaceVoList(list);
                space = spaceList.get(0);
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
        return new ObjectRestResponse<Object>().data(space);
    }

    @ApiOperation("车位管理-分页获取数据")
    @RequestMapping(value = "/queryParkingSpace",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "spaceNum", value = "车位号", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "spaceType", value = "车位类型", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "beginTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "分页条数", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页页数", dataType = "String"),
    })
    public TableResultPageResponse<Object> queryParkingSpace(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        return parkingSpaceBiz.queryParkingSpace(params);
    }

    @RequestMapping(value = "queryLicensePlateBySpace",method = RequestMethod.GET)
    public ObjectRestResponse<Object> queryLicensePlateBySpace(@RequestParam Map<String, Object> params){
        if(params.get("plateNumber")==null){
            return new ObjectRestResponse<Object>().data("");
        }
        String plateNumber = String.valueOf(params.get("plateNumber"));
        Plate plateParams=new Plate();
        plateParams.setCarNumber(plateNumber);
        Plate plate=plateBiz.selectOne(plateParams);
        if(plate!=null ){
            ParkingOrders paramsOrder=new ParkingOrders();
            paramsOrder.setPlaId(plate.getPlaId());
            paramsOrder.setOrderStatus(BaseConstants.OrderStatus.running);
            paramsOrder.setParkingBusType("2");
            ParkingOrders parkingOrders=parkingOrdersBiz.selectOne(paramsOrder);
            if(parkingOrders!=null  && !StringUtil.isEmpty(parkingOrders.getSpaceId())){
                ParkingSpace parkingSpace=parkingSpaceBiz.selectById(parkingOrders.getSpaceId());
                return new ObjectRestResponse<Object>().data(parkingSpace);
            }
        }
        return new ObjectRestResponse<Object>().data("");
    }

}
