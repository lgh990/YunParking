package com.smart.iot.pay.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.smart.iot.pay.biz.PayLogBiz;
import com.smart.iot.pay.entity.AlipayRefund;
import com.smart.iot.pay.feign.ParkingOrderFeign;
import com.smart.iot.pay.util.AlipayNotify;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

@Service
@Slf4j
public class AliPayServer {

    //第三方请求配置参数中心
    @Autowired
    private Map<String, String> requestProperty;

    @Autowired
    private ParkingOrderFeign parkingOrderFeign;

    private PayLogBiz payLogBiz;

    /**
     * 获取支付宝支付表单（在支付宝生成订单）
     *
     * @param params
     * @return
     */
    public String getAliPayForm(Map<String, String> params) {
        //阿里连接客户端参数配置
        AlipayClient client = new DefaultAlipayClient(requestProperty.get("aliPayGateUrl"), requestProperty.get("aliAppId"),
                requestProperty.get("aliPrivateSign"), requestProperty.get("format"), requestProperty.get("charset"),
                requestProperty.get("aliPublicKeys"), requestProperty.get("aliSignType"));
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(params.get("tradeNo"));
        model.setSubject(params.get("subject"));
        model.setTotalAmount(params.get("money"));
        model.setBody(params.get("desc"));
        model.setTimeoutExpress(params.get("timeout"));
        model.setProductCode(params.get("productCode"));
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(requestProperty.get("aliNotifyUrl"));
        //支付宝返回表单
        String form = "";
        try {
            form = client.pageExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    /**
     * alipay支付结果通知
     *
     * @param params
     * @return
     */
    public String notify(Map<String, String> params) {
        // 交易状态
        String tradeStatus = params.get("trade_status");
        String tradeNo = params.get("out_trade_no");
        //封装传给业务系统参数
        Map<String, String> map = Maps.newHashMap();
        map.put("tradeStatus", tradeStatus);
        map.put("tradeNo", tradeNo);
        //通知订单系统修改订单信息
        if (AlipayNotify.verify(params)) {
            parkingOrderFeign.notify(map);
        }
        return "success";
    }

    /**
     * alipay退款服务
     *
     * @param alipayRefund
     * @return
     * @throws AlipayApiException
     * @throws IllegalAccessException
     */
    public AlipayTradeRefundResponse aliRefund(AlipayRefund alipayRefund) {
        AlipayClient client = new DefaultAlipayClient(requestProperty.get("aliPayGateUrl"), requestProperty.get("aliAppId"),
                requestProperty.get("aliPrivateSign"), requestProperty.get("format"), requestProperty.get("charset"),
                requestProperty.get("aliPublicKeys"), requestProperty.get("aliSignType"));
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //将传来的退款参数转成json放入阿里sdk的request中
        request.setBizContent(JSONObject.toJSONString(alipayRefund));
        AlipayTradeRefundResponse response;
        try {
            response = client.execute(request);
        } catch (AlipayApiException e) {
            //处理访问alipay异常时，返回错误提示的response
            response = new AlipayTradeRefundResponse();
            response.setCode("500");
            response.setMsg("退款访问alipay异常");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * alipay账单服务
     *
     * @param date 如2019-05-11
     */
    public String getAliPayBill(String date) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(requestProperty.get("aliPayGateUrl"), requestProperty.get("aliAppId"),
                requestProperty.get("aliPrivateSign"), requestProperty.get("format"), requestProperty.get("charset"),
                requestProperty.get("aliPublicKeys"), requestProperty.get("aliSignType"));
        //创建API对应的request类
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        //设置业务参数
        request.setBizContent("{" +
                "    \"bill_type\":\"trade\"," +
                "    \"bill_date\":\"" + date + "\"}");
        //通过alipayClient调用API，获得对应的response类
        AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //将接口返回的对账单下载地址传入urlStr
        String urlStr = response.getBillDownloadUrl();
        //指定希望保存的文件路径
        String filePath = "/Users/panshize/yuncitys/fund_bill_" + date + ".zip";
        URL url;
        HttpURLConnection httpUrlConnection = null;
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(5 * 1000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Charsert", requestProperty.get("charset"));
            httpUrlConnection.connect();
            fis = httpUrlConnection.getInputStream();
            byte[] temp = new byte[1024];
            int b;
            fos = new FileOutputStream(new File(filePath));
            while ((b = fis.read(temp)) != -1) {
                fos.write(temp, 0, b);
                fos.flush();
            }
            String billAndCount = openZip(filePath);
            String[] billAndCountArray =  billAndCount.split("\n");
            String[] countName = null;
            String[] detailName = null;
            JSONArray detailArray = new JSONArray();
            JSONObject all = new JSONObject();

            //汇总标识
            boolean countFlag = false;
            //明细标识
            boolean detailFlag = false;
            //头信息标识
            boolean titleFlag = false;

            for(int i = 0;i<billAndCountArray.length;i++){
                String billOrCount = billAndCountArray[i].replace("\t","").replace("\r","");
                if(billOrCount.contains("业务汇总列表-")){
                    countFlag = true;
                    titleFlag = true;
                    continue;
                }else if(billOrCount.contains("汇总列表结束-")){
                    countFlag = false;
                }else if(billOrCount.contains("业务明细列表-")){
                    detailFlag = true;
                    titleFlag = true;
                    continue;
                }else if(billOrCount.contains("明细列表结束-")){
                    detailFlag = false;
                }
                if (countFlag){
                    String[] count = billOrCount.split(",");
                    if(titleFlag==true){
                        countName = count.clone();
                    }else{
                        if(countName != null){
                            JSONObject countData = new JSONObject();
                            for (int j = 0;j<count.length;j++){
                                countData.put(countName[j],count[j]);
                            }
                            all.put("count",countData);
                        }
                    }
                }else if (detailFlag){
                    String[] detail = billOrCount.split(",");
                    if(titleFlag==true){
                        detailName = detail.clone();
                    }else{
                        if(detailName != null){
                            JSONObject detailData = new JSONObject();
                            for (int j = 0;j<detail.length;j++){
                                detailData.put(detailName[j],detail[j]);
                            }
                            detailArray.add(detailData);
                        }
                    }
                }
                titleFlag = false;
            }
            all.put("billdetail",detailArray);
            return all.toJSONString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (fos != null) fos.close();
                if (httpUrlConnection != null) httpUrlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String openZip(String path) throws IOException{
        File f = new File(path);
        if ((!f.exists()) && (f.length() <= 0)) {
            throw new RuntimeException("要解压的文件不存在!");
        }
        //一定要加上编码，之前解压另外一个文件，没有加上编码导致不能解压
        ZipFile zipFile = new ZipFile(f, "GBK");
        // 输出的绝对位置
        Enumeration<ZipEntry> e = zipFile.getEntries();
        StringBuffer buffer = new StringBuffer();
        BufferedInputStream bis = null;
        InputStream is = null;
        while (e.hasMoreElements()) {
            org.apache.tools.zip.ZipEntry zipEnt = e.nextElement();
            if (zipEnt.isDirectory()) { //目录
                continue;
            } else {
                // 读写文件
                is = zipFile.getInputStream(zipEnt);
                bis = new BufferedInputStream(is);

                byte[] buff = new byte[1024];

                while (bis.read(buff) != -1) {
                    String str = new String(buff,"GBK");
                    buffer.append(str);
                }
            }
        }

        bis.close();
        is.close();
        zipFile.close();
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        AliPayServer aliPayServer = new AliPayServer();
        aliPayServer.openZip("/Users/panshize/yuncitys/fund_bill_2019-05-11.zip");
    }

}
