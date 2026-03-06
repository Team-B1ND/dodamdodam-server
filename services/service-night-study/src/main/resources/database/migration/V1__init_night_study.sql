CREATE TABLE night_studies
(
    id               BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at       datetime     NOT NULL,
    modified_at      datetime     NOT NULL,
    user_id          BINARY(16)   NOT NULL,
    content          VARCHAR(250) NOT NULL,
    type             VARCHAR(20)  NOT NULL,
    name             VARCHAR(255) NULL,
    do_need_phone    TINYINT(1)   NOT NULL DEFAULT 0,
    reason_for_phone VARCHAR(255) NULL,
    start_at         date         NOT NULL,
    end_at           date         NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    room             VARCHAR(20)  NULL,
    reject_reason    VARCHAR(255) NULL,
    is_deleted       TINYINT(1)   NOT NULL DEFAULT 0
);

CREATE INDEX idx_night_studies_user_id ON night_studies (user_id);
CREATE INDEX idx_night_studies_status ON night_studies (status);
CREATE INDEX idx_night_studies_start_at_end_at ON night_studies (start_at, end_at);
CREATE INDEX idx_night_studies_type ON night_studies (type);

CREATE TABLE night_study_members
(
    id              BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at      datetime   NOT NULL,
    modified_at     datetime   NOT NULL,
    night_study_id  BIGINT     NOT NULL,
    user_id         BINARY(16) NOT NULL
);

CREATE INDEX idx_night_study_members_night_study_id ON night_study_members (night_study_id);
CREATE INDEX idx_night_study_members_user_id ON night_study_members (user_id);

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
