CREATE TABLE admins(
    id BIGINT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_user_id BIGINT NOT NULL,
    github_id VARCHAR(255) NULL
);

ALTER TABLE admins
    ADD CONSTRAINT FK_ADMINS_ON_FK_USER FOREIGN KEY (fk_user_id) REFERENCES users (id);