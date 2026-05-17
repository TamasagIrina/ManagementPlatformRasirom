package org.example.managementplatformrasirom.repository;

import org.example.managementplatformrasirom.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
