package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.PrivateLotAuditRecord;
import com.smart.iot.parking.mapper.PrivateLotAuditRecordMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import org.springframework.stereotype.Service;

/**
 * 业主审核记录表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-08 16:38:39
 */
@Service
public class PrivateLotAuditRecordBiz extends BusinessBiz<PrivateLotAuditRecordMapper,PrivateLotAuditRecord> {
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<PrivateLotAuditRecord> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

}
