package com.yuncitys.smart.parking.common.constant.iotexper;

import java.text.MessageFormat;

public class SysConstant {
    /**
     * redis key
     */
    //PAYLOAY数据
    public static final String PAYLOAD_KEY = "IOT:EXPER:MQTT:PAYLOAD";
    //MQTT失败队列
    public static final String PAYLOAD_FAIL_KEY = "IOT:EXPER:MQTT:PAYLOAD:FAIL";
    //设备列表
    public static final String DEV_ONERANKDEV_KEY = "IOT:EXPER:DEV_ONERANKDEV";

    /**
     * 推送消息内容模板
     */
    public static interface PUSH_MSG_CONTENT{
        public static final Integer LOGIN_MSG_TYPE = 0;
        public static final String LOGIN_TITLE_MSG = "【登录提醒】";
        public static final String LOGIN_MSG = "欢迎:{0},登录！";

        public static final Integer UPDATE_PWD_MSG_TYPE = 1;
        public static final String UPDATE_PWD_TITLE_MSG = "【密码设置成功提醒】";
        public static final String UPDATE_PWD_MSG = "账号:{0}，密码设置成功！";

        public static final Integer REGISTER_MSG_TYPE = 2;
        public static final String REGISTER_TITLE_MSG = "【注册成功提醒】";
        public static final String REGISTER_MSG = "账号:{0}，注册成功！";
    }
    /**
     * 登陆类型(pwd:密码登陆，sms：短信登陆)
     */
    public static interface AUTH_TYPE{
        public final String SMS_AUTH_TYPE="sms";
        public final String PWD_AUTH_TYPE = "pwd";
    }
    /**
     * 有效标志(Y:有效；N：无效)
     */
    public static interface ENABLED_FLAG{
        final String Y="Y";
        final String N="N";
    }

    /**
     * 成功返回信息
     */
    public static interface MESSAGE{
        final int STATE=200;
        final String MSG="成功";
    }

    /**
     * 是否开放（1=开放，2=不开放）
     */
    public static interface SCENE_STATUE{
        final int YES=1;
        final int NO=2;
    }


    /**
     * 设定规则失败
     */
    public static interface RULE_MESSAGE{
        final int STATE=201;
        final String MSG="该规则已存在，无法重复设定！！";
    }

    /**
     * 异常
     */
    public static interface EXCEPTION_MESSAGE{
        final int STATE=401;
        final String MSG="参数异常，参数不能为空！！";
    }

    /**
     * 是否控制类
     */
    public static interface CONTROL_CLASS{
        final int YES=0;
        final int NO=1;
    }
    /**
     *APP用户权限类型  0：普通用户 1：管理员用户
     */
    public static interface APP_USER_TYPE{
        final int COMMON_USER=0;
        final int ADMIN_USER=1;
    }

    public interface user_type {
        public static final String common = "common"; //普通用户
        public static final String vip = "vip";  //vip用户

    }

    /**
     *用户是否设置密码（0：未设置，1：已设置）
     */
    public static interface IS_SETTING_PWD{
        final int NO=0;
        final int YES=1;
    }
    /**
     * 设备类型异常提示
     */
    public static interface DEV_TYPE_EXCEPTION{
        final int STATE=202;
        final String MSG="设备类型存在，无法添加活或修改重复设备类型！！！";
    }

    /**
     * 成功返回信息
     */
    public static interface DEV_TYPE_PARENT_EXCEPTION{
        final int STATE=203;
        final String MSG="父级已存在子集无法删除！";
    }


    /**
     * 0=已读，1=未读
     */
    public static interface FICKER {
        final int YES=0;
        final int NO=1;
    }




    /**
     * 地图颜色   白色、蓝色
     */
    public static interface PATH_IMAGE {
        public final static String SIGN_IMAGE = "sign";
        public final static String SCENE_IMAGE = "scene";
        public final static String WHITE_IMAGE = "white";
        public final static String BLUE_IMAGE = "blue";
        public final static String MAP_IMAGE = "map";
    }
}
