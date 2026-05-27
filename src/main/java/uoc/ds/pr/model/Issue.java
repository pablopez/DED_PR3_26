package uoc.ds.pr.model;

import java.time.LocalDateTime;

public class Issue extends AbstractModel {
    private Component component;
    private Worker worker;
    private IssueType issueType;
    private String description;
    private LocalDateTime dateTime;
    private boolean isResolved;

    public Issue(String id, Component component, IssueType issueType, String description, LocalDateTime dateTime) {
        super(id);
        setComponent(component);
        setDescription(description);
        setDateTime(dateTime);
        setIssueType(issueType);
        this.isResolved = false;
        this.worker = null;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isAssigned() {
        return worker != null;
    }

    public void setWorker(Worker w) {
        this.worker = w;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean b) {
        isResolved = b;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

}
