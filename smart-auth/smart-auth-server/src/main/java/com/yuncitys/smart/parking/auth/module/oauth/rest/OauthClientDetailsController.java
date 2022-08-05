package com.yuncitys.smart.parking.auth.module.oauth.rest;

import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.auth.module.oauth.biz.OauthClientDetailsBiz;
import com.yuncitys.smart.parking.auth.module.oauth.entity.OauthClientDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("oauthClientDetails")
public class OauthClientDetailsController extends BaseController<OauthClientDetailsBiz,OauthClientDetails,String> {

}
