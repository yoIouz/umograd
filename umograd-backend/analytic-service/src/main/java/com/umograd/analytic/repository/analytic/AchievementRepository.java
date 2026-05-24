package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Long> {
}
