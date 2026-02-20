CREATE TABLE out_sleepings
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id    BINARY(16)   NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reject_reason VARCHAR(255) NULL,
    start_at      DATE         NOT NULL,
    end_at        DATE         NOT NULL,
    created_at    DATETIME     NOT NULL,
    modified_at   DATETIME     NOT NULL
);
