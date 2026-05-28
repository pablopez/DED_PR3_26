package uoc.ds.pr.pr2;

import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.System;
import uoc.ds.pr.model.Worker;

public class SystemIssuesHelperImpl implements SystemIssuesHelper {
    private final SystemIssuesPR2Impl systemIssues;

    public SystemIssuesHelperImpl(SystemIssuesPR2Impl computerProjects) {
        this.systemIssues = computerProjects;
    }


    @Override
    public Worker getWorker(String id) {
        return systemIssues.workerRepository.getWorker(id);
    }

    @Override
    public int numWorkers() {
        return systemIssues.workerRepository.numWorkers();
    }

    @Override
    public System getSystem(String id) {
        return systemIssues.systemRepository.getSystem(id);
    }

    @Override
    public int numSystems() {
        return systemIssues.systemRepository.numSystems();
    }

    @Override
    public Component getComponent(String id) {
        return systemIssues.componentRepository.getComponent(id);
    }

    @Override
    public int numComponents() {
        return systemIssues.componentRepository.numComponents();
    }

    @Override
    public int numComponentsBySystem(String systemId) {

        System system =  systemIssues.systemRepository.getSystem(systemId);
        return system.numComponents();
    }

    @Override
    public int numIssues() {
        return systemIssues.issueRepository.numIssues();
    }

    @Override
    public int numIssuesByComponent(String componentId) {
        Component component =  systemIssues.componentRepository.getComponent(componentId);
        return component.getIssuesCont();
    }

    @Override
    public int numIssuesByWorker(String workerId) {
        Worker worker =  systemIssues.workerRepository.getWorker(workerId);
        return worker.numPendingIssues();
    }

    @Override
    public int numSolvedAssistances(String workerId) {
        Worker worker = systemIssues.workerRepository.getWorker(workerId);
        return worker.numSolvedAssistances();
    }

    @Override
    public int numAssignedAssistances(String workerId) {
        Worker worker = systemIssues.workerRepository.getWorker(workerId);
        return worker.numAssignedAssistances();
    }


}
