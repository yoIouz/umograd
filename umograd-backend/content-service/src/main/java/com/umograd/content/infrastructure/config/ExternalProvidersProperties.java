package com.umograd.content.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "external.providers")
public class ExternalProvidersProperties {

    private Map<String, ProviderConfig> providers;

    public Map<String, ProviderConfig> getProviders() {
        return providers;
    }

    public void setProviders(Map<String, ProviderConfig> providers) {
        this.providers = providers;
    }

    public static class ProviderConfig {
        private String baseUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("ExternalProvidersProperties loaded: " + providers);
    }
}
