package com.yuncitys.smart.parking.auth.module.generator.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.joda.time.DateTime;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @version 2016年12月19日 下午11:40:24
 */
public class GeneratorUtils {

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        // maven依赖
        templates.add("template/pom.xml.vm");

        templates.add("template/application.yml.vm");
        templates.add("template/bootstrap.yml.vm");
        // docker相关
        templates.add("template/Dockerfile.txt.vm");
        templates.add("template/wait-for-it.sh.vm");

        templates.add("template/Bootstrap.java.vm");
        templates.add("template/WebConfiguration.java.vm");
        // 分布式事物相关模板
        templates.add("template/TxFeignConfiguration.java.vm");
        templates.add("template/TxConfiguration.java.vm");
        templates.add("template/TxTransactionInterceptor.java.vm");
        templates.add("template/TxManagerHttpRequestBiz.java.vm");
        templates.add("template/TxManagerTxUrlBiz.java.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static void buildProject(Map<String, Object> config, ZipOutputStream zip) {

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        config.put("datetime", DateTime.now().toString());
        // 决定服务名\maven模块名\主文件夹名
        VelocityContext context = new VelocityContext(config);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            if (!Boolean.valueOf(config.get("tx").toString()) && template.contains("Tx")) {
                continue;
            }
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, config)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("工程渲染失败，服务名：" + config.get("clientId"), e);
            }
        }
    }


    /**
     * 获取文件名
     */
    public static String getFileName(String template, Map<String, Object> config) {
        String moduleName = config.get("clientId").toString();
        String packageName = config.get("package").toString();
        String mainPath = moduleName + File.separator;
        String packagePath = mainPath + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String ymlPath = mainPath + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        String dockerPath = mainPath + "src" + File.separator + "main" + File.separator + "docker" + File.separator;

        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        if (template.contains("application") || template.contains("bootstrap")) {
            return ymlPath + getFileName(template);
        }
        if (template.contains("Dockerfile") || template.contains("wait-for")) {
            return dockerPath + getFileName(template);
        }
        if (template.contains("pom")) {
            return mainPath + getFileName(template);
        }
        if (template.contains("WebConfiguration")) {
            return packagePath + "config" + File.separator + getFileName(template);
        }
        if (template.contains("Bootstrap")) {
            return packagePath + getFileName(template);
        }
        if (template.contains("TxFeignConfiguration") || template.contains("TxConfiguration")) {
            return packagePath + "config" + File.separator + getFileName(template);
        }
        if (template.contains("TxTransactionInterceptor")) {
            return packagePath + "interceptor" + File.separator + getFileName(template);
        }
        if (template.contains("TxManagerHttpRequestBiz") || template.contains("TxManagerTxUrlBiz")) {
            return packagePath + "biz" + File.separator + getFileName(template);
        }
        return null;
    }

    private static String getFileName(String template) {
        return template.substring("template".length() + 1, template.length() - ".vm".length());
    }

    public static void main(String[] args) throws FileNotFoundException {
        //封装模板数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("package", "com.yuncitys.smart.parking.generate");
        map.put("author", "老");
        map.put("email", "A");
        map.put("description", "自动化工程");
        // 决定服务名\maven模块名\主文件夹名
        map.put("clientId", "smart-demo-generator");
        map.put("clientSecret", "1266482");
        map.put("zipkin", true);
        map.put("tx", true);
        File file = new File("./test.zip");
        FileOutputStream outputStream = new FileOutputStream(file);
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        buildProject(map, zip);
        file.getPath();
    }
}
