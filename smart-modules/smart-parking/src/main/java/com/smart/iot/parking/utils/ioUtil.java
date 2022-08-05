package com.smart.iot.parking.utils;

import com.smart.iot.parking.constant.BaseConstants;

/**
 * Created by Administrator on 2019/3/28 0028.
 */
public class ioUtil {
    public static String getCarTypeByPlateColor(int colorType) {
        String colorTypestr="";
        switch (colorType){
            case 0:
                //未知
                colorTypestr="";
                break;
            case 1:
                colorTypestr=BaseConstants.plateType.auto;
                //蓝色
                break;
            case 2:
                colorTypestr=BaseConstants.plateType.truck;
                //黄色
                break;
            case 3:
                //白色
                colorTypestr="";
                break;
            case 4:
                //黑色
                colorTypestr="";
                break;
            case 5:
                //绿色
                colorTypestr="";
                break;
            default:
               System.out.print("未找到类型进入default");
                break;
        }
       return colorTypestr;
    }
    //车牌类型 0：未知车牌:、1：蓝牌小汽车、2：:黑牌小汽车、3：单排黄牌、4：双排黄牌、 5：警车车牌、6：武警车牌、
    // 7：个性化车牌、8：单排军车牌、9：双排军车牌、10：使馆车牌、11：香港进出中国大陆车牌、12：农用车牌、
    // 13：教练车牌、14：澳门进出中国大陆车牌、15：双层武警车牌、16：武警总队车牌、17：双层武警总队车牌、18：民航车牌、19：新能源车牌
    public static int getCarIsTtFreer(int type) {
        int colorTypestr=0;
        if(type==5 || type==6 || type==8 || type==9 || type==10 || type==15 || type==16 || type==17){
            colorTypestr=1;
        }
        return colorTypestr;
    }


    public static int getCarIsTtFreerDh(String plateType) {
        int colorTypestr=0;
        if("Normal".equals(plateType)){
            colorTypestr=0;
        }else{
            colorTypestr=1;
        }
        return colorTypestr;
    }


    public static String getCarTypeByPlateColorDh(String colorType) {
        String colorTypestr="";
        switch (colorType){
            case "":
                //未知
                colorTypestr="";
                break;
            case "Blue":
                colorTypestr=BaseConstants.plateType.auto;
                //蓝色
                break;
            case "Yellow":
                colorTypestr=BaseConstants.plateType.truck;
                //黄色
                break;
            case "White":
                //白色
                colorTypestr="";
                break;
            case "Black":
                //黑色
                colorTypestr="";
                break;
            case "Green":
                //绿色
                colorTypestr="";
                break;
            default:
                System.out.print("未找到类型进入default");
                break;
        }
        return colorTypestr;
    }
}
