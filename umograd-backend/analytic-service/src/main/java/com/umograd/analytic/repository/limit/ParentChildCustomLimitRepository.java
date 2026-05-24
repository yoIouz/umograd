package com.umograd.analytic.repository.limit;

import com.umograd.analytic.entity.limit.ParentChildCustomLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentChildCustomLimitRepository extends JpaRepository<ParentChildCustomLimitEntity, Long> {

    Optional<ParentChildCustomLimitEntity> findByParentIdAndChildId(Long parentId, Long childId);
    
    List<ParentChildCustomLimitEntity> findAllByParentId(Long parentId);
}
