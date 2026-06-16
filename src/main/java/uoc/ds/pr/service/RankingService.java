package uoc.ds.pr.service;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.NoRatedWorkerException;
import uoc.ds.pr.exceptions.NoSystemsException;
import uoc.ds.pr.exceptions.NoUserException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.model.System;
import uoc.ds.pr.model.User;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.repository.SystemRepository;
import uoc.ds.pr.repository.UserRepository;
import uoc.ds.pr.repository.WorkerRepository;

public class RankingService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final SystemRepository systemRepository;

    public RankingService(
            WorkerRepository workerRepository,
            UserRepository userRepository,
            SystemRepository systemRepository
    ) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
        this.systemRepository = systemRepository;
    }

    public Iterator<Worker> getTop10BestWorkers()
            throws NoWorkerException {
        return workerRepository.getTopNWorkers();
    }

    public Iterator<User> getTop5UsersWithMostAssistances()
            throws NoUserException {
        return userRepository.getTopAssistedUsers();
    }

    public System getSystemWithMostComponents()
            throws NoSystemsException {

        System system =
                systemRepository.getSystemWithMostComponents();

        if (system == null) {
            throw new NoSystemsException();
        }

        return system;
    }
}