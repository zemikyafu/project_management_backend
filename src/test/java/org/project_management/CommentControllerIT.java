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
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@Transactional
class CommentControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommentRepository commentRepository;

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
    String token;
    UUID userId;

    @BeforeEach
    public void setup() throws Exception {
        // User related setup
        SignupRequest userCreate = new SignupRequest(
                "Ted Tester",
                "testing@email.com",
                "Password123#"
        );

        user = UserMapper.toUser(userCreate);

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreate))
        );

        MvcResult result = mockMvc.perform(post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"" + userCreate.getEmail() + "\", \"password\": \"" + userCreate.getPassword() + "\" }")
        ).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        token = objectMapper.readTree(responseBody).path("data").path("token").asText();
        userId = UUID.fromString(objectMapper.readTree(responseBody).path("data").path("userId").asText());

        // Company related setup
        CompanyCreate companyCreate = new CompanyCreate("name", "email@email.com", "address");
        company = companyRepository.save(CompanyMapper.toCompany(companyCreate));

        // Workspace related setup
        WorkspaceCreate workspaceCreate = new WorkspaceCreate("new workspace 1", "description", company.getId());
        workspace = WorkspaceMapper.toEntity(workspaceCreate);
        workspace.setCompany(company);
        workspace = workspaceRepository.save(workspace);

        // Project related setup
        ProjectCreate projectCreate = new ProjectCreate("name", "description", ProjectStatus.NOT_STARTED, UUID.randomUUID(), new Date(12), new Date(12));
        project = ProjectMapper.toProject(projectCreate);
        project.setWorkspace(workspace);
        project = projectRepository.save(project);

        // Task related setup
        TaskCreate taskCreate = new TaskCreate("title", "content", TaskPriority.LOW, TaskStatus.BACKLOG, project.getId(), user.getId(), new Date(12));
        task = TaskMapper.toTask(taskCreate);
        task.setProject(project);
        task.setAssignee(user);
        task = taskRepository.save(task);

        // Comment related setup
        CommentCreate createComment = new CommentCreate("This is a comment", task.getId(), user.getId());
        comment = CommentMapper.toComment(createComment);
        comment.setTask(task);
        comment = commentRepository.save(comment);

        user = authRepository.findByEmail(userCreate.getEmail()).get();
    }

    @Test
    void shouldCreateCommentSuccessfully() throws Exception {
        CommentCreate newComment = new CommentCreate("This is a comment", task.getId(), user.getId());

        mockMvc.perform(post("/api/v1/tasks/" + task.getId() + "/comments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
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
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.content").value(comment.getContent()));
    }

    @Test
    void shouldGetAllComments() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments")
                        .header("Authorization", "Bearer " + token)
                )
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
        mockMvc.perform(delete("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("Comment deleted successfully"));

        // check that comment not found
        mockMvc.perform(get("/api/v1/tasks/" + task.getId() + "/comments/" + comment.getId())
                        .header("Authorization", "Bearer " + token)
                )
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
                        .header("Authorization", "Bearer " + token)
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
