package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import dev.oscarrojas.issuetracker.issue.dto.CreateIssueData;
import dev.oscarrojas.issuetracker.issue.dto.IssueDetails;
import dev.oscarrojas.issuetracker.issue.dto.UpdateIssueData;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<IssueDetails> getIssueList(@RequestParam("projectId") String projectId) {
        return issueService.getIssueList(projectId);
    }

    @PostMapping
    public IssueDetails createIssue(@RequestBody CreateIssueData body) {
        return issueService.createIssue(body);
    }

    @GetMapping("/{issueId}")
    public IssueDetails getIssue(@PathVariable("issueId") long id) {
        return issueService.getIssue(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{issueId}")
    public IssueDetails updateIssue(@PathVariable("issueId") long id, @RequestBody UpdateIssueData dto) {
        try {
            return issueService.updateIssue(id, dto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{issueId}")
    public void deleteIssue(@PathVariable("issueId") long id) {
        try {
            issueService.deleteIssue(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
