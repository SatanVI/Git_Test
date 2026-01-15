package service;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import domain.User;
import repository.UserRepository;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> listAll() {
        return repository.findAll();
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> maybe = repository.findByUsername(username);
        if (maybe.isEmpty()) return Optional.empty();
        User u = maybe.get();
        String stored = u.getPassword();
        if (stored == null) return Optional.empty();
        boolean ok = false;
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            ok = BCrypt.checkpw(password, stored);
        } else {
            ok = stored.equals(password);
            if (ok) {
                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                u.setPassword(hashed);
                repository.save(u);
            }
        }
        return ok ? Optional.of(u) : Optional.empty();
    }

    public void addUser(User user) {
        String pw = user.getPassword();
        if (pw != null && !pw.isEmpty() && !(pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$"))) {
            String hashed = BCrypt.hashpw(pw, BCrypt.gensalt());
            user.setPassword(hashed);
        }
        repository.save(user);
    }

    public void deleteUser(String id) {
        repository.delete(id);
    }
}