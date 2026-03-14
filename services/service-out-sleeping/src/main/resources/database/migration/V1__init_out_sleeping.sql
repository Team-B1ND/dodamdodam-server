CREATE TABLE out_sleepings
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at    DATETIME     NOT NULL,
    modified_at   DATETIME     NOT NULL,
    user_id       BINARY(16)   NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    start_at      DATE         NOT NULL,
    end_at        DATE         NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    reject_reason VARCHAR(255) NULL,
    is_deleted    TINYINT(1)   NOT NULL DEFAULT 0
);

CREATE INDEX idx_out_sleepings_user_id ON out_sleepings (user_id);
CREATE INDEX idx_out_sleepings_status ON out_sleepings (status);
CREATE INDEX idx_out_sleepings_start_at_end_at ON out_sleepings (start_at, end_at);
