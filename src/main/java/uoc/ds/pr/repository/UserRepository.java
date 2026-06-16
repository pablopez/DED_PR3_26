package uoc.ds.pr.repository;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.Role;
import uoc.ds.pr.exceptions.NoUserException;
import uoc.ds.pr.exceptions.UserNotFoundException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.User;
import uoc.ds.pr.storage.HashStorageStrategy;
import uoc.ds.pr.util.OrderedVector;

import java.util.Comparator;

import static uoc.ds.pr.pr3.SystemIssuesPR3.TOP_N_USERS_MOST_ASSISTANCES;

public class UserRepository extends AbstractRepository<User> {

   private final OrderedVector<User> topAssistedUsers;

    private static final Comparator<User> CMP_TOP_ASSISTANCES_USERS =
            Comparator.comparingInt(User::getAssistancesCount);

    public UserRepository() {
        super(new HashStorageStrategy<>());
        this.topAssistedUsers = new OrderedVector<>(
                TOP_N_USERS_MOST_ASSISTANCES,
                CMP_TOP_ASSISTANCES_USERS
        );
    }

    public User addUser(String id, String name, Role role, String phone) {
        return addElement(
                id,
                key -> new User(key, name, role, phone),
                (user, _) -> user.update(name, role, phone),
                name,
                role.name(),
                phone
        );
    }

    public User getUser(String id) {
        return getById(id);
    }

    public User getUserOrThrow(String id) throws UserNotFoundException {
        return getByIdOrThrow(id, UserNotFoundException::new);
    }

    public int numUsers() {
        return size();
    }

    public void addAssistance(User user, Assistance assistance) {
        topAssistedUsers.delete(user);
        user.addAssistance(assistance);
        topAssistedUsers.update(user);
    }

    public Iterator<User> getTopAssistedUsers() throws NoUserException {
        if(topAssistedUsers.isEmpty()) throw new NoUserException();
        return topAssistedUsers.values();
    }
}