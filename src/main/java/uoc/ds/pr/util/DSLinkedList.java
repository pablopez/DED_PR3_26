package uoc.ds.pr.util;


import edu.uoc.ds.adt.helpers.KeyValue;
import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.exceptions.InvalidPositionException;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;

public class DSLinkedList<E>  implements DSList<E> {

    private LinkedList<KeyValue<String, E>> data;


    public DSLinkedList() {
        data = new LinkedList<>();
    }

    public void put(String id, E element) {
        KeyValue<String, E>  keyValue = new KeyValue<>(id, element);
        data.insertEnd(keyValue);
    }

    public E get(String id) {
        Iterator<KeyValue<String, E>> it = data.values();
        boolean found = false;
        KeyValue<String, E> keyValue = null;

        while (it.hasNext() && !found) {
            keyValue = it.next();
            found =  (keyValue.getKey().equals(id));
        }
        return (found? keyValue.getValue() :  null);
    }

    @Override
    public void remove(String id) {
        Traversal<KeyValue<String, E>> traversal = data.positions();
        Position position = null;
        boolean found = false;

        while (traversal.hasNext() && !found) {
            position = traversal.next();
            KeyValue<String, E> keyValue = (KeyValue<String, E>) position.getElem();
            found = keyValue.getKey().equals(id);
        }
        if (found) {
            data.delete(position);
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Iterator<E> values() {
        final Iterator<KeyValue<String, E>> it = data.values();

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

}