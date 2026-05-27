package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Stack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StackLinkedListTest {
    Stack<Integer> stack;

    @Before
    public void setUp() {
        stack = new StackLinkedList<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
    }

    @Test
    public void addTest() {
        Assert.assertEquals(5, stack.size());
        stack.push(6);
        Assert.assertEquals(6, stack.size());
    }

    @Test
    public void addPoplTest() {
        Assert.assertEquals(5, stack.size());
        Assert.assertEquals(5, stack.pop(),0);
        Assert.assertEquals(4, stack.size());

        Assert.assertEquals(4, stack.pop(),0);
        Assert.assertEquals(3, stack.size());

        Assert.assertEquals(3, stack.pop(),0);
        Assert.assertEquals(2, stack.size());

        Assert.assertEquals(2, stack.pop(),0);
        Assert.assertEquals(1, stack.size());

        Assert.assertEquals(1, stack.pop(),0);
        Assert.assertEquals(0, stack.size());

    }

    @Test
    public void addPeekTest() {
        Assert.assertEquals(5, stack.size());
        Assert.assertEquals(5, stack.peek(),0);
        Assert.assertEquals(5, stack.size());
    }


}
