package com.umograd.analytic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "achievements", catalog = "analytic_db")
public class AchievementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "condition_expression", nullable = false)
    private String conditionExpression;

    @Column(name = "condition_value")
    private Integer conditionValue;
}