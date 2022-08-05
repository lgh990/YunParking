package com.smart.iot.parking.constant;

/**
 * Created by Administrator on 2018/8/6 0006.
 */
public interface BaseConstants {


    public interface SpaceExpirate {
        /*********************预定车位过期时间******************/
        final long expTemporary = 3600*1000;
        final long expVip = 3600*1000;
        final long expCommon = 120*1000;
        final long expPrivate1 = 3600*1000;
        final long expVipCancel = 1800*1000;
        final long leave_timeout = 120;//离场超时时间

    }
    public interface BusinessModule {
        public static final String ace_onsite_server = "smart-onsite-server"; //场内
        public static final String ace_roadside_server = "smart-roadside-server";  //路测

    }
    public interface user_type {
        public static final String common = "common"; //普通用户
        public static final String vip = "vip";  //vip用户

    }

    public interface admin_status {
        /*********************设备收费屏方式***************************/
        public static final String NORMAL_ADMISSION = "normal_admission"; //正常入场
        public static final String UNNORMAL_ADMISSION = "unnormal_admission";  //入场异常
        public static final String NORMAL_APPEARANCE = "normal_appearance";    //正常出场
        public static final String UNNORMAL_APPEARANCE = "unnormal_appearance";   //出场异常
        public static final String OVER_TIME="over_time";                            //超时
        public static final String OVER_SPACE="over_space";                            //车位已满
    }
    public interface instructions {
        /*********************命令类型***************************/
        public static final int CONTROL_CMD= 48;
        public static final int SERC_CMD= 40;
    }
    public interface feedbackType {
        /*********************命令类型***************************/
        public static final String exit_error = "exit_error";
        public static final String entr_error= "entr_error";
    }


    public interface devType {
        /*********************设备类型***************************/
        public static final String WC_GEOMAGNETIC= "212";//一级地磁车位检测器
        public static final String SECOND_WC_GEOMAGNETIC= "213";//二级地磁车位检测器
        public static final String XL_GEOMAGNETIC= "196";
        public static final String ULTRA_VEHI_DET  = "193";//超声波
        public static final String CAR_VIDEO= "200";//车位视频
        public static final String CAR_LOCK = "197";//车位锁
        public static final String BOOT_SCREEN = "209";//引导屏
        public static final String TOOL_SCREEN  = "195";//收费显示屏
        public static final String ROAD_GATE  = "199";//道闸
        public static final String RELAY  = "161";
        public static final String PARKINGVIDEO="113";

    }

    public interface status {
        /*********************设备类型***************************/
        public static final String success= "success";
        public static final String fail= "fail";
    }

    public interface parkingIoType {
        /*********************车位类型******************/
        final String exit = "exit";
        final String entrance = "entrance";
    }

    public interface SpaceType {
        /*********************车位类型******************/
        final String temporary = "temporary";
        final String vip = "vip";
        final String common = "common";
        final String private1 = "private";

    }

    public interface Position {
        /*********************车辆位置(未进场,已进场，已出场)******************/
        final String admission = "admission";
        final String unapproach = "unapproach";
        final String leave = "leave";

    }


    public interface proceStatus {
        /*********************处理状态******************/
        final String cancel = "cancel";
        final String running = "running";
        final String complete = "complete";

    }

    public interface AuditConstates {
        /*********************审核状态******************/
        final String FAIL = "fail";
        final String SUCCESS = "success";
    }
    public interface DefaultPlate {
        /*********************默认车牌******************/
        final String N = "n";
        final String Y = "y";
    }
    public interface Coordinate {
        /*********************默认车牌******************/
        public static final double lat = 5 * 0.009;
        public static final double lng = 5 * 0.009788;
    }
    public interface OrderType {
        /*********************订单类型******************/
        public static final String vip_charge = "vip_charge";//充值VIP
        public static final String recharge = "recharge";//充值
        public static final String handset = "handset";//手持机订单
        public static final String common = "common";//普通停车
        public static final String monthCard = "monthCard";//月卡停车
        public static final String refund = "refund";
        public static final String pur_monthcard= "pur_monthcard";//购买月卡
        public static final String videopile = "videopile";//视频桩订单
        public static final String vip = "vip";//vip停车订单
        public static final String shared_lot = "shared_lot";//私人车位订单
        public static final String free_admission = "free_admission";//免费订单
    }
    public interface payType {
        /*********************支付类型******************/
        public static final String alipay = "alipay";//支付宝
        public static final String wechat = "wechat";//微信
        public static final String balance = "balance";//余额
        public static final String cash = "cash";//现金
        public static final String free="free";//免费
    }
    public interface createOrderType {
        public static final String machine = "machine";//自动下单
        public static final String artificial = "artificial";//人工下单
        public static final String free_automatic = "free_automatic";//免费自动下单
        public static final String free_labor = "free_labor";//免费人工下单
    }

    public interface OrderStatus {
        /*********************订单状态******************/
        public static final String unpay = "unpay";//待支付
        public static final String complete = "complete";//已完成
        public static final String running = "running";//进行中
        public static final String exception = "exception";//异常
        public static final String refund = "refund";//退款
        public static final String cancel = "cancel";//取消

    }

    public interface msgType {
        public static final String charge = "charge"; //充值成功
        public static final String repay = "repay";
        public static final String vercode  = "vercode";
        public static final String move_car = "move_car";
        public static final String space_error = "space_error";
        public static final String space_error_title = "【车位异常】"; //车位异常本次免单
        public static final String space_error_content = "本次订单免单";
        public static final String recharge_deposit_success = "recharge_deposit_success";  //充值押金
        public static final String recharge_deposit_success_title = "【充值押金成功】";
        public  final String parking_space_info = "parking_space_info";     //车位信息 web √
        public  final String month_card_info = "month_card_info";             //月卡信息
        public  final String month_card_title = "【恭喜你，月卡购买成功】";             //月卡信息
        public  final String month_card_content = "";             //月卡信息

        public static final String entranceIo = "entranceIo"; //√
        public static final String appearance = "appearance";   //出场成功，本次扣费。。。√

        public static final String appearance_title = "【出场信息】";
        public static final String entranceIo_title = "【入场信息】";
        public static final String balance_is_not_enough = "balance_is_not_enough";
        public static final String balance_is_not_enough_title = "【余额不足】"; //出场余额不足，提示充值
        public static final String balance_is_not_enough_content = "请及时充值";
        public static final String leave_overtime = "leave_overtime";   //离场超时，提示信息
        public static final String leave_overtime_title = "号车位离场超时";
        public static final String reserver_overtime = "reserver_overtime"; //预定超时提示√
        public static final String reserver_overtime_title = "预定时间内未进场，已停止预约";
        public static final String parking_spaces_occupied = "parking_spaces_occupied"; //车位被占用，推送最新车位√
        public static final String parking_spaces_occupied__title = "车位已被占用";

        public static final String parking_occupied = "parking_occupied"; //车位被占用，推送最新车位√
        public static final String parking_occupied__title = "车位已被占用,推荐附近停车场";

        public static final String parking_spaces_lot_occupied = "parking_spaces_lot_occupied"; //车位被占用，推送最新车位√
        public static final String parking_spaces_lot_occupied_title = "车位有车";

        public static final String cost_calculation_error = "cost_calculation_error";
        public static final String counterfeit_license_plate  = "counterfeit_license_plate" ;
        public static final String other  = "other" ;

    }
    public interface parkingType {
        /*********************停车场类型******************/
        public static final String roadside = "roadside";//路边
        public static final String onsize = "onsize";//场内
    }
    public interface errorType {
        /*********************异常类型******************/
        public static final String handheld_machine = "handheld_machine";
        public static final String communication = "communication";
        public static final String flow = "flow";
        public static final String geomagnetic = "geomagnetic";
        public static final String normal = "normal";
        public static final String geomagneticTime = "geomagneticTime";

    }
    public interface enabledFlag {
        /*********************支付类型******************/
        public static final String y = "y";
        public static final String n = "n";

    }

    public interface spaceStatus {
        /*********************车位状态（为有车，0为无车）******************/
        public static final String y = "1";
        public static final String n = "0";

    }

    public interface plateType {
        public static final String truck = "truck";
        public static final String auto = "auto";
        public static final String private_parking = "private";
    }




    public interface StateConstates {
        /*********************错误码******************/
        final int RESERVER_FAIL_CODE = 87;
        final String RESERVER_FAIL_MSG = "该车位不能被预定";
        final int ORDERS_RUNING_CODE = 88;
        final String ORDERS_RUNING_MSG = "订单进行中，更改状态失败";
        final int ORDERS_HAVE_BEEN_PAID_CODE = 88;
        final String ORDERS_HAVE_BEEN_PAID_MSG = "订单已支付，无需重复提交";
        final int BIND_FAIL_CODE = 89;
        final String BIND_FAIL_MSG = "绑定失败";
        final int SPACE_EXIT_CODE = 90;
        final String SPACE_EXIT_MSG = "车位已存在";
        final int NOTALLOW_REWSERVAT_CODE=91;
        final String NOTALLOW_REWSERVAT_MSG="该时段不对外出租，请选择其他车位";
        final int EXIST_ORDERS_NOTALLOW_UNLOCK_CODE=92;
        final String EXIST_ORDERS_NOTALLOW_UNLOCK_MSG="存在进行中订单，不允许开锁";
        final int EXIST_ORDERS_NOTALLOW_CODE=93;
        final String EXIST_ORDERS_NOTALLOW_MSG="存在进行中订单，不允许修改";
        final int CANT_OPERATE_CARLOCK_CODE=93;
        final String CANT_OPERATE_CARLOCK_MSG="无权限操作车位锁";
        final int NO_ALLOW_RESERVER_CODE=94;
        final String NO_ALLOW_RESERVER_MSG="月卡用户不允许普通预定";
        final int NO_PRAVITE_SPACE_CODE=95;
        final String NO_PRAVITE_SPACE_MSG="非私人车位不允许绑定用户";
        final int NO_ONSIZE_PARKING_CODE=96;
        final String NO_ONSIZE_PARKING_MSG="该停车场不是室内停车场";
        final int NO_CANCEL_ALLOW_CODE=97;
        final String NO_CANCEL_ALLOW_MSG="vip预定离进场时间小于半个小时不允许取消";
        final int SPACE_BOOKED_CODE=98;
        final String SPACE_BOOKED_MSG="车位已被预定";
        final int SPACE_USER_BOOKED_CODE=98;
        final String SPACE_USER_BOOKED_MSG="你已预定无需重复预定，或取消预定重新进行操作！";
        final int MESSAGE_FAIL_CODE=99;
        final String MESSAGE_FAIL_MSG="接口请求失败";
        final int MESSAGE_SUCCESS_CODE=100;
        final String MESSAGE_SUCCESS_MSG="接口请求成功";
        final int USERNAME_DEXIT_ID=111;
        final String USERNAME_DEXIT_MSG="此用户不存在";
        final int IDXCODE_ERROR_ID=112;
        final String IDXCODE_ERROR_MSG="验证码错误";
        final int NUMBER_HREG_ID=113;
        final String  NUMBER_HREG_MSG="手机号已被注册";
        final int  NUMBER_DEXIT_ID=114;
        final String  NUMBER_DEXIT_MSG="手机号不存在";
        final int  PARAM_ERROR_ID=115;
        final String  PARAM_ERROR_MSG="参数不正确";
        final int  NUMBER_INLEGAL_ID=116;
        final String  NUMBER_INLEGAL_MSG="手机号不合法";
        final int  PASSCODE_ERROR_ID=117;
        final String PASSCODE_ERROR_MSG="登录密码错误";
        final int  IDCODE_INLEGAL_ID=118;
        final String  IDCODE_INLEGAL_MSG="验证码错误";
        final int LOGIN_AGAIN_ID=119;
        final String  LOGIN_AGAIN_MSG="登录身份已过期，请重新登录";
        final int OLD_PASSWORD_ERRER_ID=120;
        final String  OLD_PASSWORD_ERRER_MSG="旧密码输入错误";
        final int PARAMETER_IS_EMPTY_ID=121;
        final String  PARAMETER_IS_EMPTY_MSG="参数不能为空";
        final int PAYMENT_PASSWORD_ID=122;
        final String  PAYMENT_PASSWORD_MSG="支付密码错误，请重新输入";
        final int BALANCE_INSUFFICIENT_ID=123;
        final String  BALANCE_INSUFFICIENT_MSG="余额不足，请充值";
        final int  SPACE_NUMBER_ERRER_ID=124;
        final String SPACE_NUMBER_ERRER_CODE="该城市暂无此泊位号";
        final int NO_CAR_CODE=125;
        final String NO_CAR_MSG="请确认车是否停在该泊位上";
        final int  EXISTING_ORDER_CODE=126;
        final String  EXISTING_ORDER_MSG="泊位已存在订单或您已存在订单";
        final int ORDER_BE_OVERDUE_CODE=127;
        final String ORDER_BE_OVERDUE_MSG="对不起你结束时间已超过或等于免费时段了，无法续时！";
        final int ORDER_IS_NULL_CODE=128;
        final String ORDER_IS_NULL_MSG="订单不存在！！";
        final int CODE_SETTINGS_CODE=129;
        final String CODE_SETTINGS_MSG="请先设置支付密码，在进行下单！！";
        final int USER_ISNOT_LOGIN_ID=130;
        final String USER_ISNOT_LOGIN_MSG="用户未登录";
        final int CONTANT_IS_IN_ID=131;
        final String CONTANT_IS_IN_MSG="该位置已存在部件";
        final int DONOT_MOVE_ID=132;
        final String DONOT_MOVE_MSG="该车位已被预定/已停车";
        final int IS_HAVE_ID=133;
        final String IS_HAVE_MSG="已存在";
        final int SN_ISIN_ID=134;
        final String SN_ISIN_MSG="SN值错误";
        final int PERSON_ISIN_ID=135;
        final String PERSON_ISIN_MSG="该人员已绑定改停车场";
        final int USERNAME_ISIN_ID=136;
        final String USERNAME_ISIN_MSG="此用户名已存在";
        final int INVITATCODE_ISIN_ID=137;
        final String INVITATCODE_ISIN_MSG="你的邀请码不存在，请联系管理员！！";
        final int APP_ORDER_ID=138;
        final String APP_ORDER_MSG="该车位已存在订单，不可重复下单！！";
        final int SPACENUM_ERROR_ID=139;
        final String SPACENUM_ERROR_MSG="车位号不正确，请重新输入6位正确车位号";
        final int SPACENUM_ISIN_ID=140;
        final String SPACENUM_ISIN_MSG="车位号已存在，请重新输入6位正确车位号";
        final int DRIVE_AWAY_ID=141;
        final String DRIVE_AWAY_MSG="车辆未离场，无法结束订单！！";
        final int  VEHICLE_APPEARANCE_ID=142;
        final String VEHICLE_APPEARANCE_MSG="车辆已离场，无需重复结束订单！！";
        final int SPACE_EXCEPTION_CODE=143;
        final String SPACE_EXCEPTION_MSG="车位开小差，如需下单请与管理员联系！";
        final int BE_OVERDUE_CODE=144;
        final String BE_OVERDUE_MSG="验证码已过期，请重新获取！";
        final int IS_NOT_SCHEDULE_CODE=145   ;
        final String IS_NOT_SCHEDULE_MSG="巡检人员暂未排班";
        final int IS_NOT_DELAY_CODE=146;
        final String IS_NOT_DELAY_MSG="此泊位不存在进行中订单，不能续时！！";
        final int NOT_FULL_DATE_CODE=147;
        final String NOT_FULL_DATE_MSG="请将当天10:00到20:00的全部购买即可！！";
        final int IS_NOT_EXIT_IMAGE_CODE=146   ;
        final String IS_NOT_EXIT_IMAGE_MSG="不存在该图片";
        final int IS_NOT_CAR_CODE=148;
        final String IS_NOT_CAR_MSG="该车位无车";
        final int IS_NOT_TEMPORARY_CODE=149;
        final String IS_NOT_TEMPORARY_MSG=" 该车位不是临时车位或未绑定车牌无需挪车!";
        final int WRONG_FORMAT_PLATE_CODE=150;
        final String WRONG_FORMAT_PLATE_MSG="车牌格式不对";
        final int OCCUPY_PLATE_CODE=151;
        final String OCCUPY_PLATE_MSG="车牌已被注册，如不是本人添加，请联系管理员";
        final int GW_ULOGIN_CODE=152;
        final String GW_ULOGIN_MSG="网关未登录";
        final int PARKING_IS_NULL_CODE=153;
        final String PARKING_IS_NULL_MSG="该人员暂未绑定停车场";

        final int PARKING_IS_RESERVED_CODE=154;
        final String PARKING_IS_RESERVED_MSG="你已预定，请勿重复操作";
        final int PARKING_IS_CARD_CODE=155;
        final String PARKING_IS_CARD_MSG="你是月卡用户，无需普通预定";
        final int IS_CARD_CODE=156;
        final String IS_CARD_MSG="该车牌已是月卡用户，无需重复购买";
        final int IS_CARD_INSUFFICIENT_CODE=157;
        final String IS_CARD_INSUFFICIENT_MSG="该停车场暂无月卡，如需购买请与管理人员联系！！";

        final int PARKING_SPEACE_IS_NULL_CODE=154;
        final String PARKING_SPEACE_IS_NULL_MSG="停车场车位数不足，无法申请预定";
        final int PLATE_NOT_EXIST_CODE =155;
        final String PLATE_NOT_EXIST_MSG ="车牌不存在";

        final int PLATE_IMG_ERROR_CODE =156;
        final String PLATE_IMG_ERROR_MSG ="上传行驶证图片错误，请查证后重新上传";

        final int CARD_IS_IN_CODE =157;
        final String CARD_IS_IN_MSG ="该停车场已存在月卡";

        final int USER_IS_APPEAL_CODE =158;
        final String USER_IS_APPEAL_MSG ="你已提交,无需重复提交";
        final int CODE_SETTING_CODE=129;
        final String CODE_SETTING_MSG="请先设置支付密码，在进行提现！！";
        final int GATEWNAME_ISIN_CODE=159;
        final String GATEWNAME_ISIN_MSG="网关名重复，请确认后重新输入";
        final int CITY_ISIN_CODE=160;
        final String CITY_ISIN_MSG="该城市已存在";
        final int VERIFICATION_PASSWORD_CODE=161;
        final String VERIFICATION_PASSWORD_MSG = "验证失败，请重新输入";
        final int VIPSPACE_NUMISNULL_CODE=162;
        final String VIPSPACE_NUMISNULL_MSG = "vip车位数不足";
        final int LACK_OF_AUTHORITY_CODE=163;
        final String LACK_OF_AUTHORITY_MSG = "你没有访问该接口的权限，权限不足";

        final int ORDER_ISNOT_NULL_CODE=164;
        final String ORDER_ISNOT_NULL_MSG = "该车位存在进行中的订单，无法解绑";
        final int NETWORK_EXCEPTION_CODE=165;
        final String NETWORK_EXCEPTION_MSG="网络开小差！";

        final int HAVE_SIGNED_IN_CODE=166;
        final String HAVE_SIGNED_IN_MSG="该人员已签到，无法进行修改";
        final int CARNUM_IN_ORDER_CODE=167;
        final String CARNUM_IN_ORDER_MSG="该车牌已存在一笔进行中的订单";
        final int PLATE_NOT_AUTH_CODE =168;
        final String PLATE_NOT_AUTH_MSG ="车牌未通过验证，请查证后再预定。";
        final int PLATE_AUDITFAIL_CODE =169;
        final String PLATE_AUDITFAIL_MSG ="车牌审核失败";
        final int PLATE_AUDITSUCCESS_CODE =170;
        final String PLATE_AUDITSUCCESS_MSG ="车牌审核成功";
        final int PLATE_MORETHANTREE_CODE =171;
        final String PLATE_MORETHANTREE_MSG ="已超过可绑定车牌";
        final int PLATE_DELETEFAIL_CODE =172;
        final String PLATE_DELETEFAIL_MSG ="该车牌存在进行中或未支付订单,请先结束订单并支付";
        final int EXCEL_OUTPUTFAIL_CODE =174;
        final String EXCEL_OUTPUTFAIL_MSG ="生成excel文件过大,请选择时间段";

        final int CARD_DELETE_STR_CODE =175;
        final String CARD_DELETE_STR_MSG ="该车牌已购买月卡无法删除！！";

        final int IS_ORDER_CODE =176;
        final String IS_ORDER_MSG ="您的停车场某个车位存在进行中订单无法进行修改！！";

        final int PLATE_NOT_EXIST_USER_CODE =177;
        final String PLATE_NOT_EXIST_USER_MSG ="未绑定车牌，请绑定车牌进行操作！！";

        final int ORDER_DELETE_STR_CODE =178;
        final String ORDER_DELETE_STR_MSG ="该车牌已存在进行中订单，无法删除！";

        final int ORDER_TO_BE_PAID_CODE =179;
        final String ORDER_TO_BE_PAID_MSG ="您的账户有待支付订单，请支付在进行操作";

        final int NO_CAR_IN=180;
        final String NO_CAR_IN_MSG="该车牌未入场";
        final int APPLY_VIP_ERROR_CODE=181;
        final String APPLY_VIP_ERROR_MSG="您已申请vip无需重复申请！！";
        final int VIP_PLATE_ERROR_CODE=182;
        final String VIP_PLATE_ERROR_MSG="该车牌已存在vip预定订单，无法删除！";
        final int PLATE_TO_EXAMINE_ERROR_CODE=183;
        final String PLATE_TO_EXAMINE_ERROR_MSG="该车牌正在审核状态，请勿重复提交！";
        final int VIP_TIME_ERROR_CODE=184;
        final String VIP_TIME_ERROR_MSG="您选择的时段已申请预定，请勿重复提交！";
        final int USER_PLATE_ERROR_CODE=185;
        final String USER_PLATE_ERROR_MSG="您已提交车牌信息，请等待审核，请勿重复提交！";
        final int PLATE_IMAGE_IS_NULL_CODE=186;
        final String PLATE_IMAGE_IS_NULL_MSG="请上传行驶证后在重试！";
        final int PLATE_IS_EXIST_CODE=187;
        final String PLATE_IS_EXIST_MSG="车牌已被占用，无法申请！";


        final int MONCARDS_PARKING_CODE=255;
        final String MONCARDS_PARKING_MSG="本停车场没有开通月卡功能";


        final int OPEN_PARKING_CODE=256;
        final String OPEN_PARKING_MSG="停车场内还有订单未结算";

        final int HAVE_NOT_ORDER=257;
        final String HAVE_NOT_ORDER_MSG="不存在订单";

        final int SIGN_IN_CODE=280;
        final String SIGN_IN_MSG="用户已经在手持机上签到";

        final int SYS_DICTIONARY_CODE=281;
        final String SYS_DICTIONARY_MSG="字典类型和字典编码组合必须是唯一";

        final int NOT_CAR_LOCK_ID=282;
        final String NOT_CAR_LOCK_MSG="该车位没有绑定车位锁";

        final int VIP_SPACE_ID=283;
        final String VIP_SPACE_MSG="这是VIP订单，无法绑定车位";

        final int SET_OUT_ID=284;
        final String SET_OUT_MSG="正在通知车主挪车";

        final int USERNAME_NOT_ID=285;
        final String USERNAME_NOT_MSG="用户名不能有中文";
        final int MESSAGE_BIND_CODE = 286;
        final String MESSAGE_BIND_MSG = "不能重复绑定停车场";
        final int IS_NO_RESERVER_ERROR_CODE = 287;
        final String IS_NO_RESERVER_ERROR_MSG = "暂未预订记录,请先申请预定进行操作！！";

        final int IS_NO_ORDER_ERROR_CODE = 287;
        final String IS_NO_ORDER_ERROR_MSG = "暂未订单记录,请确定车位是否正确或先入场进行操作！！";
    }

}
