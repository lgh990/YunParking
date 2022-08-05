package com.smart.iot.dev.biz;

import com.smart.iot.dev.entity.SpaceOnerankde;
import com.smart.iot.dev.mapper.SpaceOnerankdeMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车位与设备关联表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 14:30:11
 */
@Service
public class SpaceOnerankdeBiz extends BusinessBiz<SpaceOnerankdeMapper, SpaceOnerankde> {

    public void deleteOnerankdevByDevsn(String devSn) {
        this.mapper.deleteOnerankdevByDevsn(devSn);
    }


}
