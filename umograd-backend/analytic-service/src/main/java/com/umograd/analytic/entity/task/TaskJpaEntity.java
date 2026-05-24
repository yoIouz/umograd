package com.umograd.analytic.entity.task;

import com.umograd.analytic.enums.Difficulty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks", catalog = "content_db")
@SuppressWarnings("unused")
public class TaskJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id")
    private String sourceId;

    private String title;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer minAge;
    private Integer maxAge;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskQuestionJpaEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    private List<TaskResultEntity> results = new ArrayList<>();

    public void addQuestion(TaskQuestionJpaEntity question) {
        questions.add(question);
        question.setTask(this);
    }
}
