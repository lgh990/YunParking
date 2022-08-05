package com.smart.iot.parking.biz;

import com.smart.iot.parking.constant.BaseConstants;
import com.smart.iot.parking.entity.SharingRuleRecords;
import com.smart.iot.parking.entity.UserParkingSpace;
import com.smart.iot.start.biz.EquipmentDataProcess;
import com.yuncitys.smart.parking.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.FreezeAuditRecords;
import com.smart.iot.parking.mapper.FreezeAuditRecordsMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 冻结审核记录表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-04-23 14:36:43
 */
@Service
public class FreezeAuditRecordsBiz extends BusinessBiz<FreezeAuditRecordsMapper,FreezeAuditRecords> {

    @Autowired
    public SharingRuleRecordsBiz sharingRuleRecordsBiz;
    @Autowired
    public UserParkingSpaceBiz userParkingSpaceBiz;
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(EquipmentDataProcess.class);
    //解冻定时器
    public void freezingTimer(){
        Example example=new Example(FreezeAuditRecords.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("status","1");
        criteria.andEqualTo("enabledFlag",BaseConstants.enabledFlag.y);
        List<FreezeAuditRecords> freezeAuditRecordsList=this.selectByExample(example);
        List<SharingRuleRecords> sharingRuleRecordsList=sharingRuleRecordsBiz.selectListAll();
        SharingRuleRecords sharingRuleRecords=new SharingRuleRecords();
        if(sharingRuleRecordsList!=null && sharingRuleRecordsList.size()>0){
            sharingRuleRecords=sharingRuleRecordsList.get(0);
        }
        for(FreezeAuditRecords freezeAuditRecords:freezeAuditRecordsList){
            Date freezeTime= DateUtil.parse(DateUtil.format(freezeAuditRecords.getFreezeTime()),DateUtil.YYYY_MM_DD);
            Date date= DateUtil.parse(DateUtil.format(new Date()),DateUtil.YYYY_MM_DD);
            if(sharingRuleRecords!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(freezeTime);
                Calendar calendarDate = Calendar.getInstance();
                calendar.setTime(date);
                int dd= calendar.get(Calendar.DATE)+Integer.valueOf(sharingRuleRecords.getFrozenLeadTime());
                if(dd==calendarDate.get(Calendar.DATE)){
                    //执行解除冻结
                    logger.info("============执行解除冻结");
                    UserParkingSpace userParkingSpace=userParkingSpaceBiz.selectById(freezeAuditRecords.getUserSpaceId());
                    userParkingSpace.setEnabledFlag(BaseConstants.enabledFlag.y);
                    userParkingSpaceBiz.updateById(userParkingSpace);
                    freezeAuditRecords.setEnabledFlag(BaseConstants.enabledFlag.n);
                    this.updateById(freezeAuditRecords);
                }
            }
        }
    }

}
