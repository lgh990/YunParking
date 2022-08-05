package com.smart.iot.parking.biz;

import com.smart.iot.parking.entity.ParkingComponentData;
import com.smart.iot.parking.entity.ParkingIo;
import com.smart.iot.parking.entity.ParkingSpace;
import com.smart.iot.parking.mapper.ParkingComponentDataMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 自定义部件属性
 *
 * @author Mr.AG
 * @email
 *@version 2022-07-30 16:13:25
 */
@Service
public class ParkingComponentDataBiz extends BusinessBiz<ParkingComponentDataMapper,ParkingComponentData> {
    @Autowired
    public ParkingComponentDataBiz parkingComponentDataBiz;
    @Autowired
    public ParkingIoBiz parkingIoBiz;
    @Autowired
    public ParkingSpaceBiz parkingSpaceBiz;

    /**
     * 判断该x与y轴是否有部件
     *
     * @param x
     * @param y
     * @param areaId
     * @param parkingId
     * @return
     */
    public boolean to_change_component_to_map(int x, int y, String areaId, String parkingId)
    {
        Example example = new Example(ParkingComponentData.class);
        example.createCriteria().andEqualTo("parkingAreaId", areaId);
        List<ParkingComponentData> parkingComponentDataList = parkingComponentDataBiz.selectByExample(example);
        Example example1 = new Example(ParkingIo.class);
        example.createCriteria().andEqualTo("parkingAreaId", areaId);
        List<ParkingIo> parkingIoList = parkingIoBiz.selectByExample(example1);

        ParkingSpace spaceParams=new ParkingSpace();
        spaceParams.setAreaId(areaId);
        List<ParkingSpace> parkingSpaceList = parkingSpaceBiz.selectList(spaceParams);

        for (ParkingComponentData component_data : parkingComponentDataList)
        {
            if (component_data.getX() == x && component_data.getY() == y)
            {
                return true;
            }
        }
        for (ParkingIo io : parkingIoList)
        {
            if (io.getX() == x && io.getY() == y)
            {
                return true;
            }
        }
        for (ParkingSpace spacea : parkingSpaceList)
        {
            if (Integer.valueOf(spacea.getAbscissa()) == x && Integer.valueOf(spacea.getOrdinate()) == y)
            {
                return true;
            }
        }
        return false;
    }

}
