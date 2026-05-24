package com.umograd.content.infrastructure.config;

import com.umograd.content.domain.result.TaskResultRepository;
import com.umograd.content.domain.task.TaskRepository;
import com.umograd.content.infrastructure.persistence.jpa.SpringTaskRepository;
import com.umograd.content.infrastructure.persistence.jpa.SpringTaskResultRepository;
import com.umograd.content.infrastructure.repository.TaskRepositoryImpl;
import com.umograd.content.infrastructure.repository.TaskResultRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public TaskRepository taskRepository(SpringTaskRepository jpaRepo) {
        return new TaskRepositoryImpl(jpaRepo);
    }

    @Bean
    public TaskResultRepository taskResultRepository(SpringTaskResultRepository jpaRepo) {
        return new TaskResultRepositoryImpl(jpaRepo);
    }
}
