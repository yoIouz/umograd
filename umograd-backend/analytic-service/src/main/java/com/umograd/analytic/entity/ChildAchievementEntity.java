package com.umograd.analytic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "child_achievements", catalog = "analytic_db")
public class ChildAchievementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long childId;

    @ManyToOne
    @JoinColumn(name = "achievement_id")
    private AchievementEntity achievement;

    private LocalDateTime earnedAt;
}
