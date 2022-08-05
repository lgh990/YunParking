package com.yuncitys.smart.parking.admin.rpc;

import com.alibaba.fastjson.JSONObject;
import com.yuncitys.smart.parking.admin.biz.GateLogBiz;
import com.yuncitys.smart.parking.admin.entity.GateLog;
import com.yuncitys.smart.parking.api.vo.log.LogInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-07-01 14:39
 */
@RequestMapping("api")
@RestController
public class LogRest {
    @Autowired
    private GateLogBiz gateLogBiz;
    @RequestMapping(value="/log/save",method = RequestMethod.POST)
    public @ResponseBody void saveLog(@RequestBody LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        gateLogBiz.insertSelective(log);
    }
}
