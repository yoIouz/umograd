package com.umograd.content.infrastructure.config;

import com.umograd.content.application.result.command.FinishTaskHandler;
import com.umograd.content.application.result.command.StartTaskHandler;
import com.umograd.content.application.result.query.GetChildResultsHandler;
import com.umograd.content.application.task.command.CreateTaskHandler;
import com.umograd.content.application.task.command.ImportTasksHandler;
import com.umograd.content.application.task.query.ListTasksForChildHandler;
import com.umograd.content.application.task.query.ListTasksHandler;
import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.domain.result.TaskResultRepository;
import com.umograd.content.domain.task.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateTaskHandler createTaskHandler(TaskRepository repository) {
        return new CreateTaskHandler(repository);
    }

    @Bean
    public ListTasksHandler listTasksHandler(TaskRepository repository) {
        return new ListTasksHandler(repository);
    }

    @Bean
    public StartTaskHandler startTaskHandler(TaskResultRepository repository) {
        return new StartTaskHandler(repository);
    }

    @Bean
    public FinishTaskHandler finishTaskHandler(TaskResultRepository repository) {
        return new FinishTaskHandler(repository);
    }

    @Bean
    public GetChildResultsHandler getChildResultsHandler(TaskResultRepository repository) {
        return new GetChildResultsHandler(repository);
    }

    @Bean
    public ListTasksForChildHandler listTasksForChildHandler(TaskRepository repository) {
        return new ListTasksForChildHandler(repository);
    }

    @Bean
    public ImportTasksHandler importTasksHandler(TaskRepository repository,
                                                 Map<String, ContentProvider> providers) {
        return new ImportTasksHandler(repository, providers);
    }
}
