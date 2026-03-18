CREATE TABLE meals(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_at datetime NOT NULL,
    modified_at datetime NOT NULL,
    meal_date date NOT NULL,
    meal_type VARCHAR(16) NOT NULL,
    calorie DOUBLE NOT NULL,
    menus TEXT NOT NULL
);

ALTER TABLE meals
    ADD CONSTRAINT uk_meals_date_type UNIQUE (meal_date, meal_type);

CREATE INDEX idx_meals_date ON meals (meal_date);