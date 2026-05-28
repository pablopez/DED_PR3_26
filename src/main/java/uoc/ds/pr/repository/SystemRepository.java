package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.ComponentAlreadyInstalledException;
import uoc.ds.pr.exceptions.NoSystemsException;
import uoc.ds.pr.exceptions.SystemHasNoComponentsException;
import uoc.ds.pr.exceptions.SystemNotFoundException;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.User;
import uoc.ds.pr.model.System;

public class SystemRepository
        extends AbstractRepository<uoc.ds.pr.model.System> {

    private uoc.ds.pr.model.System systemWithMostComponents;

    public SystemRepository() {
        super(new AVLStorageStrategy<>());
    }

    public uoc.ds.pr.model.System addSystem(
            String id,
            String description,
            String location,
            User user
    ) {
        return addElement(
                id,
                key -> new System(
                        key,
                        description,
                        location,
                        user
                ),
                (system, data) -> system.update(
                        data[0],
                        data[1],
                        user
                ),
                description,
                location
        );
    }

    public System getSystem(String id) {
        return getById(id);
    }

    public System getSystemOrThrow(String id)
            throws SystemNotFoundException {
        return getByIdOrThrow(id, SystemNotFoundException::new);
    }

    public int numSystems() {
        return size();
    }

    public Iterator<System> getSystems() throws NoSystemsException {
       if( this.size() == 0 ) throw new NoSystemsException();
       return values();
    }

    public void addComponent(
            String systemID,
            Component component
    ) throws ComponentAlreadyInstalledException {
        System system = getSystem(systemID);
        if(this.isInstalled(system, component)) {
            throw new ComponentAlreadyInstalledException();
        }else{
            System oldSystem = component.getSystem();
            if(oldSystem != null) {
                oldSystem.removeComponent(component);
                this.updateSystemWithMostComponents(oldSystem);
            }
            system.addComponent(component);
            this.updateSystemWithMostComponents(system);
        }
    }

    public boolean isInstalled(
            System system,
            Component component
    ) {
        return system.getComponents().get(component.getId()) != null;
    }

    public void updateSystemWithMostComponents(
            System system
    ) {
        if (systemWithMostComponents == null
                || system.numComponents()
                > systemWithMostComponents.numComponents()) {
            systemWithMostComponents = system;
        }
    }

    public Iterator<Component> getComponentsBySystem(String systemId) throws SystemHasNoComponentsException{
        System system = getSystem(systemId);
        if(system.numComponents() == 0) throw new SystemHasNoComponentsException();
        return system.components();
    }

    public System getSystemWithMostComponents() throws NoSystemsException {
        if  (systemWithMostComponents==null) {
            throw new NoSystemsException();
        }
        return systemWithMostComponents;
    }
}