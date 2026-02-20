CREATE TABLE notices(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    notice_status VARCHAR(20) NOT NULL,
    user_id VARCHAR(255) NOT NULL
);

CREATE TABLE notice_files(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    file_url LONGTEXT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    fk_notice_id BIGINT NOT NULL,
    CONSTRAINT fk_notice_file_notice FOREIGN KEY (fk_notice_id) REFERENCES notices(id)
);

CREATE INDEX idx_notices_status_id ON notices(notice_status, id DESC);
CREATE INDEX idx_notice_files_notice_id ON notice_files(fk_notice_id);
