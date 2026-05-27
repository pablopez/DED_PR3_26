package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;

public class Room extends AbstractModel {


    private String name;
    private final DictionaryAVLImpl<String, System> systems;

    public Room(String id, String name) {
        super(id);
        setName(name);
        this.systems = new DictionaryAVLImpl<>();
    }

    public String getRoomId() {
        return getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSystem(System system) {
        systems.put(system.getId(), system);
    }

    public void removeSystem(System system) {
        systems.delete(system.getId());
    }
}