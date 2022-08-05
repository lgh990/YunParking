package com.smart.iot.parking.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.feign.DepartFeign;
import com.yuncitys.ag.core.context.BaseContextHandler;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20 0020.
 */
public class publicUtils {
    //从集合树种筛选当前用户所在部门
    public static List<HashMap<String,Object>> getCurrDepart(DepartFeign departFeign)
    {
        String departID = BaseContextHandler.getDepartID();
        HashMap<String,Object> depart = departFeign.getByPK(departID);
        JSONObject departValue = JSON.parseObject(String.valueOf(depart.get(departID)));
        Object tree = departFeign.getTree();
        List<HashMap<String,Object>> treeMap = (List<HashMap<String,Object>>)tree;
        HashMap<String, Object> map=new HashMap<>();
        if(treeMap!=null && treeMap.size()>0) {
            map = treeMap.get(0);
        }
        while (true) {
            String treeMapCode = String.valueOf(map.get("code"));
            String departCode="";
            if(departValue!=null) {
                 departCode = String.valueOf(departValue.get("code"));
            }
            treeMap = (List<HashMap<String,Object>>)map.get("children");
            if(treeMap!=null && treeMap.size()>0) {
                map = treeMap.get(0);
                if (treeMapCode.equals(departCode)) {
                    break;
                }
            }
        }
        return treeMap;
    }
}
