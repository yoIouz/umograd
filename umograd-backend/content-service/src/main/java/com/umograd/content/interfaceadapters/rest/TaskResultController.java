package com.umograd.content.interfaceadapters.rest;

import com.umograd.content.application.dto.TaskResultDto;
import com.umograd.content.application.result.command.FinishTaskCommand;
import com.umograd.content.application.result.command.FinishTaskHandler;
import com.umograd.content.application.result.command.StartTaskCommand;
import com.umograd.content.application.result.command.StartTaskHandler;
import com.umograd.content.application.result.query.GetChildResultsHandler;
import com.umograd.content.application.result.query.GetChildResultsQuery;
import com.umograd.content.security.AuthenticationHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-results")
public class TaskResultController {

    private final StartTaskHandler startHandler;
    private final FinishTaskHandler finishHandler;
    private final GetChildResultsHandler resultsHandler;

    public TaskResultController(StartTaskHandler startHandler, FinishTaskHandler finishHandler, GetChildResultsHandler resultsHandler) {
        this.startHandler = startHandler;
        this.finishHandler = finishHandler;
        this.resultsHandler = resultsHandler;
    }

    @PostMapping("/{taskId}/start")
    @PreAuthorize("hasRole('CHILD')")
    public TaskResultDto start(@PathVariable Long taskId, @RequestParam Long childId) {
        return startHandler.handle(new StartTaskCommand(taskId, childId));
    }

    @PutMapping("/{taskResultId}/finish")
    @PreAuthorize("hasRole('CHILD')")
    public TaskResultDto finish(@PathVariable Long taskResultId,
                                @RequestParam(required = false) Integer score,
                                @RequestParam(required = false, defaultValue = "0") Integer attempts,
                                @RequestParam(required = false, defaultValue = "false") boolean allowZero) {
        Long childId = AuthenticationHolder.getUserId();
        return finishHandler.handle(new FinishTaskCommand(taskResultId, score, attempts, allowZero, childId));
    }

    @GetMapping("/children/{childId}")
    @PreAuthorize("hasRole('PARENT')")
    public List<TaskResultDto> byChild(@PathVariable Long childId) {
        return resultsHandler.handle(new GetChildResultsQuery(childId));
    }
}
