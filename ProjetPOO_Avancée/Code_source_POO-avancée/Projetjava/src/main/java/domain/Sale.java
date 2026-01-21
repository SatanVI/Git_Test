package domain;

import java.time.LocalDate;

public class Sale extends Transaction {
    private double montant;

    public Sale(String id, String userId, String bookIsbn, LocalDate date, String status, double montant) {
        super(id, userId, bookIsbn, date, status);
        this.montant = montant;
    }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    @Override
    public String toString() {
        return super.toString() + " | Montant: " + montant + "DHS";
    }
}