package com.example.onlinebanking.config.security;

import com.example.onlinebanking.exceptions.InternalServerException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Slf4j
@Configuration
public class JwtConfiguration {

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${app.security.jwt.keystore-alias}")
    private String keyAlias;

    @Value("${app.security.jwt.keystore-passphrase}")
    private String privateKeyPassphrase;

    @Bean
    public KeyStore keyStore() {
        try {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            var resourceAsStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException
                 | CertificateException e) {
            log.error("Unable to load keystore: {}", keyStorePath, e);
        }

        throw new InternalServerException("Unable to load keystore: " + keyStorePath);
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());

            if (key instanceof RSAPrivateKey rsaPrivateKey) {
                return rsaPrivateKey;
            }
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            log.error("Unable to load private key from keystore: {}", keyStorePath, e);
        }

        throw new InternalServerException("Unable to load private key from keystore: "
                + keyStorePath);
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();

            if (publicKey instanceof RSAPublicKey rsaPublicKey) {
                return rsaPublicKey;
            }
        } catch (KeyStoreException e) {
            log.error("Unable to load public key from keystore: {}", keyStorePath, e);
        }

        throw new InternalServerException("Unable to load public key from keystore: "
                + keyStorePath);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}
