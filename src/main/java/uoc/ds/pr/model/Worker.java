package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.sequential.Stack;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.util.AssignedAssistanceQueue;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.StackLinkedList;


public class Worker extends AbstractModel {
    private String name;
    private String address;
    private final Stack<Issue> issues;
    private IssueType issueType;
    private int solvedIssuesCont;
    private final AssignedAssistanceQueue assignedAssistances;
    private double ratingsSum;
    private final DictionaryAVLImpl<Integer, Issue> solvedIssues;
    private final DictionaryAVLImpl<String, Assistance> solvedAssistances;
    private final DSLinkedList<Rating> rates;

    public Worker(String id, String name, String address) {
        super(id);
        setName(name);
        setAddress(address);
        issues = new StackLinkedList<>();
        solvedIssues = new DictionaryAVLImpl<>();
        solvedAssistances = new DictionaryAVLImpl<>();
        assignedAssistances = new AssignedAssistanceQueue();
        rates = new DSLinkedList<>();
        ratingsSum = 0.0;
        solvedIssuesCont = 0;


    }

    public void update(String name, String address) {
        setName(name);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addIssue(Issue issue) {
        issues.push(issue);
    }

    public int numPendingIssues() {
        return this.issues.size();
    }

    public int numSolvedIssues() {
        return this.solvedIssuesCont;
    }

    public boolean hasPendingIssues() {
        return !issues.isEmpty();
    }

    public Issue solveNextIssue() {
        Issue issue = issues.pop();
        solvedIssuesCont = solvedIssuesCont + 1;
        this.solvedIssues.put(solvedIssuesCont, issue);
        issue.setResolved(true);
        return issue;
    }

    public int numSolvedAssistances() {
        return solvedAssistances.size();
    }

    public void addAssignedAssistance(Assistance assistance) {
        assignedAssistances.add(assistance);
    }

    public Assistance solveNextAssistance() {
        if (assignedAssistances.isEmpty()) {
            return null;
        }

        Assistance assistance = assignedAssistances.poll();

        assistance.setResolved(true);
        solvedAssistances.put(assistance.getId(), assistance);

        return assistance;
    }

    public int numAssignedAssistances() {
        return assignedAssistances.size();
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public void addRate(Rating rating) {
        rates.put(rating.getId(), rating);
        ratingsSum += rating.getAverage();
    }

    public double getGlobalRating() {
        if (!hasRates()) {
            return 0;
        }
        return ratingsSum / rates.size() ;
    }

    public Iterator<Issue> getSolvedIssues() {
        return solvedIssues.values();
    }

    public boolean hasRates(){
        return rates.size() > 0;
    }

}
