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
    protected RoomRepository roomRepository;
    protected AssistanceRepository assistanceRepository;
    protected RatingRepository ratingRepository;

    public SystemIssuesPR3Impl() {
        super();
        roomRepository = new RoomRepository();
        assistanceRepository = new AssistanceRepository();
        ratingRepository = new RatingRepository();
    }

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }
    
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    public AssistanceRepository getAssistanceRepository() {
        return assistanceRepository;
    }
    
    public RatingRepository getRatingRepository() {
        return ratingRepository;
    }

    public WorkerSocialNetworkRepository getWorkerSocialNetworkRepository(){
        return workerSocialNetworkRepository;
    }
    
    @Override
    public void addRoom(String roomId, String name) {
        roomRepository.addRoom(roomId, name);
    }

    @Override
    public void addUser(String userId, String name, Role role, String phone) {
        userRepository.addUser(userId, name, role, phone);
    }

    @Override
    public void addIssueType(String issueTypeId, String name) {
        issueTypeRepository.addIssueType(issueTypeId, name);
    }

    @Override
    public void assignSystemToRoom(String systemId, String roomId) throws RoomNotFoundException, SystemNotFoundException {
        Room room = roomRepository.getRoomOrThrow(roomId);
        System system = systemRepository.getSystemOrThrow(systemId);
        system.setRoom(room);
    }

    @Override
    public void assignWorkerToIssueType(String workerId, String issueTypeId) throws IssueTypeNotFoundException, WorkerNotFoundException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        IssueType issueType = issueTypeRepository.getIssueTypeOrThrow(issueTypeId);
        worker.setIssueType(issueType);
    }

    @Override
    public void addAssistance(String assistanceId, String userId, String issueTypeId, String roomId, LocalDate date, String description) throws UserNotFoundException {
        User user = userRepository.getUserOrThrow(userId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        Room room = roomRepository.getRoom(roomId);
        Assistance assistance = assistanceRepository.addAssistance(assistanceId, user, issueType, room, date, description);
        userRepository.addAssistance(user, assistance);
    }

    @Override
    public void assignAssistance(String assistanceId) throws AssistanceNotFoundException, NoWorkerException {
        assistanceRepository.assignAssistance(assistanceId);
    }

    @Override
    public Assistance solveAssistance(String workerId) throws WorkerNotFoundException, NoAssistanceException {
        return workerRepository.solveAssistance(workerId);
    }

    @Override
    public void rateAssistance(String ratingId, String userId, String assistanceId, LocalDate date, int resolutionScore, int speedScore, int treatmentScore) throws AssistanceNotResolvedException, UserIsNotCreatorException {
        Assistance assistance = assistanceRepository.getAssistance(assistanceId);
        
        if (!assistance.isResolved()) {
            throw new AssistanceNotResolvedException();
        }
        
        if (!assistance.getUser().getId().equals(userId)) {
            throw new UserIsNotCreatorException();
        }
        Rating rating = ratingRepository.addRating(ratingId, assistance, date, resolutionScore, speedScore, treatmentScore);
        assistance.setRating(rating);
        Worker worker = assistance.getWorker();
        workerRepository.rateWorker(worker, rating);
    }

    @Override
    public Iterator<Worker> getWorkersByIssueType(String issueTypeId) throws NoWorkerException {
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        return issueType.getWorkers();
    }

    @Override
    public Iterator<Worker> getTop10BestWorkers() throws NoWorkerException {
        return workerRepository.getTopNWorkers();
    }

    @Override
    public Iterator<User> getTop5UsersWithMostAssistances() throws NoUserException {
        return userRepository.getTopAssistedUsers();
    }

    @Override
    public SystemIssuesHelperPR3 getSystemIssuesHelperPR3() {
        return new SystemIssuesHelperPR3Impl(this);
    }

    @Override
    public void addFollower(String workerID, String followerId) throws FollowerNotFoundException, WorkerNotFoundException {
        Worker worker = workerRepository.getWorkerOrThrow(workerID);
        Worker follower = workerRepository.getWorker(followerId);

        if (follower == null) {
            throw new FollowerNotFoundException();
        }

        workerSocialNetworkRepository.addFollower(worker, follower);
    }

    @Override
    public Iterator<Worker> getFollowers(String workerId) throws WorkerNotFoundException, NoFollowersException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);

        if (workerSocialNetworkRepository.numFollowers(worker) == 0) {
            throw new NoFollowersException();
        }

        return workerSocialNetworkRepository.getFollowers(worker);
    }

    @Override
    public Iterator<Worker> getFollowings(String followerId) throws WorkerNotFoundException, NoFollowedException {
        Worker worker = workerRepository.getWorkerOrThrow(followerId);
        return workerSocialNetworkRepository.getFollowingsOrThrow(worker);
    }

    @Override
    public Iterator<Worker> recommendations(String followerId) throws WorkerNotFoundException, NoFollowedException {
        Worker worker = workerRepository.getWorkerOrThrow(followerId);
        return workerSocialNetworkRepository.getRecommendationsOrThrow(worker);
    }

    @Override
    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(String workerId, String issueTyoeID) throws WorkerNotFoundException, NoWorkerException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTyoeID);
        Iterator<Worker> workersByIssueType = issueType.getWorkers();

        return
                workerSocialNetworkRepository.getUnfollowedWorkersWithAssignedIssueType(
                        worker,
                        workersByIssueType
                );
    }
}
