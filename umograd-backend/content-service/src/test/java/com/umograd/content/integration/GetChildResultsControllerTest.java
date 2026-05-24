package com.umograd.content.integration;

import com.umograd.content.application.dto.TaskResultDto;
import com.umograd.content.application.result.query.GetChildResultsHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = com.umograd.content.ContentServiceApplication.class)
public class GetChildResultsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetChildResultsHandler resultsHandler;

    @Test
    @WithMockUser(username = "parent-user", roles = "PARENT")
    void shouldReturnChildResultsSuccessfullyWhenUserIsParent() throws Exception {
        Long childId = 42L;
        TaskResultDto resultDto = new TaskResultDto(
                1L, 2L, childId, "DONE", 100, "2026-05-15T00:00:00Z", "2026-05-15T00:00:10Z"
        );

        when(resultsHandler.handle(any())).thenReturn(List.of(resultDto));

        mockMvc.perform(get("/task-results/children/{childId}", childId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].childId").value(childId))
                .andExpect(jsonPath("$[0].status").value("DONE"));
    }

    @Test
    @WithMockUser(username = "regular-user", roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotParent() throws Exception {
        Long childId = 42L;

        mockMvc.perform(get("/task-results/children/{childId}", childId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        Long childId = 42L;

        mockMvc.perform(get("/task-results/children/{childId}", childId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
