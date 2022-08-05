package com.smart.iot.charge.server;

import com.smart.iot.charge.entity.ChargeByHour;
import com.yuncitys.smart.parking.common.util.DateUtil;
import com.yuncitys.smart.parking.common.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 室内停车场收费计算
 */
public class ChargingByHoursUtil {

    public static BigDecimal getprice(ChargeByHour lr, String beginDate,String endDate){
        if (lr != null) {
            if (DateUtil.parse(beginDate,null).before(DateUtil.parse(endDate,null))) {
                BigDecimal money = BigDecimal.valueOf(0);
                Date be = DateUtil.parse(beginDate,null);
                Date en = DateUtil.parse(endDate,null);
                SimpleDateFormat zerotDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd 24:00:00");
                if(StringUtil.isEmpty(lr.getOdStartTime() )){
                    return BigDecimal.valueOf(0.00);
                }
                SimpleDateFormat beformat = new SimpleDateFormat("yyyy-MM-dd " + lr.getOdStartTime() + ":00");
                SimpleDateFormat enformat = new SimpleDateFormat("yyyy-MM-dd " + lr.getOdEndTime() + ":00");
                long time = en.getTime() - be.getTime();
                int n = (int) (time/(1000*60*60*24));//天数
                long time1 = time-(1000*60*60*24*n);
                int m =  (int)(time1/(1000*60*15));//多少个15分钟
                //北京需求，未满15分钟免费
                /*  if(time < 1000*60*15)
                {
                    return  new BigDecimal(0.00);
                }
                */
                if((time1%(1000*60*15)) > 0){
                    m += 1;
                }

                //白天时段有bn个15分钟
                int x = (int) ((DateUtil.parse(enformat.format(be),null).getTime() - DateUtil.parse(beformat.format(be),null).getTime())/(1000*60*15));
                long xtime = DateUtil.parse(enformat.format(be),null).getTime() - DateUtil.parse(beformat.format(be),null).getTime();
                long ytime = (1000*60*60*24) - xtime;
                //夜间时段有yn个15分钟
                int y = ((1000*60*60*24)/(1000*60*15)) - x;
                BigDecimal moneys = new BigDecimal(0);
                long times = en.getTime() - be.getTime();
                //开始时间在白天时段
                if (DateUtil.parse(beformat.format(be),null).before(be) && be.before(DateUtil.parse(enformat.format(be),null))) {
                    long atime = DateUtil.parse(enformat.format(be),null).getTime() - be.getTime();
                    if(times < atime){
                        if(m <= 4){
                            money = new BigDecimal(m).multiply(lr.getOdFirstPrice());
                        }else{
                            money = new BigDecimal(4).multiply(lr.getOdFirstPrice()).add(new BigDecimal(m - 4).multiply(lr.getOdAfterPrice()));
                        }
                    }else{
                        int n1 = (int) (atime / (1000 * 60 * 15));
                        if (atime % (1000 * 60 * 15) != 0) {
                            n1 += 1;
                        }
                        if(n1 <= 4){
                            money = new BigDecimal(n1).multiply(lr.getOdFirstPrice());
                        }else{
                            money = new BigDecimal(4).multiply(lr.getOdFirstPrice()).add(new BigDecimal(n1 - 4).multiply(lr.getOdAfterPrice()));
                        }
                        times = times - atime;

                        while(true){
                            if(times <= 0){
                                break;
                            }else if(0 < times && times < ytime){
                                int n2 = (int) (times / (1000 * 60 * 60 * 2));
                                if (times % (1000 * 60 * 60 * 2) != 0) {
                                    n2 += 1;
                                }
                                money = money.add(new BigDecimal(n2).multiply(lr.getOdLastPrice()));
                                break;
                            }else{
                                times = times - ytime ;
                                int n2 = (int) (ytime / (1000 * 60 * 60 * 2));
                                if (ytime % (1000 * 60 * 60 * 2) != 0) {
                                    n2 += 1;
                                }
                                money = money.add(new BigDecimal(n2).multiply(lr.getOdLastPrice()));
                            }
                            if(times<=0){
                                break;
                            }else if(0 < times && times < xtime){
                                int n2 = (int) (times / (1000 * 60 * 15));
                                if(times % (1000 * 60 * 15) != 0){
                                    n2 += 1;
                                }
                                money = money.add(new BigDecimal(n2).multiply(lr.getOdAfterPrice()));
                                break;
                            }else{
                                times = times - xtime ;
                                int n2 = (int) (xtime / (1000 * 60 * 15));
                                if(xtime % (1000 * 60 * 15) != 0){
                                    n2 += 1;
                                }
                                money = money.add(new BigDecimal(n2).multiply(lr.getOdAfterPrice()));
                            }
                        }
                    }
                } else {

                    long atime = DateUtil.parse(beformat.format(be),null).getTime() - be.getTime();
                    if (atime < 0) {
                        atime += (1000 * 60 * 60 * 24);
                    }
                    if(times < atime){
                        int m2 = m / 8;
                        if (m % 8 != 0) {
                            m2 += 1;
                        }
                        money = new BigDecimal(m2).multiply(lr.getOdLastPrice());
                    }else{
                        int m1 = (int) (atime / (1000 * 60 * 60 * 2));
                        if (atime % (1000 * 60 * 60 * 2) != 0) {
                            m1 += 1;
                        }
                        money = new BigDecimal(m1).multiply(lr.getOdLastPrice());

                        times = times - atime ;
                        while(true){
                            if(times<=0){
                                break;
                            }else if(0 < times && times < xtime){
                                int n1 = (int) (times / (1000 * 60 * 15));
                                if(times % (1000 * 60 * 15) != 0){
                                    n1 += 1;
                                }
                                money = money.add(new BigDecimal(n1).multiply(lr.getOdAfterPrice()));
                                break;
                            }else{
                                times = times - xtime ;
                                int n1 = (int) (xtime / (1000 * 60 * 15));
                                if(xtime % (1000 * 60 * 15) != 0){
                                    n1 += 1;
                                }
                                money = money.add(new BigDecimal(n1).multiply(lr.getOdAfterPrice()));
                            }
                            if(times <= 0){
                                break;
                            }else if(0 < times && times < ytime){
                                int n1 = (int) (times / (1000 * 60 * 60 * 2));
                                if (times % (1000 * 60 * 60 * 2) != 0) {
                                    n1 += 1;
                                }
                                money = money.add(new BigDecimal(n1).multiply(lr.getOdLastPrice()));
                                break;
                            }else{
                                times = times - ytime ;
                                int n1 = (int) (ytime / (1000 * 60 * 60 * 2));
                                if (ytime % (1000 * 60 * 60 * 2) != 0) {
                                    n1 += 1;
                                }
                                money = money.add(new BigDecimal(n1).multiply(lr.getOdLastPrice()));
                            }

                        }
                    }

                }
                return money;

            }
        }
        return BigDecimal.valueOf(0.00);
    }



}
