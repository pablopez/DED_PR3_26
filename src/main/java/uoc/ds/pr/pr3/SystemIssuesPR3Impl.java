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
        System system = systemRepository.getSystem(systemId);
        if (system == null) {
            throw new SystemNotFoundException();
        }
        system.setRoom(room);
    }

    @Override
    public void assignWorkerToIssueType(String workerId, String issueTypeId) throws IssueTypeNotFoundException, WorkerNotFoundException {
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        IssueType issueType = issueTypeRepository.getIssueTypeOrThrow(issueTypeId);
        
        // If worker already has an issue type, remove from that issue type's workers list
        IssueType existingIssueType = worker.getIssueType();
        if (existingIssueType != null) {
            existingIssueType.removeWorker(worker);
        }
        
        // Assign new issue type to worker and add to workers list
        worker.setIssueType(issueType);
        issueType.addWorker(worker);
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
    public Assistance solveAssistance(String workerId) throws WorkerNotFoundException, AssistanceNotFoundException {
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
        
        // Create and store rating
        Rating rating = ratingRepository.addRating(ratingId, assistance, date, resolutionScore, speedScore, treatmentScore);
        
        // Set rating on assistance using the average from Rating
        assistance.setRating(rating);

        Worker worker = assistance.getWorker();

        // Add rating to worker and update top workers
        workerRepository.rateWorker(worker, rating);
    }

    @Override
    public Iterator<Worker> getWorkersByIssueType(String issueTypeId) throws NoWorkerException {
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        if (issueType == null || issueType.numWorkers() == 0) {
            throw new NoWorkerException();
        }
        return issueType.getWorkers().values();
    }

    @Override
    public Iterator<Worker> getTop10BestWorkers() throws NoWorkerException {
        if (workerRepository.numRatedWorkers() == 0) {
            throw new NoWorkerException();
        }

        return workerRepository.getTopNWorkers(10);
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

    }

    @Override
    public Iterator<Worker> getFollowers(String workerId) throws WorkerNotFoundException, NoFollowersException {
        return null;
    }

    @Override
    public Iterator<Worker> getFollowings(String followerId) throws WorkerNotFoundException, NoFollowedException {
        return null;
    }

    @Override
    public Iterator<Worker> recommendations(String followerId) throws WorkerNotFoundException, NoFollowedException {
        return null;
    }

    @Override
    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(String workerId, String issueTyoeID) throws WorkerNotFoundException, NoWorkerException {
        return null;
    }
}
