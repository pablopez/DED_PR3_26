package uoc.ds.pr.model;

import uoc.ds.pr.util.RoundRobinList;

public class IssueType extends AbstractModel {
    private String name;
    private final RoundRobinList<Worker> workers;

    public IssueType(String id, String name) {
        super(id);
        setName(name);
        this.workers = new RoundRobinList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void removeWorker(Worker worker) {
        workers.delete(worker);
    }

    public int numWorkers() {
        return workers.size();
    }

    public RoundRobinList<Worker> getWorkers() {
        return workers;
    }

    public Worker nextWorkerRoundRobin() {
        if (workers.size() == 0) return null;
        if (!workers.isInitialized()) {
            workers.init();
        }
        return workers.next();
    }

}