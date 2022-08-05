package com.smart.iot.parking.biz;

import org.springframework.stereotype.Service;

import com.smart.iot.parking.entity.CancellationAuditRecords;
import com.smart.iot.parking.mapper.CancellationAuditRecordsMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

/**
 * 注销审核记录表
 *
 * @author YUNCITYS
 * @email YUNCITYS@smart.iot.com
 * @version 2022-04-23 14:36:43
 */
@Service
public class CancellationAuditRecordsBiz extends BusinessBiz<CancellationAuditRecordsMapper,CancellationAuditRecords> {
}
