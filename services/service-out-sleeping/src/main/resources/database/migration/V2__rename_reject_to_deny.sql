ALTER TABLE out_sleepings CHANGE COLUMN reject_reason deny_reason VARCHAR(255);

UPDATE out_sleepings SET status = 'DENIED' WHERE status = 'REJECTED';
