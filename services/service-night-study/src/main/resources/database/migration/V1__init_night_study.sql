CREATE TABLE night_studies
(
    id               BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at       datetime     NOT NULL,
    modified_at      datetime     NOT NULL,
    user_id          BINARY(16)   NOT NULL,
    content          VARCHAR(250) NOT NULL,
    type             VARCHAR(20)  NOT NULL,
    do_need_phone    TINYINT(1)   NOT NULL DEFAULT 0,
    reason_for_phone VARCHAR(255) NULL,
    start_at         date         NOT NULL,
    end_at           date         NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    reject_reason    VARCHAR(255) NULL
);

CREATE INDEX idx_night_studies_user_id ON night_studies (user_id);
CREATE INDEX idx_night_studies_status ON night_studies (status);
CREATE INDEX idx_night_studies_start_at_end_at ON night_studies (start_at, end_at);

CREATE TABLE night_study_projects
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at    datetime     NOT NULL,
    modified_at   datetime     NOT NULL,
    user_id       BINARY(16)   NOT NULL,
    type          VARCHAR(20)  NOT NULL,
    name          VARCHAR(255) NOT NULL,
    description   TEXT         NOT NULL,
    start_at      date         NOT NULL,
    end_at        date         NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    room          VARCHAR(20)  NULL,
    reject_reason VARCHAR(255) NULL
);

CREATE INDEX idx_night_study_projects_user_id ON night_study_projects (user_id);
CREATE INDEX idx_night_study_projects_status ON night_study_projects (status);
CREATE INDEX idx_night_study_projects_start_at_end_at ON night_study_projects (start_at, end_at);

CREATE TABLE night_study_project_members
(
    id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at  datetime   NOT NULL,
    modified_at datetime   NOT NULL,
    project_id  BIGINT     NOT NULL,
    user_id     BINARY(16) NOT NULL,
    CONSTRAINT fk_project_members_project FOREIGN KEY (project_id) REFERENCES night_study_projects (id) ON DELETE CASCADE
);

CREATE INDEX idx_night_study_project_members_project_id ON night_study_project_members (project_id);
CREATE INDEX idx_night_study_project_members_user_id ON night_study_project_members (user_id);

CREATE TABLE night_study_bans
(
    id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at  datetime     NOT NULL,
    modified_at datetime     NOT NULL,
    user_id     BINARY(16)   NOT NULL,
    reason      VARCHAR(255) NOT NULL,
    ended_at    date         NOT NULL
);

CREATE INDEX idx_night_study_bans_user_id ON night_study_bans (user_id);
CREATE INDEX idx_night_study_bans_ended_at ON night_study_bans (ended_at);
