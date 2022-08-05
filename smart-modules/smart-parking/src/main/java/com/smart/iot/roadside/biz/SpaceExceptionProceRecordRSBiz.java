package com.smart.iot.roadside.biz;

import com.smart.iot.parking.biz.LotMsgBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.LotMsg;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.mapper.ParkingSpaceMapper;
import com.smart.iot.parking.mapper.SpaceExceptionProceRecordMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车位异常处理记录
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 18:59:22
 */
@Transactional
@Service
public class SpaceExceptionProceRecordRSBiz extends BusinessBiz<SpaceExceptionProceRecordMapper,SpaceExceptionProceRecord> {
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public ParkingSpaceMapper parkingSpaceMapper;

    public ObjectRestResponse<SpaceExceptionProceRecord> updateSpaceExceptionProceRecord(SpaceExceptionProceRecord spaceExceptionProceRecord) {
        this.updateSelectiveById(spaceExceptionProceRecord);
        SpaceExceptionProceRecord spaceExceptionProceRecord1 = this.selectById(spaceExceptionProceRecord.getId());
        LotMsg lotMsg = lotMsgMapper.queryRunLotMsgBySpaceId(spaceExceptionProceRecord1.getSpaceId());
        if(lotMsg != null)
        {
            lotMsg.setEndDate(DateUtil.getDateTime());
            lotMsgBiz.saveOrUpdate(lotMsg);
            LotMsg lotMsg1 = new LotMsg();
            lotMsg1.setBeginDate(DateUtil.getDateTime());
            lotMsg1.setSpaceId(spaceExceptionProceRecord1.getSpaceId());
            lotMsgBiz.saveOrUpdate(lotMsg1);
        }
        parkingSpaceMapper.updateSpaceStatus(BaseConstants.errorType.normal,spaceExceptionProceRecord1.getSpaceId());
        return new ObjectRestResponse<SpaceExceptionProceRecord>().data(spaceExceptionProceRecord);
    }
}
