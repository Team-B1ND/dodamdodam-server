CREATE TABLE out_sleepings (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BINARY(16)   NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    start_at      DATE         NOT NULL,
    end_at        DATE         NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reject_reason VARCHAR(255),
    created_at    DATETIME(6)  NOT NULL,
    modified_at   DATETIME(6)  NOT NULL
);

CREATE TABLE members (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BINARY(16)   NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    role        VARCHAR(20)  NOT NULL,
    grade       INT,
    room        INT,
    number      INT,
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL
);

CREATE TABLE out_sleeping_deadline (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week VARCHAR(10)  NOT NULL DEFAULT 'SUNDAY',
    time        TIME         NOT NULL DEFAULT '17:00:00',
    created_at  DATETIME(6)  NOT NULL,
    modified_at DATETIME(6)  NOT NULL
);

INSERT INTO out_sleeping_deadline (day_of_week, time, created_at, modified_at)
VALUES ('SUNDAY', '17:00:00', NOW(6), NOW(6));
