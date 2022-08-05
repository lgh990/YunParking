package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.AbnormalFeedback;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.mapper.SpaceExceptionProceRecordMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.vo.SpaceExceptionProceRecordVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 车位异常处理记录
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 18:59:22
 */
@Service
public class SpaceExceptionProceRecordBiz extends BusinessBiz<SpaceExceptionProceRecordMapper,SpaceExceptionProceRecord> {
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;

    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse<SpaceExceptionProceRecord> selectByPageQuery(Query query, String beginDate, String endDate)
    {
        return super.selectByPageQuery(query,  beginDate, endDate);
    }

    public List<SpaceExceptionProceRecordVo> abnormalFeedbackToAbnormalFeedbackVo(List<SpaceExceptionProceRecord> spaceExceptionProceRecordList){
        List<String> spaceIdList=new ArrayList<>();
        for(SpaceExceptionProceRecord spaceExceptionProceRecord:spaceExceptionProceRecordList){
            spaceIdList.add(spaceExceptionProceRecord.getSpaceId());
        }
        List<SpaceExceptionProceRecordVo> list=new ArrayList();
        List<ParkingSpace> spaceList=parkingSpaceBiz.spaceIdInList(spaceIdList);
        for(SpaceExceptionProceRecord spaceExceptionProceRecord:spaceExceptionProceRecordList){
            SpaceExceptionProceRecordVo spaceExceptionProceRecordVo=new SpaceExceptionProceRecordVo(spaceExceptionProceRecord);
            for(ParkingSpace space:spaceList){
                if(spaceExceptionProceRecord.getSpaceId().equals(space.getSpaceId())){
                    spaceExceptionProceRecordVo.setParkingSpace(space);
                }
            }
            list.add(spaceExceptionProceRecordVo);
        }
        return list;
    }

}
