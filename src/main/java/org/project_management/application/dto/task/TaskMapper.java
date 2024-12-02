package org.project_management.application.dto.task;

import org.project_management.domain.entities.task.Task;

public class TaskMapper {

    private TaskMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Task toTask(TaskCreate createDTO) {
        Task task = new Task();
        task.setTitle(createDTO.getTitle());
        task.setContent(createDTO.getContent());
        task.setPriority(createDTO.getPriority());
        task.setStatus(createDTO.getStatus());
        task.setDeadlineAt(createDTO.getDeadlineAt());
        return task;
    }

    public static Task toTaskFragment(TaskUpdate updateDTO) {
        Task task = new Task();
        task.setTitle(updateDTO.getTitle());
        task.setContent(updateDTO.getContent());
        task.setPriority(updateDTO.getPriority());
        task.setStatus(updateDTO.getStatus());
        task.setDeadlineAt(updateDTO.getDeadlineAt());
        return task;
    }
}
