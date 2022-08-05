package com.smart.iot.dev.rest;

import com.smart.iot.dev.biz.DevEarlyWarningBiz;
import com.smart.iot.dev.constant.BaseConstants;
import com.smart.iot.dev.entity.DevEarlyWarning;
import com.smart.iot.dev.entity.DevGateway;
import com.smart.iot.dev.mapper.DevGatewayMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.DeclareWarning;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("devEarlyWarning")
@CheckClientToken
@CheckUserToken
@Api(tags = "")
public class DevEarlyWarningController extends BaseController<DevEarlyWarningBiz, DevEarlyWarning,String> {

    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<DevEarlyWarning> list(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        Query query = new Query(params);
        query.remove("crtUserId");
        query.remove("isDeleted");
        Example example = new Example(DevEarlyWarning.class);
        example.and().andIsNull("gwSn");
        example.orderBy("crtTime").desc();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<DevEarlyWarning> list = baseBiz.selectByExample(example);
        return new TableResultResponse<DevEarlyWarning>(result.getTotal(), list);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<DevEarlyWarning> add(@RequestBody List<DevEarlyWarning> list){
        for(DevEarlyWarning entity:list) {
            DevEarlyWarning devGatewayMapperParan = new DevEarlyWarning();
            devGatewayMapperParan.setKey(entity.getKey());
            if (!StringUtil.isEmpty(entity.getGwSn())) {
                devGatewayMapperParan.setGwSn(entity.getGwSn());
            }
            DevEarlyWarning devEarlyWarning = baseBiz.selectOne(devGatewayMapperParan);
            if (devEarlyWarning == null) {
                devEarlyWarning=new DevEarlyWarning();
                devEarlyWarning.setId(StringUtil.uuid());

            }
            devEarlyWarning.setGwSn(entity.getGwSn());
            devEarlyWarning.setKey(entity.getKey());
            devEarlyWarning.setKeyname(entity.getKeyname());
            devEarlyWarning.setMax(entity.getMax());
            devEarlyWarning.setMin(entity.getMin());
            baseBiz.saveOrUpdate(devEarlyWarning);
        }
        return new ObjectRestResponse<DevEarlyWarning>().data(new DevEarlyWarning());
    }

    @RequestMapping(value = "queryDevEarlyWarningList",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<List<DevEarlyWarning>> queryDevEarlyWarningList(@RequestParam Map<String, Object> params) throws InvocationTargetException, ExecutionException, IllegalAccessException, NoSuchMethodException {
        String gwSn=String.valueOf(params.get("gwSn"));
        DevEarlyWarning devEarlyWarning=new DevEarlyWarning();
        devEarlyWarning.setGwSn(gwSn);
        List<DevEarlyWarning>list=baseBiz.selectList(devEarlyWarning);
        if(list==null || list.size()<=0){
           tk.mybatis.mapper.entity.Example example = new Example(DevEarlyWarning.class);
            example.and().andIsNull("gwSn");
            list=baseBiz.selectByExample(example);
            for(DevEarlyWarning devEarlyWarning1:list){
                devEarlyWarning1.setMax(null);
                devEarlyWarning1.setMin(null);
            }
        }
        return new ObjectRestResponse<List<DevEarlyWarning>>().data(list);
    }
}
