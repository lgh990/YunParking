package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.Instructions;
import com.smart.iot.parking.mapper.InstructionsMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 使用表
 *
 * @author Mr.AG
 * @email
 * @date 2018-09-19 14:12:25
 */
@Service
public class InstructionsBiz extends BusinessBiz<InstructionsMapper,Instructions> {
}
