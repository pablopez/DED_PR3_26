package uoc.ds.pr.model;


import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.util.DSLinkedList;

public class System extends AbstractModel {
    private String description;
    private String location;
    private final DSLinkedList<Component> components;
    private Room room;
    private User user;

    public System(String id, String description, String location, User user) {
        super(id);
        setDescription(description);
        setLocation(location);
        setUser(user);
        components = new DSLinkedList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void update(String description, String location,  User user) {
        setDescription(description);
        setLocation(location);
        setUser(user);
    }

    public void addComponent(Component component) {
        components.put(component.getId(), component);
    }

    public void removeComponent(Component component) {
        components.remove(component.getId());
        component.setSystem(null);
    }
    public DSLinkedList<Component> getComponents() {
        return components;
    }

    public int numComponents() {
        return components.size();
    }

    public Iterator<Component> components() {
        return components.values();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        Room oldRoom = this.room;

        if (oldRoom != null) {
            oldRoom.removeSystem(this);
        }

        this.room = room;

        if (room != null) {
            room.addSystem(this);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

