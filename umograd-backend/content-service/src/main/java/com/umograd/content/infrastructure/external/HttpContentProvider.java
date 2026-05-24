package com.umograd.content.infrastructure.external;

import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.domain.external.ExternalTaskDto;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class HttpContentProvider implements ContentProvider {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HttpContentProvider(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<ExternalTaskDto> fetchTasks(String topic, int limit) {
        String url = String.format("%s/tasks?topic=%s&limit=%d", baseUrl, topic, limit);
        ExternalTaskDto[] response = restTemplate.getForObject(url, ExternalTaskDto[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}
