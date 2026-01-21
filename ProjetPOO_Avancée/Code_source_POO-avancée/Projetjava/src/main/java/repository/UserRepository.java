package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import domain.User;

public class UserRepository {
    private final Map<String, User> usersById = new HashMap<>();
    private final FileStorage storage = FileStorage.getInstance();

    public UserRepository() {
        Map<String, User> loaded = storage.loadUsers();
        if (loaded != null) {
            usersById.putAll(loaded);
        }
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        for (User u : usersById.values()) {
            if (username.equals(u.getUsername())) return Optional.of(u);
        }
        return Optional.empty();
    }

    public void save(User user) {
        usersById.put(user.getId(), user);
        storage.saveUsers(findAll());
    }

    public void delete(String id) {
        usersById.remove(id);
        storage.saveUsers(findAll());
    }
}