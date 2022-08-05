package com.yuncitys.smart.parking.admin.biz;

import com.yuncitys.smart.parking.admin.entity.Tenant;
import com.yuncitys.smart.parking.admin.entity.User;
import com.yuncitys.smart.parking.admin.mapper.TenantMapper;
import com.yuncitys.smart.parking.admin.mapper.UserMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户表
 *
 * @author Mr.AG
 * @email
 *@version 2022-02-08 21:42:09
 */
@Service
public class TenantBiz extends BusinessBiz<TenantMapper,Tenant> {
    @Autowired
    private UserMapper userMapper;

    public void updateUser(String id, String userId) {
        Tenant tenant = this.mapper.selectByPrimaryKey(id);
        tenant.setOwner(userId);
        updateSelectiveById(tenant);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setTenantId(id);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
