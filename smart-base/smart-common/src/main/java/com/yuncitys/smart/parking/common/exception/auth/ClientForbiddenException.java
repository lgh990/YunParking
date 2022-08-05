package com.yuncitys.smart.parking.common.exception.auth;


import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.ag.core.exception.BaseException;

/**
 * Created by smart on 2017/9/12.
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, RestCodeConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
