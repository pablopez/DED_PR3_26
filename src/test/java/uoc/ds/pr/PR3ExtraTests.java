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

public class PR3ExtraTests {

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

        assertThrows(AssistanceNotResolvedException.class, () -> pr.rateAssistance(
                "R1",
                "U1",
                "UNKNOWN",
                LocalDate.now(),
                10,
                10,
                10
        ));
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
    public void solveAssistanceMustRespectFullPriorityComparator() throws Exception {
        SystemIssuesPR3 pr = new SystemIssuesPR3Impl();

        pr.addWorker("W1", "Worker 1", "A");
        pr.addIssueType("IT1", "Hardware");
        pr.assignWorkerToIssueType("W1", "IT1");
        pr.addRoom("R1", "Room 1");

        pr.addUser("U1", "Director", Role.DIRECTOR, "1");      // Rango 5
        pr.addUser("U2", "Manager", Role.MANAGER, "2");        // Rango 2
        pr.addUser("U3", "Analyst", Role.ANALYST, "3");        // Rango 1

        // Desempate por rol
        pr.addAssistance("A1", "U1", "IT1", "R1", LocalDate.of(2026, 1, 2), "A1");

        // Desempate por fecha
        pr.addAssistance("A3", "U2", "IT1", "R1", LocalDate.of(2026, 1, 2), "A3");
        pr.addAssistance("A2", "U2", "IT1", "R1", LocalDate.of(2026, 1, 1), "A2");

        // Desempate por ID
        pr.addAssistance("A5", "U3", "IT1", "R1", LocalDate.of(2026, 1, 3), "A5");
        pr.addAssistance("A4", "U3", "IT1", "R1", LocalDate.of(2026, 1, 3), "A4");

        pr.assignAssistance("A5");
        pr.assignAssistance("A3");
        pr.assignAssistance("A1");
        pr.assignAssistance("A4");
        pr.assignAssistance("A2");

        Assistance solved;

        // Debe  salir A1 porque gana por rol
        solved = pr.solveAssistance("W1");
        Assert.assertEquals("A1", solved.getId());

        // Debe salir A2 porque gana por fecha
        solved = pr.solveAssistance("W1");
        Assert.assertEquals("A2", solved.getId());

        // Debe salir A3 porque gana por fecha
        solved = pr.solveAssistance("W1");
        Assert.assertEquals("A3", solved.getId());

        // Debe desempatar la ID A4 va antes que A5
        solved = pr.solveAssistance("W1");
        Assert.assertEquals("A4", solved.getId());
    }
}
