package domain;

import java.time.LocalDate;

public class Reservation extends Transaction {
    private LocalDate expirationDate;

    public Reservation(String id, String userId, String bookIsbn, LocalDate date, String status, LocalDate expirationDate) {
        super(id, userId, bookIsbn, date, status);
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() { return expirationDate; }

    @Override
    public String toString() {
        return super.toString() + " | Expiration: " + expirationDate;
    }
}