package uoc.ds.pr.pr2;

import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.System;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.Repositories;

public class SystemIssuesHelperImpl implements SystemIssuesHelper {
    private final Repositories repositories;

    public SystemIssuesHelperImpl(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public Worker getWorker(String id) {
        return repositories.workers().getWorker(id);
    }

    @Override
    public int numWorkers() {
        return repositories.workers().numWorkers();
    }

    @Override
    public System getSystem(String id) {
        return repositories.systems().getSystem(id);
    }

    @Override
    public int numSystems() {
        return repositories.systems().numSystems();
    }

    @Override
    public Component getComponent(String id) {
        return repositories.components().getComponent(id);
    }

    @Override
    public int numComponents() {
        return repositories.components().numComponents();
    }

    @Override
    public int numComponentsBySystem(String systemId) {

        System system =  repositories.systems().getSystem(systemId);
        return system.numComponents();
    }

    @Override
    public int numIssues() {
        return repositories.issues().numIssues();
    }

    @Override
    public int numIssuesByComponent(String componentId) {
        Component component =
                repositories.components().getComponent(componentId);
        return component.getIssuesCont();
    }

    @Override
    public int numIssuesByWorker(String workerId) {
        Worker worker = repositories.workers().getWorker(workerId);
        return worker.numPendingIssues();
    }

    @Override
    public int numSolvedAssistances(String workerId) {
        Worker worker = repositories.workers().getWorker(workerId);
        return worker.numSolvedAssistances();
    }

    @Override
    public int numAssignedAssistances(String workerId) {
        Worker worker = repositories.workers().getWorker(workerId);
        return worker.numAssignedAssistances();
    }


}
