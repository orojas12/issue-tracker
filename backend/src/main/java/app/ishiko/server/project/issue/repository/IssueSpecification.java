package app.ishiko.server.project.issue.repository;

import java.util.List;
import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;
import app.ishiko.server.project.issue.dto.IssueQuery;
import app.ishiko.server.project.issue.model.Issue;
import app.ishiko.server.project.issue.model.IssueLabel_;
import app.ishiko.server.project.issue.model.Issue_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class IssueSpecification implements Specification<Issue> {
    private IssueQuery search;

    public IssueSpecification(IssueQuery search) {
        this.search = search;
    }

    @Override
    public Predicate toPredicate(Root<Issue> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        
        if (search.labels() != null) { 
            predicates.add(
                root.get(Issue_.label).get(IssueLabel_.name).in(search.labels())
            );
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
