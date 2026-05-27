package uoc.ds.pr.util;

import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Assistance;

import java.util.Comparator;

public class AssignedAssistanceQueue {
    private final PriorityQueue<Assistance> queue;

    public AssignedAssistanceQueue() {
        this.queue = new PriorityQueue<>(new AssistanceComparator());
    }

    public void add(Assistance assistance) {
        queue.add(assistance);
    }

    public Assistance poll() {
        return queue.poll();
    }

    public Assistance peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public Iterator<Assistance> values() {
        return queue.values();
    }

    private static class AssistanceComparator implements Comparator<Assistance> {

        @Override
        public int compare(Assistance a1, Assistance a2) {
            int rank1 = a1.getUser().getRole().getRank();
            int rank2 = a2.getUser().getRole().getRank();

            // La PriorityQueue devuelve primero el elemento "menor".
            // Queremos que el mayor rango salga antes.
            if (rank1 != rank2) {
                return rank2 - rank1;
            }

            // Si empatan en rango, primero la fecha más antigua.
            int dateComparison = a1.getDate().compareTo(a2.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }

            // Si empatan en fecha, primero el id menor.
            return a1.getId().compareTo(a2.getId());
        }
    }
}
