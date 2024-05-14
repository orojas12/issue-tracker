package dev.oscarrojas.issuetracker.issue;

import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
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
    public List<IssueDto> getAllIssues() {
        return issueService.getAllIssues();
    }

    @PostMapping
    public IssueDto createIssue(@RequestBody CreateIssueDto dto) {
        return issueService.createIssue(dto);
    }

    @GetMapping("/{issueId}")
    public IssueDto getIssue(@PathVariable("issueId") long id) {
        return issueService.getIssue(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{issueId}")
    public IssueDto updateIssue(@PathVariable("issueId") long id, @RequestBody UpdateIssueDto dto) {
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
