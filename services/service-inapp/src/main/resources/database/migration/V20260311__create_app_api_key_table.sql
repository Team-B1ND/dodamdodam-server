CREATE TABLE app_api_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fk_app_id BIGINT NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    expired_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    modified_at DATETIME(6) NOT NULL,
    UNIQUE KEY uk_app_api_keys_app_id (fk_app_id),
    CONSTRAINT fk_app_api_keys_app FOREIGN KEY (fk_app_id) REFERENCES apps(id) ON DELETE CASCADE
);