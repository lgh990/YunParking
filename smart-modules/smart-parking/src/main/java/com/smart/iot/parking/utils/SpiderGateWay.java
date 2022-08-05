package com.smart.iot.parking.utils;


import com.smart.iot.parking.constant.BaseConstants;

import java.io.UnsupportedEncodingException;

public class SpiderGateWay {
    public byte[] OperateBootSrceen1(long size,String terminId) {
        byte[] cmd =new byte[12];
        cmd[0] = (byte)BaseConstants.instructions.CONTROL_CMD ;
        cmd[1] = (byte)1 ;

/*
        cmd[7] = (byte)0 ;//初始化流水，服务端当前发送流水
*/

        cmd[2] = (byte)2 ;
        cmd[3] = (byte)Integer.parseInt(String.valueOf((size/100)),16) ;
        cmd[4] = (byte)Integer.parseInt(String.valueOf(size%100),16);
        return cmd;
    }
    //操作道闸
    public byte[] OperateRoadGate1(int flag) {
        byte[] cmd =new byte[12];
        cmd[0] = (byte)BaseConstants.instructions.CONTROL_CMD ;
        cmd[1] = (byte)1 ;

        cmd[2] = (byte)0 ;//初始化流水，服务端当前发送流水

        cmd[3] = (byte)1 ;
        //cmd[4] = (byte)(flag == 0?1:0) ;//0关锁   1开锁
        cmd[4] = (byte)flag;
        return cmd;
    }

    //正常入场进场(异常)
    public byte[]   OperateToolScreen1(String car_plate,String type,String userType,String money,String time,int accessType) throws UnsupportedEncodingException {
        int len=0;
        if(type.equals(BaseConstants.admin_status.NORMAL_ADMISSION)) {
            len = 32;
        }else if(type.equals(BaseConstants.admin_status.NORMAL_APPEARANCE)) {
            len = 48;
        }else if(type.equals(BaseConstants.admin_status.UNNORMAL_ADMISSION)) {
            len = 12;
        }else if (type.equals(BaseConstants.admin_status.UNNORMAL_APPEARANCE)){
            len = 12;
        }  else if (type.equals(BaseConstants.admin_status.OVER_TIME)){
            len = 12;
        }
        else if (type.equals(BaseConstants.admin_status.OVER_SPACE)){
            len = 32;
        }
        byte[] cmd =new byte[len];

        cmd[0] = (byte)BaseConstants.instructions.CONTROL_CMD ;   //包类型为48

        if(type.equals(BaseConstants.admin_status.NORMAL_ADMISSION)) {                                          //进场
            cmd[1] = (byte)1 ;                                //详细命令

            cmd[2] = (byte)0 ;//初始化流水，服务端当前发送流水  后台包计数


            byte b[] = car_plate.getBytes("gbk");//String转换为byte[]
            int plateLenth = b.length;
            cmd[4] = (byte)plateLenth ;                       //车牌数据长度
            cmd[5]=(byte)(plateLenth>>8);

            for(int i = 0;i< plateLenth;i++)                   //车牌具体数据
            {
                cmd[6 + i] = b[i];
            }
            cmd[6 + plateLenth + 3] = (byte) 0;                                //进出类型
            cmd[6 + plateLenth + 4] = (byte)getUserType(userType);                                   //用户属性
            cmd[6 + plateLenth + 5] = (byte)accessType;        //出入属性
            String a="0000";                                              //剩余时间
            byte bt[] = a.getBytes("gbk");
            int len1=bt.length;
            for(int i = 0;i< len1;i++)
            {
                cmd[7 + plateLenth + 5+ i] = bt[i];
            }
            cmd[3] = (byte)(11 + plateLenth + 5+ len1-8) ;                 //数据长度
        }else if(type.equals(BaseConstants.admin_status.NORMAL_APPEARANCE)) {                                           //出场
            cmd[1] = (byte)1 ;                                //详细命令

            cmd[2] = (byte)0 ;//初始化流水，服务端当前发送流水  后台包计数


            byte b[] = car_plate.getBytes("gbk");//String转换为byte[]
            int plateLenth = b.length;
            cmd[4] = (byte)plateLenth ;                       //车牌数据长度
            cmd[5]=(byte)(plateLenth>>8);

            for(int i = 0;i< plateLenth;i++)                   //车牌具体数据
            {
                cmd[6 + i] = b[i];
            }
            cmd[6 + plateLenth + 3] =(byte) 1;                                 //进出类型
            cmd[6 + plateLenth + 4] =(byte)getUserType(userType);      //出入属性
            cmd[6 + plateLenth + 5] = (byte)0;      //出入属性
            money = money.replace(".","");
            String ss=getString(money);                           //钱数
            byte bt[] = ss.getBytes("gbk");
            int len1=bt.length;
            for(int i = 0;i< len1;i++)
            {
                cmd[7 + plateLenth + 5+ i] = bt[i];
            }
            ss=getString(time.substring(0,3));
            bt = ss.getBytes("gbk");//停泊时间
            int len2=bt.length;
            for(int i = 0;i< len2;i++)
            {
                cmd[8 + plateLenth + 5+ len1] = bt[i];
            }
            ss=getString(time.substring(4,5));
            bt = ss.getBytes("gbk");//停泊时间
            int len4=bt.length;
            for(int i = 0;i< len2;i++)
            {
                cmd[8 + plateLenth + 5+ len1+len2] = bt[i];
            }
            String a="0000";                                                //剩余时间
            bt =  a.getBytes("gbk");
            int len3=bt.length;
            for(int i = 0;i< len3;i++)
            {
                cmd[9 + plateLenth + 5+ len1+len2+len4] = bt[i];
            }
            cmd[3] = (byte)(10 + plateLenth + 5+ len1+len2+len3-8) ;          //数据长度
        }else if(type.equals(BaseConstants.admin_status.UNNORMAL_ADMISSION)) {                                   //进场异常
            cmd[1]=(byte)2;              //详细命令
            cmd[2]=(byte)0;              //后台包计数
            cmd[3]=(byte)0;              //数据长度
            for (int i=0;i<8;i++){
                cmd[i+4]=(byte)0;
            }
        }else if (type.equals(BaseConstants.admin_status.UNNORMAL_APPEARANCE)){                                  //出场异常
            cmd[1]=(byte)3;              //详细命令
            cmd[2]=(byte)0;              //后台包计数
            cmd[3]=(byte)0;              //数据长度
            for (int i=0;i<8;i++){
                cmd[i+4]=(byte)0;
            }
        }else if (type.equals(BaseConstants.admin_status.OVER_TIME)){                                  //超时
            cmd[1]=(byte)4;              //详细命令
            cmd[2]=(byte)0;              //后台包计数
            cmd[3]=(byte)0;              //数据长度
            for (int i=0;i<8;i++){
                cmd[i+4]=(byte)0;
            }
        }else if (type.equals(BaseConstants.admin_status.OVER_SPACE)){                                  //车位已满
            cmd[1] = (byte)5 ;                                //详细命令

            cmd[2] = (byte)0 ;//初始化流水，服务端当前发送流水  后台包计数


            byte b[] = car_plate.getBytes("gbk");//String转换为byte[]
            int plateLenth = b.length;
            cmd[4] = (byte)plateLenth ;                       //车牌数据长度
            cmd[5]=(byte)(plateLenth>>8);

            for(int i = 0;i< plateLenth;i++)                   //车牌具体数据
            {
                cmd[6 + i] = b[i];
            }
            cmd[6 + plateLenth + 3] = (byte) 0;                                //进出类型
            cmd[6 + plateLenth + 4] = (byte)getUserType(userType);                                   //用户属性
            cmd[6 + plateLenth + 5] = (byte)accessType;        //出入属性
            String a="0000";                                              //剩余时间
            byte bt[] = a.getBytes("gbk");
            int len1=bt.length;
            for(int i = 0;i< len1;i++)
            {
                cmd[7 + plateLenth + 5+ i] = bt[i];
            }
            cmd[3] = (byte)(11 + plateLenth + 5+ len1-8) ;                 //数据长度
        }
        return cmd;

    }

    public int getUserType(String userType)
    {
        int type = 0;
        if(BaseConstants.OrderType.common.equals(userType))
        {
            type = 0;
        }else if(BaseConstants.OrderType.vip.equals(userType))
        {
            type = 4;
        } else if(BaseConstants.OrderType.shared_lot.equals(userType))
        {
            type = 3;
        }else if(BaseConstants.OrderType.monthCard.equals(userType))
        {
            type = 2;
        }
        return type;
    }
    //判断是否为6位数的字符串
    private String getString(String money1) {
        int s = money1.length();
        if (money1.length()!=6){
            for (int i=0;i<6-s;i++){
                money1="0"+money1;
            }
        }
        return money1;
    }
    public String  getByteStr(byte[] data){
        String byteStr ="";
        byteStr += bytesToHexString(data);
        return byteStr;
    }
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append( hv +" ");
        }
        return stringBuilder.toString();
    }


    //操作车位锁
    public byte[] OperateCarLock1(String flag) {
        byte[] cmd =new byte[12];
        cmd[0] = (byte) BaseConstants.instructions.CONTROL_CMD ;
        cmd[1] = (byte)1 ;

        cmd[2] = (byte)0 ;//初始化流水，服务端当前发送流水

        cmd[3] = (byte)1 ;
        cmd[4] = (byte)("n".equals(flag) ?1:0) ;//0关锁   1开锁
        return cmd;
    }
}
