package org.example.managementplatformrasirom.repository;

import org.example.managementplatformrasirom.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByDeletedFalse();
    List<Project> findByOwnerIdAndDeletedFalse(Long ownerId);
    List<Project> findByMembersIdAndDeletedFalse(Long memberId);
}
