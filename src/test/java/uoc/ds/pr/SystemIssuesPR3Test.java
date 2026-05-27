package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.model.System;
import uoc.ds.pr.util.CSVUtil;
import uoc.ds.pr.util.DateUtils;

import java.time.LocalDate;


public class SystemIssuesPR3Test extends SystemIssuesPR2Test {

    @Before
    public void setUp() throws Exception {
        this.systemIssues = FactorySystemIssues.getComputerProjects();
        this.helper= this.systemIssues.getSystemIssuesHelper();
        this.helperPR3 = this.systemIssues.getSystemIssuesHelperPR3();
    }


    @After
    public void tearDown() {
        this.systemIssues = null;
    }



    @Test
    public void addRoomTest() throws DSException {
        Assert.assertEquals(0, helperPR3.numRooms());

        CSVUtil.addRooms(systemIssues);

        Assert.assertEquals(23, helperPR3.numRooms());

        systemIssues.addRoom("RM35", "Room 35");
        Assert.assertEquals(23 + 1, helperPR3.numRooms());

        systemIssues.addRoom("RM36", "Room 366");
        Assert.assertEquals(24 + 1, helperPR3.numRooms());

        Room room = helperPR3.getRoom("RM36");
        Assert.assertEquals("Room 366", room.getName());

        systemIssues.addRoom("RM36", "Room 36");
        Assert.assertEquals(25, helperPR3.numRooms());

        room = helperPR3.getRoom("RM36");
        Assert.assertEquals("Room 36", room.getName());
    }

    @Test
    public void addUserTest() throws DSException {
        Assert.assertEquals(0, helperPR3.numUsers());

        CSVUtil.addUsers(systemIssues);

        Assert.assertEquals(65, helperPR3.numUsers());

        systemIssues.addUser("U66", "Peter Parker", Role.ANALYST, "555-0166");
        Assert.assertEquals(65 + 1, helperPR3.numUsers());

        systemIssues.addUser("U67", "Wanda Maximof", Role.MANAGER, "555-0167");
        Assert.assertEquals(66 + 1, helperPR3.numUsers());

        User user = helperPR3.getUser("U67");
        Assert.assertEquals("Wanda Maximof", user.getName());
        Assert.assertEquals(Role.MANAGER, user.getRole()); // Suponiendo que tu modelo usa el Enum

        systemIssues.addUser("U67", "Wanda Maximoff", Role.MANAGER, "555-0167");

        Assert.assertEquals(67, helperPR3.numUsers());

        user = helperPR3.getUser("U67");
        Assert.assertEquals("Wanda Maximoff", user.getName());
        Assert.assertEquals("555-0167", user.getPhone());
    }

    @Test
    public void addIssueTypeTest() throws DSException {
        Assert.assertEquals(0, helperPR3.numIssueTypes());

        CSVUtil.addIssueTypes(systemIssues);

        Assert.assertEquals(8, helperPR3.numIssueTypes());

        systemIssues.addIssueType("IT9", "Software Bug");
        Assert.assertEquals(8 + 1, helperPR3.numIssueTypes());

        systemIssues.addIssueType("IT10", "Clouud");
        Assert.assertEquals(9 + 1, helperPR3.numIssueTypes());

        IssueType issueType = helperPR3.getIssueType("IT10");
        Assert.assertNotNull(issueType);
        Assert.assertEquals("Clouud", issueType.getName());

        systemIssues.addIssueType("IT10", "Cloud Services");
        Assert.assertEquals(10, helperPR3.numIssueTypes());

        issueType = helperPR3.getIssueType("IT10");
        Assert.assertEquals("Cloud Services", issueType.getName());
    }


    @Test
    public void assignSystemToRoomTest() throws DSException {
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);
        Assert.assertEquals(10, helper.numSystems());

        CSVUtil.addRooms(systemIssues);
        Assert.assertEquals(23, helperPR3.numRooms());

        Assert.assertThrows(SystemNotFoundException.class, () ->
                systemIssues.assignSystemToRoom("XXXXX", "C4-236V"));


        Assert.assertThrows(RoomNotFoundException.class, () ->
                systemIssues.assignSystemToRoom("SYS06", "XXXXX"));

        systemIssues.assignSystemToRoom("SYS06", "C4-236V");

        System system = helper.getSystem("SYS06");
        Assert.assertEquals("C4-236V", system.getRoom().getRoomId());

        systemIssues.assignSystemToRoom("SYS06", "C4-339V");
        Assert.assertEquals("C4-339V", system.getRoom().getRoomId());

    }


    @Test
    public void assignWorkerToIssueTypeTest() throws DSException {
        CSVUtil.addWorkers(systemIssues);
        CSVUtil.addIssueTypes(systemIssues);
        Assert.assertEquals(28, helper.numWorkers());
        Assert.assertEquals(8, helperPR3.numIssueTypes());

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.assignWorkerToIssueType("XXXXX", "IT1"));

        Assert.assertThrows(IssueTypeNotFoundException.class, () ->
                systemIssues.assignWorkerToIssueType("W10", "XXXXX"));

        systemIssues.assignWorkerToIssueType("W10", "IT1");
        Worker worker10 = helper.getWorker("W10");
        Assert.assertEquals("IT1", worker10.getIssueType().getId());

        systemIssues.assignWorkerToIssueType("W11", "IT1");
        Worker worker11 = helper.getWorker("W11");
        Assert.assertEquals("IT1", worker11.getIssueType().getId());

    }

    @Test
    public void getWorkersByIssueTypeTest() throws DSException {

        assignWorkerToIssueTypeTest();

        Assert.assertThrows(NoWorkerException.class, () ->
                systemIssues.getWorkersByIssueType( "IT2"));

       Assert.assertEquals(2,  helperPR3.numWorkerByIssueType("IT1"));

        Iterator<Worker> it = systemIssues.getWorkersByIssueType( "IT1");

        Assert.assertTrue(it.hasNext());
        Worker worker = it.next();
        Assert.assertEquals("W10", worker.getId());

        Assert.assertTrue(it.hasNext());
        worker = it.next();
        Assert.assertEquals("W11", worker.getId());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void addAssistanceTest()  throws DSException {
        CSVUtil.addIssueTypes(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addRooms(systemIssues);


        Assert.assertEquals(0, helperPR3.numAssistances());
        Assert.assertEquals(0, helperPR3.numAssistancesByUser("U1"));

        systemIssues.addAssistance("ASS1", "U1", "IT1", "C4-240V", DateUtils.createLocalDate("11-05-2026"),
                "Wi-Fi issues");

        Assert.assertEquals(1, helperPR3.numAssistances());
        Assert.assertEquals(1, helperPR3.numAssistancesByUser("U1"));

        Assert.assertThrows(UserNotFoundException.class, () ->
                systemIssues.addAssistance("ASS1", "XXXXX", "IT1", "C4-240V", DateUtils.createLocalDate("11-05-2026"),
                        "Wi-Fi issues"));

    }


    @Test
    public void assignAssistanceTest() throws DSException {
        CSVUtil.addIssueTypes(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addRooms(systemIssues);
        CSVUtil.addWorkers(systemIssues);
        CSVUtil.assignWorkerToIssueType(systemIssues);
        CSVUtil.addAssistances(systemIssues);

        Assert.assertEquals(10, helperPR3.numWorkerByIssueType("IT1"));
        Assert.assertEquals(14, helperPR3.numWorkerByIssueType("IT2"));
        Assert.assertEquals(4, helperPR3.numWorkerByIssueType("IT3"));

        Assert.assertEquals(35, helperPR3.numAssistances());

        Iterator<Worker> it = systemIssues.getWorkersByIssueType("IT3");
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("W8", it.next().getId());
        Assert.assertEquals("W10", it.next().getId());
        Assert.assertEquals("W11", it.next().getId());
        Assert.assertEquals("W18", it.next().getId());
        Assert.assertFalse(it.hasNext());


        String[][] expectedData = {
                {"ASS01", "IT3", "W8"},
                {"ASS02", "IT3", "W10"},
                {"ASS03", "IT3", "W11"},
                {"ASS04", "IT3", "W18"},
                {"ASS05", "IT3", "W8"},
                {"ASS06", "IT3", "W10"},
                {"ASS07", "IT3", "W11"},
                {"ASS08", "IT3", "W18"},
                {"ASS09", "IT3", "W8"},
                {"ASS10", "IT3", "W10"},
                {"ASS11", "IT3", "W11"},
                {"ASS12", "IT3", "W18"},
                {"ASS13", "IT3", "W8"},
                {"ASS14", "IT3", "W10"},
                {"ASS15", "IT3", "W11"},
                {"ASS16", "IT3", "W18"},
                {"ASS17", "IT3", "W8"},
                {"ASS18", "IT3", "W10"}
        };

        for (String[] data : expectedData) {
            String assistanceId = data[0];
            String expectedIT = data[1];
            String expectedWorker = data[2];

            systemIssues.assignAssistance(assistanceId);
            Assistance assistance = helperPR3.getAssistance(assistanceId);

            Assert.assertEquals(expectedIT, assistance.getIssueType().getId());
            Assert.assertEquals(expectedWorker, assistance.getWorker().getId());

        }
    }

    @Test
    public void solvessistanceTest() throws DSException {
        assignAssistanceTest();

        Assert.assertEquals(5, helper.numAssignedAssistances("W8"));
        Assert.assertEquals(0, helper.numSolvedAssistances("W8"));
        Assistance assistance13 = systemIssues.solveAssistance("W8");
        Assert.assertEquals(4, helper.numAssignedAssistances("W8"));
        Assert.assertEquals(1, helper.numSolvedAssistances("W8"));

        Assert.assertEquals(Role.DIRECTOR, assistance13.getRole());
        Assert.assertEquals(LocalDate.of(2026, 3, 23), assistance13.getDate());
        Assert.assertEquals("ASS13", assistance13.getId());

        Assert.assertEquals(5, helper.numAssignedAssistances("W10"));
        Assert.assertEquals(0, helper.numSolvedAssistances("W10"));

        Assistance assistance14 = systemIssues.solveAssistance("W10");
        Assert.assertEquals(Role.DIRECTOR, assistance14.getRole());
        Assert.assertEquals("U41", assistance14.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 3, 23), assistance14.getDate());
        Assert.assertEquals("ASS14", assistance14.getId());


        Assistance assistance18 = systemIssues.solveAssistance("W10");
        Assert.assertEquals(Role.DIRECTOR, assistance18.getRole());
        Assert.assertEquals("U31", assistance18.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 3, 23), assistance18.getDate());
        Assert.assertEquals("ASS18", assistance18.getId());

        Assistance assistance06 = systemIssues.solveAssistance("W10");
        Assert.assertEquals(Role.DIRECTOR, assistance06.getRole());
        Assert.assertEquals("U52", assistance06.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 5, 8), assistance06.getDate());
        Assert.assertEquals("ASS06", assistance06.getId());

        Assistance assistance10 = systemIssues.solveAssistance("W10");
        Assert.assertEquals(Role.UNIT_HEAD, assistance10.getRole());
        Assert.assertEquals("U58", assistance10.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 5, 6), assistance10.getDate());
        Assert.assertEquals("ASS10", assistance10.getId());

        Assistance assistance02 = systemIssues.solveAssistance("W10");
        Assert.assertEquals(Role.MANAGER, assistance02.getRole());
        Assert.assertEquals("U24", assistance02.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 3, 14), assistance02.getDate());
        Assert.assertEquals("ASS02", assistance02.getId());

        Assistance assistance01 = systemIssues.solveAssistance("W8");
        Assert.assertEquals(Role.ANALYST, assistance01.getRole());
        Assert.assertEquals("U9", assistance01.getUser().getId());
        Assert.assertEquals(LocalDate.of(2026, 3, 12), assistance01.getDate());
        Assert.assertEquals("ASS01", assistance01.getId());


        Assert.assertEquals(0, helper.numAssignedAssistances("W10"));
        Assert.assertEquals(5, helper.numSolvedAssistances("W10"));

        Assert.assertThrows(NoAssistanceException.class, () ->
                systemIssues.solveAssistance("W10"));

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.solveAssistance("WXXXXX"));

    }


    @Test
    public void rateAssistanceTest() throws DSException {
        solvessistanceTest();

        Assert.assertThrows(UserIsNotCreatorException.class, () ->
                systemIssues.rateAssistance("R1", "U1", "ASS02", DateUtils.createLocalDate("14-05-2026"), 9, 8,9));

        systemIssues.rateAssistance("R1", "U24", "ASS02", DateUtils.createLocalDate("14-05-2026"), 9, 8,9);
        systemIssues.rateAssistance("R2", "U58", "ASS10", DateUtils.createLocalDate("12-05-2026"), 8, 8,8);
        systemIssues.rateAssistance("R3", "U41", "ASS14", DateUtils.createLocalDate("12-05-2026"), 8, 8,8);
        systemIssues.rateAssistance("R3", "U52", "ASS06", DateUtils.createLocalDate("12-05-2026"), 8, 8,8);
        systemIssues.rateAssistance("R3", "U9", "ASS01", DateUtils.createLocalDate("12-05-2026"), 3, 3,3);

        Assert.assertThrows(AssistanceNotResolvedException.class, () ->
                systemIssues.rateAssistance("R1", "U24", "ASS03", DateUtils.createLocalDate("14-05-2026"), 9, 8,9));

    }

    @Test
    public void getTop10BestWorkers() throws DSException {
        Assert.assertThrows(NoWorkerException.class, () ->
                systemIssues.getTop10BestWorkers());

        rateAssistanceTest();
        Iterator<Worker> it = systemIssues.getTop10BestWorkers();

        Assert.assertTrue(it.hasNext());
        Worker worker = it.next();
        Assert.assertEquals(8.22, worker.getGlobalRating(),0.1);
        Assert.assertEquals("W10", worker.getId());

        Assert.assertTrue(it.hasNext());
        worker = it.next();
        Assert.assertEquals(3, worker.getGlobalRating(),0.1);
        Assert.assertEquals("W8", worker.getId());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getTop5UsersWithMostAssistancesTest() throws DSException {
        Assert.assertThrows(NoUserException.class, () ->
                systemIssues.getTop5UsersWithMostAssistances());

        rateAssistanceTest();

        Iterator<User> it = systemIssues.getTop5UsersWithMostAssistances();

        record ExpectedUser(String id, int assistances) {}

        ExpectedUser[] expectedResults = {
                new ExpectedUser("U9", 4),
                new ExpectedUser("U41", 3),
                new ExpectedUser("U58", 2),
                new ExpectedUser("U23", 2),
                new ExpectedUser("U24", 2)
        };

        for (ExpectedUser expected : expectedResults) {
            Assert.assertTrue(it.hasNext());
            User user = it.next();
            Assert.assertEquals(expected.id(), user.getId());
            Assert.assertEquals(expected.assistances(), user.numAssistances());
        }

        Assert.assertFalse(it.hasNext());

    }



}