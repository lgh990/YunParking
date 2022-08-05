package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.SceneDevBiz;
import com.smart.iot.parking.entity.SceneDev;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sceneDev")
@CheckClientToken
@CheckUserToken
@Api(tags = "场景管理")
public class SceneDevController extends BaseController<SceneDevBiz,SceneDev,String> {

}
