package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.AbstractModel;

public interface Repository<T extends AbstractModel> {

    T getById(String id);

    void save(T element);

    void update(T element);

    void removeById(String id);

    boolean contains(String id);

    int size();

    Iterator<T> values();
}