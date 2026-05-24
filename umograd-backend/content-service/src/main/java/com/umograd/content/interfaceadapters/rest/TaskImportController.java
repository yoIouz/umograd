package com.umograd.content.interfaceadapters.rest;

import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.command.ImportTasksCommand;
import com.umograd.content.application.task.command.ImportTasksHandler;
import com.umograd.content.domain.external.ExternalTaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tasks/import")
public class TaskImportController {

    private final ImportTasksHandler importTasksHandler;

    public TaskImportController(ImportTasksHandler importTasksHandler) {
        this.importTasksHandler = importTasksHandler;
    }

    @GetMapping("/preview")
    //@PreAuthorize("hasRole('MODERATOR')")
    public List<ExternalTaskDto> previewFromProvider(@RequestParam String provider,
                                                     @RequestParam String topic,
                                                     @RequestParam(defaultValue = "5") int limit) {
        return importTasksHandler.preview(provider, topic, limit);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasRole('MODERATOR')")
    public TaskDto saveImportedTask(@RequestBody TaskDto dto,
                                    Authentication auth) {
        String moderator = auth.getName(); // "mod" из токена
        return importTasksHandler.saveSingle(dto, moderator);
    }

//    @GetMapping("/debug/auth")
//    public Object debugAuth(Authentication auth) {
//        return auth;
//    }
}
