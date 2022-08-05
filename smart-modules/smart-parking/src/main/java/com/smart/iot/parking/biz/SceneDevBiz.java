package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.SceneDev;
import com.smart.iot.parking.mapper.SceneDevMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 场景-设备表（车位-出入口对应设备类型）
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-07 13:53:45
 */
@Service
public class SceneDevBiz extends BusinessBiz<SceneDevMapper,SceneDev> {
}
