package patterns.singleton;

import repository.UserRepository;

public class UserRepositorySingleton {
    private static final UserRepository INSTANCE = new UserRepository();

    private UserRepositorySingleton() { }

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
