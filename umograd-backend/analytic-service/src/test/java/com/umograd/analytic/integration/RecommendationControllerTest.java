package com.umograd.analytic.integration;

import com.umograd.analytic.entity.task.TaskJpaEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.enums.Difficulty;
import com.umograd.analytic.repository.task.TaskRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = com.umograd.analytic.AnalyticServiceApplication.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskResultRepository taskResultRepository;

    @BeforeEach
    void setUp() {
        taskResultRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    void shouldReturnDefaultEasyRecommendationWhenNoDataExists() throws Exception {
        Long childId = 999L;

        mockMvc.perform(get("/api/v1/analytics/recommendation/{childId}", childId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedDifficulty").value("EASY"))
                .andExpect(jsonPath("$.message").value("Начните с простого уровня заданий."));
    }

    @Test
    void shouldReturnMediumRecommendationOnHighPerformance() throws Exception {
        Long childId = 1L;

        TaskJpaEntity task = new TaskJpaEntity();
        task.setDifficulty(Difficulty.EASY);
        TaskJpaEntity savedTask = taskRepository.save(task);

        TaskResultEntity result = new TaskResultEntity();
        result.setChildId(childId);
        result.setTaskId(savedTask.getId());
        result.setScore(100);
        result.setStatus("DONE");
        result.setStartedAt(LocalDateTime.now().minusSeconds(20));
        result.setFinishedAt(LocalDateTime.now());
        taskResultRepository.save(result);

        mockMvc.perform(get("/api/v1/analytics/recommendation/{childId}", childId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedDifficulty").value("MEDIUM"))
                .andExpect(jsonPath("$.message").value("Отличный результат! Скорость мышления на высоте. Уровень сложности повышен."));
    }
}

