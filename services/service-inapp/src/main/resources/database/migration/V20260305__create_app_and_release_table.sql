ALTER TABLE teams
    DROP COLUMN deleted;

CREATE TABLE apps(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    `description` TEXT NULL,
    subtitle VARCHAR(255) NOT NULL,
    icon_url TEXT NOT NULL,
    dark_icon_url TEXT NULL,
    inquiry_mail VARCHAR(255) NOT NULL,
    fk_team_id BIGINT NOT NULL,
    public_id BINARY(16) NOT NULL
);

ALTER TABLE apps
    ADD CONSTRAINT uc_apps_name UNIQUE (name);

ALTER TABLE apps
    ADD CONSTRAINT uc_apps_public_id UNIQUE (public_id);

ALTER TABLE apps
    ADD CONSTRAINT FK_APPS_ON_FK_TEAM FOREIGN KEY (fk_team_id) REFERENCES teams (id) ON DELETE CASCADE;

CREATE TABLE app_releases(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_app_id BIGINT NOT NULL,
    enabled BIT(1) NOT NULL,
    release_url TEXT NOT NULL,
    fk_user_id BINARY(16) NOT NULL,
    memo TEXT NULL,
    deny_result TEXT NULL,
    status VARCHAR(32) NOT NULL,
    public_id BINARY(16) NOT NULL
);

ALTER TABLE app_releases
    ADD CONSTRAINT uc_app_releases_public_id UNIQUE (public_id);

ALTER TABLE app_releases
    ADD CONSTRAINT FK_APP_RELEASES_ON_FK_APP FOREIGN KEY (fk_app_id) REFERENCES apps (id) ON DELETE CASCADE;
