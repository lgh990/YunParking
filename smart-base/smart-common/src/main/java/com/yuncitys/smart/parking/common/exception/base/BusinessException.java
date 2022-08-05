package com.yuncitys.smart.parking.common.exception.base;

import com.yuncitys.smart.parking.common.constant.RestCodeConstants;
import com.yuncitys.ag.core.exception.BaseException;

/**
 * 业务异常基础类
 * @author smart
 *@version 2022/1/13.
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, RestCodeConstants.EX_BUSINESS_BASE_CODE);
    }
}
