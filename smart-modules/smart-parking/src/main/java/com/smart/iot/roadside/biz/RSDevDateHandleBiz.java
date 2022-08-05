package com.smart.iot.roadside.biz;

import com.smart.iot.parking.biz.LotMsgBiz;
import com.smart.iot.parking.biz.OnerankdevBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.biz.PublishMsgBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.*;
import com.smart.iot.parking.mapper.LotMsgMapper;
import com.smart.iot.parking.vo.Devpackage;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RSDevDateHandleBiz {

    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public ParkingSpaceBiz spaceBiz;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    @Autowired
    public LotMsgMapper lotMsgMapper;
    @Autowired
    public LotMsgBiz lotMsgBiz;

    public void HandleOnsiteGeomagneticUpageck(Devpackage devpackage, Onerankdev onerankdev, ParkingSpace space, Parking parking) {
        space = updateAbnormalLotMsg(space);
        if ("地磁状态变化包".equals(devpackage.getUpackageType())) {
            //每次数据包发生时数据流水加1，初始值为0，表示开机第一包，而后255加1后回1；
            log.info("------------------------------------------------开始处理地磁状态变化包------------------------------------------------");
            if (space != null) {
                String occurrenceTime = devpackage.getOccurrenceTime();
                String spaceStatus = devpackage.getSpaceStatus();
                int flowPackgeNum = devpackage.getFlowPackgeNum();
                log.info(space.getSpaceNum() + "车位" + ("y".equals(spaceStatus) ?"有车":"无车"));
                space.setLotType(spaceStatus);
                //不处理地磁重发数据
                if (flowPackgeNum == onerankdev.getFlowNum() && flowPackgeNum != 0) {
                    log.info("不处理地磁重发数据");
                    return;
                }
                //判断是否断包
                boolean brokenPackge = JudegeWhetherBrokenPackge(flowPackgeNum, onerankdev.getFlowNum());
                int day = DateUtil.dateDiff("dd",DateUtil.dateTimeToStr(new Date()),occurrenceTime);
                //预防地磁脏数据
                if(day > 365 || day < -365)
                {
                    parkingOrdersRSBiz.ExceptionReportProcess(space,spaceStatus, BaseConstants.errorType.geomagneticTime);
                } else if (!brokenPackge) {
                    parkingOrdersRSBiz.ExceptionReportProcess(space,spaceStatus, BaseConstants.errorType.flow);
                } else {
                    if (space.getLotType().equals(spaceStatus)) {
                        parkingOrdersRSBiz.updateOrder(space, spaceStatus, occurrenceTime);
                    }
                }
                publishMsgBiz.publishPdaAndWebMsg(parking.getParkingId(), space);
            }
            spaceBiz.saveOrUpdate(space);
            //更新最新数据包流水号
            log.info("更新设备流水包号");
            onerankdev.setFlowNum(devpackage.getFlowPackgeNum());
            onerankdev.setLastFlowDate(DateUtil.dateTimeToStr(new Date()));
        }
        onerankdevBiz.saveOrUpdate(onerankdev);

    }

    private ParkingSpace updateAbnormalLotMsg(ParkingSpace space) {
        //判断车位是否处于失联状态
        if(space.getSpaceStatus().equals(BaseConstants.errorType.communication) ) {
            if (space.getLotType().equals(BaseConstants.enabledFlag.y)) {
                Example example = new Example(ParkingOrders.class);
                example.createCriteria().andEqualTo("orderStatus", BaseConstants.OrderStatus.running);
                example.createCriteria().andEqualTo("spaceId", space.getSpaceId());
                List<ParkingOrders> parkingOrders = parkingOrdersRSBiz.selectByExample(example);
                if (parkingOrders.size() == 0) {
                    LotMsg lotMsg = lotMsgMapper.queryRunLotMsgBySpaceId(space.getSpaceId());
                    if(lotMsg!=null) {
                        String date = DateUtil.getDateTime();
                        lotMsg.setEndDate(date);
                        lotMsgBiz.saveOrUpdate(lotMsg);
                        LotMsg newLotMsg = new LotMsg();
                        newLotMsg.setLmId(StringUtil.uuid());
                        newLotMsg.setBeginDate(date);
                        newLotMsg.setSpaceId(space.getSpaceId());
                        lotMsgBiz.saveOrUpdate(newLotMsg);
                    }
                }
                space = spaceBiz.selectById(space.getSpaceId());
            }
            space.setSpaceStatus(BaseConstants.errorType.normal);
            spaceBiz.update(space);
        }
        return space;
    }

    /**
     * 判断是否断包
     * @param flowPackgeNum
    * @param laseFlowNum
     * @return
     */
    public boolean JudegeWhetherBrokenPackge(int flowPackgeNum, Integer laseFlowNum) {
        log.info("最新记录流水包号："+laseFlowNum);
        log.info("当前流水包号："+flowPackgeNum);
        if((flowPackgeNum == laseFlowNum + 1) || (laseFlowNum == 255 && flowPackgeNum == 1) || (laseFlowNum == 0 && flowPackgeNum == 0))
        {
            return true;
        }else
        {
            return false;
        }
    }

}

