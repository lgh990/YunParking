package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.SpaceOnerankde;
import com.smart.iot.parking.mapper.SpaceOnerankdeMapper;
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
public class SpaceOnerankdeBiz extends BusinessBiz<SpaceOnerankdeMapper,SpaceOnerankde> {

    public void deleteOnerankdevByDevsn(String devSn) {
        this.mapper.deleteOnerankdevByDevsn(devSn);
    }

    public Integer addBatchOnerankde(List<SpaceOnerankde> onerankdes){
        for (SpaceOnerankde onerankde: onerankdes) {
            onerankde.setSoId(StringUtil.uuid());
        }
        return this.mapper.addBatchOnerankde(onerankdes);
    }
}
