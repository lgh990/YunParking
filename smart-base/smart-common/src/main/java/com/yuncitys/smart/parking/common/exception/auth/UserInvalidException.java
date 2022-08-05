package com.yuncitys.smart.parking.common.exception.auth;


import com.yuncitys.ag.core.exception.BaseException;
import com.yuncitys.smart.parking.common.constant.RestCodeConstants;

/**
 * Created by smart on 2017/9/8.
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, RestCodeConstants.EX_USER_PASS_INVALID_CODE);
    }
}
