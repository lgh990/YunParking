package com.smart.iot.pay.util;

import org.apache.commons.codec.binary.Base64;

import java.util.Map;


/**
 * @author zeming.fan@swiftpass.cn
 *
 */
public class SignUtil {
    //private final static Logger log = LoggerFactory.getLogger(SignUtil.class);

	//请求时根据不同签名方式去生成不同的sign
    public static String getSign(String signType,String preStr){
    	if("RSA_1_256".equals(signType)){
        	try {
        		return SignUtil.sign(preStr,"RSA_1_256","MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC0DdCHqGPqPtLQPH+66OKAcodB+CR2jIY4ypb4n4UIzWdCqT/pjAWla1/HbACP4Fk6Ys5soyreIV30Ia/gA/lM92eYwKiwdVSkXVOoAg6qGcwS2U2OBejBj95rHGJOrKDy6P5BVBFfz1+Xse+yLR2hdzwT5+vl74ZWHY43Wwwc1KOja/bc4f91NKwS/abJ2AV1hX3Q0CPB7sj8MG+FiYnmiCz7ojsdLnJZl6ZCpW6ZRjOfNv3AuGjvGJNpdgY2/ov3rEH4kPSaYrtWo7tiqGy6pqyeeH/VKhzhay2+KodnxS+qHAcBPqtb3Kc2j6qHEdT57brKK5ijmPC8RTAneKC/AgMBAAECggEAXEf8SZr3YpZCS/HF8VDszbJlOl1oqs3I+RB0Igb4ExPbSaIfHJ7kQezvUPE8tIDimncLn0hdby2gzr/5ORIwUs82bGbJzm7EK92dQ62SQIIqQrrW8eR7b8FB2JnalMILV5Qsw+THZtiNAN10oVhYKGpJA1bJY7bInhhVzG9AnQFMhw1eXDEB9M4/nHMElz53zFPX/tdbYOZd0Zz3umfxKrch1mYFeoXxFnh/Hbn3urex6SLtCblQEsXt7/oQLC87hSwTke3Sizi2Bp3qSOpAMWMVprrWMO34PqJz91/UowxhPSMltTBAphmJL5PCclEbtLQSNKkrfjaYe1qJ5PknMQKBgQD25vGBeu4HzlH9Pfp7nd57mrcmW2kivmhY1FxNphLVgcR5e7+4w7fOGwdecDtzeJZyhY8gRoLFNV2JqhTVJp8PLbqiru+B+1oM4MRms52TIYCcqFkK0uJ42JN2PL9tTCw+BAZKZbU+FfOherkCGj+98VEq92zQxASSUUzctTTa6QKBgQC6sEjgk0rEnKIIqXFeQ70AEP93tyKuymVgxJE6UelKSgK0ScPq0FVcfAL69PwTKSqEbxwVsF2T9qg+3OH03AhkBftXdlARpPYm0T1pcEJ4n95ngP3FPCv/ACvwGrwgY41jMrzPTPd11kaubZieTZdKIzRv3j5HxnFrMxa2Dr4FZwKBgCsRjf8wGmVyMAskzJTTrQOSsXAXcfeFj14vbQp+dbqYsFUgqQjp2x4wyNBIc5Zo6pV/hr/yjM0pye9Bo/CqlARpzayjJ1RdLBAjBR+FJW+M4IpzM1UYBDlgThjJ7/p41x7aRN6lb0AtRBpe9fbP7V6MwcPk9S5Vgxai5+14DohBAoGAO4QJp7atRmLc8T/7iV60+TlyCBtVLj4N9byUw+/rHKjdldZSZCCIphRO6uyBFCayjEfTv0JbgtlGp8vJKUOgVSrnBuoL+qQJaTSZjGyidEwbHgosXN2i9Qd7+m67fOPf5jj9omTaGzH1lJQR7J211ofEpslGN6GW9OLzoZSvlY8CgYAFq9NdjyOHqfvtNzl42RjBLCREL16jP0NVx41SRBuIO6oA82y5MrqN3JURgnZnazA9RIR6xqhtjbnnmgmGtL/9Sh1fbHkKMlgASM12S4CasNqVJW33+3G73yYscRZnhES66hy/mpspGuwBfy7QoMMwkyfFr8znCKJg3jIljeZj7w==");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }else{
        	return MD5.sign(preStr, "&key=" + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtA3Qh6hj6j7S0Dx/uujigHKHQfgkdoyGOMqW+J+FCM1nQqk/6YwFpWtfx2wAj+BZOmLObKMq3iFd9CGv4AP5TPdnmMCosHVUpF1TqAIOqhnMEtlNjgXowY/eaxxiTqyg8uj+QVQRX89fl7Hvsi0doXc8E+fr5e+GVh2ON1sMHNSjo2v23OH/dTSsEv2mydgFdYV90NAjwe7I/DBvhYmJ5ogs+6I7HS5yWZemQqVumUYznzb9wLho7xiTaXYGNv6L96xB+JD0mmK7VqO7Yqhsuqasnnh/1Soc4WstviqHZ8UvqhwHAT6rW9ynNo+qhxHU+e26yiuYo5jwvEUwJ3igvwIDAQAB", "utf-8");
        }
    	return null;
    }

    //对返回参数的验证签名
    public static boolean verifySign(String sign,String signType,Map<String,String> resultMap) throws Exception{
    	if("RSA_1_256".equals(signType)){
    		Map<String,String> Reparams = SignUtils.paraFilter(resultMap);
            StringBuilder Rebuf = new StringBuilder((Reparams.size() +1) * 10);
            SignUtils.buildPayParams(Rebuf,Reparams,false);
            String RepreStr = Rebuf.toString();
            if(SignUtil.verifySign(RepreStr,sign, "RSA_1_256", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqNxzebovJ6R+LF0jFyJD4vgdvj+Apmb5h+pW3T0EtDzWZAr7tyiSAtNedYvRjJCqN5cYw0rIwGMZFbD3lQHbJGC+IvpqXwPB8AWqRAwItI82fo2+AyHkq11yE27IgOjSrKofgg3GWJ6SSQonYuXZ0c09chXXiZPKYe0zRbvq83kAVsYDu1sMwi8mfiVff6CIALsehs1MOjmdLW40N1CicVmJaWuh2yee+sj1/0xMOlV1LyJq63hShBD7T93qpGbHoNkpdz+BFc2byrhv1idbB4DRbUiKynzj3FX2Nz8Dv9TFQv8p2Z8dIOst890atv3P8DO7a9FI8I1reLvFDdyPawIDAQAB")){
            	return true;
            }
    	}else if("MD5".equals(signType)){
    		if(SignUtils.checkParam(resultMap, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnmpxXjTJVhVXr2rPs9uBfWhAH9cLhBT+KJ5UiLUd0uxrPVec8WCQyZPQnnL0JOgQSBr0rWLcGVxGAdQUt748DfDGn6G/fhdFGJaHwCRfqxM7ymJ/xPm3UQNJeCF1qjDffdRilYwP4FSyiI+T8eAIeSABUTBDx7kkwvkbVi14znZMmDpLx6QNZlAclAZdHemqB/sSSsgfB7yCBJ4K0Fv4uK4qU3EQeOxhXLkitkhPm3uL5O3jgf89B1ZFpiCpI5qyysmPzJuZbvIBfa9OkVeHqINTPCWYCdx3ZD+k9+H5hoXKFDXddvxHptXrPU3APiBcDkgViYbfnFxWjYwaDQDkYQIDAQAB")){
    			return true;
    		}
    	}
    	return false;
    }
	public static boolean verifySign(String preStr,String sign,String signType, String platPublicKey) throws Exception {
		// 调用这个函数前需要先判断是MD5还是RSA
		// 商户的验签函数要同时支持MD5和RSA
		RSAUtil.SignatureSuite suite = null;

		if ("RSA_1_1".equals(signType)) {
			suite = RSAUtil.SignatureSuite.SHA1;
		} else if ("RSA_1_256".equals(signType)) {
			suite = RSAUtil.SignatureSuite.SHA256;
		} else {
			throw new Exception("不支持的签名方式");
		}

		boolean result = RSAUtil.verifySign(suite, preStr.getBytes("UTF8"), Base64.decodeBase64(sign.getBytes("UTF8")),
                platPublicKey);

		return result;
    }

    public static String sign(String preStr, String signType, String mchPrivateKey) throws Exception {
		RSAUtil.SignatureSuite suite = null;
		if ("RSA_1_1".equals(signType)) {
			suite = RSAUtil.SignatureSuite.SHA1;
		} else if ("RSA_1_256".equals(signType)) {
			suite = RSAUtil.SignatureSuite.SHA256;
		} else {
			throw new Exception("不支持的签名方式");
		}
        byte[] signBuf = RSAUtil.sign(suite, preStr.getBytes("UTF8"),
                mchPrivateKey);
        return new String(Base64.encodeBase64(signBuf), "UTF8");
    }
}
