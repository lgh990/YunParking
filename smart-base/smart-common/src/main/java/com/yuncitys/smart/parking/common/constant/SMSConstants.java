package com.yuncitys.smart.parking.common.constant;

import com.yuncitys.smart.parking.common.exception.base.BusinessException;

/**
 * @author cyd
 * @create 2018/8/16.
 */
public class SMSConstants {
    public final static String SMS_SIGNNAME = "云创智城停车";
    /**
     * 短信模版类型(快捷登录，重置密码,更换手机号码)
     */
    public static interface TEMPLATECODE{
        //快捷登录
        public final static String LOGIN = "SMS_78705054";
        //重置密码
        public final static String RESETTING_PWD = "SMS_78705051";
        //更换手机号码
        public final static String UPDATE_PHONE = "SMS_78705050";
    }

    public static String getTempCode(String type){
        String tempCode = null;
        switch (type){
            case "1":
                tempCode = TEMPLATECODE.LOGIN;
                break;
            case "2":
                tempCode = TEMPLATECODE.RESETTING_PWD;
                break;
            case "3":
                tempCode = TEMPLATECODE.UPDATE_PHONE;
                break;
            default:
                break;
        }
        return tempCode;
    }
}
