package uoc.ds.pr.service;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.IssueRepository;
import uoc.ds.pr.repository.IssueTypeRepository;
import uoc.ds.pr.repository.WorkerRepository;
import uoc.ds.pr.repository.WorkerSocialNetworkRepository;

public class WorkerSocialNetworkService {

    private final WorkerRepository workerRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final WorkerSocialNetworkRepository socialNetworkRepository;

    public WorkerSocialNetworkService(
            WorkerRepository workerRepository,
            IssueTypeRepository issueRepository,
            WorkerSocialNetworkRepository socialNetworkRepository
    ) {
        this.workerRepository = workerRepository;
        this.issueTypeRepository = issueRepository;
        this.socialNetworkRepository = socialNetworkRepository;
    }

    public void addFollower(String workerId, String followerId)
            throws WorkerNotFoundException, FollowerNotFoundException {

        Worker followed = workerRepository.getWorker(workerId);

        if (followed == null) {
            throw new WorkerNotFoundException();
        }

        Worker follower = workerRepository.getWorker(followerId);

        if (follower == null) {
            throw new FollowerNotFoundException();
        }

        socialNetworkRepository.addFollower(followed, follower);
    }

    public Iterator<Worker> getFollowings(String followerId)
            throws WorkerNotFoundException, NoFollowedException {

        Worker worker = workerRepository.getWorkerOrThrow(followerId);

        return socialNetworkRepository.getFollowingsOrThrow(worker);
    }

    public Iterator<Worker> getFollowers(String workerId) throws WorkerNotFoundException, NoFollowersException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);

        if (socialNetworkRepository.numFollowers(worker) == 0) {
            throw new NoFollowersException();
        }

        return socialNetworkRepository.getFollowers(worker);
    }

    public Iterator<Worker> recommendations(String followerId) throws WorkerNotFoundException, NoFollowedException {
        Worker worker = workerRepository.getWorkerOrThrow(followerId);
        return socialNetworkRepository.getRecommendationsOrThrow(worker);
    }

    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(String workerId, String issueTyoeID) throws WorkerNotFoundException, NoWorkerException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTyoeID);
        Iterator<Worker> workersByIssueType = issueType.getWorkers();

        return
                socialNetworkRepository.getUnfollowedWorkersWithAssignedIssueType(
                        worker,
                        workersByIssueType
                );
    }
}