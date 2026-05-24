package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.SystemLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLogEntity, Long> {

    List<SystemLogEntity> findByEventTypeOrderByCreatedAtDesc(String eventType);
}
