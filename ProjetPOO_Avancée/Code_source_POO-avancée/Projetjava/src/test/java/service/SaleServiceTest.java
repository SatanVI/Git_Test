package service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.Book;
import domain.User;
import patterns.observer.EventBus;
import repository.BookRepository;

public class SaleServiceTest {

    private static class InMemoryBookRepo extends BookRepository {
        private final Map<String, Book> map = new HashMap<>();

        @Override
        public java.util.List<Book> findAll() { return new java.util.ArrayList<>(map.values()); }

        @Override
        public java.util.Optional<Book> findByIsbn(String isbn) { return java.util.Optional.ofNullable(map.get(isbn)); }

        @Override
        public void save(Book book) { map.put(book.getIsbn(), book); }

        @Override
        public void delete(String isbn) { map.remove(isbn); }

        public void seed(Book b) { map.put(b.getIsbn(), b); }
    }

    @Test
    public void createSale_decreasesStock_and_publishes() throws Exception {
        Path salesPath = Paths.get("data", "sales.txt");
        List<String> backup = null;
        if (Files.exists(salesPath)) backup = Files.readAllLines(salesPath, StandardCharsets.UTF_8);
        try {
            InMemoryBookRepo repo = new InMemoryBookRepo();
            EventBus bus = new EventBus();
            final boolean[] called = {false};
            bus.subscribe("SALE_CREATED", msg -> called[0] = true);

            Book b = new Book(java.util.UUID.randomUUID().toString(), "ISBN-SALE-1", "LivreTest", "A", null, 20.0, 2);
            repo.seed(b);
            User u = new User(java.util.UUID.randomUUID().toString(), "Client", "USER");
            u.setType("standard");

            SaleService svc = new SaleService(repo, bus);
            domain.Sale sale = svc.createSale(b, u, "especes");
            assertNotNull(sale);
            java.util.Optional<Book> stored = repo.findByIsbn("ISBN-SALE-1");
            assertTrue(stored.isPresent());
            assertEquals(1, stored.get().getStock());
            assertTrue(called[0]);
            assertEquals(u.getId(), sale.getUserId());
        } finally {
            if (backup != null) Files.write(salesPath, backup, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
