package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingBiz;
import com.smart.iot.parking.biz.UserParkingBiz;
import com.smart.iot.parking.entity.Parking;
import com.smart.iot.parking.entity.UserFeedback;
import com.smart.iot.parking.entity.UserParking;
import com.smart.iot.parking.feign.DepartFeign;
import com.smart.iot.parking.feign.UserFeign;
import com.smart.iot.parking.utils.publicUtils;
import com.smart.iot.parking.vo.UserParkingVo;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import io.swagger.annotations.Api;
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
@RequestMapping("userParking")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户-停车场")
public class UserParkingController extends BaseController<UserParkingBiz,UserParking,String> {
    @Autowired
    public UserParkingBiz userParkingBiz;
    @Autowired
    public DepartFeign departFeign;
    @Autowired
    public UserFeign userFeign;
    @Autowired
    public ParkingBiz parkingBiz;
    @RequestMapping(value = "addUserParkingRole",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增停车场人员-停车场绑定")
    public BaseResponse addUserParkingRole(@RequestBody Map<String, Object> params){
        return userParkingBiz.addUserParkingRole(params);
    }

    @ApiOperation("分页车位用户数据")
    @RequestMapping(value = "/userParkingBypage",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Object> userParkingBypage(@RequestParam Map<String, Object> params){
        HashMap<String,Object> map = new HashMap<String,Object>();
        //查询列表数据
        Query query = new Query(params);
        List<UserParking> userParkings = baseBiz.selectByPageQuery(query,null,null).getData().getRows();
        List<UserParkingVo> userParkingVoList=userParkingBiz.userParkingToUserParkingVo(userParkings);
        map.put("userParking",userParkingVoList);
        List<HashMap<String,Object>> treeMap = publicUtils.getCurrDepart(departFeign);
        treeMap.get(0).remove("children");
        map.put("depart",treeMap);
        return new ObjectRestResponse<Object>().data(map);
    }



    @Override
    public TableResultResponse list(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        Query query = new Query(params);
        TableResultResponse tableResultPageResponse=baseBiz.selectByQuery(query);
        if(tableResultPageResponse!=null && tableResultPageResponse.getData()!=null) {
            List<UserParking> userParkingList = tableResultPageResponse.getData().getRows();
            List<UserParkingVo> userParkingVoList=baseBiz.userParkingToUserParkingVo(userParkingList);
            tableResultPageResponse.getData().setRows(userParkingVoList);
        }
        return tableResultPageResponse;
    }

}
