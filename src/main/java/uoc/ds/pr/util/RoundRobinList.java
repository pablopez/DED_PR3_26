package uoc.ds.pr.util;
import edu.uoc.ds.traversal.Iterator;

public class RoundRobinList<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private int pointer;

    private boolean initialized;

    public RoundRobinList() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
        pointer = 0;
        initialized = false;
    }

    public boolean add(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                return false;
            }
        }
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }


    public int size() {
        return size;
    }

    public Iterator<E> values() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    return null;
                }
                return (E) elements[index++];
            }
        };
    }

    public void delete(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                int numMoved = size - i - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, i + 1, elements, i, numMoved);
                }

                elements[--size] = null;

                if (pointer > i) {
                    pointer--;
                } else if (pointer >= size) {
                    pointer = 0;
                }
            }
        }
    }

    public void init() {
        this.pointer = 0;
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        if (!initialized || size == 0) {
            return null;
        }
        if (pointer >= size) {
            pointer = 0;
        }
       return (E) elements[pointer++];
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elements.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(elements, 0, newArray, 0, size);
            elements = newArray;
        }
    }
}