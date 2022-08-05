package com.smart.iot.parking.biz;

import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.SharingRuleRecords;
import com.smart.iot.parking.mapper.SharingRuleRecordsMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

/**
 * 共享规则记录表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-04-23 14:36:43
 */
@Service
public class SharingRuleRecordsBiz extends BusinessBiz<SharingRuleRecordsMapper,SharingRuleRecords> {
}
