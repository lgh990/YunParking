package com.yuncitys.smart.oss.controller;

import com.yuncitys.smart.oss.cloud.OSSFactory;
import com.yuncitys.smart.parking.common.exception.base.BusinessException;
import com.yuncitys.smart.parking.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 文件上传
 *
 * @author smart
 */
@RestController
@RequestMapping("/oss")
public class OssController{
	@Autowired
	private OSSFactory ossFactory;
	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	public ObjectRestResponse<String> upload(@RequestParam("file") MultipartFile file,String pathName) throws Exception {
		if (file.isEmpty()) {
			throw new BusinessException("上传文件不能为空");
		}
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		suffix=""+System.currentTimeMillis()+suffix;
		String url = ossFactory.build().uploadSuffix(file.getBytes(), suffix,pathName);
		System.out.println("==============================url:"+url);
		return new ObjectRestResponse<>().data(url);
	}

	public static MultipartFile base64ToMultipart(String base64, int len) {
		base64="data:image/jpeg;base64,"+base64;
		try {
			String[] baseStrs = base64.split(",");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = new byte[0];
			b = decoder.decodeBuffer(baseStrs[1]);

			for(int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}

			return new BASE64DecodedMultipartFile(b, "content-type=multipart/*");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("/imageUpload")
	public ObjectRestResponse<String> imageUpload(String str,String pathName) throws Exception {
		MultipartFile file = base64ToMultipart(str,0);
		if (file.isEmpty()) {
			throw new BusinessException("上传文件不能为空");
		}
		//上传文件
		String suffix =System.currentTimeMillis()+".jpg";
		String url = ossFactory.build().uploadSuffix(file.getBytes(), suffix,pathName);
		System.out.println("==============================url:"+url);
		return new ObjectRestResponse<>().data(url);
	}

	/**
	 * 上传文件
	 */
	@RequestMapping("/uploadIotExper")
	public ObjectRestResponse<String> uploadIotExper(@RequestParam(value="map",required=false) MultipartFile map,String typeCode,@RequestParam(value="scene",required=false) MultipartFile
			scene, @RequestParam(value="white",required=false) MultipartFile white,@RequestParam(value="blue",required=false) MultipartFile blue,
													 @RequestParam(value="sign",required=false) MultipartFile sign) throws Exception {
		Map map1=new HashMap<>();
		if (map !=null && !map.isEmpty() ) {
			String suffix = map.getName()+typeCode+".png";
			String url=ossFactory.build().uploadSuffix(map.getBytes(), suffix);
			map1.put(suffix,url);
		}
		if (scene !=null && !scene.isEmpty() ) {
			String suffix = scene.getName()+typeCode+".png";
			String url=ossFactory.build().uploadSuffix(scene.getBytes(), suffix);
			map1.put(suffix,url);
		}
		if (white !=null && !white.isEmpty() ) {
			String suffix = white.getName()+typeCode+".png";
			String url=ossFactory.build().uploadSuffix(white.getBytes(), suffix);
			map1.put(suffix,url);
		}
		if (blue !=null && !blue.isEmpty() ) {
			String suffix = blue.getName()+typeCode+".png";
			String url=ossFactory.build().uploadSuffix(blue.getBytes(), suffix);
			map1.put(suffix,url);
		}
		if (sign !=null && !sign.isEmpty() ) {
			String suffix = sign.getName()+typeCode+".png";
			String url=ossFactory.build().uploadSuffix(sign.getBytes(), suffix);
			map1.put(suffix,url);
		}
		return new ObjectRestResponse<>().data(map1);
	}

}
