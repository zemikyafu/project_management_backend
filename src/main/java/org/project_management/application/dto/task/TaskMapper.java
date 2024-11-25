package org.project_management.application.dto.task;

import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;

public class TaskMapper {
    public static Task toTask(TaskCreate createDTO, Project project, User assignee) {
        return new Task(
                createDTO.getTitle(),
                createDTO.getContent(),
                createDTO.getPriority(),
                createDTO.getStatus(),
                project,
                assignee,
                new java.util.Date(),  // Created At
                null,  // Edited At (not applicable for creation)
                null,  // Closed At (not applicable for creation)
                createDTO.getDeadlineAt()
        );
    }

    public static Task toEntity(TaskUpdate updateDTO, Task existingTask) {
        // considering assignee cant be changed

        if (updateDTO.getTitle() != null) {
            existingTask.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getContent() != null) {
            existingTask.setContent(updateDTO.getContent());
        }
        if (updateDTO.getPriority() != null) {
            existingTask.setPriority(updateDTO.getPriority());
        }
        if (updateDTO.getStatus() != null) {
            existingTask.setStatus(updateDTO.getStatus());
        }
        // as deadline can be empty/null
        existingTask.setDeadlineAt(updateDTO.getDeadlineAt());

        existingTask.setEditedAt(new java.util.Date());  // Set the edited date to current time
        return existingTask;
    }

}
