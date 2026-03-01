ALTER TABLE `dodam-user`.users
    DROP COLUMN status;

ALTER TABLE `dodam-user`.users
    ADD status VARCHAR(100) NOT NULL DEFAULT 'PENDING';