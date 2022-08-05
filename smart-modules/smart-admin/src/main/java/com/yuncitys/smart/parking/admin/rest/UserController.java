package com.yuncitys.smart.parking.admin.rest;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.admin.biz.MenuBiz;
import com.yuncitys.smart.parking.admin.biz.PositionBiz;
import com.yuncitys.smart.parking.admin.biz.UserBiz;
import com.yuncitys.smart.parking.admin.entity.Menu;
import com.yuncitys.smart.parking.admin.entity.Position;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.admin.mapper.DepartMapper;
import com.yuncitys.smart.parking.admin.rpc.service.PermissionService;
import com.yuncitys.smart.parking.admin.vo.AuthUser;
import com.yuncitys.smart.parking.admin.vo.FrontUser;
import com.yuncitys.smart.parking.admin.vo.MenuTree;
import com.yuncitys.smart.parking.api.vo.user.UserInfo;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.auth.client.annotation.IgnoreUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.swing.StringUIClientPropertyKey;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-08 11:51
 */
@RestController
@RequestMapping("user")
@CheckUserToken
@CheckClientToken
@Api(tags = "用户模块")
public class UserController extends BaseController<UserBiz, User,String> {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PositionBiz positionBiz;

    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private DepartMapper departMapper;
    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();
    @IgnoreUserToken
    @ApiOperation("账户密码验证")
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ObjectRestResponse<UserInfo> validate(String username, String password) {
        return new ObjectRestResponse<UserInfo>().data(permissionService.validate(username, password));
    }

    @IgnoreUserToken
    @ApiOperation("根据账户名获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ObjectRestResponse<AuthUser> validate(String username) {
        AuthUser user = new AuthUser();
        BeanUtils.copyProperties(baseBiz.getUserByUsername(username), user);
        return new ObjectRestResponse<AuthUser>().data(user);
    }


    @ApiOperation("账户修改密码")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> changePassword(String oldPass, String newPass) {
        return new ObjectRestResponse<Boolean>().data(baseBiz.changePassword(oldPass, newPass));
    }
    @ApiOperation("获取用户列表")
    @RequestMapping(value = "/userInList", method = RequestMethod.POST)
    public List<User> userInList(@RequestParam List<String> userIdList) {
        Example example=new Example(User.class);
        example.createCriteria().andIn("id",userIdList);
        List<User> authUsers=baseBiz.selectByExample(example);
        return authUsers;
    }


    @ApiOperation("获取用户信息接口")
    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo() throws Exception {
        FrontUser userInfo = permissionService.getUserInfo();
        if (userInfo == null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @ApiOperation("获取用户访问菜单")
    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    public
    @ResponseBody
    List<MenuTree> getMenusByUsername() throws Exception {
        return permissionService.getMenusByUsername();
    }

    @ApiOperation("获取所有菜单")
    @RequestMapping(value = "/front/menu/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Menu> getAllMenus() throws Exception {
        return menuBiz.selectListAll();
    }

    @ApiOperation("获取用户可管辖部门id列表")
    @RequestMapping(value = "/dataDepart", method = RequestMethod.GET)
    public List<String> getUserDataDepartIds(String userId) {
        if (BaseContextHandler.getUserID().equals(userId)) {
            return baseBiz.getUserDataDepartIds(userId);
        }
        return new ArrayList<>();
    }

    @ApiOperation("获取用户流程审批岗位")
    @RequestMapping(value = "/flowPosition", method = RequestMethod.GET)
    public List<String> getUserFlowPositions(String userId) {
        if (BaseContextHandler.getUserID().equals(userId)) {
            return positionBiz.getUserFlowPosition(userId).stream().map(Position::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    @ApiOperation("添加手持机人员")
    @RequestMapping(value = "/saveOrUpdateUser", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<User> saveOrUpdateUser(@RequestBody Map<String, Object> params) {
        User user=baseBiz.getUserByUsername(String.valueOf(params.get("username")));
        if(user!=null){
            return new ObjectRestResponse<User>().BaseResponse(500,"手机号已存在，请重新输入！！");
        }
         user=new User();
        String uuid=StringUtil.uuid();
        user.setId(uuid);
        user.setDepartId(String.valueOf(params.get("departId")));
        user.setAttr1(String.valueOf(params.get("attr1")));
        user.setName(String.valueOf(params.get("name")));
        user.setUsername(String.valueOf(params.get("username")));
        user.setPassword(encoder.encode(String.valueOf(params.get("password"))));
        baseBiz.saveOrUpdate1(user);
        departMapper.insertDepartUser(StringUtil.uuid(),user.getDepartId(),uuid,BaseContextHandler.getUserID());
        return new ObjectRestResponse<User>().data(user);
    }

}
