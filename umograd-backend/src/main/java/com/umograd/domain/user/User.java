package com.umograd.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true) // email может быть необязательным
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @Column(name = "avatar_url", columnDefinition = "LONGTEXT")
    private String avatarUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<User> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @Column(name = "parent_consent")
    private boolean parentConsent;
}
