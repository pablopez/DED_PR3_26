package uoc.ds.pr.pr2;

import uoc.ds.pr.model.Worker;
import uoc.ds.pr.model.System;
import  uoc.ds.pr.model.Component;

public interface SystemIssuesHelper {
    Worker getWorker(String id);
    int numWorkers();

    System getSystem(String id);
    int numSystems();

    Component getComponent(String id);
    int numComponents();

    int numComponentsBySystem(String systemId);

    int numIssues();

    int numIssuesByComponent(String componentId);

    int numIssuesByWorker(String workerId);

    int numSolvedAssistances(String workerId);

    int numAssignedAssistances(String workerId);
}
