package service;

import java.util.List;
import java.util.Optional;

import domain.Book;
import repository.BookRepository;

public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> listAll() {
        return repository.findAll();
    }

    public Optional<Book> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    public List<Book> findByTitle(String titre) {
        List<Book> res = new java.util.ArrayList<>();
        for (Book b : repository.findAll()) {
            if (b.getTitre() != null && b.getTitre().toLowerCase().contains(titre.toLowerCase())) {
                res.add(b);
            }
        }
        return res;
    }

    public List<Book> findByCategory(String categorieId) {
        return repository.findByCategory(categorieId);
    }

    public void addBook(Book book) {
        repository.save(book);
    }

    public void updateStock(String isbn, int delta) {
        repository.findByIsbn(isbn).ifPresent(book -> {
            book.setStock(book.getStock() + delta);
            repository.save(book);
        });
    }

    public void deleteBook(String isbn) {
        repository.delete(isbn);
    }
}