CREATE TABLE bus(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE bus_applicant(
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    member_id VARCHAR(255) NOT NULL,
    fk_bus_id BIGINT NOT NULL,
    seat INT NULL,
    boarding_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_bus_applicant_bus FOREIGN KEY (fk_bus_id) REFERENCES bus(id)
);

CREATE INDEX idx_bus_applicant_bus_id ON bus_applicant(fk_bus_id);
CREATE INDEX idx_bus_applicant_member_id ON bus_applicant(member_id);
