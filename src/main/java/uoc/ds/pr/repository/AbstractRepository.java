package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.AbstractModel;
import uoc.ds.pr.storage.StorageStrategy;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiConsumer;

public abstract class AbstractRepository<T extends AbstractModel>
        implements Repository<T> {

    private final StorageStrategy<T> storage;

    protected AbstractRepository(StorageStrategy<T> storage) {
        this.storage = storage;
    }

    @Override
    public T getById(String id) {
        return storage.get(id);
    }

    @Override
    public void save(T element) {
        storage.put(element);
    }

    @Override
    public void update(T element) {
    }

    @Override
    public void removeById(String id) {
        storage.remove(id);
    }

    @Override
    public boolean contains(String id) {
        return storage.contains(id);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public Iterator<T> values() {
        return storage.values();
    }

    protected T addElement(
            String id,
            Function<String, T> creator,
            BiConsumer<T, String[]> updater,
            String... values
    ) {
        T element = getById(id);

        if (element == null) {
            element = creator.apply(id);
            save(element);
        } else {
            updater.accept(element, values);
            update(element);
        }

        return element;
    }

    protected <E extends Exception> T getByIdOrThrow(
            String id,
            Supplier<E> exceptionSupplier
    ) throws E {
        T element = getById(id);

        if (element == null) {
            throw exceptionSupplier.get();
        }

        return element;
    }
}
