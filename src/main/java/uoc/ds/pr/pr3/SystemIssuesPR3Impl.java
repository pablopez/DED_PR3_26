package uoc.ds.pr.pr3;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.Role;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.model.System;
import uoc.ds.pr.pr2.SystemIssuesPR2Impl;
import uoc.ds.pr.repository.*;

import java.time.LocalDate;

public class SystemIssuesPR3Impl extends SystemIssuesPR2Impl implements SystemIssuesPR3 {

    SystemIssuesHelperPR3Impl helper;

    public SystemIssuesPR3Impl(Repositories repositories) {
        super(repositories);
        helper = new SystemIssuesHelperPR3Impl(repositories);
    }

    public SystemIssuesPR3Impl() {
        this(new Repositories());
    }
    
    @Override
    public void addRoom(String roomId, String name) {
        this.services.core().addRoom(roomId, name);
    }

    @Override
    public void addUser(String userId, String name, Role role, String phone) {
        this.services.core().addUser(userId, name, role, phone);
    }

    @Override
    public void addIssueType(String issueTypeId, String name) {
        this.services.core().addIssueType(issueTypeId, name);
    }

    @Override
    public void assignSystemToRoom(String systemId, String roomId) throws RoomNotFoundException, SystemNotFoundException {
        this.services.infra().assignSystemToRoom(systemId, roomId);
    }

    @Override
    public void assignWorkerToIssueType(String workerId, String issueTypeId) throws IssueTypeNotFoundException, WorkerNotFoundException {
        this.services.workerIssueTypes().assignWorkerToIssueType(workerId, issueTypeId);
    }

    @Override
    public void addAssistance(String assistanceId, String userId, String issueTypeId, String roomId, LocalDate date, String description) throws UserNotFoundException {
        this.services.assistances().addAssistance(assistanceId, userId, issueTypeId, roomId, date, description);
    }

    @Override
    public void assignAssistance(String assistanceId) throws AssistanceNotFoundException, NoWorkerException {
        this.services.assistances().assignAssistance(assistanceId);
    }

    @Override
    public Assistance solveAssistance(String workerId) throws WorkerNotFoundException, NoAssistanceException {
        return this.services.assistances().solveAssistance(workerId);
    }

    @Override
    public void rateAssistance(String ratingId, String userId, String assistanceId, LocalDate date, int resolutionScore, int speedScore, int treatmentScore) throws AssistanceNotResolvedException, UserIsNotCreatorException {
        this.services.assistances().rateAssistance(ratingId, userId, assistanceId, date, resolutionScore, speedScore, treatmentScore);
    }

    @Override
    public Iterator<Worker> getWorkersByIssueType(String issueTypeId) throws NoWorkerException {
        return this.services.workerIssueTypes().getWorkersByIssueType(issueTypeId);
    }

    @Override
    public Iterator<Worker> getTop10BestWorkers() throws NoWorkerException {
        return this.services.rankings().getTop10BestWorkers();
    }

    @Override
    public Iterator<User> getTop5UsersWithMostAssistances() throws NoUserException {
        return this.services.rankings().getTop5UsersWithMostAssistances();
    }

    @Override
    public SystemIssuesHelperPR3 getSystemIssuesHelperPR3() {
        return this.helper;
    }

    @Override
    public void addFollower(String workerID, String followerId) throws FollowerNotFoundException, WorkerNotFoundException {
        this.services.socialNetwork().addFollower(workerID, followerId);
    }

    @Override
    public Iterator<Worker> getFollowers(String workerId) throws WorkerNotFoundException, NoFollowersException {
       return this.services.socialNetwork().getFollowers(workerId);
    }

    @Override
    public Iterator<Worker> getFollowings(String followerId) throws WorkerNotFoundException, NoFollowedException {
        return this.services.socialNetwork().getFollowings(followerId);
    }

    @Override
    public Iterator<Worker> recommendations(String followerId) throws WorkerNotFoundException, NoFollowedException {
        return this.services.socialNetwork().recommendations(followerId);
    }

    @Override
    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(String workerId, String issueTyoeID) throws WorkerNotFoundException, NoWorkerException {
        return this.services.socialNetwork().getUnfollowedWorkersWithAssignedIssueType(workerId, issueTyoeID);
    }
}
