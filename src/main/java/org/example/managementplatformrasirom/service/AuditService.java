package org.example.managementplatformrasirom.service;


import lombok.RequiredArgsConstructor;
import org.example.managementplatformrasirom.model.AuditLog;
import org.example.managementplatformrasirom.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public void log(String action, String performedBy, String entityType, Long entityId, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
