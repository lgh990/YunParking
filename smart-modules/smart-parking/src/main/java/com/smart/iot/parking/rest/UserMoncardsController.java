package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.UserMoncardsBiz;
import com.smart.iot.parking.entity.UserMoncards;
import com.smart.iot.parking.mapper.UserMoncardsMapper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("userMoncards")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户-月卡表")
public class UserMoncardsController extends BaseController<UserMoncardsBiz,UserMoncards,String> {
    @Autowired
    public UserMoncardsMapper userMoncardsMapper;
    @Autowired
    public UserMoncardsBiz userMoncardsBiz;

    @RequestMapping(value = "/queryMonthCardByUserId",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据用户id查询月卡用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "userId", value = "用户编号", required = true, dataType = "String")
    })
    public TableResultResponse queryMonthCardByUserId(String userId){
          return userMoncardsBiz.queryMonthCardByUserId(userId);
    }

    @ApiOperation("月卡-分页获取数据")
    @RequestMapping(value = "/queryMonthCards",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "moncardsTelephone", value = "卡主手机号", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "beginTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "分页条数", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页页数", dataType = "String"),
    })
    public TableResultPageResponse<Object> queryMonthCards(@RequestParam Map<String, Object> params){
        return userMoncardsBiz.queryMonthCards(params);
    }

}
