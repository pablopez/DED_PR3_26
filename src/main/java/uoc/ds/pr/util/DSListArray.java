package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.KeyValue;
import edu.uoc.ds.exceptions.InvalidPositionException;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;

public class DSListArray<E> implements DSList<E> {

    KeyValue<String, E>[] theArray;
    int num;

    public DSListArray(int len) {
        theArray = new KeyValue[len];
    }

    public E get(String id)  {

        for (int i = 0; i < num; i++) {
            KeyValue<String, E> kv = theArray[i];
            if (kv != null && kv.getKey().equals(id)){
                return kv.getValue();
            }
        }
        return null;
    }

    public void put(String id, E elem) {
        theArray[num++] = new KeyValue<>(id, elem);
    }

    public void update(String id, E elem) {
        for (KeyValue<String, E> kv : theArray) {
            if (kv!=null && kv.getKey().equals(id)){
                kv.setValue(elem);
            }
        }
    }

    @Override
    public void remove(String id) {
        boolean found = false;
        int i =0;
        while (i < num && !found) {
            found = theArray[i].getKey().equals(id);
            i++;
        }

        if (found) {
            for (int j = i; j < num - 1; j++) {
                theArray[j] = theArray[j + 1];
            }
            theArray[num - 1] = null;
            num--;
        }
    }

    public Iterator<E> values() {
        final Iterator<KeyValue<String, E>> it = new IteratorArrayImpl<>(theArray, num,0);

        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() throws InvalidPositionException {
                return it.next().getValue();
            }
        };
    }
    public int size() {
        return num;
    }
}
