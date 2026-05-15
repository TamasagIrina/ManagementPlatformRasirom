CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       priority VARCHAR(50) NOT NULL,
                       status VARCHAR(50) NOT NULL DEFAULT 'TODO',
                       deadline TIMESTAMP,
                       assigned_to BIGINT,
                       created_by BIGINT NOT NULL,
                       project_id BIGINT NOT NULL,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP,
                       CONSTRAINT fk_tasks_assigned FOREIGN KEY (assigned_to) REFERENCES users(id),
                       CONSTRAINT fk_tasks_creator FOREIGN KEY (created_by) REFERENCES users(id),
                       CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id)
);