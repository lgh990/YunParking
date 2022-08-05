package com.smart.iot.parking.utils;


import com.alibaba.fastjson.JSONObject;
import com.smart.iot.parking.srever.IRedisService;
import com.yuncitys.smart.parking.common.constant.RedisKeyConstants;
import com.yuncitys.smart.parking.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 *
 * @项目名称: --
 * @版权所有: --
 * @技术支持: --
 * @单元名称: http协议GET/POST请求工具类
 * @开始时间: 2017-10-14
 * @开发人员: --
 */
@Slf4j
@Component
@Service
public  class HttpUtil {
    @Value("${rpc.api_url}")
    public   String api_url;
    @Value("${rpc.rpc_url}")
    public  String rpc_url;
    @Value("${rpc.get_devparams_url}")
    public  String get_devparams_url;
    @Value("${rpc.get_token_url}")
    public  String get_token_url;
    @Value("${rpc.username}")
    public  String username;
    @Value("${rpc.password}")
    public  String password;
    public static String  TOKEN="";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public  void main(String[] args) {
        //post rpc
        JSONObject rpcParams = new JSONObject();
        //String requestBody = "{\"method\":\"readDevAttribute\",\"params\":\"all\"}";
        String requestBody = "{\"method\":\"setTime\",\"params\":\"{\\\"id\\\":\\\"1\\\",\\\"type\\\":\\\"sf\\\",\\\"packetType\\\":\\\"U\\\",\\\"data\\\":\\\"D0 07 00 98 ED 9D EC 00 00 81 C1 00\\\"}\"} ";
        String rsRpc = sendPost(api_url+rpc_url, requestBody);

        HashMap<String, String> mData = new HashMap<String, String>();
        mData.put("type","default");
        mData.put("limit","1000");
        String s = ajaxProxyGet(get_devparams_url.toString(),mData);

    }


    public  String ajaxProxyGet(String path, HashMap<String, String> mData){
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            //GET请求直接在链接后面拼上请求参数
            String mPath = path + "?";
            for(String key:mData.keySet()){
                mPath += key + "=" + mData.get(key) + "&";
            }
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Authorization",TOKEN);
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器
            conn.connect();
            // 获得响应状态
            int responseCode = conn.getResponseCode();

            if(HttpURLConnection.HTTP_UNAUTHORIZED == responseCode){
                JSONObject params = new JSONObject();
                params.put("username", username);
                params.put("password",password);
                //获取TOKEN
                String rs = sendPost(api_url+get_token_url, params.toJSONString());
                TOKEN = "Bearer " + JSONObject.parseObject(rs).get("token");
                System.out.println(TOKEN);
                return ajaxProxyGet(path,mData);
            }
            if(responseCode != 200){
                log.info("下发失败,异常编码:"+responseCode);
                return responseCode+"";
            }
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭输入流
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
    /**
     * 发送http POST请求
     *
     * @param
     * @return 远程响应结果
     */
    public  String sendPost(String u, String json ) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", null);
        result.put("code", 200);
        result.put("msg", null);
        int index=u.lastIndexOf("/");
        String devId=u.substring(index+1,u.length());
        log.info("devId=========="+devId);
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        String key=RedisKeyConstants.REDIS_HTTP_RPC_COUNT_KEY;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(u);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if(TOKEN != null){
                connection.setRequestProperty("X-Authorization", TOKEN);
                connection.setRequestProperty("Content-type", "application/json");
            }
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
            if (!"".equals(json)) {
                out.writeBytes(json);
            }
            out.flush();
            out.close();

            // 获得响应状态
            int responseCode = connection.getResponseCode();
            if(HttpURLConnection.HTTP_UNAUTHORIZED == responseCode){
                JSONObject params = new JSONObject();
                params.put("username",username);
                params.put("password",password);
                //获取TOKEN
                String rs = sendPost(api_url+get_token_url, params.toJSONString());
                TOKEN = "Bearer "+ JSONObject.parseObject(rs).get("token");
                System.out.println(TOKEN);
                return sendPost(u,json);
            }
            long count=redisTemplate.opsForValue().get(key+devId)==null?1:Long.valueOf(redisTemplate.opsForValue().get(key+devId));
            if(responseCode != 200){
                if(count>=3){
                    log.info("重发3次返回状态:"+responseCode);
                    redisTemplate.delete(key+devId);
                    return result+"";
                }else{
                    result.put("success", false);
                    result.put("data", null);
                    result.put("code", responseCode);
                    result.put("msg", "下发失败,异常编码");
                    count=redisTemplate.opsForValue().increment(key+devId,1);
                    log.info("下发失败,异常编码，进行第"+count+"次下发:"+responseCode);
                    sendPost(u, json);
                    try {
                        Thread.sleep(1000 * count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sbf.append(lines);
            }
            System.out.println(sbf);
            String code= parseCode(sbf.toString());
            if(!StringUtil.isEmpty(code) && Integer.valueOf(code) != 200){
                if(count>=3){
                    log.info("重发3次返回状态:"+responseCode);
                    redisTemplate.delete(key+devId);
                    return result+"";
                }else {
                    result.put("success", false);
                    result.put("data", null);
                    result.put("code", responseCode);
                    result.put("msg", "下发失败,异常编码");
                    count = redisTemplate.opsForValue().increment(key + devId, 1);
                    log.info("下发失败,异常编码，进行第" + count + "次下发:" + responseCode);
                    sendPost(u, json);
                    try {
                        Thread.sleep(1000 * count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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
        }
        redisTemplate.delete(key+devId);
        return sbf.toString();
    }




    private static String parseCode(String jsStr){
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
