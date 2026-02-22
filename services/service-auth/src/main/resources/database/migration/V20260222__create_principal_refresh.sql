CREATE TABLE principal_refresh_tokens(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    fk_principal_id BIGINT NOT NULL,
    token TEXT NOT NULL,
    user_agent VARCHAR(255) NULL
);

ALTER TABLE principal_refresh_tokens
    ADD CONSTRAINT FK_PRINCIPAL_REFRESH_TOKENS_ON_FK_PRINCIPAL FOREIGN KEY (fk_principal_id) REFERENCES principals (id);