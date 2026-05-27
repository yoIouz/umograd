package com.umograd.analytic.repository.task;

import com.umograd.analytic.entity.task.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskResultRepository extends JpaRepository<TaskResultEntity, Long> {

    @Query(value = """
        SELECT * FROM (
            SELECT *,\s
                   ROW_NUMBER() OVER (PARTITION BY child_id ORDER BY finished_at DESC) as rn
            FROM content_db.task_results
            WHERE child_id = :childId
        ) t
        WHERE rn <= :limit
       \s""", nativeQuery = true)
    List<TaskResultEntity> findLastResultsWithWindow(@Param("childId") Long childId, @Param("limit") int limit);

    List<TaskResultEntity> findAllByChildId(Long childId);

    @Query(value = """
        SELECT * FROM content_db.task_results 
        WHERE child_id = :childId AND status = 'DONE' AND started_at IS NOT NULL
        ORDER BY finished_at DESC 
        LIMIT 3
        """, nativeQuery = true)
    List<TaskResultEntity> findLastFinishedResults(@Param("childId") Long childId);
}
