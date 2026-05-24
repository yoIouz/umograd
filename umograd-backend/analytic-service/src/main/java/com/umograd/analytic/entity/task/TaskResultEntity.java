package com.umograd.analytic.entity.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_results", catalog = "content_db")
public class TaskResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "child_id")
    private Long childId;

    private Integer score;

    private String status;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private TaskJpaEntity task;
}
