package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.mapper.AppUserMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-24 18:05:24
 */
@Service
public class AppUserBiz extends BusinessBiz<AppUserMapper,AppUser> {
    public List<AppUser> userIdInList(List<String> userIdList){
        if(userIdList!=null && userIdList.size()>0) {
            Example example = new Example(AppUser.class);
            example.createCriteria().andIn("id", userIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }

    public Object queryUserCountByTime(Map params){
        String queryType = params.get("queryType").toString();
        String dateStr = params.get("queryDate").toString();
        if(queryType.equals("Year")){
            params.put("queryYear",dateStr);
            return mapper.queryEveryMonthCountByYear(params);
        }else{
            String[] dates = dateStr.split("-");
            params.put("queryYear",dates[0]);
            params.put("queryMonth",dates[1]);
            return mapper.queryEveryDayCountByMouth(params);
        }
    }

    public List queryUserByType(){
        List returnList = Lists.newArrayList();
        Map commonMap = Maps.newHashMap();
        Map vipMap = Maps.newHashMap();
        Map paramsMap = Maps.newHashMap();
        paramsMap.put("userType","common");
        Long commonCount = mapper.queryUserByType(paramsMap);
        paramsMap.put("userType","vip");
        Long vipCount = mapper.queryUserByType(paramsMap);

        commonMap.put("Name","普通用户");
        commonMap.put("Key","commonCount");
        commonMap.put("Value",commonCount);

        vipMap.put("Name","vip用户");
        vipMap.put("Key","vipCount");
        vipMap.put("Value",vipCount);

        returnList.add(commonMap);
        returnList.add(vipMap);
        return returnList;
    }
}
