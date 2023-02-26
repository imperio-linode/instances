package com.bntech.imperio.instances.service.impl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
@Slf4j
public class GatewayClients {

    private final String keyStoreLocation;
    private final String keyStorePassword;
    private final String keyStoreType;
    private final String trustStoreLocation;
    private final String trustStorePassword;

    public GatewayClients(@Value("${infrastructure.tls.key-store}") String keyStoreLocation,
                          @Value("${infrastructure.tls.key-store-password}") String keyStorePassword,
                          @Value("${infrastructure.tls.key-store-type}") String keyStoreType,
                          @Value("${infrastructure.tls.ca}") String trustStoreLocation,
                          @Value("${infrastructure.tls.ca-password}") String trustStorePassword) {
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePassword = keyStorePassword;
        this.keyStoreType = keyStoreType;
        this.trustStoreLocation = trustStoreLocation;
        this.trustStorePassword = trustStorePassword;
    }

    @Bean
    public HttpClient tlsClient() {
        return HttpClient.create()
        .secure(spec -> {
            try {
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(new FileInputStream(ResourceUtils.getFile(keyStoreLocation)), keyStorePassword.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

                KeyStore trustStore = KeyStore.getInstance(keyStoreType);
                trustStore.load(new FileInputStream((ResourceUtils.getFile(trustStoreLocation))), trustStorePassword.toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);

                SslContext context = SslContextBuilder.forClient()
                        .keyManager(keyManagerFactory)
                        .trustManager(trustManagerFactory)
                        .build();

                spec.sslContext(context);
            } catch (Exception e) {
                log.warn("Unable to set SSL Context", e);
            }
        });
    }
}
