package com.umograd.analytic.repository.task;

import com.umograd.analytic.entity.task.TaskJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskJpaEntity, Long> {

    @NonNull
    @EntityGraph(attributePaths = "results")
    List<TaskJpaEntity> findAll();
}
