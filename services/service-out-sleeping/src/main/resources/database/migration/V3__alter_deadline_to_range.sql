ALTER TABLE out_sleeping_deadlines DROP INDEX uk_out_sleeping_deadline_day;

ALTER TABLE out_sleeping_deadlines
    CHANGE COLUMN day_of_week start_day_of_week VARCHAR(10) NOT NULL,
    CHANGE COLUMN time start_time TIME NOT NULL DEFAULT '00:00:00',
    ADD COLUMN end_day_of_week VARCHAR(10) NOT NULL DEFAULT 'FRIDAY' AFTER start_time,
    ADD COLUMN end_time TIME NOT NULL DEFAULT '17:00:00' AFTER end_day_of_week;
