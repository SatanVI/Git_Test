package service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.User;
import repository.UserRepository;

public class UserServiceTest {

    private static class TestUserRepository extends UserRepository {
        private final java.util.Map<String, User> map = new java.util.HashMap<>();

        @Override
        public java.util.List<User> findAll() {
            return new java.util.ArrayList<>(map.values());
        }

        @Override
        public Optional<User> findById(String id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public Optional<User> findByUsername(String username) {
            if (username == null) return Optional.empty();
            for (User u : map.values()) {
                if (username.equals(u.getUsername())) return Optional.of(u);
            }
            return Optional.empty();
        }

        @Override
        public void save(User user) {
            map.put(user.getId(), user);
        }

        @Override
        public void delete(String id) {
            map.remove(id);
        }

        public void seed(User u) {
            map.put(u.getId(), u);
        }
    }

    @Test
    public void addUser_hashesPassword() {
        TestUserRepository repo = new TestUserRepository();
        UserService svc = new UserService(repo);
        User u = new User(java.util.UUID.randomUUID().toString(), "Bangoura", "USER");
        u.setUsername("Bangoura");
        u.setPassword("0000");
        svc.addUser(u);
        Optional<User> stored = repo.findByUsername("Bangoura");
        assertTrue(stored.isPresent());
        String pw = stored.get().getPassword();
        assertNotNull(pw);
        assertTrue(pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$"));
    }

    @Test
    public void authenticate_migratesPlainPassword() {
        TestUserRepository repo = new TestUserRepository();
        UserService svc = new UserService(repo);
        User u = new User(java.util.UUID.randomUUID().toString(), "Ahmed", "USER");
        u.setUsername("Ahmed");
        u.setPassword("0000");
        repo.seed(u);
        Optional<User> auth = svc.authenticate("Ahmed", "0000");
        assertTrue(auth.isPresent());
        Optional<User> stored = repo.findByUsername("Ahmed");
        assertTrue(stored.isPresent());
        String pw = stored.get().getPassword();
        assertNotNull(pw);
        assertTrue(pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$"));
    }
}
