package com.smart.iot.dev.rest;


import com.smart.iot.dev.biz.IoOnerankdeBiz;
import com.smart.iot.dev.biz.SpaceOnerankdeBiz;
import com.smart.iot.dev.constant.BaseConstants;
import com.smart.iot.dev.entity.IoOnerankde;
import com.smart.iot.dev.entity.SpaceOnerankde;
import com.smart.iot.dev.feign.ParkingFeign;
import com.smart.iot.dev.mapper.IoOnerankdeMapper;
import com.smart.iot.dev.mapper.OnerankdevMapper;
import com.smart.iot.dev.mapper.SpaceOnerankdeMapper;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.smart.iot.dev.biz.OnerankdevBiz;
import com.smart.iot.dev.entity.Onerankdev;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("onerankdev")
@CheckClientToken
@CheckUserToken
@Api(tags = "设备表")
public class OnerankdevController extends BaseController<OnerankdevBiz,Onerankdev,String> {
    @Autowired
    public OnerankdevBiz onerankdevBiz;
    @Autowired
    public ParkingFeign parkingFeign;
    @Autowired
    public SpaceOnerankdeMapper spaceOnerankdeMapper;
    @Autowired
    public SpaceOnerankdeBiz spaceOnerankdeBiz;
    @Autowired
    public IoOnerankdeMapper ioOnerankdeMapper;
    @Autowired
    public IoOnerankdeBiz ioOnerankdeBiz;
    @Autowired
    public OnerankdevMapper onerankdevMapper;

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<Onerankdev> add(@RequestBody Onerankdev entity){
        entity.setDevId(StringUtil.uuid());
        entity.setOnerankdevDevSn(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<Onerankdev>().data(entity);
    }

    @RequestMapping(value = "/queryOnerankDevList",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询设备列表")
    public TableResultPageResponse<Object> queryOnerankDevList(@RequestBody Map<String, String> params){
        return onerankdevBiz.queryOnerankDevList(params);
    }

    @RequestMapping(value = "/devBinding",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询设备列表")
    public ObjectRestResponse devBinding(@RequestBody HashMap<String,String> params){
        String type=String.valueOf(params.get("type"));
        String id=String.valueOf(params.get("id"));//type=io 表示出入口编号  type=space 车位编号
        String onerankdevSn=String.valueOf(params.get("onerankdevSn"));//设备sn
        if(type.equals("space")) {
            SpaceOnerankde spaceOnerankde1 = spaceOnerankdeMapper.queryDevBySpaceIdAndDevSn(id, onerankdevSn);
            if (spaceOnerankde1 != null) {
                return new ObjectRestResponse<SpaceOnerankde>().BaseResponse(BaseConstants.StateConstates.BIND_FAIL_CODE, BaseConstants.StateConstates.BIND_FAIL_MSG);
            }
            SpaceOnerankde spaceOnerankde=new SpaceOnerankde();
            spaceOnerankde.setOnerankdevSn(onerankdevSn);
            spaceOnerankde.setSpaceId(id);
            spaceOnerankde.setSoId(StringUtil.uuid());
            spaceOnerankdeBiz.insertSelective(spaceOnerankde);
        }else if(type.equals("io")){
            IoOnerankde ioOnerankde1 = ioOnerankdeMapper.queryDevByIoIdAndDevSn(id,onerankdevSn);
            if(ioOnerankde1 != null)
            {
                return new ObjectRestResponse<IoOnerankde>().BaseResponse(BaseConstants.StateConstates.BIND_FAIL_CODE,BaseConstants.StateConstates.BIND_FAIL_MSG);
            }
            IoOnerankde ioOnerankde=new IoOnerankde();
            ioOnerankde.setOnerankdevSn(onerankdevSn);
            ioOnerankde.setParkingioId(id);
            ioOnerankde.setIoOkId(StringUtil.uuid());
            ioOnerankdeBiz.insertSelective(ioOnerankde);
        }
        return new ObjectRestResponse<SpaceOnerankde>();
    }

    @RequestMapping(value = "/deleteBindDev",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("删除车位绑定对象")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="body", name = "devSn", value = "设备sn", required = true, dataType = "String")
    })
    public ObjectRestResponse deleteBindDev(@RequestBody HashMap<String,String> params){
        String type=String.valueOf(params.get("type"));
        String id=String.valueOf(params.get("id"));//type=io 表示出入口编号  type=space 车位编号
        String onerankdevSn=String.valueOf(params.get("onerankdevSn"));//设备sn
        if(type.equals("space")) {
            SpaceOnerankde spaceOnerankde=new SpaceOnerankde();
            spaceOnerankde.setSpaceId(id);
            spaceOnerankde.setOnerankdevSn(onerankdevSn);
            spaceOnerankdeBiz.delete(spaceOnerankde);
        }else if(type.equals("io")){
            IoOnerankde ioOnerankde=new IoOnerankde();
            ioOnerankde.setParkingioId(id);
            ioOnerankde.setOnerankdevSn(onerankdevSn);
            ioOnerankdeBiz.delete(ioOnerankde);
        }
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "/queryParkingList",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询停车场列表")
    public TableResultPageResponse queryParkingList(@RequestParam Map<String, Object> params){
        return parkingFeign.queryParkingList(params);
    }

    @RequestMapping(value = "/queryParkingAreaList",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询区层列表")
    public TableResultPageResponse queryParkingAreaList(@RequestParam Map<String, Object> params, HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers",
                "Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,token");
        return parkingFeign.queryParkingAreaList(params);
    }

    @RequestMapping(value = "/queryDevTypeList",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询设备列表")
    public ObjectRestResponse queryDevTypeList(@RequestParam Map<String, Object> params){
        List list=onerankdevMapper.queryDevTypeList();
        return ObjectRestResponse.ok(list);
    }



    @RequestMapping(value = "/infoIssue",method = RequestMethod.POST)
    @ApiOperation("指令下发")
    public Object infoIssue(@RequestParam Map<String, Object> params){
        String method = (String) params.get("method");
        String param = (String) params.get("param");
        String devSn = "";
        if(params.get("deviceId")!=null){
            devSn = (String) params.get("deviceId");
        }
        return baseBiz.infoIssue(method,param,devSn);
    }

    @RequestMapping(value = "/queryBeforeDevBindList",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询设备列表")
    public TableResultPageResponse<Object> queryBeforeDevBindList(@RequestParam Map<String, Object> params){
        return onerankdevBiz.queryBeforeDevBindList(params);
    }

    @RequestMapping(value = "/queryDevSnAndDeviceId",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询设备sn")
    public List<Object> queryDevSnAndDeviceId(@RequestParam Map<String, Object> params){
        return onerankdevBiz.queryDevSnAndDeviceId(params);
    }

}
