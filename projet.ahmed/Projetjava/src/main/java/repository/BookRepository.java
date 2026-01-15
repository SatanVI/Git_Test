package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import domain.Book;

public class BookRepository {
    private final Map<String, Book> booksByIsbn = new HashMap<>();
    private final FileStorage storage = FileStorage.getInstance();

    
    public BookRepository() {
        Map<String, Book> loaded = storage.loadBooks();
        if (loaded != null) {
            booksByIsbn.putAll(loaded);
        }
    }

    public List<Book> findAll() {
        return new ArrayList<>(booksByIsbn.values());
    }

    public List<Book> findByCategory(String categorieId) {
        List<Book> res = new ArrayList<>();
        for (Book b : booksByIsbn.values()) {
            if (b.getCategorieId() != null && b.getCategorieId().equals(categorieId)) {
                res.add(b);
            }
        }
        return res;
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    public void save(Book book) {
        booksByIsbn.put(book.getIsbn(), book);
        storage.saveBooks(findAll());
    }

    public void delete(String isbn) {
        booksByIsbn.remove(isbn);
        storage.saveBooks(findAll());
    }
}