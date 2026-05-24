CREATE TABLE users
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    username  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    parent_id BIGINT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_PARENT FOREIGN KEY (parent_id) REFERENCES users (id);