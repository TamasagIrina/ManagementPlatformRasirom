CREATE TABLE audit_log (
                           id BIGSERIAL PRIMARY KEY,
                           action VARCHAR(100) NOT NULL,
                           performed_by VARCHAR(255),
                           entity_type VARCHAR(50),
                           entity_id BIGINT,
                           details TEXT,
                           created_at TIMESTAMP NOT NULL DEFAULT NOW()
);