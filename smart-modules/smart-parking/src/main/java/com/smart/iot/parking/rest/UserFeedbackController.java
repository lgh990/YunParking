package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.AppUserBiz;
import com.smart.iot.parking.biz.UserFeedbackBiz;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.ParkingAdvisory;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.entity.UserFeedback;
import com.smart.iot.parking.vo.ParkingAreaVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.biz.BaseBiz;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("userFeedback")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户反馈信息")
public class UserFeedbackController extends BaseController<UserFeedbackBiz,UserFeedback,String> {


    @Override
    public ObjectRestResponse<UserFeedback> add(@RequestBody UserFeedback entity){
        entity.setFbId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<UserFeedback>().data(entity);
    }

    @Override
    public TableResultResponse list(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {

        String beginTime="";
        if(params.get("beginTime")!=null) {
            beginTime = String.valueOf(params.get("beginTime"));
        }
        String endTime="";
        if(params.get("beginTime")!=null) {
            endTime=String.valueOf(params.get("endTime"));
        }
        params.remove("beginTime");
        params.remove("endTime");
        Query query = new Query(params);
        //查询列表数据

        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        Example example = new Example(UserFeedback.class);
        Example.Criteria criteria=baseBiz.query2criteria1(query, example);
        if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            criteria.andBetween("crtTime", beginTime, endTime);
        }
        List list = baseBiz.selectByExample(example);
        List<ParkingAreaVo> parkingAreaVoList=baseBiz.userFeedbackToMap(list);
        return new TableResultResponse(result.getTotal(), parkingAreaVoList);
    }


}
