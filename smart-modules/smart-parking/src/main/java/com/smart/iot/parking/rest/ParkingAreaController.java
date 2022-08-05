package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.ParkingAreaBiz;
import com.smart.iot.parking.entity.ParkingArea;
import com.smart.iot.parking.vo.ParkingAreaVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("parkingArea")
@CheckUserToken
@CheckClientToken
@Api(tags = "区层管理")
public class ParkingAreaController extends BaseController<ParkingAreaBiz,ParkingArea,String> {
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public ParkingAreaBiz parkingAreaBiz;

    @Override
    public ObjectRestResponse<ParkingArea> add(@RequestBody ParkingArea entity){
        entity.setAreaId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<ParkingArea>().data(entity);
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
        Query query = new Query(params);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        Example example = new Example(ParkingArea.class);
        example.createCriteria().andEqualTo("parkingId",params.get("parkingId"));
        if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)) {
            example.createCriteria().andBetween("crtTime", beginTime, endTime);
        }
        List list = parkingAreaBiz.selectByExample(example);
        List<ParkingAreaVo> parkingAreaVoList=parkingAreaBiz.parkingAreaToParkingAreaVo(list);
        return new TableResultResponse(result.getTotal(), parkingAreaVoList);
    }

    @ApiOperation("区层管理-分页获取数据")
    @RequestMapping(value = "/queryParkingArea",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "areaName", value = "区层名称", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "areaType", value = "区层类型", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "beginTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "limit", value = "分页条数", dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "page", value = "分页页数", dataType = "String"),
    })
    public TableResultPageResponse<Object> queryParkingArea(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        return parkingAreaBiz.queryParkingArea(params);
    }

    /*public TableResultResponse<ParkingArea> list(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        Query query = new Query(params);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        Example example = new Example(ParkingArea.class);
        example.createCriteria().andEqualTo("parkingId",params.get("parkingId"));
        List list = parkingAreaBiz.selectByExample(example);
        return new TableResultResponse<ParkingArea>(result.getTotal(), list);
    }*/

}
