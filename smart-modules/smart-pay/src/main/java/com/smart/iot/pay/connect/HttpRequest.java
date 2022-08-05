package com.smart.iot.pay.connect;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.parking.KeyStore;
import java.util.Map;

@Component
public class HttpRequest {

    //请求配置参数集合
    @Autowired
    private Map<String,String> requestProperty;

    /**
     * http公用请求方法
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public String request(String requestUrl, String requestMethod, String outputStr) {
        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        try
        {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
//            if(requestProperty.get("sslEnabled")!=null && "Y".equals(requestProperty.get("sslEnabled"))){
//                setCert(conn);
//            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr)
            {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes(requestProperty.get("charset")));
                outputStream.close();
            }
            // 从输入流读取返回内容
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, requestProperty.get("charset"));
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str;
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str+"\n");
            }
            // 释放资源
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        } finally{
            if(conn !=null)
                conn.disconnect();
            try {
                if(bufferedReader != null )
                    bufferedReader.close();
                if(inputStreamReader != null)
                    inputStreamReader.close();
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 给HttpsURLConnection设置数字证书
     * @param connection
     * @throws IOException
     */

    private void setCert(HttpsURLConnection connection) throws IOException{
        FileInputStream instream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(requestProperty.get("sslUsername"));
            //读取本机存放的PKCS12证书文件
            //certPath:数字证书路径
            instream = new FileInputStream(new File(requestProperty.get("wcCertPath")));
            //指定PKCS12的密码(商户ID)
            char[] customId = "1488614362".toCharArray();
            keyStore.load(instream, customId);
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,  customId).build();
            //指定TLS版本
            SSLSocketFactory ssf = sslcontext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            instream.close();
        }
    }

    public CloseableHttpResponse sslRequest(String appId,String url,String paramsStr) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(requestProperty.get("sslUsername"));
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,appId.toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        HttpPost httppost = new HttpPost(url);
        StringEntity se = new StringEntity(paramsStr);
        httppost.setEntity(se);
        return httpclient.execute(httppost);
    }

}
