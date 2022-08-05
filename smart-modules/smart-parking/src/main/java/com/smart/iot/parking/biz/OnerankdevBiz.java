package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.DevType;
import com.smart.iot.parking.entity.Onerankdev;
import com.smart.iot.parking.mapper.DevTypeMapper;
import com.smart.iot.parking.mapper.OnerankdevMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:1
 */
@Transactional
@Service
public class OnerankdevBiz extends BusinessBiz<OnerankdevMapper,Onerankdev> {
    @Autowired
    public DevTypeMapper devTypeMapper;
    @Autowired
    public SceneDevBiz sceneDevBiz;
    public ObjectRestResponse<Object> queryOnerankdevBySpaceid(String id) {
        List<Onerankdev> onerankdevList = this.mapper.queryOnerankdevBySpaceid(id);
        List<DevType> sceneDevs = devTypeMapper.queryOnerankeByScenetype("space");
        Map map = new HashMap<String,Object>();
        map.put("onerankdevList",onerankdevList);
        map.put("sceneDevList",sceneDevs);
        return new ObjectRestResponse<Object>().data(map);
    }

    public TableResultResponse<Onerankdev> querySpaceUnbindOnerankdevByDevtype(String devType) {
        List<Onerankdev> onerankdevList = this.mapper.querySpaceUnbindOnerankdevByDevtype(devType);
        return new TableResultResponse<Onerankdev>(onerankdevList.size(),onerankdevList);
    }

    public ObjectRestResponse<Object> queryBindOnerankdevByIoid(String id) {
        List<Onerankdev> onerankdevList = this.mapper.queryBindOnerankdevByIoidAndDevType(id,null);
        List<DevType> sceneDevs = devTypeMapper.queryOnerankeByScenetype("io");
        Map map = new HashMap<String,Object>();
        map.put("onerankdevList",onerankdevList);
        map.put("sceneDevList",sceneDevs);
        return new ObjectRestResponse<Object>().data(map);
    }

    public TableResultResponse<Onerankdev> queryIoUnbindOnerankdevByDevtype(String devType) {
        List<Onerankdev> onerankdevList = this.mapper.queryIoUnbindOnerankdevByDevtype(devType);
        return new TableResultResponse<Onerankdev>(onerankdevList.size(),onerankdevList);
    }

}
