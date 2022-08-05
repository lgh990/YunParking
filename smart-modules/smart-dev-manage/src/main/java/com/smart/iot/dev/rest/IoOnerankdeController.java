package com.smart.iot.dev.rest;

import com.smart.iot.dev.biz.IoOnerankdeBiz;
import com.smart.iot.dev.constant.BaseConstants;
import com.smart.iot.dev.entity.IoOnerankde;
import com.smart.iot.dev.mapper.IoOnerankdeMapper;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ioOnerankde")
@CheckClientToken
@CheckUserToken
@Api(tags = "设备-出入口绑定")
public class IoOnerankdeController extends BaseController<IoOnerankdeBiz, IoOnerankde,String> {
    @Autowired
    public IoOnerankdeMapper ioOnerankdeMapper;
    @RequestMapping(value = "/deleteIoOnerankde",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("解绑")
    public ObjectRestResponse<IoOnerankde> deleteIoOnerankde(@RequestBody IoOnerankde ioOnerankde){
        IoOnerankde ioOnerankde1 = baseBiz.selectOne(ioOnerankde);
        baseBiz.delete(ioOnerankde1);
        return ObjectRestResponse.ok();
    }

    @Override
    public ObjectRestResponse<IoOnerankde> add(@RequestBody IoOnerankde ioOnerankde){
        IoOnerankde ioOnerankde1 = ioOnerankdeMapper.queryDevByIoIdAndDevSn(ioOnerankde.getParkingioId(),ioOnerankde.getOnerankdevSn());
        if(ioOnerankde1 != null)
        {
            return new ObjectRestResponse<IoOnerankde>().BaseResponse(BaseConstants.StateConstates.BIND_FAIL_CODE,BaseConstants.StateConstates.BIND_FAIL_MSG);
        }
        ioOnerankde.setIoOkId(StringUtil.uuid());
        baseBiz.insertSelective(ioOnerankde);
        return new ObjectRestResponse<IoOnerankde>().data(ioOnerankde);
    }


}
