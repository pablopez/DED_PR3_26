package uoc.ds.pr.pr3;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.Role;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.User;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.pr2.SystemIssues;

import java.time.LocalDate;

public interface SystemIssuesPR3 extends SystemIssues {
    public static final int MAX_ROOMS = 125;
    public static final int MAX_ISSUE_TYPES= 25;
    public static final int MAX_RATING = 225;
    public static final int TOP_N_BEST_WORKERS = 10;
    public static final int TOP_N_USERS_MOST_ASSISTANCES = 5;


    void addRoom(String roomId, String name);

    void addUser(String userId, String name, Role role, String phone);

    void addIssueType(String issueTypeId, String name);

    void assignSystemToRoom(String systemId, String roomId) throws RoomNotFoundException, SystemNotFoundException;

    void assignWorkerToIssueType(String workerId, String issueTypeId) throws IssueTypeNotFoundException, WorkerNotFoundException;

    void addAssistance(String assistanceId, String userId, String issueTypeId,
                       String roomId, LocalDate date, String description) throws UserNotFoundException;

    void assignAssistance(String assistanceId) throws AssistanceNotFoundException, NoWorkerException;

    Assistance solveAssistance(String workerId) throws  WorkerNotFoundException, NoAssistanceException;

    void rateAssistance(String ratingId, String userId, String assistanceId,
                        LocalDate date, int resolutionScore, int speedScore, int treatmentScore)
            throws AssistanceNotResolvedException, UserIsNotCreatorException;

    Iterator<Worker> getWorkersByIssueType(String issueTypeId) throws NoWorkerException;

    Iterator<Worker> getTop10BestWorkers() throws NoWorkerException;

    Iterator<User> getTop5UsersWithMostAssistances() throws NoUserException;

    SystemIssuesHelperPR3 getSystemIssuesHelperPR3();

    public void addFollower(String workerID, String followerId) throws FollowerNotFoundException, WorkerNotFoundException;
    public Iterator<Worker> getFollowers(String workerId) throws WorkerNotFoundException, NoFollowersException;
    public Iterator<Worker> getFollowings(String followerId) throws WorkerNotFoundException, NoFollowedException;

    public Iterator<Worker> recommendations(String followerId) throws WorkerNotFoundException, NoFollowedException;

    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(String workerId, String issueTyoeID) throws WorkerNotFoundException, NoWorkerException;



}
