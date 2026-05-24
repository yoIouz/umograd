package com.umograd.content.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umograd.content.application.dto.CreateTaskRequest;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.command.CreateTaskHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = com.umograd.content.ContentServiceApplication.class)
class CreateTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTaskHandler createTaskHandler;

    @Test
    @WithMockUser(username = "test-moderator", roles = "MODERATOR")
    void shouldCreateTaskSuccessfullyWhenUserIsModerator() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "Math Title",
                "Description",
                6,
                12,
                "EASY",
                new TaskContentDto(Collections.emptyList())
        );

        TaskDto expectedDto = new TaskDto(
                1L,
                null,
                "Math Title",
                "Description",
                6,
                12,
                "EASY",
                "test-moderator",
                null,
                null,
                new TaskContentDto(Collections.emptyList())
        );

        when(createTaskHandler.handle(any())).thenReturn(expectedDto);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Math Title"))
                .andExpect(jsonPath("$.createdBy").value("test-moderator"));
    }

    @Test
    @WithMockUser(username = "regular-user", roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotModerator() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "Title", "Desc", 5, 10, "HARD", new TaskContentDto(Collections.emptyList())
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "Title", "Desc", 5, 10, "HARD", new TaskContentDto(Collections.emptyList())
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
