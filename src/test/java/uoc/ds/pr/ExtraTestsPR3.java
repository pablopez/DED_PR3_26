package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.exceptions.AssistanceNotResolvedException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.pr3.SystemIssuesPR3;
import uoc.ds.pr.pr3.SystemIssuesPR3Impl;

import java.time.LocalDate;

import static org.junit.Assert.assertThrows;

public class ExtraTestsPR3 {

    @Test
    public void pr3ShouldShareStateWithInheritedPR2Methods() throws Exception {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addUser("U1", "User 1", Role.ANALYST, "111");
        pr.addSystem("SYS1", "System 1", "Lab", "U1");
        pr.addRoom("R1", "Room 1");

        pr.assignSystemToRoom("SYS1", "R1");

        Assert.assertEquals(1, pr.getSystemIssuesHelper().numSystems());
        Assert.assertEquals(1, pr.getSystemIssuesHelperPR3().numRooms());
        Assert.assertEquals("R1", pr.getSystemIssuesHelper().getSystem("SYS1").getRoom().getId());
    }

    @Test
    public void getTop10BestWorkersWithoutRatedWorkersShouldThrow() {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addWorker("W1", "Worker 1", "Address");

        assertThrows(NoWorkerException.class, pr::getTop10BestWorkers);
    }

    @Test
    public void rateUnknownAssistanceShouldThrowAssistanceNotResolved() {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        assertThrows(AssistanceNotResolvedException.class, () -> {
            pr.rateAssistance(
                    "R1",
                    "U1",
                    "UNKNOWN",
                    LocalDate.now(),
                    10,
                    10,
                    10
            );
        });
    }

    @Test
    public void unfollowedWorkersShouldExcludeAlreadyFollowedWorkers() throws Exception {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addWorker("W1", "Worker 1", "Address");
        pr.addWorker("W2", "Worker 2", "Address");
        pr.addWorker("W3", "Worker 3", "Address");

        pr.addIssueType("IT1", "Hardware");

        pr.assignWorkerToIssueType("W1", "IT1");
        pr.assignWorkerToIssueType("W2", "IT1");
        pr.assignWorkerToIssueType("W3", "IT1");

        pr.addFollower("W2", "W1");

        Iterator<Worker> it =
                pr.getUnfollowedWorkersWithAssignedIssueType("W1", "IT1");

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W3", it.next().getId());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void reassignWorkerToAnotherIssueTypeMustMoveWorker() throws Exception {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addWorker("W1", "Worker 1", "A");
        pr.addIssueType("IT1", "Hardware");
        pr.addIssueType("IT2", "Software");

        pr.assignWorkerToIssueType("W1", "IT1");
        Assert.assertEquals(1, pr.getSystemIssuesHelperPR3().numWorkerByIssueType("IT1"));
        Assert.assertEquals(0, pr.getSystemIssuesHelperPR3().numWorkerByIssueType("IT2"));

        pr.assignWorkerToIssueType("W1", "IT2");
        Assert.assertEquals(0, pr.getSystemIssuesHelperPR3().numWorkerByIssueType("IT1"));
        Assert.assertEquals(1, pr.getSystemIssuesHelperPR3().numWorkerByIssueType("IT2"));
    }

    @Test
    public void solveAssistanceMustRespectPriority() throws Exception {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addWorker("W1", "Worker 1", "A");
        pr.addIssueType("IT1", "Printer23412");
        pr.assignWorkerToIssueType("W1", "IT1");

        pr.addRoom("R1", "Room 1");

        pr.addUser("U1", "Analyst", Role.ANALYST, "111");
        pr.addUser("U2", "Director", Role.DIRECTOR, "222");

        pr.addAssistance("A1", "U1", "IT1", "R1", LocalDate.of(2024, 1, 1), "Low priority");
        pr.addAssistance("A2", "U2", "IT1", "R1", LocalDate.of(2024, 1, 2), "High priority");

        pr.assignAssistance("A1");
        pr.assignAssistance("A2");

        Assistance solved = pr.solveAssistance("W1");

        Assert.assertEquals("A2", solved.getId());
    }
}
