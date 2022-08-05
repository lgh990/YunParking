package com.yuncitys.smart.parking.admin.rest;

import com.yuncitys.smart.parking.admin.biz.PositionBiz;
import com.yuncitys.smart.parking.admin.entity.Position;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.admin.vo.DepartTree;
import com.yuncitys.smart.parking.admin.vo.GroupTree;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("position")
@CheckUserToken
@CheckClientToken
@Api(tags="岗位模块")
public class PositionController extends BaseController<PositionBiz, Position,String> {

    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("岗位关联用户")
    public ObjectRestResponse modifyUsers(@PathVariable String id, String users) {
        baseBiz.modifyPositionUsers(id, users);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取岗位用户列表")
    public ObjectRestResponse<List<User>> getUsers(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<User>>().data(baseBiz.getPositionUsers(positionId));
    }

    @RequestMapping(value = "/{id}/group", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("岗位关联角色")
    public ObjectRestResponse modifyRoles(@PathVariable String id, String groups) {
        baseBiz.modifyPositionGroups(id, groups);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取岗位角色列表")
    @RequestMapping(value = "/{id}/group", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<GroupTree>> getRoles(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<GroupTree>>().data(baseBiz.getPositionGroups(positionId));
    }

    @ApiOperation("岗位分配管辖部门")
    @RequestMapping(value = "/{id}/depart", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyDeparts(@PathVariable String id, String departs) {
        baseBiz.modifyPositionDeparts(id, departs);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/depart", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取岗位管辖部门列表")
    public ObjectRestResponse<List<DepartTree>> getDeparts(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<DepartTree>>().data(baseBiz.getPositionDeparts(positionId));
    }
}
