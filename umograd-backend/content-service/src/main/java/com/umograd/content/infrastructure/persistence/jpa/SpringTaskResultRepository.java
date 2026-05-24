package com.umograd.content.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringTaskResultRepository extends JpaRepository<TaskResultJpaEntity, Long> {
    List<TaskResultJpaEntity> findByChildId(Long childId);
    List<TaskResultJpaEntity> findByTaskId(Long taskId);
}
