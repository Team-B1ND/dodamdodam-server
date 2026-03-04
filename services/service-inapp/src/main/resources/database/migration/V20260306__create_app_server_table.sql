CREATE TABLE app_servers(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    fk_app_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    server_address VARCHAR(255) NOT NULL,
    redirect_path VARCHAR(255) NOT NULL,
    prefix_level INT NOT NULL,
    enabled BIT(1) NOT NULL,
    deny_result TEXT NULL,
    status VARCHAR(32) NOT NULL
);

ALTER TABLE app_servers
    ADD CONSTRAINT uc_app_servers_fk_app UNIQUE (fk_app_id);

ALTER TABLE app_servers
    ADD CONSTRAINT FK_APP_SERVERS_ON_FK_APP FOREIGN KEY (fk_app_id) REFERENCES apps (id) ON DELETE CASCADE;
