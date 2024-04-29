package app.ishiko.server.project;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import app.ishiko.server.exception.HttpErrorResponseBodyDto;
import app.ishiko.server.exception.InvalidInputException;
import app.ishiko.server.exception.NotFoundException;
import app.ishiko.server.project.issue.dto.CreateIssueDto;
import app.ishiko.server.project.issue.dto.CreateIssueRequest;
import app.ishiko.server.project.issue.dto.IssueDto;
import app.ishiko.server.project.issue.dto.IssueQuery;
import app.ishiko.server.project.issue.service.IssueService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/resource/projects")
public class ProjectController {

    private final IssueService issueService;
    private final ProjectService projectService;

    public ProjectController(IssueService issueService,
            ProjectService projectService) {
        this.issueService = issueService;
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public ProjectDto getProject(Authentication auth,
            @PathVariable String projectId) throws NotFoundException {

        return projectService.getProjectData(projectId, auth.getName());
    }

    @GetMapping("/{projectId}/issues")
    public List<IssueDto> getProjectIssues(Authentication auth,
            @PathVariable String projectId, 
            @RequestParam(required = false) Integer page,
            IssueQuery query
        ) throws NotFoundException {
        Pageable pageable;
        int defaultPageSize = 20;

        if (!projectService.projectExists(projectId, auth.getName())) {
            throw new NotFoundException("Project id " + projectId
                    + " not found for user " + auth.getName());
        }

        if (page == null) {
            pageable = PageRequest.of(1, defaultPageSize);
        } else {
            pageable = PageRequest.of(page, defaultPageSize);
        }

        return issueService.getIssues(projectId, query, pageable);
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public IssueDto getProjectIssue(Authentication auth,
            @PathVariable String projectId, @PathVariable Integer issueId)
            throws NotFoundException {
        
        if (!projectService.projectExists(projectId, auth.getName())) {
            throw new NotFoundException("Project id " + projectId
                    + " not found for user " + auth.getName());
        }

        return issueService.getIssue(issueId);
    }

    @PostMapping("/{projectId}/issues")
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDto createProjectIssue(Authentication auth,
            @PathVariable String projectId,
            @RequestBody CreateIssueRequest body)
            throws NotFoundException, InvalidInputException {

        if (!projectService.projectExists(projectId, auth.getName())) {
            throw new NotFoundException("Project id " + projectId
                    + " not found for user " + auth.getName());
        }
        
        CreateIssueDto dto = new CreateIssueDto(body.getSubject(),
                body.getDescription(), body.getDueDate(), projectId,
                auth.getName(), body.getStatus(), body.getLabel());

        return issueService.createIssue(dto);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpErrorResponseBodyDto notFound(HttpServletRequest req,
            Exception e) {
        return new HttpErrorResponseBodyDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public HttpErrorResponseBodyDto invalidInput(HttpServletRequest req,
            Exception e) {
        return new HttpErrorResponseBodyDto(e.getMessage());
    }
}
