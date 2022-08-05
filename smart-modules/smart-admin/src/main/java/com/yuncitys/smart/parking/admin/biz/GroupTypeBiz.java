package com.yuncitys.smart.parking.admin.biz;

import com.yuncitys.smart.parking.admin.entity.GroupType;
import com.yuncitys.smart.parking.admin.mapper.GroupTypeMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-12 8:48
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BusinessBiz<GroupTypeMapper,GroupType> {
}
