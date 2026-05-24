package com.umograd.analytic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parent_recommendations", catalog = "analytic_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "child_id", nullable = false)
    private Long childId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "is_completed")
    private boolean isCompleted = false;
}
