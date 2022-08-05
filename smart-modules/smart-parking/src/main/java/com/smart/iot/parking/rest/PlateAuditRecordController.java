package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.AppUserBiz;
import com.smart.iot.parking.biz.PlateAuditRecordBiz;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.PlateAuditRecord;
import com.smart.iot.parking.vo.PlateAuditRecordVo;
import com.yuncitys.smart.merge.core.MergeCore;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.BaseResponse;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.Query;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("plateAuditRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "车牌审核记录")
public class PlateAuditRecordController extends BaseController<PlateAuditRecordBiz,PlateAuditRecord,String> {
    @Autowired
    public PlateAuditRecordBiz plateAuditRecordBiz;
    @Autowired
    public MergeCore mergeCore;
    @Autowired
    public AppUserBiz appUserBiz;
    @Override
    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse list(@RequestParam Map<String, Object> params)  {
        String beginTime=String.valueOf(params.get("beginTime"));
        String endTime=String.valueOf(params.get("endTime"));
        params.remove("beginTime");
        params.remove("endTime");
        //查询列表数据
        Query query = new Query(params);
        TableResultResponse tableResultPageResponse=baseBiz.selectByQuery(query,beginTime,endTime);
        List list = tableResultPageResponse.getData().getRows();
        List<PlateAuditRecordVo> plateAuditRecordVoList=baseBiz.plateAuditRecordToPlateAuditRecordVo(list);
        tableResultPageResponse.getData().setRows(plateAuditRecordVoList);
        return tableResultPageResponse;
    }


    @Override
    public ObjectRestResponse<PlateAuditRecord> add(@RequestBody PlateAuditRecord plateAuditRecord){
        plateAuditRecord.setRecordId(StringUtil.uuid());
        baseBiz.insertSelective(plateAuditRecord);
        return new ObjectRestResponse<PlateAuditRecord>().data(plateAuditRecord);
    }

    @RequestMapping(value = "/plateAudit",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("审核车牌接口")
    public BaseResponse plateAudit(@RequestBody  PlateAuditRecord plateAuditRecord) throws Exception {
        return plateAuditRecordBiz.auditDrivLicense(plateAuditRecord);
    }

}
