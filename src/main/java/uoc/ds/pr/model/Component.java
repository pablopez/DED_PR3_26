package uoc.ds.pr.model;

import uoc.ds.pr.util.DSLinkedList;

import java.util.Comparator;

public class Component extends AbstractModel {
    public static final Comparator<Component> CMP = (o1, o2) -> o1.getId().compareTo(o2.getId());
    private String trademark;
    private String model;
    private String serial;
    private System system;
    private final DSLinkedList<Issue> issues;

    public Component(String key, String trademark, String model, String serial) {
        super(key);
        setTrademark(trademark);
        setModel(model);
        setSerial(serial);
        issues = new DSLinkedList<>();
    }

    public void update(String trademark, String model, String serial) {
        setTrademark(trademark);
        setModel(model);
        setSerial(serial);
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public System getSystem() {
        return system;
    }

    public void addIssue(Issue issue) {
        issues.put(issue.getId(), issue);
    }

    public int getIssuesCont() {
        return issues.size();
    }
}
