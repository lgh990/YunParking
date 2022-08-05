package com.smart.iot.parking.utils;

import java.io.UnsupportedEncodingException;
import java.parking.MessageDigest;
import java.parking.NoSuchAlgorithmException;
import java.util.Random;

public class MD5 {

	private final static String[] hexDigits = {
	      "0", "1", "2", "3", "4", "5", "6", "7",
	      "8", "9", "a", "b", "c", "d", "e", "f"};

	private static String byteArrayToHexString(byte[] b) {
	    StringBuffer resultSb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	      resultSb.append(byteToHexString(b[i]));
	    }
	    return resultSb.toString();
	  }

	  private static String byteToHexString(byte b) {
	    int n = b;
	    if (n < 0) {
            n = 256 + n;
        }
	    int d1 = n / 16;
	    int d2 = n % 16;
	    return hexDigits[d1] + hexDigits[d2];
	  }

	  public static String MD5(String origin) {
	    String resultString = null;

	    try {
	      resultString=new String(origin);
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      resultString=byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
	    }
	    catch (Exception ex) {

	    }
	    return resultString;
	  }

	  public static String encode2hex(String source) {
	   byte[] data = encode2bytes(source);
	   StringBuffer hexString = new StringBuffer();
	   for (int i = 0; i < data.length; i++) {
	    String hex = Integer.toHexString(0xff & data[i]);

	    if (hex.length() == 1) {
	     hexString.append('0');
	    }

	    hexString.append(hex);
	   }

	   return hexString.toString();
	  }

	  public static byte[] encode2bytes(String source) {
	   byte[] result = null;
	   try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.reset();
	    md.update(source.getBytes("UTF-8"));
	    result = md.digest();
	   } catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	   }

	   return result;
	  }

	  public static boolean validate(String unknown , String okHex) {
	   return okHex.equals(encode2hex(unknown));
	  }

	  public static void main(String[] args) {
		System.out.println(MD5.MD5("admin"));
	}

	  public static String genRandomNum(int pwd_len){

	   final int  maxNum = 36;
	   int i;
	   int count = 0;
	   char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
	     'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	     'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	   StringBuffer pwd = new StringBuffer("");
	   Random r = new Random();
	   while(count < pwd_len){

	    i = Math.abs(r.nextInt(maxNum));

	    if (i >= 0 && i < str.length) {
	     pwd.append(str[i]);
	     count ++;
	    }
	   }

	   return pwd.toString();
	  }
}
