package com.yuncitys.smart.parking.common.exception.auth;


import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.ag.core.exception.BaseException;

/**
 *
 * @author smart
 * @version 2022/9/12
 */
public class UserForbiddenException extends BaseException {
    public UserForbiddenException(String message) {
        super(message, RestCodeConstants.EX_USER_FORBIDDEN_CODE);
    }

}
