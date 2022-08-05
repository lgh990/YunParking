package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.AbnormalFeedback;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.mapper.AbnormalFeedbackMapper;
import com.smart.iot.parking.srever.TablePageResultParser;
import com.smart.iot.parking.vo.AbnormalFeedbackVo;
import com.yuncitys.smart.merge.annonation.MergeResult;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 车位故障上报表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 18:21:12
 */
@Service
public class AbnormalFeedbackBiz extends BusinessBiz<AbnormalFeedbackMapper,AbnormalFeedback> {
    @Autowired
    public UserMessageBiz userMessageBiz;
    @Autowired
    public AppUserBiz appUserBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;
    @MergeResult(resultParser = TablePageResultParser.class)
    @Override
    public TableResultPageResponse selectByPageQuery(Query query, String beginDate, String endDate)
    {
        TableResultPageResponse<AbnormalFeedback> tableResultPageResponse=super.selectByPageQuery(query,  beginDate, endDate);
        if(tableResultPageResponse!=null && tableResultPageResponse.getData()!=null){
            List<AbnormalFeedback> abnormalFeedbackList=tableResultPageResponse.getData().getRows();
            List<AbnormalFeedbackVo> abnormalFeedbackVoList=abnormalFeedbackToAbnormalFeedbackVo(abnormalFeedbackList);
            long total = tableResultPageResponse.getData().getTotal();
            long offset = tableResultPageResponse.getData().getOffset();
            long limit = tableResultPageResponse.getData().getLimit();
            TableResultPageResponse<AbnormalFeedbackVo> resultPageResponse=new TableResultPageResponse(total,abnormalFeedbackVoList,offset,limit);
            return resultPageResponse;
        }
        return tableResultPageResponse;
    }

    public List<AbnormalFeedbackVo> abnormalFeedbackToAbnormalFeedbackVo(List<AbnormalFeedback> abnormalFeedbackList){
        List<String> userIdList=new ArrayList<>();
        for(AbnormalFeedback abnormalFeedback:abnormalFeedbackList){
            userIdList.add(abnormalFeedback.getUserId());
        }
        List<AbnormalFeedbackVo> list=new ArrayList();
        List<AppUser> userList=appUserBiz.userIdInList(userIdList);
        for(AbnormalFeedback abnormalFeedback:abnormalFeedbackList){
            AbnormalFeedbackVo abnormalFeedbackVo=new AbnormalFeedbackVo(abnormalFeedback);
            for(AppUser appUser:userList){
                if(appUser.getId().equals(abnormalFeedbackVo.getUserId())){
                    abnormalFeedbackVo.setAppUser(appUser);
                }
            }
            list.add(abnormalFeedbackVo);
        }
        return list;
    }


}
