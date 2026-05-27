package uoc.ds.pr.pr3;

import uoc.ds.pr.model.*;
import uoc.ds.pr.repository.*;

public class SystemIssuesHelperPR3Impl implements SystemIssuesHelperPR3 {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AssistanceRepository assistanceRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final WorkerRepository workerRepository;
    private final WorkerSocialNetworkRepository workerSocialNetworkRepository;
    public SystemIssuesHelperPR3Impl(SystemIssuesPR3Impl systemIssues) {
        this.roomRepository = systemIssues.getRoomRepository();
        this.userRepository = systemIssues.getUserRepository();
        this.assistanceRepository = systemIssues.getAssistanceRepository();
        this.issueTypeRepository = systemIssues.getIssueTypeRepository();
        this.workerRepository = systemIssues.getWorkerRepository();
        this.workerSocialNetworkRepository = systemIssues.getWorkerSocialNetworkRepository();
    }

    @Override
    public int numRooms() {
        return roomRepository.numRooms();
    }

    @Override
    public Room getRoom(String id) {
        return roomRepository.getRoom(id);
    }

    @Override
    public int numUsers() {
        return userRepository.numUsers();
    }

    @Override
    public User getUser(String id) {
        return userRepository.getUser(id);
    }

    @Override
    public int numIssueTypes() {
        return issueTypeRepository.numIssueTypes();
    }

    @Override
    public IssueType getIssueType(String id) {
        return issueTypeRepository.getIssueType(id);
    }

    @Override
    public int numAssistances() {
        return assistanceRepository.numAssistances();
    }

    @Override
    public int numAssistancesByUser(String id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            return 0;
        }
        return user.getAssistancesCount();
    }

    @Override
    public int numWorkerByIssueType(String issueTypeId) {
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        if (issueType == null) {
            return 0;
        }
        return issueType.numWorkers();
    }

    @Override
    public Assistance getAssistance(String assistanceId) {
        return assistanceRepository.getAssistance(assistanceId);
    }

    @Override
    public int numFollowers(String userId) {
        Worker worker = workerRepository.getWorker(userId);

        if (worker == null) {
            return 0;
        }

        return workerSocialNetworkRepository.numFollowers(worker);
    }

    @Override
    public int numFollowings(String userId) {
        Worker worker = workerRepository.getWorker(userId);

        if (worker == null) {
            return 0;
        }

        return workerSocialNetworkRepository.numFollowings(worker);
    }
}
