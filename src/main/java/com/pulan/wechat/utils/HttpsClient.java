package com.pulan.wechat.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpsClient {
    public void ssl(){
        CloseableHttpClient httpclient = null;
        FileInputStream instream = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File("E:\\ideaworkspace\\wechat\\src\\main\\resources\\keystore.p12"));
            trustStore.load(instream,"lch940906.".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //相信自己的CA和所有自签名的证书
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore,new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpGet httpGet = new HttpGet("http://localhost:8080/wechat/test");
            try {
                CloseableHttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String body = EntityUtils.toString(entity);
                System.out.println(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }
}
