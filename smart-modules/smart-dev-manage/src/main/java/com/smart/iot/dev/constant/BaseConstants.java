package com.smart.iot.dev.constant;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
public interface BaseConstants {

    public interface enabledFlag {
        public static final String y = "y";
        public static final String n = "n";
    }





    public interface StateConstates {
        /*********************错误码******************/
        final int RESERVER_FAIL_CODE = 87;
        final String RESERVER_FAIL_MSG = "该车位不能被预定";
        final int BIND_FAIL_CODE = 89;
        final String BIND_FAIL_MSG = "绑定失败";
    }

}
