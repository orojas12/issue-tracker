package app.ishiko.server.project.issue.service;

import app.ishiko.server.exception.InvalidInputException;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.ProjectRepository;
import app.ishiko.server.project.issue.dto.CreateIssueDto;
import app.ishiko.server.project.issue.dto.IssueDto;
import app.ishiko.server.project.issue.dto.IssueLabelDto;
import app.ishiko.server.project.issue.dto.IssueQuery;
import app.ishiko.server.project.issue.dto.IssueStatusDto;
import app.ishiko.server.project.issue.dto.UpdateIssueDto;
import app.ishiko.server.project.issue.model.Issue;
import app.ishiko.server.project.issue.model.IssueLabel;
import app.ishiko.server.project.issue.model.IssueStatus;
import app.ishiko.server.project.issue.repository.IssueLabelRepository;
import app.ishiko.server.project.issue.repository.IssueRepository;
import app.ishiko.server.project.issue.repository.IssueSpecification;
import app.ishiko.server.project.issue.repository.IssueStatusRepository;
import app.ishiko.server.user.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueStatusRepository issueStatusRepository;
    private final IssueLabelRepository issueLabelRepository;
    private final ProjectRepository projectRepository;
    private final AppUserRepository userRepository;

    public IssueService(IssueRepository issueRepository,
            IssueStatusRepository issueStatusRepository,
            IssueLabelRepository issueLabelRepository,
            ProjectRepository projectRepository,
            AppUserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.issueStatusRepository = issueStatusRepository;
        this.issueLabelRepository = issueLabelRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<IssueDto> getIssues(String projectId, Pageable pageable) {
        List<Issue> issues = issueRepository
                .findAllByProject_IdOrderByCreateDateDesc(projectId, pageable);
        return issues.stream().map(this::entityToDto).toList();
    }
    
    public List<IssueDto> getIssues(String projectId, IssueQuery query, Pageable pageable) {
        IssueSpecification spec = new IssueSpecification(query);
        Page<Issue> issues = issueRepository.findAll(spec, pageable);
        return issues.stream().map(this::entityToDto).toList();
    }

    public List<IssueStatusDto> getIssueStatuses() {
        return issueStatusRepository.findAll().stream().map(this::entityToDto)
                .toList();
    }

    public List<IssueLabelDto> getIssueLabels() {
        return issueLabelRepository.findAll().stream().map(this::entityToDto)
                .toList();
    }

    public IssueDto getIssue(Integer id) throws NotFoundException {
        Optional<Issue> issueOptional = issueRepository.findById(id);
        if (issueOptional.isPresent()) {
            Issue issue = issueOptional.get();
            return entityToDto(issue);
        } else {
            throw new NotFoundException("Issue id " + id + " not found");
        }
    }

    public IssueDto createIssue(CreateIssueDto dto)
            throws InvalidInputException {
        Issue issue = new Issue();
        issue.setSubject(dto.getSubject());
        if (dto.getDescription().isPresent()) {
            issue.setDescription(dto.getDescription().get());
        }
        if (dto.getDueDate().isPresent()) {
            issue.setDueDate(dto.getDueDate().get());
        }
        issue.setCreateDate(Instant.now());
        if (dto.getStatus().isPresent()) {
            Optional<IssueStatus> statusOptional =
                    issueStatusRepository.findById(dto.getStatus().get());
            if (statusOptional.isPresent()) {
                issue.setStatus(statusOptional.get());
            } else
                throw new InvalidInputException(
                        "Issue status '" + dto.getStatus() + "' is invalid");
        }
        if (dto.getLabel().isPresent()) {
            Optional<IssueLabel> labelOptional =
                    issueLabelRepository.findById(dto.getLabel().get());
            if (labelOptional.isPresent()) {
                issue.setLabel(labelOptional.get());
            } else
                throw new InvalidInputException(
                        "Issue label '" + dto.getLabel() + "' is invalid");
        }

        issue.setProject(projectRepository.getReferenceById(dto.getProject()));
        issue.setAuthor(userRepository.getReferenceById(dto.getAuthor()));

        Issue savedIssue = issueRepository.save(issue);
        return entityToDto(savedIssue);
    }

    public IssueDto updateIssue(UpdateIssueDto dto, String username)
            throws NotFoundException, InvalidInputException {

        Optional<Issue> optionalIssue = issueRepository.findById(dto.getId());

        if (optionalIssue.isEmpty()) {
            throw new NotFoundException(
                    "Issue id '" + dto.getId() + "' not found");
        }

        Issue issue = optionalIssue.get();

        // verify user is author of this issue
        if (issue.getAuthor().getUsername() != username) {
            throw new InvalidInputException("User " + username
                    + " does not have permission" + " to edit this issue");
        }

        issue.setSubject(dto.getSubject());
        if (dto.getDescription().isPresent()) {
            issue.setDescription(dto.getDescription().get());
        }
        if (dto.getDueDate().isPresent()) {
            issue.setDueDate(dto.getDueDate().get());
        }
        if (dto.getStatus().isPresent()) {
            Optional<IssueStatus> statusOptional =
                    issueStatusRepository.findById(dto.getStatus().get());
            if (statusOptional.isPresent()) {
                issue.setStatus(statusOptional.get());
            } else {
                throw new InvalidInputException("Issue status '"
                        + dto.getStatus().get().toString() + "' is invalid");
            }
        }
        if (dto.getLabel().isPresent()) {
            Optional<IssueLabel> labelOptional =
                    issueLabelRepository.findById(dto.getLabel().get());
            if (labelOptional.isPresent()) {
                issue.setLabel(labelOptional.get());
            } else {
                throw new InvalidInputException(
                        "Issue label '" + dto.getLabel() + "' is invalid");
            }
        } else {
            issue.setLabel(null);
        }
        Issue savedIssue = issueRepository.save(issue);
        return entityToDto(savedIssue);
    }

    public void deleteIssue(int id) throws NotFoundException {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        if (optionalIssue.isPresent()) {
            issueRepository.delete(optionalIssue.get());
        } else {
            throw new NotFoundException("Issue id '" + id + "' not found");
        }
    }

    public IssueDto entityToDto(Issue issue) {
        IssueDto issueDto = new IssueDto();
        issueDto.setId(issue.getId());
        issueDto.setSubject(issue.getSubject());
        issueDto.setCreateDate(issue.getCreateDate());
        issueDto.setProject(issue.getProject().getId());
        issueDto.setAuthor(issue.getAuthor().getUsername());
        if (issue.getDescription().isPresent()) {
            issueDto.setDescription(issue.getDescription().get());
        }
        if (issue.getDueDate().isPresent()) {
            issueDto.setDueDate(issue.getDueDate().get());
        }
        if (issue.getStatus().isPresent()) {
            issueDto.setStatus(entityToDto(issue.getStatus().get()));
        }
        if (issue.getLabel().isPresent()) {
            issueDto.setLabel(entityToDto(issue.getLabel().get()));
        }
        return issueDto;
    }

    public IssueStatusDto entityToDto(IssueStatus status) {
        return new IssueStatusDto(status.getId(), status.getName());
    }

    public IssueLabelDto entityToDto(IssueLabel label) {
        return new IssueLabelDto(label.getId(), label.getName());
    }
}
