package uoc.ds.pr.repository;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraphImpl;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.util.DSLinkedList;

public class WorkerSocialNetworkRepository {

    private final DirectedGraphImpl<Worker, String> graph;

    public WorkerSocialNetworkRepository() {
        this.graph = new DirectedGraphImpl<>();
    }

    public void addWorker(Worker worker) {
        if (worker != null && graph.getVertex(worker) == null) {
            graph.newVertex(worker);
        }
    }

    public void addFollower(Worker followed, Worker follower) {
        addWorker(followed);
        addWorker(follower);

        Vertex<Worker> followedVertex = graph.getVertex(followed);
        Vertex<Worker> followerVertex = graph.getVertex(follower);

        if (graph.getEdge(followerVertex, followedVertex) == null) {
            graph.newEdge(followerVertex, followedVertex);
        }
    }

    public boolean isFollower(Worker possibleFollower, Worker followed) {
        Vertex<Worker> followerVertex = graph.getVertex(possibleFollower);
        Vertex<Worker> followedVertex = graph.getVertex(followed);

        if (followerVertex == null || followedVertex == null) {
            return false;
        }

        return graph.getEdge(followerVertex, followedVertex) != null;
    }

    public Iterator<Worker> getFollowers(Worker worker) {
        Vertex<Worker> vertex = graph.getVertex(worker);

        if (vertex == null) {
            return emptyIterator();
        }

        Iterator<Edge<String, Worker>> edges =
                graph.edgedWithDestination(vertex);

        return sources(edges);
    }

    public Iterator<Worker> getFollowings(Worker worker) {
        Vertex<Worker> vertex = graph.getVertex(worker);

        if (vertex == null) {
            return emptyIterator();
        }

        Iterator<Edge<String, Worker>> edges =
                graph.edgesWithSource(vertex);

        return destinations(edges);
    }

    public int numFollowers(Worker worker) {
        return count(getFollowers(worker));
    }

    public int numFollowings(Worker worker) {
        return count(getFollowings(worker));
    }

    public Iterator<Worker> recommendations(Worker worker) {
        DSLinkedList<Worker> result = new DSLinkedList<>();

        Iterator<Worker> directFollowers = getFollowers(worker);

        while (directFollowers.hasNext()) {
            Worker directFollower = directFollowers.next();

            Iterator<Worker> secondLevelFollowers =
                    getFollowers(directFollower);

            while (secondLevelFollowers.hasNext()) {
                Worker candidate = secondLevelFollowers.next();

                if (!candidate.getId().equals(worker.getId())
                        && !isFollower(candidate, worker)
                        && result.get(candidate.getId()) == null) {
                    result.put(candidate.getId(), candidate);
                }
            }
        }

        return result.values();
    }

    public Iterator<Worker> getUnfollowedWorkersWithAssignedIssueType(
            Worker worker,
            Iterator<Worker> workersByIssueType
    ) {
        DSLinkedList<Worker> result = new DSLinkedList<>();

        while (workersByIssueType.hasNext()) {
            Worker candidate = workersByIssueType.next();

            if (!candidate.getId().equals(worker.getId())
                    && !isFollower(candidate, worker)
                    && result.get(candidate.getId()) == null) {
                result.put(candidate.getId(), candidate);
            }
        }

        return result.values();
    }

    private Iterator<Worker> sources(Iterator<Edge<String, Worker>> edges) {
        return new Iterator<Worker>() {

            @Override
            public boolean hasNext() {
                return edges.hasNext();
            }

            @Override
            public Worker next() {
                DirectedEdge<String, Worker> edge =
                        (DirectedEdge<String, Worker>) edges.next();

                return edge.getVertexSrc().getValue();
            }
        };
    }

    private Iterator<Worker> destinations(Iterator<Edge<String, Worker>> edges) {
        return new Iterator<Worker>() {

            @Override
            public boolean hasNext() {
                return edges.hasNext();
            }

            @Override
            public Worker next() {
                DirectedEdge<String, Worker> edge =
                        (DirectedEdge<String, Worker>) edges.next();

                return edge.getVertexDst().getValue();
            }
        };
    }

    private int count(Iterator<Worker> iterator) {
        int count = 0;

        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        return count;
    }

    private Iterator<Worker> emptyIterator() {
        return new Iterator<Worker>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Worker next() {
                return null;
            }
        };
    }
}
