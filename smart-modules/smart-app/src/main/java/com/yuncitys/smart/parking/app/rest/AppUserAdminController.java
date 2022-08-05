package com.yuncitys.smart.parking.app.rest;

import com.yuncitys.smart.parking.app.biz.AppUserBiz;
import com.yuncitys.smart.parking.app.entity.AppUser;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员用户信息管理
 */
@RestController
@RequestMapping("admin/appUser")
@CheckClientToken
@CheckUserToken
public class AppUserAdminController extends BaseController<AppUserBiz, AppUser,Integer> {

}
