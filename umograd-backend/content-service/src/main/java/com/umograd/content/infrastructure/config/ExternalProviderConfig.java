package com.umograd.content.infrastructure.config;

import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.infrastructure.external.OpenTdbContentProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class ExternalProviderConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Map<String, ContentProvider> contentProviders(OpenTdbContentProvider openTdbContentProvider) {
        Map<String, ContentProvider> map = Map.of("opentdb", openTdbContentProvider);
        System.out.println("Registered providers: " + map.keySet());
        return map;
    }
}
//    @Bean
//    public Map<String, ContentProvider> contentProviders(RestTemplate restTemplate,
//                                                         ExternalProvidersProperties props) {
//        Map<String, ContentProvider> map = new HashMap<>();
//        if (props.getProviders() == null) {
//            System.out.println("⚠️ Providers map is NULL — конфиг не подхватился");
//        } else {
//            System.out.println("Loaded providers: " + props.getProviders().keySet());
//        }
//        if (props.getProviders() != null) {
//            props.getProviders().forEach((name, cfg) -> {
//                map.put(name, new HttpContentProvider(restTemplate, cfg.getBaseUrl()));
//            });
//        }
//        return map;
//    }
