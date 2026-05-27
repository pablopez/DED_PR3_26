package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.pr2.SystemIssuesHelper;
import uoc.ds.pr.pr3.SystemIssuesHelperPR3;
import uoc.ds.pr.pr3.SystemIssuesPR3;
import uoc.ds.pr.pr3.SystemIssuesPR3Impl;

public class SystemIssuesPR3PlusTest {
    protected SystemIssuesPR3 systemIssues;
    protected SystemIssuesHelper helper;
    protected SystemIssuesHelperPR3 helperPR3;


    /**
     * Follower graph representation:
     * <pre>
     * [W1: Juan]
     * │
     * ├── [W2: Anna]
     * │    │
     * │    └── [W4: Patricia]
     * │
     * └── [W3: Alberto]
     * │
     * ├── [W5: Marta]
     * │    │
     * │    └── [W7: Pablo]
     * │
     * └── [W6: Pedro]
     * </pre>
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        this.systemIssues = new SystemIssuesPR3Impl();
        this.helper = this.systemIssues.getSystemIssuesHelper();
        this.helperPR3 = this.systemIssues.getSystemIssuesHelperPR3();


        systemIssues.addWorker("W1", "juan", "address1");
        systemIssues.addWorker("W2", "Anna", "address2");
        systemIssues.addWorker("W3", "Alberto", "address3");
        systemIssues.addWorker("W4", "Patricia", "adress4");
        systemIssues.addWorker("W5", "Marta", "address5");
        systemIssues.addWorker("W6", "Pedro", "address6");
        systemIssues.addWorker("W7", "Pablo", "address7");


        systemIssues.addFollower("W1", "W2");
        systemIssues.addFollower("W1", "W3");
        systemIssues.addFollower("W2", "W4");
        systemIssues.addFollower("W3", "W5");
        systemIssues.addFollower("W3", "W6");
        systemIssues.addFollower("W5", "W7");
    }

    @After
    public void tearDown() {
        systemIssues=null;
    }

    @Test
    public void addFollowerTest() throws DSException {
        Assert.assertEquals(2, helperPR3.numFollowers("W1"));
        Assert.assertEquals(1, helperPR3.numFollowers("W2"));
        Assert.assertEquals(2, helperPR3.numFollowers("W3"));
        Assert.assertEquals(0, helperPR3.numFollowers("W4"));
        Assert.assertEquals(1, helperPR3.numFollowers("W5"));
        Assert.assertEquals(0, helperPR3.numFollowers("W7"));

        Assert.assertEquals(1, helperPR3.numFollowings("W2"));
        Assert.assertEquals(1, helperPR3.numFollowings("W4"));
        Assert.assertEquals(1, helperPR3.numFollowings("W7"));
        Assert.assertEquals(0, helperPR3.numFollowings("W1"));

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.addFollower("XXXXXXXXXX", "W7"));

        Assert.assertThrows(FollowerNotFoundException.class, () ->
                systemIssues.addFollower("W7", "XXXXXXX"));

    }


    @Test
    public void getFollowersTest() throws DSException {
        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.getFollowers("XXXXXXXXXX"));

        Iterator<Worker> it1 = systemIssues.getFollowers("W1");

        Assert.assertEquals("W2", it1.next().getId());
        Assert.assertEquals("W3", it1.next().getId());
        Assert.assertFalse(it1.hasNext());

        Iterator<Worker> it2 = systemIssues.getFollowers("W2");
        Assert.assertEquals("W4", it2.next().getId());
        Assert.assertFalse(it2.hasNext());

        Iterator<Worker> it3 = systemIssues.getFollowers("W3");
        Assert.assertEquals("W5", it3.next().getId());
        Assert.assertEquals("W6", it3.next().getId());
        Assert.assertFalse(it3.hasNext());

        Iterator<Worker> it4 = systemIssues.getFollowers("W5");
        Assert.assertEquals("W7", it4.next().getId());

        Assert.assertThrows(NoFollowersException.class, () ->
                systemIssues.getFollowers("W4"));


    }


    @Test
    public void getFollowingsTest() throws DSException {

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.getFollowings("XXXXXXX"));

        Iterator<Worker> it = systemIssues.getFollowings("W7");
        Assert.assertEquals("W5", it.next().getId());
        Assert.assertFalse(it.hasNext());

        Iterator<Worker> it1 = systemIssues.getFollowings("W5");
        Assert.assertEquals("W3", it1.next().getId());
        Assert.assertFalse(it1.hasNext());

        Iterator<Worker> it2= systemIssues.getFollowings("W2");
        Assert.assertEquals("W1", it2.next().getId());
        Assert.assertFalse(it2.hasNext());

        Iterator<Worker> it3= systemIssues.getFollowings("W3");
        Assert.assertEquals("W1", it3.next().getId());
        Assert.assertFalse(it3.hasNext());

        Iterator<Worker> it4= systemIssues.getFollowings("W6");
        Assert.assertEquals("W3", it4.next().getId());
        Assert.assertFalse(it4.hasNext());

        Assert.assertThrows(NoFollowedException.class, () ->
                systemIssues.getFollowings("W1"));

    }

    @Test
    public void recommendationTest() throws DSException {
        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.recommendations("XXXXXXXX"));


        Iterator<Worker> it =  systemIssues.recommendations("W1");
        Assert.assertEquals("W4", it.next().getId());
        Assert.assertEquals("W5", it.next().getId());
        Assert.assertEquals("W6", it.next().getId());
        Assert.assertFalse(it.hasNext());


        Assert.assertThrows(NoFollowedException.class, () ->
                systemIssues.recommendations("W5"));

    }

    @Test
    public void getUnfollowedColleaguesTest() throws DSException {
        systemIssues.addIssueType("IT1", "...");

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.getUnfollowedWorkersWithAssignedIssueType("XXXX", "IT1"));

        systemIssues.assignWorkerToIssueType("W1", "IT1");
        systemIssues.assignWorkerToIssueType("W2", "IT1");
        systemIssues.assignWorkerToIssueType("W3", "IT1");
        systemIssues.assignWorkerToIssueType("W4", "IT1");
        systemIssues.assignWorkerToIssueType("W5", "IT1");
        systemIssues.assignWorkerToIssueType("W6", "IT1");
        systemIssues.assignWorkerToIssueType("W7", "IT1");

        Iterator<Worker> it = systemIssues.getUnfollowedWorkersWithAssignedIssueType("W1", "IT1");
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W4", it.next().getId());

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W5", it.next().getId());

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W6", it.next().getId());

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W7", it.next().getId());

        Assert.assertFalse(it.hasNext());

        Iterator<Worker> it2 = systemIssues.getUnfollowedWorkersWithAssignedIssueType("W2", "IT1");
        Assert.assertTrue(it2.hasNext());
        Assert.assertEquals("W1", it2.next().getId());

        Assert.assertTrue(it2.hasNext());
        Assert.assertEquals("W3", it2.next().getId());

        Assert.assertTrue(it2.hasNext());
        Assert.assertEquals("W5", it2.next().getId());

        Assert.assertTrue(it2.hasNext());
        Assert.assertEquals("W6", it2.next().getId());

        Assert.assertTrue(it2.hasNext());
        Assert.assertEquals("W7", it2.next().getId());


        Assert.assertFalse(it2.hasNext());

    }

}