package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.Citys;
import com.smart.iot.parking.mapper.CitysMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;

/**
 * 区县行政编码字典表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-24 13:49:19
 */
@Service
public class CitysBiz extends BusinessBiz<CitysMapper,Citys> {
}
