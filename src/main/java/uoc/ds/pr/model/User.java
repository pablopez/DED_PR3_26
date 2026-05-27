package uoc.ds.pr.model;

import uoc.ds.pr.Role;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;

public class User extends AbstractModel {
    private String name;
    private Role role;
    private String phone;
    private final DictionaryAVLImpl<String, Assistance> assistances;

    public User(String id, String name, Role role, String phone) {
        super(id);
        setName(name);
        setRole(role);
        setPhone(phone);
        this.assistances = new DictionaryAVLImpl<>();
    }

    public void update(String name, Role role, String phone) {
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addAssistance(Assistance assistance) {
        assistances.put(assistance.getId(), assistance);
    }

    public int getAssistancesCount() {
        return assistances.size();
    }

    public int numAssistances() {
        return assistances.size();
    }
}