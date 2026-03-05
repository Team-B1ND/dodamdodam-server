CREATE TABLE teams(
    deleted BIT(1) NOT NULL,
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    name VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    icon_url TEXT NULL,
    github_url VARCHAR(255) NULL,
    public_id BINARY(16) NOT NULL
);

ALTER TABLE teams
    ADD CONSTRAINT uc_teams_publicid UNIQUE (public_id);

CREATE TABLE team_members(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fk_team_id BIGINT NOT NULL,
    fk_user_id BINARY(16) NOT NULL,
    is_owner BIT(1) NOT NULL
);

ALTER TABLE team_members
    ADD CONSTRAINT FK_TEAM_MEMBERS_ON_FK_TEAM FOREIGN KEY (fk_team_id) REFERENCES teams (id);