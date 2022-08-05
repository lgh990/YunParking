package com.smart.iot.roadside.rest;

import com.smart.iot.parking.biz.SpaceExceptionProceRecordBiz;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.roadside.biz.SpaceExceptionProceRecordRSBiz;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spaceExceptionProceRecordRS")
@CheckClientToken
@CheckUserToken
@Api(tags = "室外车位异常记录")
public class SpaceExceptionProceRecordRSController extends BaseController<SpaceExceptionProceRecordBiz,SpaceExceptionProceRecord,String> {
    @Autowired
    public SpaceExceptionProceRecordRSBiz spaceExceptionProceRecordRSBiz;

    //更新异常车位记录，异常车位判断是否有车，有车状态下存在停车记录则把停车记录停掉，新建一笔以当前时间为准的停车记录
    @Override
    public ObjectRestResponse<SpaceExceptionProceRecord> update(@RequestBody SpaceExceptionProceRecord spaceExceptionProceRecord){
        return spaceExceptionProceRecordRSBiz.updateSpaceExceptionProceRecord(spaceExceptionProceRecord);
    }

}
