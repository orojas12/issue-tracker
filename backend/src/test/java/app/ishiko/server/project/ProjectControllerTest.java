package app.ishiko.server.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.ishiko.server.exception.InvalidInputException;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.Project;
import app.ishiko.server.project.ProjectController;
import app.ishiko.server.project.ProjectDto;
import app.ishiko.server.project.ProjectRepository;
import app.ishiko.server.project.ProjectService;
import app.ishiko.server.project.issue.dto.CreateIssueDto;
import app.ishiko.server.project.issue.dto.CreateIssueRequest;
import app.ishiko.server.project.issue.dto.IssueDto;
import app.ishiko.server.project.issue.dto.IssueLabelDto;
import app.ishiko.server.project.issue.dto.IssueStatusDto;
import app.ishiko.server.project.issue.service.IssueService;
import app.ishiko.server.user.AppUser;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private IssueService issueService;
    @MockBean
    private ProjectService projectService;

    private final String BASE_URL = "/resource/projects";

    static boolean isCreateIssueDtoEqual(CreateIssueDto dto1,
            CreateIssueDto dto2) {
        return dto1.getSubject().equals(dto2.getSubject())
                && dto1.getDescription().equals(dto2.getDescription())
                && dto1.getDueDate().orElseThrow()
                        .equals(dto2.getDueDate().orElseThrow())
                && dto1.getProject().equals(dto2.getProject())
                && dto1.getAuthor().equals(dto2.getAuthor())
                && dto1.getStatus().orElseThrow() == dto2.getStatus()
                        .orElseThrow()
                && dto1.getLabel().orElseThrow() == dto2.getLabel()
                        .orElseThrow();
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectData_ProjectId_returnsHttp200AndProjectData()
            throws Exception {
        AppUser user = new AppUser("john");
        Project project = new Project("project_1", "name", null, user);
        List<IssueStatusDto> statuses =
                List.of(new IssueStatusDto(1, "status_1"));
        List<IssueLabelDto> labels = List.of(new IssueLabelDto(1, "label_1"));
        ProjectDto result = new ProjectDto(project.getId(), project.getName(),
                project.getDescription(), user.getUsername());
        result.setStatuses(statuses);
        result.setLabels(labels);
        when(projectService.getProjectData(project.getId(), user.getUsername()))
                .thenReturn(result);

        mockMvc.perform(get(BASE_URL + "/{projectId}", project.getId()))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id", is(result.getId())),
                        jsonPath("$.name", is(result.getName())),
                        jsonPath("$.description", is(result.getDescription())),
                        jsonPath("$.owner", is(user.getUsername())),
                        jsonPath("$.statuses[0].id",
                                is(statuses.get(0).getId())),
                        jsonPath("$.statuses[0].name",
                                is(statuses.get(0).getName())),
                        jsonPath("$.labels[0].id", is(labels.get(0).getId())),
                        jsonPath("$.labels[0].name",
                                is(labels.get(0).getName())));
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectIssues_ProjectIdAndNoPage_returnsHttp200AndIssues() throws Exception {
        int pageNumber = 1;
        int pageSize = 20;
        String username = "john";
        Project project = new Project("project_1", "name", null);
        List<IssueDto> issues = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            issues.add(new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label")));
        }
        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.getIssues(eq(project.getId()), any(PageRequest.class))).thenReturn(issues);

        mockMvc.perform(get(BASE_URL + "/{projectId}/issues", project.getId()))
                .andExpectAll(status().isOk(),
                        jsonPath("$", hasSize(pageSize)),
                        jsonPath("$[0].id", is(issues.get(0).getId())),
                        jsonPath("$[0].subject",
                                is(issues.get(0).getSubject())),
                        jsonPath("$[0].description",
                                is(issues.get(0).getDescription().get())),
                        jsonPath("$[0].createDate",
                                is(issues.get(0).getCreateDate().toString())),
                        jsonPath("$[0].dueDate",
                                is(issues.get(0).getDueDate().orElseThrow()
                                        .toString())),
                        jsonPath("$[0].status.id",
                                is(issues.get(0).getStatus().orElseThrow()
                                        .getId())),
                        jsonPath("$[0].status.name",
                                is(issues.get(0).getStatus().orElseThrow()
                                        .getName())),
                        jsonPath("$[0].label.id",
                                is(issues.get(0).getLabel().orElseThrow()
                                        .getId())),
                        jsonPath("$[0].label.name", is(issues.get(0).getLabel()
                                .orElseThrow().getName())));

        ArgumentCaptor<PageRequest> arg = ArgumentCaptor.forClass(PageRequest.class);
        verify(issueService).getIssues(eq(project.getId()), arg.capture());
        assertThat(arg.getValue().getPageNumber()).isEqualTo(pageNumber);
        assertThat(arg.getValue().getPageSize()).isEqualTo(pageSize);
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectIssues_ProjectIdAndPage_returnsHttp200AndIssues() throws Exception {
        int pageNumber = 2;
        int pageSize = 20;
        String username = "john";
        Project project = new Project("project_1", "name", null);
        List<IssueDto> issues = new ArrayList<>();
        // populate issues list
        for (int i = 0; i < pageSize; i++) {
            issues.add(new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label")));
        }
        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.getIssues(eq(project.getId()), any(PageRequest.class))).thenReturn(issues);

        mockMvc.perform(get(BASE_URL + "/{projectId}/issues?page={pageNumber}", project.getId(), pageNumber))
                .andExpectAll(status().isOk(),
                        jsonPath("$", hasSize(pageSize)),
                        jsonPath("$[0].id", is(issues.get(0).getId())),
                        jsonPath("$[0].subject",
                                is(issues.get(0).getSubject())),
                        jsonPath("$[0].description",
                                is(issues.get(0).getDescription().get())),
                        jsonPath("$[0].createDate",
                                is(issues.get(0).getCreateDate().toString())),
                        jsonPath("$[0].dueDate",
                                is(issues.get(0).getDueDate().orElseThrow()
                                        .toString())),
                        jsonPath("$[0].status.id",
                                is(issues.get(0).getStatus().orElseThrow()
                                        .getId())),
                        jsonPath("$[0].status.name",
                                is(issues.get(0).getStatus().orElseThrow()
                                        .getName())),
                        jsonPath("$[0].label.id",
                                is(issues.get(0).getLabel().orElseThrow()
                                        .getId())),
                        jsonPath("$[0].label.name", is(issues.get(0).getLabel()
                                .orElseThrow().getName())));

        ArgumentCaptor<PageRequest> arg = ArgumentCaptor.forClass(PageRequest.class);
        verify(issueService).getIssues(eq(project.getId()), arg.capture());
        assertThat(arg.getValue().getPageNumber()).isEqualTo(pageNumber);
        assertThat(arg.getValue().getPageSize()).isEqualTo(pageSize);
    }
    
    @Test
    @WithMockUser(value = "john")
    void getProjectIssues_NonExistentProjectId_returnsHttp404AndErrorMsg()
            throws Exception {
        String username = "john";
        Project project = new Project("project_1", "name", null);
        IssueDto dto = new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label"));
        List<IssueDto> issues = List.of(dto);
        when(projectService.projectExists(project.getId(), username)).thenReturn(false);
        when(issueService.getIssues(eq(project.getId()), any(PageRequest.class))).thenReturn(issues);

        mockMvc.perform(get(BASE_URL + "/{projectId}/issues", project.getId()))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message", isA(String.class)));
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectIssue_ProjectIdAndIssueId_returnsHttp200AndIssue()
            throws Exception, NotFoundException {
        String username = "john";
        Project project = new Project("project_1", "name", null);
        IssueDto dto = new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label"));
        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.getIssue(dto.getId())).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/{projectId}/issues/{issueId}",
                project.getId(), dto.getId()))
                .andExpectAll(jsonPath("$.id", is(dto.getId())),
                        jsonPath("$.subject", is(dto.getSubject())),
                        jsonPath("$.description",
                                is(dto.getDescription().orElseThrow())),
                        jsonPath("$.createDate",
                                is(dto.getCreateDate().toString())),
                        jsonPath("$.dueDate",
                                is(dto.getDueDate().orElseThrow().toString())),
                        jsonPath("$.project", is(dto.getProject())),
                        jsonPath("$.author", is(dto.getAuthor())),
                        jsonPath("$.status.id",
                                is(dto.getStatus().orElseThrow().getId())),
                        jsonPath("$.status.name",
                                is(dto.getStatus().orElseThrow().getName())),
                        jsonPath("$.label.id",
                                is(dto.getLabel().orElseThrow().getId())),
                        jsonPath("$.label.name",
                                is(dto.getLabel().orElseThrow().getName())));
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectIssue_NonExistentProjectId_returnsHttp404()
            throws Exception {
        String username = "john";
        Project project = new Project("project_1", "name", null);
        IssueDto dto = new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label"));
        when(projectService.projectExists(project.getId(), username)).thenReturn(false);
        when(issueService.getIssue(dto.getId())).thenReturn(dto);
        mockMvc.perform(get(BASE_URL + "/{projectId}/issues/{issueId}",
                project.getId(), dto.getId()))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message", isA(String.class)));
    }

    @Test
    @WithMockUser(value = "john")
    void getProjectIssue_NonExistentIssueId_returnsHttp404() throws Exception {
        String username = "john";
        Project project = new Project("project_1", "name", null);
        IssueDto dto = new IssueDto(1, "subject", "desc", Instant.now(),
                Instant.now(), "project_1", username,
                new IssueStatusDto(2, "status"), new IssueLabelDto(3, "label"));
        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.getIssue(dto.getId()))
                .thenThrow(new NotFoundException("not found"));
        mockMvc.perform(get(BASE_URL + "/{projectId}/issues/{issueId}",
                project.getId(), dto.getId()))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message", is("not found")));
    }

    @Test
    @WithMockUser(value = "john")
    void createIssue_CreateIssueDto_ReturnsHttp201AndCreatedData()
            throws Exception {
        String username = "john";
        Project project = new Project("project_1", "name", "desc");
        CreateIssueRequest body = new CreateIssueRequest("subject", "desc",
                Instant.now(), project.getId(), 2, 3);
        CreateIssueDto createIssueDto = new CreateIssueDto(body.getSubject(),
                body.getDescription(), body.getDueDate(), project.getId(),
                username, body.getStatus(), body.getLabel());
        IssueDto issueDto = new IssueDto(1, body.getSubject(),
                body.getDescription(), Instant.now(), body.getDueDate(),
                project.getId(), username,
                new IssueStatusDto(body.getStatus(), "status"),
                new IssueLabelDto(body.getLabel(), "label"));
        String json = objectMapper.writeValueAsString(body);

        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.createIssue(any())).thenReturn(issueDto);

        mockMvc.perform(
                post(BASE_URL + "/{projectId}/issues", "project_1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(status().isCreated(),
                        jsonPath("$.id", is(issueDto.getId())),
                        jsonPath("$.subject", is(issueDto.getSubject())),
                        jsonPath("$.description",
                                is(issueDto.getDescription().orElseThrow())),
                        jsonPath("$.createDate",
                                is(issueDto.getCreateDate().toString())),
                        jsonPath("$.dueDate",
                                is(issueDto.getDueDate().orElseThrow()
                                        .toString())),
                        jsonPath("$.status.id",
                                is(issueDto.getStatus().orElseThrow().getId())),
                        jsonPath("$.status.name",
                                is(issueDto.getStatus().orElseThrow()
                                        .getName())),
                        jsonPath("$.label.id",
                                is(issueDto.getLabel().orElseThrow().getId())),
                        jsonPath("$.label.name", is(
                                issueDto.getLabel().orElseThrow().getName())));

        verify(issueService).createIssue(argThat(arg -> {
            CreateIssueDto argDto = (CreateIssueDto) arg;
            return isCreateIssueDtoEqual(argDto, createIssueDto);
        }));
    }

    @Test
    @WithMockUser(value = "john")
    void createIssue_CreateIssueDtoWithNonExistentProjectId_ReturnsHttp404AndErrorMsg()
            throws Exception {
        String username = "john";
        Project project = new Project("project_1", "name", "desc");
        CreateIssueRequest body = new CreateIssueRequest("subject", "desc",
                Instant.now(), project.getId(), 2, 3);
        IssueDto issueDto = new IssueDto(1, body.getSubject(),
                body.getDescription(), Instant.now(), body.getDueDate(),
                project.getId(), username,
                new IssueStatusDto(body.getStatus(), "status"),
                new IssueLabelDto(body.getLabel(), "label"));
        String json = objectMapper.writeValueAsString(body);

        when(projectService.projectExists(project.getId(), username)).thenReturn(false);
        when(issueService.createIssue(any())).thenReturn(issueDto);

        mockMvc.perform(
                post(BASE_URL + "/{projectId}/issues", "project_1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message", isA(String.class)));

        verify(issueService, times(0)).createIssue(any());
    }

    @Test
    @WithMockUser(value = "john")
    void createIssue_InvalidCreateIssueDto_ReturnsHttp400AndErroMsg()
            throws Exception {
        String invalidMsg = "invalid";
        String username = "john";
        Project project = new Project("project_1", "name", "desc");
        CreateIssueRequest body = new CreateIssueRequest("subject", "desc",
                Instant.now(), project.getId(), 2, 3);
        CreateIssueDto createIssueDto = new CreateIssueDto(body.getSubject(),
                body.getDescription(), body.getDueDate(), project.getId(),
                username, body.getStatus(), body.getLabel());
        String json = objectMapper.writeValueAsString(body);

        when(projectService.projectExists(project.getId(), username)).thenReturn(true);
        when(issueService.createIssue(any()))
                .thenThrow(new InvalidInputException(invalidMsg));

        mockMvc.perform(
                post(BASE_URL + "/{projectId}/issues", "project_1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.message", is(invalidMsg)));

        verify(issueService).createIssue(argThat(arg -> {
            CreateIssueDto argDto = (CreateIssueDto) arg;
            return isCreateIssueDtoEqual(argDto, createIssueDto);
        }));
    }
}
