package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.UserFeedback;
import com.smart.iot.parking.mapper.UserFeedbackMapper;
import com.smart.iot.parking.vo.UserFeedbackVo;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户反馈表
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-28 14:54:11
 */
@Service
public class UserFeedbackBiz extends BusinessBiz<UserFeedbackMapper,UserFeedback> {
    @Autowired
    public AppUserBiz appUserBiz;
    public List<UserFeedbackVo> userFeedbackToMap(List<UserFeedback> userFeedbackList){
        List<String> userIdList=new ArrayList<>();
        for(UserFeedback userFeedback:userFeedbackList){
            userIdList.add(userFeedback.getUserId());
        }
        List<AppUser> appUserList=appUserBiz.userIdInList(userIdList);
        List<UserFeedbackVo> list=new ArrayList();
        for(UserFeedback userFeedback:userFeedbackList){
            UserFeedbackVo userFeedbackVo=new UserFeedbackVo(userFeedback);
            for(AppUser appUser:appUserList){
                if(appUser.getId().equals(userFeedback.getUserId())){
                    userFeedbackVo.setAppUser(appUser);
                }
            }
            list.add(userFeedbackVo);
        }
        return list;
    }
}
