package com.umograd.analytic.integration;

import com.umograd.analytic.entity.AchievementEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.repository.analytic.AchievementRepository;
import com.umograd.analytic.repository.analytic.ChildAchievementRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = com.umograd.analytic.AnalyticServiceApplication.class)
class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private ChildAchievementRepository childAchievementRepository;

    @Autowired
    private TaskResultRepository taskResultRepository;

    @BeforeEach
    void setUp() {
        childAchievementRepository.deleteAll();
        taskResultRepository.deleteAll();
        achievementRepository.deleteAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAchievementsExist() throws Exception {
        Long childId = 1L;

        mockMvc.perform(post("/api/v1/analytics/achievements/process/{childId}", childId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldGrantAchievementWhenConditionsAreMet() throws Exception {
        Long childId = 4L;

        AchievementEntity achievement = new AchievementEntity();
        achievement.setName("test");
        achievement.setDescription("Тестовая награда");
        achievement.setConditionExpression("#results.size() >= #targetValue");
        achievement.setConditionValue(1);
        achievementRepository.save(achievement);

        TaskResultEntity result = new TaskResultEntity();
        result.setChildId(childId);
        result.setTaskId(999L);
        result.setStatus("DONE");
        result.setScore(100);
        result.setStartedAt(LocalDateTime.now().minusSeconds(10));
        result.setFinishedAt(LocalDateTime.now());
        taskResultRepository.save(result);

        mockMvc.perform(post("/api/v1/analytics/achievements/process/{childId}", childId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("test")); // Дополнительно проверяем имя выданной награды
    }
}
