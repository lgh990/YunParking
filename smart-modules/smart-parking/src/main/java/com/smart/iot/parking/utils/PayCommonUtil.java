package com.smart.iot.parking.utils;


import org.apache.http.ssl.SSLContexts;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.parking.KeyStore;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayCommonUtil
{
    @Value("${wcpay.wc_app_appid}")
    private  String wc_app_appid;
    @Value("${wcpay.wc_app_mchid}")
    private  String wc_app_mchid;
    @Value("${wcpay.wc_app_apikey}")
    private  String wc_app_apikey;
    @Value("${wcpay.wc_app_notify_url}")
    private  String wc_app_notify_url;
    @Value("${wcpay.wc_cert_path}")
    private  String wc_cert_path;
    @Value("${ssl_enabled}")
    private  String ssl_enabled;

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @return boolean
     */
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY)
    {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v))
            {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + API_KEY);

        // 算出摘要
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();

        // System.out.println(tenpaySign + "    " + mysign);
        return tenpaySign.equals(mysign);
    }

    /**
     * @author
     * @date 2016-4-22
     * @Description：sign签名
     * @param characterEncoding
     *            编码格式
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY)
    {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" +API_KEY);
        System.out.println("======================================"+sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * @author
     * @date 2016-4-22
     * @Description：将请求参数转换为xml格式的string
     * @param parameters
     *            请求参数
     * @return
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k))
            {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            }
            else
            {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length
     *            int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length)
    {
        int num = 1;
        double random = Math.random();
        if (random < 0.1)
        {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++)
        {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime()
    {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    // xml解析
    public static Map doXMLParse(String strxml) throws IOException, JDOMException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml))
        {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty())
            {
                v = e.getTextNormalize();
            }
            else
            {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        // 关闭流
        in.close();

        return m;
    }

    public static String getChildrenText(List children)
    {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty())
        {
            Iterator it = children.iterator();
            while (it.hasNext())
            {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty())
                {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    // 请求方法
    public  String httpsRequest(String requestUrl, String requestMethod, String outputStr)
    {
        try
        {

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if(ssl_enabled!=null && "Y".equals(ssl_enabled)){
                setCert(conn);
            }
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
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        }
        catch (ConnectException ce)
        {
            System.out.println("连接超时：{}" + ce);
        }
        catch (Exception e)
        {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }

    /**
     * 给HttpsURLConnection设置数字证书
     * @param connection
     * @throws IOException
     */
    private  void setCert(HttpsURLConnection connection) throws IOException{
        FileInputStream instream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读取本机存放的PKCS12证书文件
            instream = new FileInputStream(new File(wc_cert_path)); //certPath:数字证书路径            System.out.println("=============================================");
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, wc_app_mchid.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,  wc_app_mchid.toCharArray()).build();
            //指定TLS版本
            SSLSocketFactory ssf = sslcontext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            instream.close();
        }
    }
    // 生成签名
    public  String createSign(String characterEncoding, SortedMap<Object, Object> parameters)
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wc_app_apikey);
        System.out.println(sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    // 生成签名
    public static String createSignApplet(String characterEncoding, SortedMap<Object, Object> parameters,String key)
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 生成32为字符串
     *
     * @return
     */
    public static String getGUID()
    {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }
}
