package com.smart.iot.roadside.biz;

import com.smart.iot.parking.biz.LotMsgBiz;
import com.smart.iot.parking.biz.ParkingSpaceBiz;
import com.smart.iot.parking.biz.PublishMsgBiz;
import com.smart.iot.parking.biz.SpaceExceptionProceRecordBiz;
import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.AbnormalFeedback;
import com.smart.iot.parking.entity.Onerankdev;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.mapper.AbnormalFeedbackMapper;
import com.smart.iot.parking.mapper.OnerankdevMapper;
import com.smart.iot.parking.mapper.UserMessageMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车位故障上报表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 18:21:12
 */
@Transactional
@Service
public class AbnormalFeedbackRSBiz extends BusinessBiz<AbnormalFeedbackMapper,AbnormalFeedback> {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @Autowired
    public LotMsgBiz lotMsgBiz;
    @Autowired
    public OnerankdevMapper onerankdevMapper;
    @Autowired
    public SpaceExceptionProceRecordBiz spaceExceptionProceRecordBiz;
    @Autowired
    public ParkingOrdersRSBiz parkingOrdersRSBiz;
    @Autowired
    public UserMessageMapper userMessageMapper;
    @Autowired
    public PublishMsgBiz publishMsgBiz;
    public ObjectRestResponse<AbnormalFeedback> haddleSpaceErrRecord(AbnormalFeedback abnormalFeedback) {
        String id = abnormalFeedback.getId();
        String proceStatus = abnormalFeedback.getProceStatus();

        //根据异常记录查询数据
        AbnormalFeedback abnormalFeedback1 = this.selectById(id);
        abnormalFeedback1.setProceStatus(proceStatus);
        this.saveOrUpdate(abnormalFeedback1);
        if(proceStatus.equals(BaseConstants.proceStatus.cancel)){
            return ObjectRestResponse.ok();
        }
        //根据记录查询车位
        ParkingSpace space = parkingSpaceBiz.selectById(abnormalFeedback1.getSpaceId());
        //将车位设为异常，不允许下单
        //判断车位状态，如果为系统上报异常状态，按照优先级，则不处理
        if(space.getSpaceStatus().equals(BaseConstants.errorType.normal)){
            space.setSpaceStatus(BaseConstants.errorType.handheld_machine);//手持机上报异常
            parkingSpaceBiz.saveOrUpdate(space);
            Onerankdev onerankdev = onerankdevMapper.queryOnerankdevBySpaceidAndDevType(space.getSpaceId(),BaseConstants.devType.SECOND_WC_GEOMAGNETIC);
            if(onerankdev == null)
            {
                onerankdev = onerankdevMapper.queryOnerankdevBySpaceidAndDevType(space.getSpaceId(),BaseConstants.devType.WC_GEOMAGNETIC);
            }
            if(onerankdev != null){
                //添加维护人员处理记录
                SpaceExceptionProceRecord processingRecord = new SpaceExceptionProceRecord();
                processingRecord.setBeginTime(DateUtil.getDateTime());
                processingRecord.setDevType(onerankdev.getOnerankdevType());
                processingRecord.setOnerankdeSn(onerankdev.getOnerankdevDevSn());
                processingRecord.setErrorType(BaseConstants.errorType.handheld_machine);
                processingRecord.setProceStatus(BaseConstants.proceStatus.running);
                processingRecord.setFeedbackType(abnormalFeedback.getFbType());
                processingRecord.setSpaceId(space.getSpaceId());
                spaceExceptionProceRecordBiz.saveOrUpdate(processingRecord);
            }
            publishMsgBiz.publishPdaAndWebMsg(space.getParkingId(), space);
        }
        //判断订单号是否为空
        if(!StringUtil.isEmpty(abnormalFeedback1.getOrderNum())){
            //关闭上传订单
            parkingOrdersRSBiz.closeOrder(abnormalFeedback1.getOrderNum());
            //关闭进行中订单
            parkingOrdersRSBiz.ExceptionReportProcess(space,null, BaseConstants.errorType.handheld_machine);
            //用户上报信息设置为已处理
            userMessageMapper.updateUserMessageByOrderNum(abnormalFeedback1.getOrderNum(),BaseConstants.proceStatus.complete);
        }
        return ObjectRestResponse.ok();
    }
}
