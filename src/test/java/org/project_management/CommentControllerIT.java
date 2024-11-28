package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentMapper;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.application.dto.company.CompanyCreate;
import org.project_management.application.dto.company.CompanyMapper;
import org.project_management.application.dto.project.ProjectCreate;
import org.project_management.application.dto.project.ProjectMapper;
import org.project_management.application.dto.task.TaskCreate;
import org.project_management.application.dto.task.TaskMapper;
import org.project_management.application.dto.user.SignupRequest;
import org.project_management.application.dto.user.UserMapper;
import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceMapper;
import org.project_management.domain.abstractions.*;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.project.ProjectStatus;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskPriority;
import org.project_management.domain.entities.task.TaskStatus;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
public class CommentControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AuthRepository authRepository;

    User user;
    Task task;
    Project project;
    Comment comment;
    Workspace workspace;
    Company company;

    @BeforeEach
    public void setup() {
        /*
        CompanyCreate companyCreate = new CompanyCreate("name", "email@email.com", "address");
        company = companyRepository.save(CompanyMapper.toCompany(companyCreate));
        WorkspaceCreate workspaceCreate = new WorkspaceCreate("new workspace 1", "description", company.getId());
        workspace = workspaceRepository.save(WorkspaceMapper.toEntity(workspaceCreate, company));
        SignupRequest userCreate = new SignupRequest("Ted Tester", "testing@email.com", "Password123#");
        user = authRepository.save(UserMapper.toUser(userCreate));
        ProjectCreate projectCreate = new ProjectCreate("name", "description", ProjectStatus.NOT_STARTED, UUID.randomUUID(), new Date(12), new Date(12));
        project = projectRepository.save(ProjectMapper.toProject(projectCreate, workspace));
        TaskCreate taskCreate = new TaskCreate("title", "content", TaskPriority.LOW, TaskStatus.BACKLOG, project.getId(), null, new Date(12));
        task = taskRepository.save(TaskMapper.toTask(taskCreate, project, user));
        CommentCreate createComment = new CommentCreate("This is a comment", task.getId(), user.getId());
        comment = commentRepository.save(CommentMapper.toComment(createComment, task, user));
         */
    }

    @Test
    void shouldCreateCommentSuccessfully() throws Exception {
        CommentCreate newComment = new CommentCreate("This is a comment", task.getId(), user.getId());

        mockMvc.perform(post("/api/v1/tasks/" + task.getId() + "/comments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.content").value(newComment.getContent()))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void shouldGetCommentById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.content").value(comment.getContent()));
    }

    @Test
    void shouldGetAllComments() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].content").value(comment.getContent()));
    }

    @Test
    void shouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("Comment deleted successfully"));

        // check that comment not found
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("Comment not found with id: " + comment.getId()))
                .andExpect(jsonPath("$.data").value((Object) null));
    }

    @Test
    void shouldUpdateExistingComment() throws Exception {
        CommentUpdate commentUpdate = new CommentUpdate(comment.getId(), task.getId(), "updated comment");

        mockMvc.perform(put("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.content").value(commentUpdate.getContent()))
                .andExpect(jsonPath("$.data.taskId").value(commentUpdate.getTaskId()))
                .andExpect(jsonPath("$.data.id").value(comment.getId().toString()));
    }
}
