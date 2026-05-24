create schema if not exists umograd;
create schema if not exists content_db;
create schema if not exists analytic_db;

create table if not exists analytic_db.system_logs
(
    id          bigint auto_increment
        primary key,
    user_id     bigint       not null,
    username    varchar(255) not null,
    event_type  varchar(100) not null,
    endpoint    varchar(255) null,
    description text         null,
    created_at  datetime(6)  not null
);

create table if not exists analytic_db.achievements
(
    id                   bigint auto_increment
        primary key,
    name                 varchar(255) not null,
    description          varchar(255) null,
    icon_url             varchar(255) null,
    condition_expression varchar(255) not null,
    condition_value      int          not null
);

create table if not exists analytic_db.child_achievements
(
    id             bigint auto_increment
        primary key,
    child_id       bigint                              not null,
    achievement_id bigint                              not null,
    earned_at      timestamp default CURRENT_TIMESTAMP null,
    constraint fk_achievement
        foreign key (achievement_id) references analytic_db.achievements (id)
);

create table if not exists content_db.flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

CREATE TABLE IF NOT EXISTS analytic_db.parent_recommendations (
                                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                                  parent_id BIGINT NOT NULL,
                                                                  child_id BIGINT NOT NULL,
                                                                  task_id BIGINT NOT NULL,
                                                                  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                                  is_completed TINYINT(1) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS analytic_db.parent_age_limits (
                                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                             parent_id BIGINT NOT NULL,
                                                             age INT NOT NULL,
                                                             max_minutes INT NOT NULL,
                                                             UNIQUE KEY uk_parent_age (parent_id, age)
);

CREATE TABLE IF NOT EXISTS analytic_db.parent_child_custom_limits (
                                                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                                      parent_id BIGINT NOT NULL,
                                                                      child_id BIGINT NOT NULL,
                                                                      custom_minutes INT NOT NULL,
                                                                      UNIQUE KEY uk_parent_child (parent_id, child_id)
);

CREATE TABLE IF NOT EXISTS analytic_db.parent_difficulty_settings (
                                                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                                      parent_id BIGINT NOT NULL,
                                                                      child_id BIGINT NOT NULL,
                                                                      selected_difficulty VARCHAR(20) NOT NULL,
                                                                      UNIQUE KEY uk_parent_child_diff (parent_id, child_id)
);

create table if not exists umograd.flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on content_db.flyway_schema_history (success);

create index flyway_schema_history_s_idx
    on umograd.flyway_schema_history (success);

create table if not exists content_db.task_results
(
    id          bigint auto_increment
        primary key,
    child_id    bigint      not null,
    started_at  datetime(6) null,
    finished_at datetime(6) null,
    score       int         null,
    status      varchar(20) not null,
    task_id     bigint      not null
);

create table if not exists content_db.tasks
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)                     null,
    created_by  varchar(255)                    null,
    description varchar(255)                    null,
    difficulty  enum ('EASY', 'HARD', 'MEDIUM') null,
    max_age     int                             null,
    min_age     int                             null,
    title       varchar(255)                    null,
    updated_at  datetime(6)                     null,
    source_id   varchar(255)                    null
);

create table if not exists content_db.task_questions
(
    id           bigint auto_increment
        primary key,
    task_id      bigint       not null,
    content_type varchar(255) null,
    question     varchar(255) null,
    options      longtext     null,
    answer       varchar(255) null,
    hint         varchar(255) null,
    constraint fk_task
        foreign key (task_id) references content_db.tasks (id)
            on delete cascade
);

create table if not exists umograd.users
(
    id             bigint auto_increment
        primary key,
    username       varchar(255)         not null,
    email          varchar(255)         null,
    password       varchar(255)         not null,
    parent_id      bigint               null,
    avatar_url     longtext             null,
    birth_date     date                 null,
    parent_consent tinyint(1) default 0 null,
    constraint uc_users_email
        unique (email),
    constraint uc_users_username
        unique (username),
    constraint FK_USERS_ON_PARENT
        foreign key (parent_id) references umograd.users (id)
);

create table if not exists umograd.user_roles
(
    user_id bigint                                               not null,
    role    enum ('ROLE_CHILD', 'ROLE_MODERATOR', 'ROLE_PARENT') null,
    constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id) references umograd.users (id)
);

