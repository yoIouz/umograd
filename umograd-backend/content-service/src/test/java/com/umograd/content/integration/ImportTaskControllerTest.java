package com.umograd.content.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.command.ImportTasksHandler;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = com.umograd.content.ContentServiceApplication.class)
class ImportTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ImportTasksHandler importTasksHandler;

    @Test
    @WithMockUser(username = "mod-user", roles = {"MODERATOR", "USER"})
    void shouldSaveImportedTaskSuccessfully() throws Exception {
        TaskContentDto emptyContent = new TaskContentDto(Collections.emptyList());
        TaskDto inputDto = new TaskDto(
                null, "src-001", "Imported Math", "Desc", 6, 12, "EASY",
                null, null, null, emptyContent
        );

        TaskDto expectedDto = new TaskDto(
                1L, "src-001", "Imported Math", "Desc", 6, 12, "EASY",
                "mod-user", null, null, emptyContent
        );

        when(importTasksHandler.saveSingle(any(TaskDto.class), eq("mod-user"))).thenReturn(expectedDto);

        mockMvc.perform(post("/tasks/import/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Imported Math"))
                .andExpect(jsonPath("$.createdBy").value("mod-user"));
    }

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        TaskContentDto emptyContent = new TaskContentDto(Collections.emptyList());
        TaskDto inputDto = new TaskDto(
                null, "src-001", "Imported Math", "Desc", 6, 12, "EASY",
                null, null, null, emptyContent
        );

        mockMvc.perform(post("/tasks/import/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isForbidden());
    }

}
