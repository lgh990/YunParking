package com.yuncitys.smart.parking.common.exception.auth;


import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.ag.core.exception.BaseException;

/**
 * Created by smart on 2017/9/10.
 */
public class ClientInvalidException extends BaseException {
    public ClientInvalidException(String message) {
        super(message, RestCodeConstants.EX_CLIENT_INVALID_CODE);
    }
}
