package uoc.ds.pr.pr3;

import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.model.User;

public interface SystemIssuesHelperPR3 {
    public int numRooms();

    Room getRoom(String id);

    int numUsers();

    User getUser(String u67);

    int numIssueTypes();

    IssueType getIssueType(String id);

    int numAssistances();

    int numAssistancesByUser(String id);

    int numWorkerByIssueType(String issueTypeId);

    Assistance getAssistance(String assistanceId);

    int numFollowers(String userId);

    int numFollowings(String userId);
}
