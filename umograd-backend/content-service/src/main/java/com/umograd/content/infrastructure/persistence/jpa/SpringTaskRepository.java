package com.umograd.content.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringTaskRepository extends JpaRepository<TaskJpaEntity, Long> {}
