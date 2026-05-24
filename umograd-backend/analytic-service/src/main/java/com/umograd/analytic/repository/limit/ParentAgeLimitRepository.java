package com.umograd.analytic.repository.limit;

import com.umograd.analytic.entity.limit.ParentAgeLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentAgeLimitRepository extends JpaRepository<ParentAgeLimitEntity, Long> {

    List<ParentAgeLimitEntity> findAllByParentId(Long parentId);

    Optional<ParentAgeLimitEntity> findByParentIdAndAge(Long parentId, int age);
}
