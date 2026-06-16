package uoc.ds.pr.service;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.ComponentAlreadyInstalledException;
import uoc.ds.pr.exceptions.NoSystemsException;
import uoc.ds.pr.exceptions.RoomNotFoundException;
import uoc.ds.pr.exceptions.SystemHasNoComponentsException;
import uoc.ds.pr.exceptions.SystemNotFoundException;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.model.System;
import uoc.ds.pr.repository.ComponentRepository;
import uoc.ds.pr.repository.RoomRepository;
import uoc.ds.pr.repository.SystemRepository;

public class InfrastructureService {

    private final SystemRepository systemRepository;
    private final ComponentRepository componentRepository;
    private final RoomRepository roomRepository;

    public InfrastructureService(
            SystemRepository systemRepository,
            ComponentRepository componentRepository,
            RoomRepository roomRepository
    ) {
        this.systemRepository = systemRepository;
        this.componentRepository = componentRepository;
        this.roomRepository = roomRepository;
    }

    public void assignSystemToRoom(String systemId, String roomId)
            throws SystemNotFoundException, RoomNotFoundException {

        uoc.ds.pr.model.System system =
                systemRepository.getSystemOrThrow(systemId);

        Room room = roomRepository.getRoomOrThrow(roomId);

        system.setRoom(room);
    }

    public void installComponentToSystem(String componentId, String systemId)
            throws ComponentAlreadyInstalledException {

        Component component =
                componentRepository.getComponent(componentId);


        if (component.getSystem() != null) {
            throw new ComponentAlreadyInstalledException();
        }

        systemRepository.addComponent(systemId, component);
    }

    public Iterator<System> getSystems()
            throws NoSystemsException {

        if (systemRepository.numSystems() == 0) {
            throw new NoSystemsException();
        }

        return systemRepository.getSystems();
    }

    public Iterator<Component> getComponentsBySystem(String systemId)
            throws SystemHasNoComponentsException {

        System system =
                systemRepository.getSystem(systemId);

        if (system == null || system.numComponents() == 0) {
            throw new SystemHasNoComponentsException();
        }

        return system.components();
    }
}