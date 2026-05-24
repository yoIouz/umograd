package com.umograd.content.unit;

import com.umograd.content.application.dto.QuestionDto;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.command.ImportTasksHandler;
import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.domain.task.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class ImportTaskHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private java.util.Map<String, ContentProvider> providers;

    @InjectMocks
    private ImportTasksHandler handler;

    @Test
    void shouldSuccessfullySaveSingleTaskAndReturnMappedDto() {
        String createdBy = "author-123";
        QuestionDto inputQuestion = new QuestionDto(
                "SINGLE_CHOICE",
                "2+2?",
                List.of("3", "4"),
                "4", "Easy math"
        );
        TaskContentDto inputContent = new TaskContentDto(List.of(inputQuestion));
        TaskDto inputDto = new TaskDto(
                null, "src-001",
                "Math",
                "Desc",
                6,
                12,
                "EASY",
                null, null, null, inputContent
        );

        TaskId savedId = new TaskId(555L);
        List<Question> savedQuestions = List.of(new Question(
                "SINGLE_CHOICE",
                "2+2?",
                List.of("3", "4"),
                "4",
                "Easy math")
        );

        Task savedTaskFromDb = Task.createNew(
                savedId,
                "src-001",
                new TaskTitle("Math"),
                new TaskDescription("Desc"),
                createdBy,
                LocalDateTime.now(),
                new AgeRange(6, 12),
                Difficulty.EASY,
                new TaskContent(savedQuestions)
        );

        when(taskRepository.save(any(Task.class))).thenReturn(savedTaskFromDb);
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        TaskDto resultDto = handler.saveSingle(inputDto, createdBy);

        verify(taskRepository, times(1)).save(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();

        assertNull(capturedTask.id());
        assertEquals("src-001", capturedTask.sourceId());
        assertEquals("Math", capturedTask.title().value());
        assertEquals("Desc", capturedTask.description().value());
        assertEquals(createdBy, capturedTask.createdBy());
        assertNotNull(capturedTask.createdAt());
        assertEquals(6, capturedTask.ageRange().min());
        assertEquals(12, capturedTask.ageRange().max());
        assertEquals(Difficulty.EASY, capturedTask.difficulty());

        assertEquals(1, capturedTask.content().questions().size());
        Question capturedQuestion = capturedTask.content().questions().get(0);
        assertEquals("SINGLE_CHOICE", capturedQuestion.contentType());

        assertNotNull(resultDto);
        assertEquals("555", resultDto.id().toString());
        assertEquals("src-001", resultDto.sourceId());
        assertEquals("Math", resultDto.title());
        assertEquals("EASY", resultDto.difficulty());
        assertEquals(createdBy, resultDto.createdBy());
        assertNotNull(resultDto.content());
        assertEquals(1, resultDto.content().questionDtos().size());
        QuestionDto resultQuestionDto = resultDto.content().questionDtos().get(0);
        assertEquals("2+2?", resultQuestionDto.question());
        assertEquals("4", resultQuestionDto.answer());
    }
}
