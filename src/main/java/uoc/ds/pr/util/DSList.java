package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;

public interface DSList<E> {
    void put(String id, E element);
    E get(String id);
    void remove(String id);
    int size();
    Iterator<E> values();
}
