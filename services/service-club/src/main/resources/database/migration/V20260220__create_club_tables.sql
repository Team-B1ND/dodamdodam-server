CREATE TABLE IF NOT EXISTS club (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    short_description VARCHAR(255) NOT NULL,
    description TEXT,
    image TEXT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    teacher_id BIGINT,
    state VARCHAR(50) NOT NULL,
    reason VARCHAR(255),
    created_at DATETIME(6),
    modified_at DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS club_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission VARCHAR(50) NOT NULL,
    club_status VARCHAR(50) NOT NULL,
    priority VARCHAR(50),
    student_id BIGINT NOT NULL,
    fk_club_id BIGINT NOT NULL,
    introduction TEXT,
    created_at DATETIME(6),
    modified_at DATETIME(6),
    CONSTRAINT fk_club_member_club FOREIGN KEY (fk_club_id) REFERENCES club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS club_of_time (
    id VARCHAR(50) PRIMARY KEY,
    start DATE NOT NULL,
    end DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
