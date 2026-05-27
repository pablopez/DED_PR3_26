package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Container;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.Stack;

public class StackLinkedList<E> extends LinkedList<E> implements Stack<E>, Container<E> {

    public StackLinkedList() {
        super();
    }
    @Override
    public void push(E e) {
        super.insertEnd(e);
    }


    @Override
    public E peek() {
        LinkedNode<E> last = this.last;
        return last.getElem();
    }

    @Override
    public E pop() {
        E e= peek();
        super.deleteLast();
        return e;
    }
}
