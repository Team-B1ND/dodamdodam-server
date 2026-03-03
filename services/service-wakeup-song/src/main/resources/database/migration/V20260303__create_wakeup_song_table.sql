CREATE TABLE wakeup_songs
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    created_at    datetime     NOT NULL,
    modified_at   datetime     NOT NULL,
    user_id       BINARY(16)   NOT NULL,
    video_title   VARCHAR(255) NOT NULL,
    video_id      VARCHAR(255) NOT NULL,
    video_url     VARCHAR(512) NOT NULL,
    channel_title VARCHAR(255) NOT NULL,
    thumbnail     TEXT         NULL,
    status        VARCHAR(20)  NOT NULL
);

CREATE INDEX idx_wakeup_songs_user_id ON wakeup_songs (user_id);
CREATE INDEX idx_wakeup_songs_status ON wakeup_songs (status);
CREATE INDEX idx_wakeup_songs_created_at ON wakeup_songs (created_at);
