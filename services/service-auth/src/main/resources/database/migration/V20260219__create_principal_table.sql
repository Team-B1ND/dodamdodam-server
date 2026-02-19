CREATE TABLE principals(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_user_id BINARY(16) NULL,
    status BIT(1) NOT NULL,
    username VARCHAR(255) NULL,
    roles VARCHAR(255) NULL
);

ALTER TABLE principals
    ADD CONSTRAINT uc_principals_fk_user UNIQUE (fk_user_id)