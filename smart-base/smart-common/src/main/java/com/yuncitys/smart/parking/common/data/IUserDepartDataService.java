package com.yuncitys.smart.parking.common.data;

import java.util.List;

/**
 * @author smart
 * @create 2018/2/11.
 */
public interface IUserDepartDataService {
    /**
     * 根据用户获取用户可访问的数据部门Id
     * @param userId
     * @return
     */
    public List<String> getUserDataDepartIds(String userId);
}
