package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.IssueNotFoundException;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Worker;

import java.time.LocalDateTime;

public class IssueRepository extends AbstractRepository<Issue> {

    public IssueRepository() {
        super(new AVLStorageStrategy<>());
    }

    public Issue addIssue(
            String id,
            Component component,
            IssueType issueType,
            String description,
            LocalDateTime dateTime
    ) {
        Issue issue = getById(id);

        if (issue == null) {
            issue = new Issue(
                    id,
                    component,
                    issueType,
                    description,
                    dateTime
            );

            save(issue);
            component.addIssue(issue);
        } else {
            /*
               habría que devolver un error en caso de que se intentara duplicar un issue? o editarlo?
               dejo este bloque pendiente para ver qué hacer y devolviendo issue tal cual
            */
        }

        return issue;
    }

    public Issue getIssue(String id) {
        return getById(id);
    }

    public Issue getIssueOrThrow(String id)
            throws IssueNotFoundException {
        return getByIdOrThrow(id, IssueNotFoundException::new);
    }

    public int numIssues() {
        return size();
    }

    public void assignIssue(Issue issue, Worker worker) {
        issue.setWorker(worker);
        worker.addIssue(issue);
    }
}