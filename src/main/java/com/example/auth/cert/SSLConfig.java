package com.example.auth.cert;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SSLConfig {

    @PostConstruct
    public void init() {
        try {
            DisableSSLCertValidation.disableSslVerification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
