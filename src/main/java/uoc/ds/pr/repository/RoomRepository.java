package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.RoomNotFoundException;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.storage.VectorStorageStrategy;

import static uoc.ds.pr.pr3.SystemIssuesPR3.MAX_ROOMS;

public class RoomRepository extends AbstractRepository<Room> {

    public RoomRepository() {
        super(new VectorStorageStrategy<>(MAX_ROOMS));
    }

    public Room addRoom(String id, String name) {
        return addElement(
                id,
                key -> new Room(key, name),
                (room, data) -> room.setName(data[0]),
                name
        );
    }

    public Room getRoom(String id) {
        return getById(id);
    }

    public Room getRoomOrThrow(String id)
            throws RoomNotFoundException {
        return getByIdOrThrow(id, RoomNotFoundException::new);
    }

    public int numRooms() {
        return size();
    }
}