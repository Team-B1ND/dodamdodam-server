CREATE TABLE out_sleepings(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    fk_user_id BINARY(16) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    start_at DATE NOT NULL,
    end_at DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    reject_reason VARCHAR(255)
);

CREATE INDEX idx_out_sleepings_start_at_end_at ON out_sleepings(start_at, end_at);

CREATE TABLE out_sleeping_deadlines(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    time TIME NOT NULL DEFAULT '17:00:00',
    CONSTRAINT uk_out_sleeping_deadline_day UNIQUE (day_of_week)
);
