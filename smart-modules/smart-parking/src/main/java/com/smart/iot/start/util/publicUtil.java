package com.smart.iot.start.util;

/**
 * Created by Administrator on 2019/4/23 0023.
 */
public class publicUtil {



    public static String licensePlateHead(Integer number){
        String plateHead="";
        switch (number){
            case 0:
                plateHead="京";
                break;
            case 1:
                plateHead="津";
                break;
            case 2:
                plateHead="冀";
                break;
            case 3:
                plateHead="晋";
                break;
            case 4:
                plateHead="蒙";
                break;
            case 5:
                plateHead="辽";
                break;
            case 6:
                plateHead="吉";
                break;
            case 7:
                plateHead="黑";
                break;
            case 8:
                plateHead="沪";
                break;
            case 9:
                plateHead="苏";
                break;
            case 10:
                plateHead="浙";
                break;
            case 11:
                plateHead="皖";
                break;
            case 12:
                plateHead="闽";
                break;
            case 13:
                plateHead="赣";
                break;
            case 14:
                plateHead="鲁";
                break;
            case 15:
                plateHead="豫";
                break;
            case 16:
                plateHead="鄂";
                break;
            case 17:
                plateHead="湘";
                break;
            case 18:
                plateHead="粤";
                break;
            case 19:
                plateHead="桂";
                break;
            case 20:
                plateHead="琼";
                break;
            case 21:
                plateHead="渝";
                break;
            case 22:
                plateHead="川";
                break;
            case 23:
                plateHead="贵";
                break;
            case 24:
                plateHead="云";
                break;
            case 25:
                plateHead="藏";
                break;
            case 26:
                plateHead="陕";
                break;
            case 27:
                plateHead="甘";
                break;
            case 28:
                plateHead="青";
                break;
            case 29:
                plateHead="宁";
                break;
            case 30:
                plateHead="新";
                break;
            case 31:
                plateHead="港";
                break;
            case 32:
                plateHead="澳";
                break;
            case 33:
                plateHead="台";
                break;
            case 34:
                plateHead="警";
                break;
            case 35:
                plateHead="使";
                break;
            case 36:
                plateHead="WJ";
                break;
            case 37:
                plateHead="领";
                break;
            case 38:
                plateHead="学";
                break;
            default:
                System.out.print("未找到类型进入default");
                break;
        }
        return plateHead;
    }
}
