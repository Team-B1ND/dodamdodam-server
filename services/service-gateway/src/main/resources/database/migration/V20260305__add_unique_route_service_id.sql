ALTER TABLE routes
    ADD CONSTRAINT uc_routes_service_id UNIQUE (service_id);
