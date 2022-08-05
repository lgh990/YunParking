package com.yuncitys.smart.parking.admin.service;

import com.yuncitys.smart.parking.admin.biz.UserBiz;
import com.yuncitys.smart.parking.common.data.IUserDepartDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/11.
 */
@Component
public class UserDepartDataService implements IUserDepartDataService {
    @Autowired
    private UserBiz userFeign;
    @Override
    public List<String> getUserDataDepartIds(String userId) {
        // 获取用户授权的部门数据权限,此处模拟两个账户
        return userFeign.getUserDataDepartIds(userId);
    }
}
