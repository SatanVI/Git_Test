package domain;

import java.time.LocalDate;

public class Rental extends Transaction {
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double penalite;

    public Rental(String id, String userId, String bookIsbn, LocalDate date, String status, LocalDate dueDate) {
        super(id, userId, bookIsbn, date, status);
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getPenalite() { return penalite; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setPenalite(double penalite) { this.penalite = penalite; }

    @Override
    public String toString() {
        return super.toString() + " | Retour prévu: " + dueDate + " | Retour réel: " + returnDate + " | Pénalité: " + penalite;
    }
}