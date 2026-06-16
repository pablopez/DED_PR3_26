package uoc.ds.pr.pr2;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.model.System;
import uoc.ds.pr.repository.*;
import uoc.ds.pr.service.Services;

import java.time.LocalDateTime;


public class SystemIssuesPR2Impl implements SystemIssues {
    protected final Repositories repositories;
    protected final Services services;
    private final SystemIssuesHelper helper;

    protected SystemIssuesPR2Impl(Repositories repositories) {
        this.repositories = repositories;
        this.services = new Services(repositories);
        this.helper = new SystemIssuesHelperImpl(repositories);
    }

    @Override
    public void addWorker(String workerId, String name, String address) {
        this.services.core().addWorker(workerId, name, address);
    }

    @Override
    public void addSystem(String systemId, String description, String location, String userId) throws UserNotFoundException {
        this.services.core().addSystem(systemId, description, location, userId);
    }

    @Override
    public void addComponent(String componentId, String trademark, String model, String serial) {
        this.services.core().addComponent(componentId, trademark, model, serial);

    }

    @Override
    public void installComponentToSystem(String componentId, String systemId) throws ComponentAlreadyInstalledException {
        this.services.infra().installComponentToSystem(componentId, systemId);
    }

    @Override
    public Issue createIssue(String issueId, String componentId, String issueTypeId, String description, LocalDateTime dateTime) throws ComponentNotFoundException {
        return this.services.core().addIssue(issueId, componentId, issueTypeId, description, dateTime);
    }

    @Override
    public void assignIssue(String issueId, String workerId) throws IssueNotFoundException,  WorkerNotFoundException,
        IssueAlreadyAssignedException, IssueAlreadyResolvedException {
        this.services.issues().assignIssue(issueId, workerId);
    }

    @Override
    public Issue solveIssue(String workerId) throws WorkerNotFoundException, NoIssuesException {
        return this.services.issues().solveIssue(workerId);
    }

    @Override
    public Iterator<System> getSystems() throws NoSystemsException {
        return  this.services.infra().getSystems();
    }

    @Override
    public Iterator<Component> getComponentsBySystem(String systemId) throws SystemHasNoComponentsException {
        return this.services.infra().getComponentsBySystem(systemId);
    }

    @Override
    public Iterator<Issue> getDoneIssuesByWorker(String workerId) throws NoIssuesException {
       return this.services.issues().getDoneIssuesByWorker(workerId);
    }

    @Override
    public Worker getTopWorker() throws NoWorkerException {
        return this.services.rankings().getTopWorker();
    }

    @Override
    public System  getSystemWithMostComponents() throws NoSystemsException {
        return this.services.rankings().getSystemWithMostComponents();
    }

    @Override
    public SystemIssuesHelper getSystemIssuesHelper() {
        return this.helper;
    }
}