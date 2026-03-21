CREATE TABLE users(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_image TEXT NULL,
    phone VARCHAR(255) NULL,
    status SMALLINT NULL,
    public_id BINARY(16) NOT NULL UNIQUE
);

CREATE TABLE user_roles(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_user_id BIGINT NOT NULL,
    `role` VARCHAR(255) NULL
);

ALTER TABLE user_roles
    ADD CONSTRAINT FK_USER_ROLES_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id);

CREATE TABLE students(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_user_id BIGINT NOT NULL UNIQUE,
    grade INT NOT NULL,
    room INT NOT NULL,
    number INT NOT NULL
);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id);

CREATE TABLE teachers(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at  datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_user_id  BIGINT NOT NULL UNIQUE,
    position VARCHAR(255) NULL
);

ALTER TABLE teachers
    ADD CONSTRAINT FK_TEACHERS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id);