package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.Issue;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.pr2.SystemIssues;
import uoc.ds.pr.pr2.SystemIssuesHelper;
import uoc.ds.pr.model.System;
import uoc.ds.pr.pr3.SystemIssuesHelperPR3;
import uoc.ds.pr.pr3.SystemIssuesPR3;
import uoc.ds.pr.util.CSVUtil;
import uoc.ds.pr.util.DateUtils;


public class SystemIssuesPR2Test {

    protected SystemIssuesPR3 systemIssues;
    protected SystemIssuesHelper helper;
    protected SystemIssuesHelperPR3 helperPR3;

    @Before
    public void setUp() throws Exception {
        this.systemIssues = FactorySystemIssues.getComputerProjects();
        this.helper = this.systemIssues.getSystemIssuesHelper();
    }


    @After
    public void tearDown() {
        this.systemIssues = null;
    }



    @Test
    public void addWorkerTest() throws DSException {
        Assert.assertEquals(0, helper.numWorkers());

        CSVUtil.addWorkers(systemIssues);

        Assert.assertEquals(28, helper.numWorkers());
        systemIssues.addWorker("WRK35", "Worker 35",  "Address 35");
        Assert.assertEquals(28+1, helper.numWorkers());

        systemIssues.addWorker("W36", "Worker 366", "Address 366");
        Assert.assertEquals(29+1, helper.numWorkers());

        Worker worker = helper.getWorker("W36");
        Assert.assertEquals("Worker 366", worker.getName());
        Assert.assertEquals("Address 366", worker.getAddress());

        systemIssues.addWorker("W36", "Worker 36",  "Address 36");
        Assert.assertEquals(30, helper.numWorkers());

        worker = helper.getWorker("W36");
        Assert.assertEquals("Worker 36", worker.getName());
        Assert.assertEquals("Address 36", worker.getAddress());

    }

    @Test
    public void addSystemTest() throws DSException {
        Assert.assertEquals(0, helper.numSystems());
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);

        Assert.assertEquals(10, helper.numSystems());
        systemIssues.addSystem("System355", "System 355",  "Address 35", "U11");
        Assert.assertEquals(10+1, helper.numSystems());

        systemIssues.addSystem("System366", "System 366*****", "Address 366", "U12");
        Assert.assertEquals(11+1, helper.numSystems());

        System system = helper.getSystem("System366");
        Assert.assertEquals("System 366*****", system.getDescription());
        Assert.assertEquals("Address 366", system.getLocation());

        systemIssues.addSystem("System366", "System 36",  "Address 36", "U12");
        Assert.assertEquals(12, helper.numSystems());

        system = helper.getSystem("System366");
        Assert.assertEquals("System 36", system.getDescription());
        Assert.assertEquals("Address 36", system.getLocation());
    }


    @Test
    public void addComponentTest() throws DSException {
        Assert.assertEquals(0, helper.numComponents());
        CSVUtil.addComponents(systemIssues);

        Assert.assertEquals(20, helper.numComponents());
        systemIssues.addComponent("COMP500", "Dell", "UltraSharp 27", "SN-998877");
        Assert.assertEquals(20+1, helper.numComponents());

        systemIssues.addComponent("COMP501", "Logitech", "MX Master 3*****", "SN-112233");
        Assert.assertEquals(21+1, helper.numComponents());

        Component component = helper.getComponent("COMP501");
        Assert.assertEquals("Logitech", component.getTrademark());
        Assert.assertEquals("MX Master 3*****", component.getModel());
        Assert.assertEquals("SN-112233", component.getSerial());

        systemIssues.addComponent("COMP501", "Logitech", "MX Anywhere", "SN-CHANGED");
        Assert.assertEquals(22, helper.numComponents());

        component = helper.getComponent("COMP501");
        Assert.assertEquals("Logitech", component.getTrademark());
        Assert.assertEquals("MX Anywhere", component.getModel());
        Assert.assertEquals("SN-CHANGED", component.getSerial());
    }

    @Test
    public void installComponentToSystemTest() throws DSException {
        Assert.assertEquals(0, helper.numComponents());
        Assert.assertEquals(0, helper.numSystems());

        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);
        CSVUtil.addComponents(systemIssues);


        Assert.assertEquals(20, helper.numComponents());
        Assert.assertEquals(10, helper.numSystems());

        CSVUtil.installComponent2Systems(systemIssues);

        Assert.assertEquals(5, helper.numComponentsBySystem("SYS01"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS02"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS03"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS04"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS05"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS06"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS07"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS08"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS09"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS10"));

        Assert.assertThrows(ComponentAlreadyInstalledException.class, () ->
            systemIssues.installComponentToSystem("COMP19", "SYS09"));
    }

    @Test
    public void createIssueTest() throws DSException {
        CSVUtil.addWorkers(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);
        CSVUtil.addComponents(systemIssues);
        CSVUtil.installComponent2Systems(systemIssues);

        Assert.assertEquals(0, helper.numIssues());
        CSVUtil.addIssues(systemIssues);

        Assert.assertEquals(34, helper.numIssues());
        Assert.assertEquals(2, helper.numIssuesByComponent("COMP01"));
        Assert.assertEquals(2, helper.numIssuesByComponent("COMP02"));
        Assert.assertEquals(1, helper.numIssuesByComponent("COMP13"));

        Assert.assertThrows(ComponentNotFoundException.class, () ->
                systemIssues.createIssue("ISS-035","COMPXXXX","IT1",
                        "Razer Keyboard RGB lighting synchronization error", DateUtils.createDateTime("2026-03-17T15:00:00")));


        systemIssues.createIssue("ISS-035","COMP13","IT1",
                "Razer Keyboard RGB lighting synchronization error", DateUtils.createDateTime("2026-03-17T15:00:00"));

        Assert.assertEquals(35, helper.numIssues());
        Assert.assertEquals(2, helper.numIssuesByComponent("COMP01"));
        Assert.assertEquals(2, helper.numIssuesByComponent("COMP02"));
        Assert.assertEquals(2, helper.numIssuesByComponent("COMP13"));
    }

    @Test
    public void assignIssueTest() throws DSException {
        CSVUtil.addWorkers(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);
        CSVUtil.addComponents(systemIssues);
        CSVUtil.installComponent2Systems(systemIssues);
        CSVUtil.addIssueTypes(systemIssues);
        CSVUtil.addIssues(systemIssues);

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.assignIssue("ISS-015","WXXXX"));

        Assert.assertThrows(IssueNotFoundException.class, () ->
                systemIssues.assignIssue("ISS-XXX","W15"));

        systemIssues.assignIssue("ISS-005","W15");
        systemIssues.assignIssue("ISS-006","W15");
        systemIssues.assignIssue("ISS-007","W15");

        Assert.assertThrows(IssueAlreadyAssignedException.class, () ->
                systemIssues.assignIssue("ISS-006","W14"));


        Assert.assertEquals(3, helper.numIssuesByWorker("W15"));

        Issue issue = systemIssues.createIssue("ISS-035","COMP13","IT1",
                "Razer Keyboard RGB lighting synchronization error", DateUtils.createDateTime("2026-03-17T15:00:00"));

        Assert.assertEquals("IT1", issue.getIssueType().getId());

        issue.setResolved(true);
        Assert.assertThrows(IssueAlreadyResolvedException.class, () ->
                systemIssues.assignIssue("ISS-035","W14"));


        CSVUtil.addIssueAsignments(systemIssues);

        Assert.assertEquals(3, helper.numIssuesByWorker("W19"));
        Assert.assertEquals(5, helper.numIssuesByWorker("W20"));

    }

    @Test
    public void solveIssueTest() throws DSException {
        assignIssueTest();

        Assert.assertThrows(WorkerNotFoundException.class, () ->
                systemIssues.solveIssue("WXXXX"));

        Assert.assertThrows(NoIssuesException.class, () ->
                systemIssues.solveIssue("W5"));

        Assert.assertEquals(3, helper.numIssuesByWorker("W19"));
        Assert.assertEquals(5, helper.numIssuesByWorker("W20"));


        Issue issue = systemIssues.solveIssue("W19");
        Assert.assertEquals(2, helper.numIssuesByWorker("W19"));
        Assert.assertEquals("ISS-023", issue.getId());
        Assert.assertTrue( issue.isResolved());

        issue = systemIssues.solveIssue("W19");
        Assert.assertEquals(1, helper.numIssuesByWorker("W19"));
        Assert.assertEquals("ISS-012", issue.getId());
        Assert.assertTrue( issue.isResolved());

        issue = systemIssues.solveIssue("W19");
        Assert.assertEquals(0, helper.numIssuesByWorker("W19"));
        Assert.assertEquals("ISS-001", issue.getId());
        Assert.assertTrue( issue.isResolved());

        Assert.assertThrows(NoIssuesException.class, () ->
                systemIssues.solveIssue("W19"));

    }

    @Test
    public void getSystemsTest() throws DSException {

        Assert.assertEquals(0, helper.numSystems());
        Assert.assertThrows(NoSystemsException.class, () ->
                systemIssues.getSystems());

        CSVUtil.addWorkers(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);
        CSVUtil.addComponents(systemIssues);
        CSVUtil.installComponent2Systems(systemIssues);

        Assert.assertEquals(10, helper.numSystems());
        Iterator<System> it = systemIssues.getSystems();

        Assert.assertTrue(it.hasNext());
        System system = it.next();
        Assert.assertEquals("SYS01", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS02", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS03", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS04", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS05", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS06", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS07", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS08", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS09", system.getId());

        Assert.assertTrue(it.hasNext());
        system = it.next();
        Assert.assertEquals("SYS10", system.getId());

        Assert.assertFalse(it.hasNext());

    }


    @Test
    public void getComponentsBySystemTest() throws DSException {
        Assert.assertEquals(0, helper.numComponents());
        Assert.assertEquals(0, helper.numSystems());

        CSVUtil.addWorkers(systemIssues);
        CSVUtil.addUsers(systemIssues);
        CSVUtil.addSystems(systemIssues);

        Assert.assertThrows(SystemHasNoComponentsException.class, () ->
                systemIssues.getComponentsBySystem("SYS01"));

        CSVUtil.addComponents(systemIssues);
        CSVUtil.installComponent2Systems(systemIssues);

        Assert.assertEquals(20, helper.numComponents());
        Assert.assertEquals(10, helper.numSystems());
        Assert.assertEquals(5,  helper.numComponentsBySystem("SYS01"));

        Iterator<Component> it = systemIssues.getComponentsBySystem("SYS01");

        Assert.assertTrue(it.hasNext());
        Component component = it.next();
        Assert.assertEquals("COMP01", component.getId());

        Assert.assertTrue(it.hasNext());
        component = it.next();
        Assert.assertEquals("COMP02", component.getId());

        Assert.assertTrue(it.hasNext());
        component = it.next();
        Assert.assertEquals("COMP03", component.getId());

        Assert.assertTrue(it.hasNext());
        component = it.next();
        Assert.assertEquals("COMP04", component.getId());

        Assert.assertTrue(it.hasNext());
        component = it.next();
        Assert.assertEquals("COMP17", component.getId());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getDoneIssuesByWorkerTest() throws DSException {

        CSVUtil.addWorkers(systemIssues);

        Assert.assertThrows(NoIssuesException.class, () ->
                systemIssues.getDoneIssuesByWorker("W19"));

        solveIssueTest();

        Iterator<Issue> it = systemIssues.getDoneIssuesByWorker("W19");

        Assert.assertTrue(it.hasNext());
        Issue issue = it.next();
        Assert.assertEquals("ISS-023", issue.getId());
        Assert.assertTrue(issue.isResolved());

        Assert.assertTrue(it.hasNext());
        issue = it.next();
        Assert.assertEquals("ISS-012", issue.getId());
        Assert.assertTrue(issue.isResolved());

        Assert.assertTrue(it.hasNext());
        issue = it.next();
        Assert.assertEquals("ISS-001", issue.getId());
        Assert.assertTrue(issue.isResolved());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getTopWorkerTest() throws DSException {

        Assert.assertThrows(NoWorkerException.class, () ->
                systemIssues.getTopWorker());

        getDoneIssuesByWorkerTest();

        Worker worker = systemIssues.getTopWorker();
        Assert.assertEquals("W19", worker.getId());

        Issue issue = systemIssues.solveIssue("W20");
        issue = systemIssues.solveIssue("W20");
        issue = systemIssues.solveIssue("W20");

        worker = systemIssues.getTopWorker();
        Assert.assertEquals("W19", worker.getId());

        issue = systemIssues.solveIssue("W20");

        worker = systemIssues.getTopWorker();
        Assert.assertEquals("W20", worker.getId());

    }

    @Test
    public void getSystemWithMostComponentsTest() throws DSException {

        Assert.assertThrows(NoSystemsException.class, () ->
                systemIssues.getSystemWithMostComponents());

        installComponentToSystemTest();

        System system = systemIssues.getSystemWithMostComponents();

        Assert.assertEquals(5, helper.numComponentsBySystem("SYS01"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS02"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS03"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS04"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS05"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS06"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS07"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS08"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS09"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS10"));
        Assert.assertEquals("SYS01", system.getId());

        String[][] components = {
                {"COMP21", "Microsoft", "Surface Dock 2", "M-77665"},
                {"COMP22", "ViewSonic", "VP2785-4K Monitor", "V-11229"},
                {"COMP23", "Netgear", "Nighthawk Switch", "N-99887"},
                {"COMP24", "Wacom", "Cintiq Pro 16", "W-33445"},
                {"COMP25", "Jabra", "Speak 750 Speakerphone", "J-22110"},
                {"COMP26", "Kingston", "IronKey USB Drive", "K-88990"}
        };

        for (String[] component : components) {
            systemIssues.addComponent(component[0], component[1], component[2], component[3]);
            systemIssues.installComponentToSystem(component[0], "SYS10");
        }

        system = systemIssues.getSystemWithMostComponents();

        Assert.assertEquals(5, helper.numComponentsBySystem("SYS01"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS02"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS03"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS04"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS05"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS06"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS07"));
        Assert.assertEquals(2, helper.numComponentsBySystem("SYS08"));
        Assert.assertEquals(1, helper.numComponentsBySystem("SYS09"));
        Assert.assertEquals(7, helper.numComponentsBySystem("SYS10"));
        Assert.assertEquals("SYS10", system.getId());




    }
}