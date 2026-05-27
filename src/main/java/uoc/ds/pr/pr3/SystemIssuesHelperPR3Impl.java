package uoc.ds.pr.pr3;

import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.model.User;
import uoc.ds.pr.repository.*;

public class SystemIssuesHelperPR3Impl implements SystemIssuesHelperPR3 {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AssistanceRepository assistanceRepository;
    private final IssueTypeRepository issueTypeRepository;
    public SystemIssuesHelperPR3Impl(SystemIssuesPR3Impl systemIssues) {
        this.roomRepository = systemIssues.getRoomRepository();
        this.userRepository = systemIssues.getUserRepository();
        this.assistanceRepository = systemIssues.getAssistanceRepository();
        this.issueTypeRepository = systemIssues.getIssueTypeRepository();
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
        return issueType.getWorkers().size();
    }

    @Override
    public Assistance getAssistance(String assistanceId) {
        return assistanceRepository.getAssistance(assistanceId);
    }

    @Override
    public int numFollowers(String userId) {
        // TODO: Implement when follower functionality is added
        return 0;
    }

    @Override
    public int numFollowings(String userId) {
        // TODO: Implement when follower functionality is added
        return 0;
    }
}
