package uoc.ds.pr.pr2;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Component;

import java.time.LocalDateTime;

import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.System;
import uoc.ds.pr.model.Worker;

/**
 * Interface representing the System Issues Management System (ADT SystemIssues).
 * Manages the registration and relationship between workers, systems, components, and issues.
 */
public interface SystemIssues {

    public static final int MAX_WORKERS = 35;
    public static final int MAX_SYSTEMS= 150;
    public static final int MAX_COMPONENTS = 110;

    /**
     * @pre True.
     * @post If the worker identifier is new, the total number of workers increases by one.
     * Otherwise, the existing worker's data is updated.
     * @param workerId Unique identifier for the worker.
     * @param name Full name of the worker.
     * @param address Physical address of the worker.
     */
    void addWorker(String workerId, String name, String address);

    /**
     * @pre True.
     * @post If the system identifier is new, the total number of systems increases by one.
     * Otherwise, the existing system's data is updated.
     * @param systemId Unique identifier for the system.
     * @param description Brief description of the system.
     * @param location Physical location of the system.
     * @param userId identifier for the user
     */
    void addSystem(String systemId, String description, String location, String userId) throws UserNotFoundException;

    /**
     * @pre True.
     * @post If the component identifier is new, the total number of components increases by one.
     * Otherwise, the existing component's data is updated.
     * @param componentId Unique identifier for the component.
     * @param trademark Brand or trademark of the component.
     * @param model Model name or number.
     * @param serial Serial number of the component unit.
     */
    void addComponent(String componentId, String trademark, String model, String serial);

    /**
     * @pre The component and the system both exist.
     * @post The component is added to the system's component list.
     * The system associated with the component is updated.
     * If the component is already present, an error is indicated.
     * @param componentId The ID of the component to install.
     * @param systemId The ID of the target system.
     * @throws ComponentAlreadyInstalledException If the component is already installed or entities do not exist.
     */
    void installComponentToSystem(String componentId, String systemId) throws ComponentAlreadyInstalledException;

    /**
     * @pre The issue report does not exist.
     * @post An issue report is added to the general set and to the component's issue list.
     * The component associated with the issue is updated.
     * If the component does not exist, an error is indicated.
     * @param issueId Unique identifier for the issue.
     * @param componentId ID of the component related to the issue.
     * @param description Textual description of the problem.
     * @param dateTime Date and time when the issue was created.
     * @throws ComponentNotFoundException if the component is missing.
     */
    Issue createIssue(String issueId, String componentId, String issueTypeId, String description, LocalDateTime dateTime) throws ComponentNotFoundException;

    /**
     * @pre True.
     * @post The issue is pushed (stacked) onto the worker's assigned tasks.
     * The worker associated with the issue is updated.
     * If the issue or the worker does not exist, an error is indicated.
     * @param issueId The ID of the issue to assign.
     * @param workerId The ID of the worker responsible for the issue.
     * @throws IssueNotFoundException If the issue cannot be found.
     * @throws WorkerNotFoundException If the worker cannot be found.
     * @throws IssueAlreadyAssignedException If the issue is already assigned.
     * @throws IssueAlreadyResolvedException if the issue is already resolved
     */
    void assignIssue(String issueId, String workerId) throws IssueNotFoundException,  WorkerNotFoundException, IssueAlreadyAssignedException, IssueAlreadyResolvedException;

    /**
     * @pre True.
     * @post The most recently assigned issue (LIFO) is popped from the assigned tasks
     * and added to the worker's list of completed issues.
     * If the worker does not exist or has no assigned issues, an error is indicated.
     * @param workerId The ID of the worker solving the issue.
     * @throws WorkerNotFoundException If the worker is missing
     * @throws NoIssuesException if the issues stack is empty.
     */
    Issue solveIssue(String workerId) throws WorkerNotFoundException, NoIssuesException;

    /**
     * @pre True.
     * @post Returns an iterator over all systems.
     * If no systems exist, an error is indicated.
     * @return Iterator of registered System objects.
     * @throws NoSystemsException If the system registry is empty.
     */
    Iterator<System> getSystems() throws NoSystemsException;

    /**
     * @pre The system exists.
     * @post Returns an iterator over all components within a specific system.
     * If there are no components, an error is indicated.
     * @param systemId The ID of the system to query.
     * @return Iterator of Component objects belonging to the system.
     * @throws SystemHasNoComponentsException If has no components.
     */
    Iterator<Component> getComponentsBySystem(String systemId) throws SystemHasNoComponentsException;

    /**
     * @pre The worker exists.
     * @post Returns an iterator over all issues completed by a specific worker.
     * If there are no completed issues, an error is indicated.
     * @param workerId The ID of the worker to query.
     * @return Iterator of Issue objects resolved by the worker.
     * @throws NoIssuesException If the worker has no resolved issues.
     */
    Iterator<Issue> getDoneIssuesByWorker(String workerId) throws NoIssuesException;

    /**
     * @pre True.
     * @post Returns the worker who has completed the most issues.
     * If none exist, an error is indicated.
     * @return The Worker object with the highest number of resolved issues.
     * @throws NoWorkerException If there are no workers
     */
    Worker getTopWorker() throws NoWorkerException;

    /**
     * @pre True.
     * @post Returns the system that contains the most components.
     * If none exist, an error is indicated.
     * @return The System object with the largest number of components.
     * @throws NoSystemsException If no systems are registered.
     */
    System  getSystemWithMostComponents() throws NoSystemsException;

    /**
     * retrieve  an instance of the SystemIssuesHelper to provide
     * auxiliary management and utility functions.
     * @return the SystemIssuesHelper instance
     */
    SystemIssuesHelper getSystemIssuesHelper();

}