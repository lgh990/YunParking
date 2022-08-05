package com.smart.iot.dev.biz;

import com.smart.iot.dev.utils.PageUtil;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import org.springframework.stereotype.Service;

import com.smart.iot.dev.entity.MqttMsg;
import com.smart.iot.dev.mapper.MqttMsgMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

import java.util.List;
import java.util.Map;

/**
 * MQTT日志表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@Service
public class MqttMsgBiz extends BusinessBiz<MqttMsgMapper,MqttMsg> {

    public TableResultPageResponse<Object> mqttMsgQuery(Map params){
        PageUtil.makeStartPoint(params);
        if(params.get("devType")!=null&&!"".equals(params.get("devType"))){
            String oldDevType = params.get("devType").toString();
            params.put("devType","\"devTypeCode\":"+"\""+oldDevType+"\"");
        }
        if(params.get("devSn")!=null&&!"".equals(params.get("devSn"))){
            String oldDevSn = params.get("devSn").toString();
            params.put("devSn","\"deviceId\":"+"\""+oldDevSn+"\"");
        }
        List<Object> mqttMsg = mapper.mqttMsgQuery(params);
        return new TableResultPageResponse(mapper.mqttMsgCount(params),
                mqttMsg,Long.parseLong(params.get("startPoint").toString()),Long.parseLong(params.get("limit").toString()) );
    }

}
