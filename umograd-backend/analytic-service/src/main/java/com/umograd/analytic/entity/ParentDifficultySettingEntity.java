package com.umograd.analytic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent_difficulty_settings", catalog = "analytic_db")
public class ParentDifficultySettingEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "child_id", nullable = false)
    private Long childId;

    @Column(name = "selected_difficulty", nullable = false)
    private String selectedDifficulty;
}
