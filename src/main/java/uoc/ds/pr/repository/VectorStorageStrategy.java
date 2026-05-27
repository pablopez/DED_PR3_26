package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.AbstractModel;
import uoc.ds.pr.util.DSListArray;

public class VectorStorageStrategy<T extends AbstractModel>
        implements StorageStrategy<T> {

    private final DSListArray<T> data;

    public VectorStorageStrategy(int max) {
        this.data = new DSListArray<>(max);
    }

    @Override
    public T get(String id) {
        return data.get(id);
    }

    @Override
    public void put(T element) {
        data.put(element.getId(), element);
    }

    @Override
    public void remove(String id) {
        data.remove(id);
    }

    @Override
    public boolean contains(String id) {
        return data.get(id) != null;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Iterator<T> values() {
        return data.values();
    }
}