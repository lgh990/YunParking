package com.smart.iot.parking.rest;

import com.smart.iot.parking.biz.InstructionsBiz;
import com.smart.iot.parking.entity.Instructions;
import com.yuncitys.smart.parking.auth.client.annotation.CheckClientToken;
import com.yuncitys.smart.parking.auth.client.annotation.CheckUserToken;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import com.yuncitys.smart.parking.common.rest.BaseController;
import com.yuncitys.smart.parking.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("instructions")
@CheckClientToken
@CheckUserToken
@Api(tags = "使用说明")
public class InstructionsController extends BaseController<InstructionsBiz,Instructions,String> {

    @RequestMapping(value = "/addInstructions",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("使用说明")
    public ObjectRestResponse<Instructions> addInstructions(@RequestBody Instructions entity){
        entity.setInstId(StringUtil.uuid());
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<Instructions>().data(entity);
    }

}
