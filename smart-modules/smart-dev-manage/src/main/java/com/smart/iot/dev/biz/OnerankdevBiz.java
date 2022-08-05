package com.smart.iot.dev.biz;

import com.alibaba.fastjson.JSONObject;
import com.smart.iot.dev.entity.SpaceOnerankde;
import com.smart.iot.dev.mapper.SpaceOnerankdeMapper;
import com.smart.iot.dev.utils.PageUtil;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.msg.TableResultPageResponse;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.smart.iot.dev.entity.Onerankdev;
import com.smart.iot.dev.mapper.OnerankdevMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 设备表
 *
 * @author yuncitys
 * @email yuncitys@smart.iot.com
 * @version 2022-07-08 09:28:51
 */
@Service
@Log4j
public class OnerankdevBiz extends BusinessBiz<OnerankdevMapper,Onerankdev> {
    @Autowired
    public OnerankdevMapper onerankdevMapper;

    private String token;

    @Value("${rpc.username}")
    private String username;
    @Value("${rpc.password}")
    private String password;
    @Value("${rpc.api_url}")
    private String api_url;
    @Value("${rpc.rpc_url}")
    private String rpc_url;
    @Value("${rpc.get_devparams_url}")
    private String get_devparams_url;
    @Value("${rpc.get_token_url}")
    private String get_token_url;

    @Autowired
    public SpaceOnerankdeMapper spaceOnerankdeMapper;

    public TableResultPageResponse<Object> queryOnerankDevList(Map params) {
        PageUtil.makeStartPoint(params);
        List<Object> onerankdevList = onerankdevMapper.queryOnerankdevList(params);
        return new TableResultPageResponse<Object>(onerankdevMapper.queryOnerankdevCount(params),
                onerankdevList,
                Long.parseLong(params.get("startPoint").toString()),
                Long.parseLong(params.get("limit").toString()));
    }


    public TableResultPageResponse<Object> queryBeforeDevBindList(Map params) {
        PageUtil.makeStartPoint(params);
        String devType="";
        if(!StringUtil.isEmpty(String.valueOf(params.get("devType")))){
            devType=String.valueOf(params.get("devType"));
        }

        String sceneType=onerankdevMapper.querySceneType(devType);
        if(!StringUtil.isEmpty(sceneType) && sceneType.equals("io")){
            //查询出入口
            List<Object> ioList = onerankdevMapper.queryIoList(params);
            return new TableResultPageResponse<Object>(onerankdevMapper.queryIoCount(params),
                    ioList,
                    Long.parseLong(params.get("startPoint").toString()),
                    Long.parseLong(params.get("limit").toString()));
        }
        //查询车位
        List<Object> spaceList = onerankdevMapper.querySpaceList(params);
        return new TableResultPageResponse<Object>(onerankdevMapper.querySpaceCount(params),
                spaceList,
                Long.parseLong(params.get("startPoint").toString()),
                Long.parseLong(params.get("limit").toString()));
    }

    public Object infoIssue(String method,String params,String devSn){
        String requestBody = "{\"method\":\""+method+"\",\"params\":\""+params+"\"} ";
        String url = api_url + rpc_url + devSn;
        JSONObject resultJson = sendPost(url,requestBody);

        // 获得响应状态
        int responseCode = Integer.parseInt(resultJson.get("code").toString());
        if(HttpURLConnection.HTTP_UNAUTHORIZED == responseCode){
            JSONObject paramsLogin = new JSONObject();
            paramsLogin.put("username",username);
            paramsLogin.put("password",password);
            //获取TOKEN
            JSONObject rs = sendPost(api_url+get_token_url, paramsLogin.toJSONString());
            JSONObject tokenJson= JSONObject.parseObject(rs.get("data").toString());
            token = "Bearer "+ tokenJson.get("token");
            log.info(token);
            resultJson = sendPost(url,requestBody);
        }

        return resultJson;
    }

    public List queryDevSnAndDeviceId(Map params){
        return mapper.queryDevSnAndDeviceId(params);
    }

//    public static void main(String[] args) {
//        JSONObject paramsLogin = new JSONObject();
//        paramsLogin.put("username","dev@dev.com");
//        paramsLogin.put("password","123456");
//        JSONObject resultJson = new OnerankdevBiz().sendPost("http://localhost:8080"+"/api/auth/login", paramsLogin.toJSONString());
//    }

    /**
     * 发送http POST请求
     *
     * @param
     * @return 远程响应结果
     */
    private JSONObject sendPost(String urlStr,String paramsJson) {
        log.info("request url :" + urlStr);
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", null);
        result.put("code", 200);
        result.put("msg", null);
        int index=urlStr.lastIndexOf("/");
        String devId=urlStr.substring(index+1,urlStr.length());
        log.info("devId=========="+devId);
        StringBuffer sbf = new StringBuffer();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            if(token != null){
                connection.setRequestProperty("X-Authorization", token);
            }
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.addRequestProperty("role", "Admin");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            if (!"".equals(paramsJson)) {
                out.writeBytes(paramsJson);
                log.info(paramsJson);
            }
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbf.append(lines);
            }
            log.info(sbf);

            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            log.info(e.getMessage());
        } catch (IOException e) {

            // TODO Auto-generated catch block
            /// e.printStackTrace();
            log.info(e.getMessage());
            try {
                result.put("code",connection.getResponseCode());
                result.put("success",false);
                result.put("msg",connection.getResponseMessage());
                return result;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        result.put("data",JSONObject.parseObject(sbf.toString()));
        return result;
    }

    private String parseCode(String jsStr){
        String code = "404";
        if(StringUtils.isNotEmpty(jsStr)){
            JSONObject jsonObject = JSONObject.parseObject(jsStr);
            String data = jsonObject.getString("data");

            JSONObject jsonObjectData = JSONObject.parseObject(data);
            if(jsonObjectData!=null) {
                String result = jsonObjectData.getString("result");
                if ("0".equals(result) || "true".equals(result) ) {
                    code = "200";
                }
            }else{
                code = jsonObject.getString("code");
            }
        }
        return code;
    }


}
