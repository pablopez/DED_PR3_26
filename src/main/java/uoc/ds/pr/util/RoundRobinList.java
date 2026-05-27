package uoc.ds.pr.util;

/**
 * A simple round‐robin list implementation that iterates through its
 * elements in the order they were added and wraps back to the
 * beginning when it reaches the end. The list must be explicitly
 * initialized via {@link #init()} before iteration begins. Once
 * initialized, calls to {@link #next()} will return elements in
 * sequence and wrap around indefinitely. Calling {@link #init()} again
 * resets the iteration back to the first element without modifying
 * the underlying data.
 *
 * <p>This class avoids using {@code java.util} collections to
 * remain compatible with the project’s restrictions. Internally it
 * manages a resizable array of elements.</p>
 *
 * @param <E> the type of elements stored in the list
 */
public class RoundRobinList<E> {
    /**
     * Default initial capacity for the underlying array. Chosen to
     * accommodate typical small lists without immediate resizing.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Array to store elements. The array is resized as elements are
     * added. Generic arrays cannot be created directly, so this field
     * is an Object array cast to {@code E[]}. All access is
     * type‐checked by the caller.
     */
    private E[] elements;

    /**
     * Number of elements currently stored in the list.
     */
    private int size;

    /**
     * Index of the next element to return when iterating. This
     * pointer wraps back to zero when it reaches {@code size}.
     */
    private int currentIndex;

    /**
     * Flag indicating whether {@link #init()} has been called. Iteration
     * only proceeds when this flag is {@code true}; otherwise
     * {@link #next()} will return {@code null}.
     */
    private boolean initialized;

    /**
     * Constructs an empty round‐robin list with default capacity.
     */
    @SuppressWarnings("unchecked")
    public RoundRobinList() {
        // Create an Object array and cast to E[]. This is safe because
        // all operations store and retrieve E instances only.
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        currentIndex = 0;
        initialized = false;
    }

    /**
     * Adds an element to the end of the list only if it doesn't already exist.
     * The order of insertion determines the order of iteration. If the underlying
     * array is full, its capacity is doubled to accommodate new entries.
     * Uses {@link Object#equals(Object)} for duplicate comparison.
     *
     * @param element the element to add; may be {@code null}
     * @return {@code true} if the element was added, {@code false} if it already existed
     */
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

    /**
     * Returns the number of elements stored in the list.
     *
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in this list.
     * The iterator traverses elements in insertion order.
     *
     * @return an iterator over the elements
     */
    public edu.uoc.ds.traversal.Iterator<E> values() {
        return new edu.uoc.ds.traversal.Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    return null;
                }
                return elements[index++];
            }
        };
    }

    /**
     * Removes the specified element from the list if it exists.
     * After removal, the list is compacted and the iteration pointer
     * is adjusted if necessary to maintain valid iteration state.
     *
     * @param element the element to remove; may be {@code null}
     * @return {@code true} if the element was found and removed, {@code false} otherwise
     */
    public boolean delete(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                // Shift elements to fill the gap
                for (int j = i; j < size - 1; j++) {
                    elements[j] = elements[j + 1];
                }
                elements[size - 1] = null;
                size--;
                // Adjust currentIndex if it was after or at the removed position
                if (currentIndex > i) {
                    currentIndex--;
                } else if (currentIndex >= size) {
                    currentIndex = 0;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Resets the iteration pointer to the start of the list and
     * marks the list as initialized. After calling this method,
     * subsequent calls to {@link #next()} will begin from the first
     * element and then proceed in round‐robin order.
     */
    public void init() {
        this.currentIndex = 0;
        this.initialized = true;
    }

    /**
     * Returns whether {@link #init()} has been called. Until
     * initialization occurs, {@link #next()} will return {@code null}
     * to signal that iteration has not begun.
     *
     * @return {@code true} if the list has been initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the next element in round‐robin order. If the list is
     * empty or has not been initialized, this method returns
     * {@code null}. After reaching the end of the list, the pointer
     * wraps back to the beginning.
     *
     * @return the next element, or {@code null} if uninitialized or empty
     */
    public E next() {
        if (!initialized || size == 0) {
            return null;
        }
        // Wrap around if needed
        if (currentIndex >= size) {
            currentIndex = 0;
        }
        E value = elements[currentIndex++];
        return value;
    }

    /**
     * Ensures that the underlying array has capacity for at least
     * {@code minCapacity} elements. If not, the array is resized to
     * double its current size or to the required minimum, whichever
     * is larger.
     *
     * @param minCapacity the minimum required capacity
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elements.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = oldCapacity * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            // Create a new array and copy existing elements
            E[] newArray = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
    }
}