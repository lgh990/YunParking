package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.ParkingBusinessType;
import com.smart.iot.parking.mapper.ParkingBusinessTypeMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 停车场业务模式表
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-31 10:54:15
 */
@Service
public class ParkingBusinessTypeBiz extends BusinessBiz<ParkingBusinessTypeMapper,ParkingBusinessType> {

    public List<ParkingBusinessType> businessTypeInList(List<String> businessTypeIdList){
        if(businessTypeIdList!=null && businessTypeIdList.size()>0) {
            Example example = new Example(ParkingBusinessType.class);
            example.createCriteria().andIn("id", businessTypeIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }
}
