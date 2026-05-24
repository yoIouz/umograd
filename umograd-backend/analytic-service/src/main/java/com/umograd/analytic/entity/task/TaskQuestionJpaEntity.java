package com.umograd.analytic.entity.task;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task_questions", catalog = "content_db")
public class TaskQuestionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentType;
    private String question;

    @Lob
    private String options;

    private String answer;

    private String hint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskJpaEntity task;
}