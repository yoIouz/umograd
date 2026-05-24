package com.umograd.analytic.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "system_logs", catalog = "analytic_db")
public class SystemLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String username;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "endpoint")
    private String endpoint;

    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
