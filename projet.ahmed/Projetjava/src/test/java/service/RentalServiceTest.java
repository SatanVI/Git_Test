package service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.Book;
import domain.Rental;
import domain.User;
import patterns.observer.EventBus;
import repository.BookRepository;

public class RentalServiceTest {

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
    public void createRental_decreasesStock_and_appends() throws Exception {
        Path rentalsPath = Paths.get("data", "rentals.txt");
        List<String> backup = null;
        if (Files.exists(rentalsPath)) backup = Files.readAllLines(rentalsPath, StandardCharsets.UTF_8);
        try {
            InMemoryBookRepo repo = new InMemoryBookRepo();
            EventBus bus = new EventBus();
            final boolean[] called = {false};
            bus.subscribe("RENTAL_CREATED", msg -> called[0] = true);

            Book b = new Book(java.util.UUID.randomUUID().toString(), "ISBN-RENT-1", "LivreLouer", "A", null, 5.0, 3);
            repo.seed(b);
            User u = new User(java.util.UUID.randomUUID().toString(), "Client", "USER");

            RentalService svc = new RentalService(repo, bus, null);
            Rental r = svc.createRental(b, u, 7);
            assertNotNull(r);
            java.util.Optional<Book> stored = repo.findByIsbn("ISBN-RENT-1");
            assertTrue(stored.isPresent());
            assertEquals(2, stored.get().getStock());
            assertTrue(called[0]);
        } finally {
            if (backup != null) Files.write(rentalsPath, backup, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @Test
    public void returnBook_calculatesPenalty_and_charges() {
        InMemoryBookRepo repo = new InMemoryBookRepo();
        EventBus bus = new EventBus();
        final boolean[] latePublished = {false};
        bus.subscribe("LATE_RENTAL", msg -> latePublished[0] = true);

        domain.Rental rental = new Rental("r1", "u1", "ISBN-RET-1", LocalDate.now().minusDays(10), "EN_COURS", LocalDate.now().minusDays(5));
        User u = new User("u1", "Client", "USER");

        final boolean[] charged = {false};
        PaymentService ps = new PaymentService(bus) {
            @Override
            public String chargePenalty(User user, double amount, String paymentType) {
                charged[0] = true;
                return "charged";
            }
        };

        RentalService svc = new RentalService(repo, bus, ps);
        String res = svc.returnBook(rental, u, LocalDate.now(), "especes");
        assertEquals("charged", res);
        assertTrue(latePublished[0]);
        assertTrue(charged[0]);
        assertTrue(rental.getPenalite() >= 0);
    }
}
