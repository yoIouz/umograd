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

    @Query(value = """
        SELECT 
            DATE_FORMAT(tr.finished_at, '%Y-%m-%d') as date, 
            COALESCE(AVG(tr.score), 0.0) as averageScore,
            COALESCE(AVG(TIMESTAMPDIFF(SECOND, tr.started_at, tr.finished_at)), 0.0) as averageTimeSeconds,
            t.difficulty as difficulty
        FROM content_db.task_results tr
        JOIN content_db.tasks t ON tr.task_id = t.id
        WHERE tr.child_id = :childId 
          AND tr.finished_at IS NOT NULL
          AND tr.finished_at >= CASE 
              WHEN :period = 'day' THEN NOW() - INTERVAL 1 DAY
              WHEN :period = 'week' THEN NOW() - INTERVAL 1 WEEK
              WHEN :period = 'month' THEN NOW() - INTERVAL 1 MONTH
              ELSE NOW() - INTERVAL 1 YEAR
          END
        GROUP BY DATE_FORMAT(tr.finished_at, '%Y-%m-%d'), t.difficulty
        ORDER BY date ASC
        """, nativeQuery = true)
    List<Object[]> getAdvancedChildProgressHistory(@Param("childId") Long childId, @Param("period") String period);

    @Query(value = """
        SELECT * FROM content_db.task_results 
        WHERE child_id = :childId AND status = 'DONE' AND started_at IS NOT NULL
        ORDER BY finished_at DESC 
        LIMIT 3
        """, nativeQuery = true)
    List<TaskResultEntity> findLastFinishedResults(@Param("childId") Long childId);
}
