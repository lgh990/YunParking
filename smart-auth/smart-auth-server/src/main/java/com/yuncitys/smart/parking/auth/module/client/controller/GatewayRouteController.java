package com.yuncitys.smart.parking.auth.module.client.controller;

import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.auth.module.client.biz.GatewayRouteBiz;
import com.yuncitys.smart.parking.auth.module.client.entity.GatewayRoute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("gatewayRoute")
public class GatewayRouteController extends BaseController<GatewayRouteBiz,GatewayRoute,String> {

}
