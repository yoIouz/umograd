package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.ParentRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRecommendationRepository extends JpaRepository<ParentRecommendationEntity, Long> {

    @Query("SELECT r.taskId FROM ParentRecommendationEntity r WHERE r.childId = :childId AND r.isCompleted = false")
    List<Long> findActiveTaskIdsByChildId(@Param("childId") Long childId);
    
    Optional<ParentRecommendationEntity> findByChildIdAndTaskIdAndIsCompleted(Long childId, Long taskId, boolean isCompleted);

    List<ParentRecommendationEntity> findAllByChildIdAndIsCompleted(Long childId, boolean b);
}
