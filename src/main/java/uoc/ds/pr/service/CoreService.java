package uoc.ds.pr.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.Role;
import uoc.ds.pr.exceptions.ComponentNotFoundException;
import uoc.ds.pr.exceptions.UserNotFoundException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.model.User;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.model.System;
import uoc.ds.pr.repository.AssistanceRepository;
import uoc.ds.pr.repository.ComponentRepository;
import uoc.ds.pr.repository.IssueRepository;
import uoc.ds.pr.repository.IssueTypeRepository;
import uoc.ds.pr.repository.RoomRepository;
import uoc.ds.pr.repository.SystemRepository;
import uoc.ds.pr.repository.UserRepository;
import uoc.ds.pr.repository.WorkerRepository;
import uoc.ds.pr.repository.WorkerSocialNetworkRepository;

public class CoreService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final SystemRepository systemRepository;
    private final ComponentRepository componentRepository;
    private final IssueRepository issueRepository;
    private final AssistanceRepository assistanceRepository;
    private final WorkerSocialNetworkRepository socialNetworkRepository;

    public CoreService(
            WorkerRepository workerRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            IssueTypeRepository issueTypeRepository,
            SystemRepository systemRepository,
            ComponentRepository componentRepository,
            IssueRepository issueRepository,
            AssistanceRepository assistanceRepository,
            WorkerSocialNetworkRepository socialNetworkRepository
    ) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.systemRepository = systemRepository;
        this.componentRepository = componentRepository;
        this.issueRepository = issueRepository;
        this.assistanceRepository = assistanceRepository;
        this.socialNetworkRepository = socialNetworkRepository;
    }

    public User addUser(
            String userId,
            String name,
            Role role,
            String phone
    ) {
        return userRepository.addUser(userId, name, role, phone);
    }

    public Worker addWorker(
            String workerId,
            String name,
            String address
    ) {
        Worker worker = workerRepository.addWorker(workerId, name, address);
        socialNetworkRepository.addWorker(worker);
        return worker;
    }

    public Room addRoom(
            String roomId,
            String name
    ) {
        return roomRepository.addRoom(roomId, name);
    }

    public IssueType addIssueType(
            String issueTypeId,
            String description
    ) {
        return issueTypeRepository.addIssueType(issueTypeId, description);
    }

    public System addSystem(
            String systemId,
            String description,
            String location,
            String userId
    ) throws UserNotFoundException {

        User user = userRepository.getUserOrThrow(userId);

        return systemRepository.addSystem(
                systemId,
                description,
                location,
                user
        );
    }

    public Component addComponent(
            String componentId,
            String trademark,
            String model,
            String serialNumber
    ) {
       return componentRepository.addComponent(
                componentId,
                trademark,
                model,
                serialNumber
        );
    }

    public Issue addIssue(
            String issueId,
            String componentId,
            String issueTypeId,
            String description,
            LocalDateTime date
    ) throws ComponentNotFoundException {

        Component component =
                componentRepository.getComponentOrThrow(componentId);

        IssueType issueType =
                issueTypeRepository.getIssueType(issueTypeId);

        return issueRepository.addIssue(
                issueId,
                component,
                issueType,
                description,
                date
        );
    }

    public Assistance addAssistance(
            String assistanceId,
            String userId,
            String issueTypeId,
            String roomId,
            LocalDate date,
            String description
    ) throws UserNotFoundException {

        User user = userRepository.getUserOrThrow(userId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        Room room = roomRepository.getRoom(roomId);

        Assistance assistance = assistanceRepository.addAssistance(
                assistanceId,
                user,
                issueType,
                room,
                date,
                description
        );

        userRepository.addAssistance(user, assistance);

        return assistance;
    }
}