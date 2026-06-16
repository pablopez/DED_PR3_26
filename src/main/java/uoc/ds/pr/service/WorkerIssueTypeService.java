package uoc.ds.pr.service;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.IssueTypeNotFoundException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.exceptions.WorkerNotFoundException;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.IssueTypeRepository;
import uoc.ds.pr.repository.WorkerRepository;

public class WorkerIssueTypeService {

    private final WorkerRepository workerRepository;
    private final IssueTypeRepository issueTypeRepository;

    public WorkerIssueTypeService(
            WorkerRepository workerRepository,
            IssueTypeRepository issueTypeRepository
    ) {
        this.workerRepository = workerRepository;
        this.issueTypeRepository = issueTypeRepository;
    }

    public void assignWorkerToIssueType(String workerId, String issueTypeId)
            throws WorkerNotFoundException, IssueTypeNotFoundException {

        Worker worker = workerRepository.getWorkerOrThrow(workerId);

        IssueType issueType =
                issueTypeRepository.getIssueTypeOrThrow(issueTypeId);

        worker.setIssueType(issueType);
    }

    public Iterator<Worker> getWorkersByIssueType(String issueTypeId) throws NoWorkerException {
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        return issueType.getWorkers();
    }
}