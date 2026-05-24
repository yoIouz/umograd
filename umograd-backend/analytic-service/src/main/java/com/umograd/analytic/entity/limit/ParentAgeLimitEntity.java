package com.umograd.analytic.entity.limit;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent_age_limits", catalog = "analytic_db")
public class ParentAgeLimitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "max_minutes", nullable = false)
    private int maxMinutes;
}

