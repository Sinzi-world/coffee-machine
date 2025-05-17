package com.example.coffemachine.config;

import org.springframework.core.io.Resource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
@ConfigurationProperties(prefix = "digital-signature")
@Data
public class DigitalSignatureConfig {
    private Resource privateKey;
    private Resource publicKey;

    @Bean
    public PrivateKey privateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKey.getURI()));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(publicKey.getURI()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}