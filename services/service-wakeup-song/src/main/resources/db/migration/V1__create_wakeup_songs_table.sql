CREATE TABLE wakeup_songs
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id    BINARY(16)   NOT NULL,
    video_id      VARCHAR(20)  NOT NULL,
    video_title   VARCHAR(255) NOT NULL,
    video_url     VARCHAR(500) NOT NULL,
    channel_title VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(500) NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    play_at       DATE         NULL,
    created_at    DATETIME     NOT NULL,
    modified_at   DATETIME     NOT NULL
);
