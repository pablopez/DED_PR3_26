package uoc.ds.pr.storage;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.AbstractModel;

public class AVLStorageStrategy<T extends AbstractModel>
        implements StorageStrategy<T> {

    private final DictionaryAVLImpl<String, T> data;

    public AVLStorageStrategy() {
        this.data = new DictionaryAVLImpl<>();
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
        data.delete(id);
    }

    @Override
    public boolean contains(String id) {
        return data.containsKey(id);
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