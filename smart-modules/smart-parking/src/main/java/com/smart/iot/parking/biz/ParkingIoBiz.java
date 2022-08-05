package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.ParkingIo;
import com.smart.iot.parking.mapper.ParkingIoMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 出入口
 *
 * @author Mr.AG
 * @email
 *@version 2022-08-16 17:19:40
 */
@Service
public class ParkingIoBiz extends BusinessBiz<ParkingIoMapper,ParkingIo> {
    public List<ParkingIo> ioIdInList(List<String> ioIdList){
        if(ioIdList!=null && ioIdList.size()>0) {
            Example example = new Example(ParkingIo.class);
            example.createCriteria().andIn("parkingIoId", ioIdList);
            return this.selectByExample(example);
        }
        return new ArrayList<>();
    }
}
