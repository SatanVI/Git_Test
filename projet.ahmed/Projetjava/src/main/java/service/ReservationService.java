package service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import domain.Book;
import domain.Reservation;
import domain.User;
import patterns.observer.EventBus;
import repository.BookRepository;

public class ReservationService {
    private final BookRepository bookRepo;
    private final EventBus bus;
    private final Map<String, Queue<Reservation>> reservationsByBook = new HashMap<>();

    public ReservationService(BookRepository bookRepo, EventBus bus) {
        this.bookRepo = bookRepo;
        this.bus = bus;
    }

    public Reservation createReservation(Book book, User user, int validDays) {
        LocalDate start = LocalDate.now();
        LocalDate expiration = start.plusDays(validDays);

        Reservation reservation = new Reservation(UUID.randomUUID().toString(), user.getId(), book.getIsbn(),
                start, "ACTIVE", expiration);

        reservationsByBook.computeIfAbsent(book.getIsbn(), k -> new LinkedList<>()).add(reservation);

        bus.publish("RESERVATION_CREATED", "Nouvelle réservation: " + reservation);

        try {
            repository.FileStorage.getInstance().appendReservation(reservation);
        } catch (Exception ignored) {}

        return reservation;
    }

    public void notifyAvailability(Book book) {
        Queue<Reservation> queue = reservationsByBook.get(book.getIsbn());
        if (queue != null && !queue.isEmpty()) {
            Reservation next = queue.poll();
            bus.publish("RESERVATION_READY", "Livre disponible pour réservation " + next.getId() + " (Utilisateur: " + next.getUserId() + ")");
            next.setStatus("VALIDEE");
        }
    }

    public void expireReservations() {
        LocalDate today = LocalDate.now();
        for (Queue<Reservation> queue : reservationsByBook.values()) {
            for (Reservation r : queue) {
                if (r.getExpirationDate().isBefore(today)) {
                    r.setStatus("EXPIREE");
                    bus.publish("RESERVATION_EXPIRED", "Réservation expirée: " + r.getId());
                }
            }
        }
    }
}