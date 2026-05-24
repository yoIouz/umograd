package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.ParentDifficultySettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentDifficultySettingRepository extends JpaRepository<ParentDifficultySettingEntity, Long> {

    Optional<ParentDifficultySettingEntity> findByParentIdAndChildId(Long parentId, Long childId);

    Optional<ParentDifficultySettingEntity> findByChildId(Long childId);
}
