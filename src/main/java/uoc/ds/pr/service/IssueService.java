package uoc.ds.pr.service;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.IssueAlreadyAssignedException;
import uoc.ds.pr.exceptions.IssueAlreadyResolvedException;
import uoc.ds.pr.exceptions.IssueNotFoundException;
import uoc.ds.pr.exceptions.NoIssuesException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.exceptions.WorkerNotFoundException;
import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.IssueRepository;
import uoc.ds.pr.repository.WorkerRepository;

public class IssueService {

    private final IssueRepository issueRepository;
    private final WorkerRepository workerRepository;

    public IssueService(
            IssueRepository issueRepository,
            WorkerRepository workerRepository
    ) {
        this.issueRepository = issueRepository;
        this.workerRepository = workerRepository;
    }

    public void assignIssue(String issueId, String workerId)
            throws IssueNotFoundException,  WorkerNotFoundException,
            IssueAlreadyAssignedException, IssueAlreadyResolvedException{
        Issue issue = issueRepository.getIssueOrThrow(issueId);
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        issueRepository.assignIssue(issue, worker);
    }

    public Issue solveIssue(String workerId)
            throws WorkerNotFoundException, NoIssuesException {
        return workerRepository.solveIssue(workerId);
    }

    public Iterator<Issue> getDoneIssuesByWorker(String workerId)
            throws NoIssuesException {

        return workerRepository.doneIssues(workerId);
    }

    public Worker getTopWorker() throws NoWorkerException {
        Worker worker = workerRepository.getTopWorker();

        if (worker == null) {
            throw new NoWorkerException();
        }

        return worker;
    }

    private boolean canWorkerSolveIssue(Worker worker, Issue issue) {
        IssueType workerIssueType = worker.getIssueType();
        IssueType issueType = issue.getIssueType();

        if (workerIssueType == null || issueType == null) {
            return false;
        }

        return workerIssueType.getId().equals(issueType.getId());
    }
}