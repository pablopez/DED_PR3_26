package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoundRobinTest {
    RoundRobinList<Integer> roundRobinList;

    @Before
    public void setUp() {
        roundRobinList = new RoundRobinList<>();
        roundRobinList.add(1);
        roundRobinList.add(2);
        roundRobinList.add(3);
        roundRobinList.add(4);
        roundRobinList.add(5);
    }

    @Test
    public void test() {
        Assert.assertFalse(roundRobinList.isInitialized());
        roundRobinList.init();
        Assert.assertTrue(roundRobinList.isInitialized());
        Assert.assertEquals(5, roundRobinList.size());
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);
        Assert.assertEquals(3, roundRobinList.next(),0);
        Assert.assertEquals(4, roundRobinList.next(),0);
        Assert.assertEquals(5, roundRobinList.next(),0);
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);
        Assert.assertEquals(3, roundRobinList.next(),0);
        Assert.assertEquals(4, roundRobinList.next(),0);
        Assert.assertEquals(5, roundRobinList.next(),0);
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);
        Assert.assertEquals(3, roundRobinList.next(),0);
        Assert.assertEquals(4, roundRobinList.next(),0);
        Assert.assertEquals(5, roundRobinList.next(),0);
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);

        roundRobinList.init();
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);
        Assert.assertEquals(3, roundRobinList.next(),0);
        Assert.assertEquals(4, roundRobinList.next(),0);
        Assert.assertEquals(5, roundRobinList.next(),0);
        Assert.assertEquals(1, roundRobinList.next(),0);
        Assert.assertEquals(2, roundRobinList.next(),0);
        Assert.assertEquals(3, roundRobinList.next(),0);

    }
}
