package com.yuncitys.smart.parking.admin.rest;

import com.yuncitys.smart.parking.admin.biz.DepartBiz;
import com.yuncitys.smart.parking.admin.entity.Depart;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.admin.vo.DepartTree;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author smart
 */
@RestController
@RequestMapping("depart")
@Api(tags = "部门管理")
public class DepartController extends BaseController<DepartBiz,Depart,String> {
    @CheckUserToken
    @ApiOperation("获取部门树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DepartTree> getTree() {
        List<Depart> departs = this.baseBiz.selectListAll();
        List<DepartTree> trees = new ArrayList<>();
        departs.forEach(dictType -> {
            trees.add(new DepartTree(dictType.getId(), dictType.getParentId(), dictType.getName(),dictType.getCode()));
        });
        return TreeUtil.bulid(trees, "-1", null);
    }
    @CheckUserToken
    @ApiOperation("获取部门关联用户")
    @RequestMapping(value = "user",method = RequestMethod.GET)
    public TableResultResponse<User> getDepartUsers(String departId,String userName){
        return this.baseBiz.getDepartUsers(departId,userName);
    }
    @CheckUserToken
    @ApiOperation("部门添加用户")
    @RequestMapping(value = "user",method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> addDepartUser(String departId, String userIds){
        this.baseBiz.addDepartUser(departId,userIds);
        return new ObjectRestResponse<>().data(true);
    }
    @CheckUserToken
    @ApiOperation("部门移除用户")
    @RequestMapping(value = "user",method = RequestMethod.DELETE)
    public ObjectRestResponse<Boolean> delDepartUser(String departId,String userId){
        this.baseBiz.delDepartUser(departId,userId);
        return new ObjectRestResponse<>().data(true);
    }
    @CheckUserToken
    @ApiOperation("获取部门信息")
    @RequestMapping(value = "getByPK/{id}",method = RequestMethod.GET)
    public Map<String,String> getDepart(@PathVariable String id){
        return this.baseBiz.getDeparts(id);
    }

}
