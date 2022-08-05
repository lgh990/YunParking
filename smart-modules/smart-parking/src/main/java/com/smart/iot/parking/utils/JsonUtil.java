package com.smart.iot.parking.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2019/3/28 0028.
 */
public class JsonUtil {
    public static Map  JsonStringToMap(String str) {
        JSONObject jasonObject = JSONObject.parseObject(str);
        Map map = (Map) jasonObject;
        return map;
    }

    public static<T> String objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper();
             // Convert object to JSON string
               String jsonStr = "";
                 try {
                          jsonStr =  mapper.writeValueAsString(obj);
                    } catch (IOException e) {
                      throw e;
                    }
               return JSONObject.toJSON(obj).toString();
    }
}
