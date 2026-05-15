CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          owner_id BIGINT NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                          deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          CONSTRAINT fk_projects_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE project_members (
                                 project_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 PRIMARY KEY (project_id, user_id),
                                 CONSTRAINT fk_pm_project FOREIGN KEY (project_id) REFERENCES projects(id),
                                 CONSTRAINT fk_pm_user FOREIGN KEY (user_id) REFERENCES users(id)
);