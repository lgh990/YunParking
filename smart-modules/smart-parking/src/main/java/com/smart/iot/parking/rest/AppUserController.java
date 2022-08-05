package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.AppUserBiz;
import com.smart.iot.parking.entity.AppUser;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("appUser")
@CheckClientToken
@CheckUserToken
@Api(tags = "app用户表")
public class AppUserController extends BaseController<AppUserBiz,AppUser,String> {


    @Override
    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<AppUser> list(@RequestParam Map<String, Object> params){
        String beginTime="";
        if(params.get("beginTime")!=null) {
            beginTime = String.valueOf(params.get("beginTime"));
        }
        String endTime="";
        if(params.get("beginTime")!=null) {
            endTime=String.valueOf(params.get("endTime"));
        }
        params.remove("beginTime");
        params.remove("endTime");
        Query query = new Query(params);
        return baseBiz.selectByQuery(query,beginTime,endTime);
    }

    @IgnoreUserToken
    @ApiOperation("根据账户名获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ObjectRestResponse<AppUser> validate(String username) {
        AppUser user = new AppUser();
        return new ObjectRestResponse<AppUser>().data(getUserByUsername(username));
    }


    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public AppUser getUserByUsername(String username) {
        AppUser user = new AppUser();
        user.setName(username);
        return baseBiz.selectOne(user);
    }

    @ApiOperation("用户增长数")
    @RequestMapping(value = "/queryUserGrowth")
    public Object queryUserGrowth(@RequestParam Map params){
        return baseBiz.queryUserCountByTime(params);
    }

    @ApiOperation("用户类型数")
    @RequestMapping(value = "/queryUserByType")
    public List queryUserByType(){
        return baseBiz.queryUserByType();
    }

    @ApiOperation("用户总数")
    @RequestMapping(value = "/selectCount")
    public Map selectCount(){
        Map map = Maps.newHashMap();
        map.put("selectCount",baseBiz.selectCount(null));
        return map;
    }


}
