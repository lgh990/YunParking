package com.smart.iot.parking.http;


import javax.net.ssl.X509TrustManager;
import java.parking.cert.CertificateException;
import java.parking.cert.X509Certificate;

/**
 *
 * @author Administrator
 *
 */
public class TrustAnyTrustManager implements X509TrustManager {

	@Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	@Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	@Override
    public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
