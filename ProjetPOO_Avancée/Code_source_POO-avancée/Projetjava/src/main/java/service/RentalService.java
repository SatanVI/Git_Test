package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import domain.Book;
import domain.Rental;
import domain.User;
import patterns.observer.EventBus;
import patterns.strategy.CappedPenalty;
import patterns.strategy.PenaltyStrategy;
import repository.BookRepository;
import repository.FileStorage;

public class RentalService {
    private final BookRepository bookRepo;
    private final EventBus bus;
    private final PenaltyStrategy penaltyStrategy;
    private final PaymentService paymentService;

    public RentalService(BookRepository bookRepo, EventBus bus, PaymentService paymentService) {
        this.bookRepo = bookRepo;
        this.bus = bus;
        this.paymentService = paymentService;
        this.penaltyStrategy = new CappedPenalty(2.0, 50.0);
    }

    public Rental createRental(Book book, User user, int days) {
        LocalDate start = LocalDate.now();
        LocalDate due = start.plusDays(days);


        if (book.getStock() <= 0) {
            throw new IllegalStateException("Stock insuffisant pour la location du livre: " + book.getTitre());
        }
        book.setStock(book.getStock() - 1);
        bookRepo.save(book);

        Rental rental = new Rental(
                UUID.randomUUID().toString(),
                user.getId(),
                book.getIsbn(),
                start,
                "EN_COURS",
                due
        );

        bus.publish("RENTAL_CREATED", "Nouvelle location : " + rental);
    
        try { FileStorage.getInstance().appendRental(rental); } catch (Exception ignored) {}
        return rental;
    }

    public String returnBook(Rental rental, User user, LocalDate returnDate, String paymentType) {
        rental.setReturnDate(returnDate);

        int daysLate = (int) ChronoUnit.DAYS.between(rental.getDueDate(), returnDate);
        double penalty = penaltyStrategy.compute(Math.max(0, daysLate));

       
        rental.setPenalite(penalty);

        String paymentMsg = null;
        if (daysLate > 0) {
            bus.publish("LATE_RENTAL",
                    "Retard détecté pour location " + rental.getId() + " | Pénalité: " + penalty + "DHS");
           
            if (paymentService != null && user != null) {
                paymentMsg = paymentService.chargePenalty(user, penalty, paymentType);
            }
        } else {
            bus.publish("RENTAL_RETURNED",
                    "Livre retourné à temps pour location " + rental.getId());
        }
        return paymentMsg;
    }
}