package com.yuncitys.smart.parking.common.exception.auth;


import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.ag.core.exception.BaseException;

/**
 * Created by smart on 2017/9/8.
 */
public class NonLoginException extends BaseException {
    public NonLoginException(String message) {
        super(message, RestCodeConstants.EX_USER_INVALID_CODE);
    }
}
