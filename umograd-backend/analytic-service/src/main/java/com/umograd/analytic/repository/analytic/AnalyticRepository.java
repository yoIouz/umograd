package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.task.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticRepository extends JpaRepository<TaskResultEntity, Long> {
}
