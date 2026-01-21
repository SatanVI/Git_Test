package domain;

import java.time.LocalDate;

public abstract class Transaction {
    protected final String id;
    protected final String userId;
    protected final String bookIsbn;
    protected LocalDate date;
    protected String status;

    public Transaction(String id, String userId, String bookIsbn, LocalDate date, String status) {
        this.id = id;
        this.userId = userId;
        this.bookIsbn = bookIsbn;
        this.date = date;
        this.status = status;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getBookIsbn() { return bookIsbn; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Transaction %s | Livre: %s | Utilisateur: %s | Date: %s | Statut: %s",
                id, bookIsbn, userId, date, status);
    }
}