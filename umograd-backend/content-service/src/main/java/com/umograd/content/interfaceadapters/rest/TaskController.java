package com.umograd.content.interfaceadapters.rest;

import com.umograd.content.application.dto.CreateTaskRequest;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.command.*;
import com.umograd.content.application.task.query.*;
import com.umograd.content.infrastructure.persistence.jpa.User;
import com.umograd.content.infrastructure.persistence.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final CreateTaskHandler createTaskHandler;
    private UserRepository userRepository;
    private final ListTasksHandler listTasksHandler;
    private final EditTasksHandler editTasksHandler;
    private final DeleteTaskHandler deleteTaskHandler;
    private final ListTasksForChildHandler listTasksForChildHandler;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MODERATOR')")
    public TaskDto create(@RequestBody CreateTaskRequest req, Authentication auth) {
        String moderator = auth.getName();
        var cmd = new CreateTaskCommand(
                req.title(),
                req.description(),
                moderator,
                req.minAge(),
                req.maxAge(),
                req.difficulty(),
                req.content()
        );
        return createTaskHandler.handle(cmd);
    }

    @GetMapping
    //@PreAuthorize("hasAnyRole('MODERATOR','PARENT')")
    public List<TaskDto> list() {
        return listTasksHandler.handle(new ListTasksQuery());
    }

    @GetMapping("/for-child")
    @PreAuthorize("hasRole('CHILD')")
    public List<TaskDto> listForChild(@RequestParam int age,
                                      @RequestParam(required = false) String difficulty) {
        return listTasksForChildHandler.handle(new ListTasksForChildQuery(age, difficulty));
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id) {
        return listTasksHandler.handle(new ListTasksQuery()).stream()
                .filter(t -> t.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(editTasksHandler.handle(new EditTasksCommand(id, taskDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        deleteTaskHandler.handle(new DeleteTaskCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/block")
    public void blockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setUsername(user.getUsername() + "_BLOCKED");
        userRepository.save(user);
    }

}


