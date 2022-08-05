package com.smart.iot.dev.utils;

import java.util.Map;

public class PageUtil {

    public static void makeStartPoint(Map params){
        Integer page = Integer.valueOf(params.get("page").toString());
        Integer limit = Integer.valueOf(params.get("limit").toString());
        Integer startPoint = ((page-1)*limit);
        params.put("startPoint",startPoint);
    }

}
