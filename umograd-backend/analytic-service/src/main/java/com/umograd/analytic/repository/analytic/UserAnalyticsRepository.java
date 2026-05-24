package com.umograd.analytic.repository.analytic;

import com.umograd.analytic.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnalyticsRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select u.parent_id from umograd.users u where u.id = :childId", nativeQuery = true)
    Long getParentId(@Param("childId") Long childId);
}
