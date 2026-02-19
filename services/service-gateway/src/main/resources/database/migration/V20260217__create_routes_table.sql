CREATE TABLE routes (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    service_id CHAR(36) NOT NULL,
    path VARCHAR(255) NOT NULL,
    target_uri VARCHAR(512) NOT NULL,
    strip_prefix INT NOT NULL DEFAULT 0,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    modified_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_routes_service_id ON routes(service_id);
CREATE INDEX idx_routes_enabled ON routes(enabled);