package uoc.ds.pr.pr3;

import uoc.ds.pr.model.*;
import uoc.ds.pr.repository.*;

public class SystemIssuesHelperPR3Impl implements SystemIssuesHelperPR3 {
    private final Repositories repositories;

    public SystemIssuesHelperPR3Impl(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public int numRooms() {
        return repositories.rooms().numRooms();
    }

    @Override
    public Room getRoom(String id) {
        return repositories.rooms().getRoom(id);
    }

    @Override
    public int numUsers() {
        return repositories.users().numUsers();
    }

    @Override
    public User getUser(String id) {
        return repositories.users().getUser(id);
    }

    @Override
    public int numIssueTypes() {
        return repositories.issueTypes().numIssueTypes();
    }

    @Override
    public IssueType getIssueType(String id) {
        return repositories.issueTypes().getIssueType(id);
    }

    @Override
    public int numAssistances() {
        return repositories.assistances().numAssistances();
    }

    @Override
    public int numAssistancesByUser(String id) {
        User user = repositories.users().getUser(id);
        if (user == null) {
            return 0;
        }
        return user.getAssistancesCount();
    }

    @Override
    public int numWorkerByIssueType(String issueTypeId) {
        IssueType issueType = repositories.issueTypes().getIssueType(issueTypeId);
        if (issueType == null) {
            return 0;
        }
        return issueType.numWorkers();
    }

    @Override
    public Assistance getAssistance(String assistanceId) {
        return repositories.assistances().getAssistance(assistanceId);
    }

    @Override
    public int numFollowers(String userId) {
        Worker worker = repositories.workers().getWorker(userId);

        if (worker == null) {
            return 0;
        }

        return repositories.socialNetwork().numFollowers(worker);
    }

    @Override
    public int numFollowings(String userId) {
        Worker worker = repositories.workers().getWorker(userId);

        if (worker == null) {
            return 0;
        }

        return repositories.socialNetwork().numFollowings(worker);
    }
}
