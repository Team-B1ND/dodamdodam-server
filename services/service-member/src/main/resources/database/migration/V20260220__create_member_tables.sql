CREATE TABLE member (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255) NOT NULL,
    profile_image TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL
);

CREATE TABLE member_role (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_member_role_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL UNIQUE,
    grade INT NOT NULL,
    room INT NOT NULL,
    number INT NOT NULL,
    code VARCHAR(255) UNIQUE,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_student_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE teacher (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL UNIQUE,
    tel VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_teacher_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE parent (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_parent_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE student_relation (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    relation VARCHAR(255) NOT NULL,
    fk_student_id BIGINT NOT NULL,
    fk_parent_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_student_relation_student FOREIGN KEY (fk_student_id) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_student_relation_parent FOREIGN KEY (fk_parent_id) REFERENCES parent(id) ON DELETE CASCADE
);

CREATE TABLE broadcast_club_member (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_broadcast_club_member_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE dormitory_manage_member (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_member_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    CONSTRAINT fk_dormitory_manage_member_member FOREIGN KEY (fk_member_id) REFERENCES member(id) ON DELETE CASCADE
);
