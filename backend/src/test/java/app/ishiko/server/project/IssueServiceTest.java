package app.ishiko.server.project;

import app.ishiko.server.exception.InvalidInputException;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.issue.dto.CreateIssueDto;
import app.ishiko.server.project.issue.dto.IssueDto;
import app.ishiko.server.project.issue.dto.IssueLabelDto;
import app.ishiko.server.project.issue.dto.IssueStatusDto;
import app.ishiko.server.project.issue.dto.UpdateIssueDto;
import app.ishiko.server.project.issue.model.Issue;
import app.ishiko.server.project.issue.model.IssueLabel;
import app.ishiko.server.project.issue.model.IssueStatus;
import app.ishiko.server.project.issue.repository.IssueLabelRepository;
import app.ishiko.server.project.issue.repository.IssueRepository;
import app.ishiko.server.project.issue.repository.IssueStatusRepository;
import app.ishiko.server.project.issue.service.IssueService;
import app.ishiko.server.user.AppUser;
import app.ishiko.server.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;
    @Mock
    private IssueStatusRepository statusRepository;
    @Mock
    private IssueLabelRepository labelRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AppUserRepository userRepository;

    IssueService getIssueService() {
        return new IssueService(issueRepository, statusRepository,
                labelRepository, projectRepository, userRepository);
    }

    @Test
    void getIssues_ProjectIdAndPage_getsIssuesFirstPage() {
        int pageSize = 20;
        List<Issue> issues = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            issues.add(new Issue());
        }
        IssueDto dto = new IssueDto();
        Pageable pageable = PageRequest.of(1, pageSize);
        when(issueRepository
                .findAllByProject_IdOrderByCreateDateDesc("project_1", pageable))
                        .thenReturn(issues);
        IssueService service = spy(getIssueService());
        doReturn(dto).when(service).entityToDto(any(Issue.class));
        List<IssueDto> issueDtoList = service.getIssues("project_1", pageable);
        verify(issueRepository, times(1))
                .findAllByProject_IdOrderByCreateDateDesc("project_1", pageable);
        assertThat(issueDtoList.size()).isEqualTo(pageSize);
    }

    @Test
    void getIssue_IssueId_getsIssueById() throws NotFoundException {
        Issue issue = new Issue();
        var dto = new IssueDto();
        issue.setId(1);
        when(issueRepository.findById(issue.getId()))
                .thenReturn(Optional.of(issue));
        IssueService service = spy(getIssueService());
        doReturn(dto).when(service).entityToDto(issue);
        var newDto = service.getIssue(issue.getId());
        verify(issueRepository, times(1)).findById(issue.getId());
        assertThat(newDto).isEqualTo(dto);
    }

    @Test
    void getIssue_NotFoundIssueId_throwsNotFoundException() {
        var id = 1;
        when(issueRepository.findById(id)).thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.getIssue(1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createIssue_CreateIssueDto_createsIssue()
            throws InvalidInputException {
        var project = new Project("project_1", "project 1", null);
        var user = new AppUser("oscar");
        var dto = new CreateIssueDto("subject", "desc", null, project.getId(),
                user.getUsername(), 1, 2);
        var status =
                new IssueStatus(dto.getStatus().orElseThrow(), "", project);
        var label = new IssueLabel(dto.getLabel().orElseThrow(), "", project);
        when(statusRepository.findById(dto.getStatus().orElseThrow()))
                .thenReturn(Optional.of(status));
        when(labelRepository.findById(dto.getLabel().orElseThrow()))
                .thenReturn(Optional.of(label));
        when(issueRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(projectRepository.getReferenceById(project.getId()))
                .thenReturn(project);
        when(userRepository.getReferenceById(user.getUsername()))
                .thenReturn(user);
        ArgumentCaptor<Issue> arg = ArgumentCaptor.forClass(Issue.class);
        IssueService service = getIssueService();
        IssueDto createdIssueDto = service.createIssue(dto);
        verify(issueRepository).save(arg.capture());
        assertThat(arg.getValue().getSubject()).isEqualTo(dto.getSubject());
        assertThat(arg.getValue().getDescription())
                .isEqualTo(dto.getDescription());
        assertThat(arg.getValue().getDueDate()).isEqualTo(dto.getDueDate());
        assertThat(arg.getValue().getStatus().orElseThrow().getId())
                .isEqualTo(dto.getStatus().orElseThrow());
        assertThat(arg.getValue().getLabel().orElseThrow().getId())
                .isEqualTo(dto.getLabel().orElseThrow());
    }

    @Test
    void createIssue_CreateIssueDtoWithInvalidStatus_throwsInvalidInputException() {
        var project = new Project("project_1", "project 1", null);
        var user = new AppUser("oscar");
        var dto = new CreateIssueDto("subject", "desc", null, project.getId(),
                user.getUsername(), 1, 2);
        when(statusRepository.findById(dto.getStatus().orElseThrow()))
                .thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.createIssue(dto))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void createIssue_CreateIssueDtoWithInvalidLabel_throwsInvalidInputException() {
        var project = new Project("project_1", "project 1", null);
        var user = new AppUser("oscar");
        var dto = new CreateIssueDto("subject", "desc", null, project.getId(),
                user.getUsername(), 1, 2);
        var status =
                new IssueStatus(dto.getStatus().orElseThrow(), "", project);
        when(statusRepository.findById(dto.getStatus().orElseThrow()))
                .thenReturn(Optional.of(status));
        when(labelRepository.findById(dto.getLabel().orElseThrow()))
                .thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.createIssue(dto))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void updateIssue_UpdateIssueDto_updatesIssue()
            throws NotFoundException, InvalidInputException {
        var dto = new UpdateIssueDto(1, "subject", "desc", null, 1, 2);
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        when(statusRepository.findById(dto.getStatus().orElseThrow()))
                .thenReturn(Optional.of(status));
        when(labelRepository.findById(dto.getLabel().orElseThrow()))
                .thenReturn(Optional.of(label));
        when(issueRepository.findById(dto.getId()))
                .thenReturn(Optional.of(issue));
        when(issueRepository.save(issue)).thenAnswer(i -> i.getArguments()[0]);
        ArgumentCaptor<Issue> arg = ArgumentCaptor.forClass(Issue.class);
        IssueService service = getIssueService();
        IssueDto createdIssueDto = service.updateIssue(dto, user.getUsername());
        verify(issueRepository).save(arg.capture());
        assertThat(arg.getValue().getSubject()).isEqualTo(dto.getSubject());
        assertThat(arg.getValue().getDescription())
                .isEqualTo(dto.getDescription());
        assertThat(arg.getValue().getDueDate()).isEqualTo(dto.getDueDate());
        assertThat(arg.getValue().getStatus().orElseThrow().getId())
                .isEqualTo(dto.getStatus().orElseThrow());
        assertThat(arg.getValue().getLabel().orElseThrow().getId())
                .isEqualTo(dto.getLabel().orElseThrow());
    }

    @Test
    void updateIssue_NotFoundId_throwsNotFoundException() {
        var dto = new UpdateIssueDto(1, "subject", "desc", null, 1, 2);
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        when(issueRepository.findById(dto.getId()))
                .thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.updateIssue(dto, "john"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateIssue_UpdateIssueDtoWithInvalidStatus_throwsInvalidInputException() {
        var dto = new UpdateIssueDto(1, "subject", "desc", null, 1, 2);
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        when(issueRepository.findById(dto.getId()))
                .thenReturn(Optional.of(issue));
        when(statusRepository.findById(dto.getStatus().get()))
                .thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.updateIssue(dto, user.getUsername()))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void updateIssue_UpdateIssueDtoWithInvalidLabel_throwsInvalidInputException() {
        var dto = new UpdateIssueDto(1, "subject", "desc", null, 1, 2);
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        when(issueRepository.findById(dto.getId()))
                .thenReturn(Optional.of(issue));
        when(statusRepository.findById(dto.getStatus().orElseThrow()))
                .thenReturn(Optional.of(status));
        when(labelRepository.findById(dto.getLabel().orElseThrow()))
                .thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.updateIssue(dto, user.getUsername()))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void deleteIssue_IssueId_deletesIssue() throws NotFoundException {
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        when(issueRepository.findById(issue.getId()))
                .thenReturn(Optional.of(issue));
        IssueService service = getIssueService();
        service.deleteIssue(issue.getId());
        verify(issueRepository).delete(issue);
    }

    @Test
    void deleteIssue_NotFoundId_throwsNotFoundException() {
        var id = 1;
        when(issueRepository.findById(id)).thenReturn(Optional.empty());
        IssueService service = getIssueService();
        assertThatThrownBy(() -> service.deleteIssue(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void entityToDto_Issue_returnsDtoWithSameData() {
        var user = new AppUser("oscar");
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        var label = new IssueLabel(2, "label", project);
        var issue = new Issue(3, "subject", "desc", Instant.now(), null, user,
                project, status, label);
        IssueService service = getIssueService();
        IssueDto dto = service.entityToDto(issue);
        assertThat(dto.getId()).isEqualTo(issue.getId());
        assertThat(dto.getSubject()).isEqualTo(issue.getSubject());
        assertThat(dto.getDescription().orElseThrow())
                .isEqualTo(issue.getDescription().orElseThrow());
        assertThat(dto.getCreateDate()).isEqualTo(issue.getCreateDate());
        assertThat(dto.getDueDate().orElse(null))
                .isEqualTo(issue.getDueDate().orElse(null));
        assertThat(dto.getAuthor()).isEqualTo(issue.getAuthor().getUsername());
        assertThat(dto.getProject()).isEqualTo(issue.getProject().getId());
        assertThat(dto.getStatus().orElseThrow().getId())
                .isEqualTo(status.getId());
        assertThat(dto.getStatus().orElseThrow().getName())
                .isEqualTo(status.getName());
        assertThat(dto.getLabel().orElseThrow().getId())
                .isEqualTo(label.getId());
        assertThat(dto.getLabel().orElseThrow().getName())
                .isEqualTo(label.getName());
    }

    @Test
    void entityToDto_IssueStatus_returnsDtoWithSameData() {
        var project = new Project("project_1", "name", null);
        var status = new IssueStatus(1, "status", project);
        IssueService service = getIssueService();
        IssueStatusDto dto = service.entityToDto(status);
        assertThat(dto.getId()).isEqualTo(status.getId());
        assertThat(dto.getName()).isEqualTo(status.getName());
    }

    @Test
    void entityToDto_IssueLabel_returnsDtoWithSameData() {
        var project = new Project("project_1", "name", null);
        var label = new IssueLabel(1, "label", project);
        IssueService service = getIssueService();
        IssueLabelDto dto = service.entityToDto(label);
        assertThat(dto.getId()).isEqualTo(label.getId());
        assertThat(dto.getName()).isEqualTo(label.getName());
    }
}
