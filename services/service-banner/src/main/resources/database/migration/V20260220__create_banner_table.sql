CREATE TABLE IF NOT EXISTS banner (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    image_url TEXT NOT NULL,
    redirect_url TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    expire_at DATETIME(6) NOT NULL,
    created_at DATETIME(6),
    modified_at DATETIME(6)
);
