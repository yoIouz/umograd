package com.umograd.content.infrastructure.external;

import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.domain.external.ExternalQuestionDto;
import com.umograd.content.domain.external.ExternalTaskContentDto;
import com.umograd.content.domain.external.ExternalTaskDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component("opentdb")
public class OpenTdbContentProvider implements ContentProvider {

    private final RestTemplate restTemplate;

    public OpenTdbContentProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ExternalTaskDto> fetchTasks(String topic, int limit) {
        int categoryId = OpenTdbCategories.CATEGORY_MAP.getOrDefault(topic, 9);

        String url = "https://opentdb.com/api.php?amount=" + limit +
                "&category=" + categoryId +
                "&type=multiple";

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        @SuppressWarnings("all")
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        if (results == null || results.isEmpty()) {
            return List.of();
        }

        return results.stream().map(q -> {
            String questionText = (String) q.get("question");
            String correct = (String) q.get("correct_answer");
            @SuppressWarnings("unchecked")
            List<String> incorrect = (List<String>) q.get("incorrect_answers");
            String difficulty = ((String) q.get("difficulty")).toUpperCase();

            ExternalQuestionDto questionObj = new ExternalQuestionDto(
                    "MULTIPLE_CHOICE",
                    questionText,
                    mergeAnswers(correct, incorrect),
                    correct,
                    null
            );

            String sourceId = "opentdb-" + categoryId + "-" + System.identityHashCode(q);

            return new ExternalTaskDto(
                    sourceId,
                    "Trivia: " + topic,
                    "Вопрос из категории " + topic,
                    10, 99,
                    difficulty,
                    List.of(topic),
                    new ExternalTaskContentDto(List.of(questionObj)) // Теперь это список
            );
        }).toList();
    }

    private List<String> mergeAnswers(String correct, List<String> incorrect) {
        List<String> all = new java.util.ArrayList<>(incorrect);
        all.add(correct);
        java.util.Collections.shuffle(all);
        return all;
    }
}

