package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.SpaceExceptionProceRecordBiz;
import com.smart.iot.parking.entity.ParkingArea;
import com.smart.iot.parking.entity.SpaceExceptionProceRecord;
import com.smart.iot.parking.vo.ParkingAreaVo;
import com.smart.iot.parking.vo.SpaceExceptionProceRecordVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("spaceExceptionProceRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "车位异常处理记录")
public class SpaceExceptionProceRecordController extends BaseController<SpaceExceptionProceRecordBiz,SpaceExceptionProceRecord,String> {

    @Override
    public TableResultResponse list(@RequestParam Map<String, Object> params){
        if(params.containsKey("errorCause"))
        {
            if(StringUtil.isBlank(params.get("errorCause").toString()))
            {
                params.remove("errorCause");
            }
        }
        if(params.containsKey("errorType"))
        {
            if(StringUtil.isBlank(params.get("errorType").toString()))
            {
                params.remove("errorType");
            }
        }
        //查询列表数据
        Query query = new Query(params);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        Example example = new Example(SpaceExceptionProceRecord.class);
        example.createCriteria().andEqualTo("errorType",params.get("errorType"));
        example.createCriteria().andEqualTo("errorCause",params.get("errorCause"));
        List list = baseBiz.selectByExample(example);
        List<SpaceExceptionProceRecordVo> spaceExceptionProceRecordVoList=baseBiz.abnormalFeedbackToAbnormalFeedbackVo(list);
        return new TableResultResponse(result.getTotal(), spaceExceptionProceRecordVoList);

    }
}
