package org.example.managementplatformrasirom.repository;

import org.example.managementplatformrasirom.model.Task;
import org.example.managementplatformrasirom.model.TaskPriority;
import org.example.managementplatformrasirom.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDeletedFalse();
    List<Task> findByStatusAndDeletedFalse(TaskStatus status);
    List<Task> findByPriorityAndDeletedFalse(TaskPriority priority);
    List<Task> findByAssignedToIdAndDeletedFalse(Long userId);
    List<Task> findByProjectIdAndDeletedFalse(Long projectId);
}
