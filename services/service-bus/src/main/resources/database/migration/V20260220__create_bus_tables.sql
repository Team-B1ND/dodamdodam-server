CREATE TABLE buses(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE bus_applicants(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    fk_bus_id BIGINT NOT NULL,
    seat INT NULL,
    boarding_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_bus_applicant_bus FOREIGN KEY (fk_bus_id) REFERENCES buses(id)
);

CREATE INDEX idx_bus_applicants_bus_id ON bus_applicants(fk_bus_id);
CREATE INDEX idx_bus_applicants_user_id ON bus_applicants(user_id);
