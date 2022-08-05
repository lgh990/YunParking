package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.AppUserBiz;
import com.smart.iot.parking.biz.PrivateLotAuditRecordBiz;
import com.smart.iot.parking.entity.AppUser;
import com.smart.iot.parking.entity.ParkingArea;
import com.smart.iot.parking.entity.PrivateLotAuditRecord;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("privateLotAuditRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "私人车位申请记录管理")
public class PrivateLotAuditRecordController extends BaseController<PrivateLotAuditRecordBiz,PrivateLotAuditRecord,String> {
    @Autowired
    public AppUserBiz appUserBiz;
    @Override
    @ApiOperation("分页获取数据")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<PrivateLotAuditRecord> list(@RequestParam Map<String, Object> params) throws ExecutionException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //查询列表数据
       /* String telephone = String.valueOf(params.get("telephone"));
        if(StringUtil.isEmpty(telephone)){
            params.remove("telephone");
        }*/
        return super.list(params);
    }

    @Override
    public ObjectRestResponse<PrivateLotAuditRecord> add(@RequestBody PrivateLotAuditRecord entity){
        AppUser appUser=appUserBiz.selectById(entity.getUserId());
        entity.setTelephone(appUser.getMobile());
        entity.setPvRecordId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<PrivateLotAuditRecord>().data(entity);
    }
}
