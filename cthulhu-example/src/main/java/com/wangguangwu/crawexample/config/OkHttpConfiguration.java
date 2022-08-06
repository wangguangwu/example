package com.wangguangwu.crawexample.config;

import com.wangguangwu.crawexample.properties.HttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
@Configuration
public class OkHttpConfiguration {

    @Resource
    private HttpProperties httpProperties;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient
                .Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                // 是否开启缓存
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .connectTimeout(httpProperties.getConnnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpProperties.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpProperties.getWriteTimeout(), TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // Ignore
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // Ignore
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(
                httpProperties.getMaxIdleConnections(),
                httpProperties.getKeepAliveDuration(),
                TimeUnit.SECONDS
        );
    }

}
