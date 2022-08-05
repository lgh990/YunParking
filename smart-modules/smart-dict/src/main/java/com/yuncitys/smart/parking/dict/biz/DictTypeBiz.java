package com.yuncitys.smart.parking.dict.biz;

import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.util.UUIDUtils;
import com.yuncitys.smart.parking.dict.entity.DictType;
import com.yuncitys.smart.parking.dict.mapper.DictTypeMapper;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author Mr.AG
 * @email
 *@version 2022-01-30 19:45:55
 */
@Service
public class DictTypeBiz extends BusinessBiz<DictTypeMapper,DictType> {
    @Override
    public void insertSelective(DictType entity) {
        entity.setId(UUIDUtils.generateUuid());
        super.insertSelective(entity);
    }
}
