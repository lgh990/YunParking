package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.CitysBiz;
import com.smart.iot.parking.entity.Citys;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("citys")
@CheckClientToken
@CheckUserToken
@Api(tags = "城市管理")
public class CitysController extends BaseController<CitysBiz,Citys,String> {

    @Override
    public TableResultResponse<Citys> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectByQuery(query,null,null);
    }

}
