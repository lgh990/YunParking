package com.yuncitys.smart.parking.auth.common.util.jwt;

import java.util.Date;
import java.util.Map;

/**
 * Created by ace on 2017/9/10.
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    Date getExpireTime();

    Map<String, String> getOtherInfo();
}
