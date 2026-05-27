package com.umograd.analytic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
@Table(name = "users", catalog = "umograd")
public class UserEntity {

    @Id
    private Long id;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_consent")
    private boolean parentConsent;

    @Column(name = "has_active_subscription")
    private boolean hasActiveSubscription;

    public int getAge() {
        if (this.birthDate == null) return 18;
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}

