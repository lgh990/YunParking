package com.smart.iot.dev.constant;

/**
 * Created by Administrator on 2018/8/16 0016.
 */
public interface DevConstants {
    public interface toolSrceen {
        /******************出入属性（进场）：00 临时用户； 02 月卡用户；03请入场停车  05此车已经入场  08车位已经满了  其它 请入场停车****************/
        public static final int CASUAL_USER =0;             //临时用户
        public static final int MONCARDS_USER =2;             //月卡用户
        public static final int GOIN_SPACE =3;             //请入场停车
        public static final int CAR_INED =5;             //此车已经入场
        public static final int SPACE_FULL =8;             //车位已经满了
    }
    /***************** msgType |string   |mqtt消息类型：1：设备遥测数据，  2：设备遥测属性|） *************************/
    public static interface mqMsgType {
        public static final int DEV_ATTR = 2;
        public static final int DEV_DATA = 1;
    }
    public static interface packageType {
        /*********************设备类型***************************/
        public static final String U_PACKAGE = "U";
        public static final String M_PACKAGE = "M";
        public static final String P_PACKAGE = "P";
        public static final String STATE1_PACKAGE = "state1";
        public static final String STATE2_PACKAGE = "state2";
    }
    /***************** 1(流水号异常2通讯异常3地磁上报异常4手持机上报异常5时间异常6视频桩数据错误) *************************/
    public static interface devErrType {
        public static final String COMMON = "COMMON";
        public static final String FLOWNUM_ERR = "FLOWNUM_ERR";
        public static final String CONMU_ERR = "CONMU_ERR";
        public static final String GEOMAG_ERR = "GEOMAG_ERR";
        public static final String HANDDMAC_ERR = "HANDDMAC_ERR";
        public static final String TIME_ERR ="TIME_ERR";

        /*******************************************************************/
    }

}
