package org.project_management.application.dto.task;

import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;

public class TaskMapper {
    public static Task toTask(TaskCreate createDTO) {
        Task task = new Task();
        task.setTitle(createDTO.getTitle());
        task.setContent(createDTO.getContent());
        task.setPriority(createDTO.getPriority());
        task.setStatus(createDTO.getStatus());
        task.setDeadlineAt(createDTO.getDeadlineAt());
        // No project or assignee association here, this is handled in the service layer
        return task;
    }

    public static Task toTaskFragment(TaskUpdate updateDTO) {
        Task task = new Task();
        task.setTitle(updateDTO.getTitle());
        task.setContent(updateDTO.getContent());
        task.setPriority(updateDTO.getPriority());
        task.setStatus(updateDTO.getStatus());
        task.setDeadlineAt(updateDTO.getDeadlineAt());
        // Do not modify the project or assignee, handled in service layer
        return task;
    }
}
