package com.umograd.content.unit;

import com.umograd.content.application.dto.TaskResultDto;
import com.umograd.content.application.result.query.GetChildResultsHandler;
import com.umograd.content.application.result.query.GetChildResultsQuery;
import com.umograd.content.domain.result.TaskResult;
import com.umograd.content.domain.result.TaskResultId;
import com.umograd.content.domain.result.TaskResultRepository;
import com.umograd.content.domain.result.TaskResultStatus;
import com.umograd.content.domain.task.TaskId;
import com.umograd.content.domain.task.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetChildResultsHandlerTest {

    @Mock
    private TaskResultRepository repository;

    @InjectMocks
    private GetChildResultsHandler handler;

    @Test
    void shouldReturnChildResults(){
        Long childId = 42L;
        GetChildResultsQuery query = new GetChildResultsQuery(childId);

        TaskResult entity = mock(TaskResult.class);

        TaskResultId mockId = mock(TaskResultId.class);
        when(mockId.value()).thenReturn(new TaskResultId(1L).value());
        when(entity.getId()).thenReturn(mockId);

        TaskId mockTaskId = mock(TaskId.class);
        when(mockTaskId.value()).thenReturn(new TaskId(1L).value());
        when(entity.getTaskId()).thenReturn(mockTaskId);

        when(entity.getChildId()).thenReturn(childId);

        TaskResultStatus mockStatus = TaskResultStatus.DONE;
        when(entity.getStatus()).thenReturn(mockStatus);

        when(entity.getScore()).thenReturn(100);

        LocalDateTime now = LocalDateTime.now();
        when(entity.getFinishedAt()).thenReturn(now);
        when(entity.getStartedAt()).thenReturn(now);

        when(repository.findByChildId(childId)).thenReturn(List.of(entity));

        List<TaskResultDto> result = handler.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());

        TaskResultDto dto = result.get(0);
        assertEquals("1", dto.id().toString());
        assertEquals("1", dto.taskId().toString());
        assertEquals(childId, dto.childId());
        assertEquals("DONE", dto.status());
        assertEquals(100, dto.score());
        assertEquals(now.toString(), dto.finishedAt());
        assertEquals(now.toString(), dto.startedAt());

        verify(repository, times(1)).findByChildId(childId);

    }
}
