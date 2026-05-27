package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.AbstractModel;

public interface StorageStrategy<T extends AbstractModel> {

    T get(String id);

    void put(T element);

    void remove(String id);

    boolean contains(String id);

    int size();

    Iterator<T> values();
}
