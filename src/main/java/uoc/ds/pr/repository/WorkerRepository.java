package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.AssistanceNotFoundException;
import uoc.ds.pr.exceptions.NoAssistanceException;
import uoc.ds.pr.exceptions.NoIssuesException;
import uoc.ds.pr.exceptions.WorkerNotFoundException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.Rating;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.util.OrderedVector;

import java.util.Comparator;

import static uoc.ds.pr.pr3.SystemIssuesPR3.TOP_N_BEST_WORKERS;

public class WorkerRepository extends AbstractRepository<Worker> {

    private Worker topWorker;
    private final OrderedVector<Worker> topWorkers;

    private static final Comparator<Worker> CMP_TOP_WORKERS =
            (w1, w2) -> {
                int cmp = Double.compare(
                        w1.getGlobalRating(),
                        w2.getGlobalRating()
                );

                if (cmp != 0) {
                    return cmp;
                }

                return w2.getId().compareTo(w1.getId());
            };

    public WorkerRepository() {
        super(new HashStorageStrategy<>());
        this.topWorkers = new OrderedVector<>(
                TOP_N_BEST_WORKERS,
                CMP_TOP_WORKERS
        );
    }

    public Worker addWorker(String id, String name, String address) {
        return addElement(
                id,
                key -> new Worker(key, name, address),
                (worker, data) -> worker.update(data[0], data[1]),
                name,
                address
        );
    }

    public Worker getWorker(String id) {
        return getById(id);
    }

    public Worker getWorkerOrThrow(String id) throws WorkerNotFoundException {
        return getByIdOrThrow(id, WorkerNotFoundException::new);
    }

    public int numWorkers() {
        return size();
    }

    public void updateTopWorker(Worker worker) {
        if (topWorker == null
                || worker.numSolvedIssues() > topWorker.numSolvedIssues()
                || (worker.numSolvedIssues() == topWorker.numSolvedIssues()
                    && worker.getId().compareTo(topWorker.getId()) < 0)) {
            topWorker = worker;
        }
    }

    public Worker getTopWorker() {
        return topWorker;
    }

    public void rateWorker(Worker worker, Rating rating) {
        if (worker == null) {
            return;
        }

        topWorkers.delete(worker);

        worker.addRate(rating);

        if (worker.hasRates()) {
            topWorkers.update(worker);
        }
    }

    public Issue solveIssue(String workerId)  throws WorkerNotFoundException, NoIssuesException {
        Worker w =  getWorkerOrThrow(workerId);
        if(!w.hasPendingIssues()){
            throw new NoIssuesException();
        }
        Issue issue = w.solveNextIssue();
        updateTopWorker(w);
        return issue;
    }

    public Assistance solveAssistance(String workerId) throws WorkerNotFoundException, NoAssistanceException {
        Worker worker = getWorkerOrThrow(workerId);
        Assistance assistance = worker.solveNextAssistance();

        if (assistance == null) {
            throw new NoAssistanceException();
        }

        return assistance;
    }

    public Iterator<Issue> doneIssues(String workerId) throws NoIssuesException {
        try {
            Worker w =  getWorkerOrThrow(workerId);
            if(w.numSolvedIssues() == 0){
                throw new NoIssuesException();
            }
            return w.getSolvedIssues();
        } catch (WorkerNotFoundException e) {
            throw new NoIssuesException();
        }
    }

    public int numRatedWorkers() {
        return topWorkers.size();
    }

    public Iterator<Worker> getTopNWorkers(int n) {
        return topWorkers.values();
    }
}