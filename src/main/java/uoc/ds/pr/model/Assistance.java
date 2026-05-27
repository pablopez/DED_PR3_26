package uoc.ds.pr.model;

import uoc.ds.pr.Role;
import java.time.LocalDate;

public class Assistance extends AbstractModel {
    private User user;
    private Worker worker;
    private Room room;
    private IssueType issueType;
    private LocalDate date;
    private String description;
    private Rating rating;
    private boolean resolved;

    public Assistance(String id, User user, IssueType issueType, Room room, LocalDate date, String description) {
        super(id);
        setUser(user);
        setIssueType(issueType);
        setRoom(room);
        setDate(date);
        setDescription(description);
        this.worker = null;
        this.rating = null;
        this.resolved = false;
    }

    public User getUser() {
        return user;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Worker getWorker() {
        return worker;
    }

    public Room getRoom() { return room; }

    public void setRoom(Room room) { this.room = room;}

    public void setUser(User user) {
        this.user = user;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) { this.resolved = resolved; }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public boolean isRated() {
        return this.getRating()  != null;
    }

    public Role getRole() {
        return this.user.getRole();
    }
}