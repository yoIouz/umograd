package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.ChildAchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildAchievementRepository extends JpaRepository<ChildAchievementEntity, Long> {

    boolean existsByChildIdAndAchievementId(Long childId, Long achievementId);

    List<ChildAchievementEntity> findAllByChildId(Long childId);
}
