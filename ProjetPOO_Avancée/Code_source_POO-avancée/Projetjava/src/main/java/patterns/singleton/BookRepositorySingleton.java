package patterns.singleton;

import repository.BookRepository;


public class BookRepositorySingleton {
    private static final BookRepository INSTANCE = new BookRepository();

    private BookRepositorySingleton() { }

    public static BookRepository getInstance() {
        return INSTANCE;
    }
}
