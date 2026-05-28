package uoc.ds.pr.pr2;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.model.System;
import uoc.ds.pr.repository.*;

import java.time.LocalDateTime;


public class SystemIssuesPR2Impl implements SystemIssues {
    protected WorkerRepository workerRepository;
    protected UserRepository userRepository;
    protected SystemRepository systemRepository;
    protected ComponentRepository componentRepository;
    protected IssueRepository issueRepository;
    protected IssueTypeRepository issueTypeRepository;
    protected WorkerSocialNetworkRepository workerSocialNetworkRepository;

    public SystemIssuesPR2Impl() {
        workerRepository = new WorkerRepository();
        systemRepository = new SystemRepository();
        userRepository = new UserRepository();
        componentRepository = new ComponentRepository();
        issueRepository = new IssueRepository();
        issueTypeRepository = new IssueTypeRepository();
        workerSocialNetworkRepository = new WorkerSocialNetworkRepository();
    }

    public WorkerRepository getWorkerRepository() {
        return workerRepository;
    }

    public SystemRepository getSystemRepository() {
        return systemRepository;
    }

    public ComponentRepository getComponentRepository() {
        return componentRepository;
    }

    public IssueRepository getIssueRepository() {
        return issueRepository;
    }

    public IssueTypeRepository getIssueTypeRepository() {
        return issueTypeRepository;
    }



    @Override
    public void addWorker(String workerId, String name, String address) {
        Worker w = workerRepository.addWorker(workerId, name, address);
        workerSocialNetworkRepository.addWorker(w);
    }

    @Override
    public void addSystem(String systemId, String description, String location, String userId) throws UserNotFoundException {
        User user = userRepository.getUserOrThrow(userId);
        systemRepository.addSystem(systemId, description, location, user);

    }

    @Override
    public void addComponent(String componentId, String trademark, String model, String serial) {
        componentRepository.addComponent(componentId, trademark, model, serial);

    }

    @Override
    public void installComponentToSystem(String componentId, String systemId) throws ComponentAlreadyInstalledException {
        Component component = componentRepository.getComponent(componentId);
        systemRepository.addComponent(systemId, component);
    }

    @Override
    public Issue createIssue(String issueId, String componentId, String issueTypeId, String description, LocalDateTime dateTime) throws ComponentNotFoundException {
        Component component = componentRepository.getComponentOrThrow(componentId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        return issueRepository.addIssue(issueId, component, issueType, description, dateTime);
    }

    @Override
    public void assignIssue(String issueId, String workerId) throws IssueNotFoundException,  WorkerNotFoundException,
        IssueAlreadyAssignedException, IssueAlreadyResolvedException {
        Issue issue = issueRepository.getIssueOrThrow(issueId);
        Worker worker = workerRepository.getWorkerOrThrow(workerId);
        issueRepository.assignIssue(issue, worker);
    }

    @Override
    public Issue solveIssue(String workerId) throws WorkerNotFoundException, NoIssuesException {
        return workerRepository.solveIssue(workerId);
    }

    @Override
    public Iterator<System> getSystems() throws NoSystemsException {
        return  systemRepository.getSystems();
    }

    @Override
    public Iterator<Component> getComponentsBySystem(String systemId) throws SystemHasNoComponentsException {
        return systemRepository.getComponentsBySystem(systemId);
    }

    @Override
    public Iterator<Issue> getDoneIssuesByWorker(String workerId) throws NoIssuesException {
       return workerRepository.doneIssues(workerId);
    }

    @Override
    public Worker getTopWorker() throws NoWorkerException {
        return workerRepository.getTopWorker();
    }

    @Override
    public System  getSystemWithMostComponents() throws NoSystemsException {
        return systemRepository.getSystemWithMostComponents();
    }

    @Override
    public SystemIssuesHelper getSystemIssuesHelper() {
        return new SystemIssuesHelperImpl(this);
    }
}