CREATE TABLE schedules(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    schedule_date date NOT NULL,
    grade INT NOT NULL,
    room INT NOT NULL,
    `period` INT NOT NULL,
    `subject` VARCHAR(100) NOT NULL,
    teacher VARCHAR(50) NOT NULL
);

ALTER TABLE schedules
    ADD CONSTRAINT uk_schedule_date_grade_class_period UNIQUE (schedule_date, grade, room, period);

CREATE INDEX idx_schedule_date ON schedules (schedule_date);
CREATE INDEX idx_schedule_date_grade_class ON schedules (schedule_date, grade, room);