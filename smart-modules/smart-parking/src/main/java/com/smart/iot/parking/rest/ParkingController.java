package com.smart.iot.parking.rest;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.ParkingBusinessType;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.feign.ChargeRulesTypeFeign;
import com.smart.iot.parking.feign.DepartFeign;
import com.smart.iot.parking.utils.publicUtils;
import com.smart.iot.parking.vo.ParkingVo;
import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("parking")
@CheckUserToken
@CheckClientToken
@Api(tags = "停车场管理")
public class ParkingController extends BaseController<ParkingBiz,Parking,String> {

    @Autowired
    public ParkingBiz parkingBiz;
    @Autowired
    public DepartFeign departFeign;
    @Autowired
    public ChargeRulesTypeFeign chargeRulesTypeFeign;
    @RequestMapping(value = "/queryComponentDataByAreaid",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据区层id查询所有组件")
    public ObjectRestResponse<Map<String, Object>> queryComponentDataByAreaid(String areaId){
        ObjectRestResponse<Map<String, Object>> entityObjectRestResponse = new ObjectRestResponse<>();
        entityObjectRestResponse = parkingBiz.queryMapInfo(areaId);
        return entityObjectRestResponse;
    }

    @ApiOperation("获取附近停车场数据")
    @RequestMapping(value = "/queryNearbyParking",method = RequestMethod.POST)
    @ResponseBody
    public TableResultResponse queryNearbyParking(@RequestBody Map<String, Object> params){
         List<Parking> parkingList= parkingBiz.queryNearbyParking(params);
        List<ParkingVo> parkingVoList=parkingBiz.parkingToParkingVo(parkingList);
        TableResultResponse tableResultResponse=new TableResultResponse(parkingVoList.size(),parkingVoList);
        return tableResultResponse;
    }


    @ApiOperation("查询月卡停车场")
    @RequestMapping(value = "/queryMonthlyCardByParking",method = RequestMethod.GET)
    public TableResultPageResponse queryMonthlyCardByParking(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        TableResultPageResponse tableResultPageResponse=baseBiz.selectByPageQuery(query,null,null);
        if(tableResultPageResponse!=null && tableResultPageResponse.getData()!=null ) {
            List<ParkingVo> parkingVoList=baseBiz.parkingToParkingVos(tableResultPageResponse.getData().getRows());
            tableResultPageResponse.getData().setRows(parkingVoList);
        }
        return tableResultPageResponse;
    }


    @ApiOperation("根据停车场id更新停车场首小时价格")
    @RequestMapping(value = "/updateFirstHourPriceByParkingId",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Parking> updateFirstHourPriceByParkingId(@RequestBody Parking parking){
        BigDecimal price = parking.getFirstHourPrice();
        parking = parkingBiz.selectById(parking.getParkingId());
        parking.setFirstHourPrice(price);
        baseBiz.saveOrUpdate(parking);
        return new ObjectRestResponse<Parking>().data(parking);
    }

    @ApiOperation("根据停车场id更新停车场首小时价格")
    @RequestMapping(value = "/updateFirstHourPrice",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Parking> updateFirstHourPrice(@RequestBody Map<String, Object> params){
        BigDecimal price =new BigDecimal(String.valueOf(params.get("firstHourPrice")));
        Parking parking = parkingBiz.selectById(String.valueOf(params.get("parkingId")));
        parking.setFirstHourPrice(price);
        baseBiz.saveOrUpdate(parking);
        return new ObjectRestResponse<Parking>().data(parking);
    }



    @ApiOperation("根据权限获取停车场")
    @RequestMapping(value = "/queryParkingByRole",method = RequestMethod.GET)
    public TableResultPageResponse queryParkingByRole(@RequestParam Map<String, Object> params){
        String userID = BaseContextHandler.getUserID();
        List<HashMap<String,Object>> treeMap = publicUtils.getCurrDepart(departFeign);
        HashMap<String,Object> map = (HashMap<String, Object>) treeMap.get(0);
        if(!"PARKING_ADMIN".equals(map.get("code")))//停车场超级管理员,只查询所有停车场管理员
        {
            params.put("userId",userID);
        }
        //查询列表数据
        Query query = new Query(params);
        TableResultPageResponse tableResultPageResponse=baseBiz.selectByPageQuery(query,null,null);
        if(tableResultPageResponse!=null && tableResultPageResponse.getData()!=null ) {
            List<ParkingVo> parkingVoList=baseBiz.parkingToParkingVo(tableResultPageResponse.getData().getRows());
            tableResultPageResponse.getData().setRows(parkingVoList);
        }
        return tableResultPageResponse;
    }

    @Override
    public ObjectRestResponse<Parking> add(@RequestBody Parking parking){
        return parkingBiz.add(parking);
    }


    @ApiOperation("添加停车场并且收费规则")
    @RequestMapping(value = "/addParkingAndChargeRules",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Parking> addParkingAndChargeRules(@RequestBody Map<String, Object> params){
            Parking parking = new Parking();
        try {
            Map parkingMap = (Map) params.get("parking");
            Map chargeRule = (Map) params.get("chargeRule");
            parking.setChargeRuleId(String.valueOf(parkingMap.get("chargeRuleId")));
            parking.setCityId(String.valueOf(parkingMap.get("cityId")));
            parking.setParkingAddress(String.valueOf(parkingMap.get("parkingAddress")));
            parking.setParkingBusType(String.valueOf(parkingMap.get("parkingBusType")));
            parking.setParkingName(String.valueOf(parkingMap.get("parkingName")));
            parking.setParkingType(String.valueOf(parkingMap.get("parkingType")));
            parking.setPointLat(String.valueOf(parkingMap.get("pointLat")));
            parking.setPointLng(String.valueOf(parkingMap.get("pointLng")));
            parking.setUserId(String.valueOf(parkingMap.get("userId")));
            parking.setParkingId(StringUtil.uuid());
            parkingBiz.add(parking);
            chargeRule.put("parkingId",parking.getParkingId());
            chargeRulesTypeFeign.addOrUpdateChargeRule(chargeRule);
        }catch (Exception e){
            e.getMessage();
        }
        return new ObjectRestResponse<Parking>().data(parking);
    }

    @ApiOperation("停车场总数图")
    @RequestMapping(value = "/queryEveryMonthCountByYear",method = RequestMethod.POST)
    public Map queryEveryMonthCountByYear(@RequestParam Map params){
        return baseBiz.queryEveryMonthCountByYear(params);
    }

    @ApiOperation("停车场同比")
    @RequestMapping(value = "/queryMonthCount",method = RequestMethod.POST)
    public Map queryMonthCount(@RequestParam Map params){
        Map primary = Maps.newHashMap();
        Map secondary = Maps.newHashMap();
        primary.put("year",params.get("primaryYear"));
        secondary.put("year",params.get("secondaryYear"));
        if(params.get("address")!=null){
            primary.put("address",params.get("address"));
            secondary.put("address",params.get("address"));
        }
        if(params.get("parkingId")!=null){
            primary.put("parkingId",params.get("parkingId"));
            secondary.put("parkingId",params.get("parkingId"));
        }
        String primaryQueryMonth = new String(params.get("primaryMonth").toString());
        String secondaryQueryMonth = new String(params.get("secondaryMonth").toString());
        if(primaryQueryMonth.contains(",")){
            String[] primaryMonths = primaryQueryMonth.split(",");
            String[] secondaryMonths = secondaryQueryMonth.split(",");
            if(primaryMonths.length>=3){
                primary.put("firstMonth",primaryMonths[0]);
                primary.put("secondMonth",primaryMonths[1]);
                primary.put("thirdMonth",primaryMonths[2]);
            }
            if(secondaryMonths.length>=3){
                secondary.put("firstMonth",secondaryMonths[0]);
                secondary.put("secondMonth",secondaryMonths[1]);
                secondary.put("thirdMonth",secondaryMonths[2]);
            }
        }else{
            primary.put("month",primaryQueryMonth);
            secondary.put("month",secondaryQueryMonth);
        }
        return baseBiz.queryMonthCount(primary,secondary);
    }

}
