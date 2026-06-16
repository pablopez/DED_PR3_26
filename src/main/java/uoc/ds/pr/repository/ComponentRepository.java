package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.ComponentNotFoundException;
import uoc.ds.pr.model.Component;
import uoc.ds.pr.storage.AVLStorageStrategy;

public class ComponentRepository extends AbstractRepository<Component> {

    public ComponentRepository() {
        super(new AVLStorageStrategy<>());
    }

    public Component addComponent(
            String id,
            String trademark,
            String model,
            String serial
    ) {
        return addElement(
                id,
                key -> new Component(key, trademark, model, serial),
                (component, data) -> component.update(
                        data[0],
                        data[1],
                        data[2]
                ),
                trademark,
                model,
                serial
        );
    }

    public Component getComponent(String id) {
        return getById(id);
    }

    public Component getComponentOrThrow(String id)
            throws ComponentNotFoundException {
        return getByIdOrThrow(id, ComponentNotFoundException::new);
    }

    public int numComponents() {
        return size();
    }
}