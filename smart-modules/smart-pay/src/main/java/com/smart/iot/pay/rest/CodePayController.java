package com.smart.iot.pay.rest;


import com.smart.iot.pay.util.XmlUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smart.iot.pay.util.SignUtil;
import com.smart.iot.pay.util.testUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("codePay")
@Slf4j
@Api(tags = "二维码支付")
public class CodePayController {
    @RequestMapping("payResultSerlet")
    public void pushParkingOrder(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
        try{
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-type", "text/html;charset=UTF-8");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String FEATURE = null;
        try {
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);

            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            dbf.setFeature(FEATURE, false);

            // Disable external DTDs as well
            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            dbf.setFeature(FEATURE, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
            // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
            // (http://cwe.mitre.org/data/definitions/918.html) and denial
            // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

            // remaining parser logic
        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            System.out.println("ParserConfigurationException was thrown. The feature '" +
                    FEATURE + "' is probably not supported by your XML processor.");
        }
            /*catch (SAXException e) {
            	// On Apache, this should be thrown when disallowing DOCTYPE
            	System.out.println("A DOCTYPE was passed into the XML document");
            }
            catch (IOException e) {
            	// XXE that points to a file that doesn't exist
            	System.out.println("IOException occurred, XXE may still possible: " + e.getMessage());
            }*/

        String resString = XmlUtils.parseRequst(req);
        System.out.println("通知内容：" + resString);

        String respString = "fail";
        if (resString != null && !"".equals(resString)) {
            Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");

            String res = XmlUtils.toXml(map);
            String sign_type = map.get("sign_type");
            String reSign = map.get("sign");
            System.out.println("通知内容：" + res);
            if (map.containsKey("sign")) {
                if (SignUtil.verifySign(reSign, sign_type, map)) {
                    String status = map.get("status");
                    if (status != null && "0".equals(status)) {
                        String result_code = map.get("result_code");
                        if (result_code != null && "0".equals(result_code)) {
                            if (testUtil.orderResult == null) {
                                testUtil.orderResult = new HashMap<String, String>();
                            }
                            String out_trade_no = map.get("out_trade_no");
                            testUtil.orderResult.put(out_trade_no, "1");

                        }
                    }
                    respString = "success";
                }
            }
        }
        resp.getWriter().write(respString);
    }catch(Exception e)
    {
        e.printStackTrace();
    }
}
}
