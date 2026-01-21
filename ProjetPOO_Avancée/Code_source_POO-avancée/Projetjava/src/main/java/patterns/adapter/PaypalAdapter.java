package patterns.adapter;

import patterns.factory.Payment;

public class PaypalAdapter implements Payment {
    private final ExternalPaypalClient client;

    public PaypalAdapter(ExternalPaypalClient client) {
        this.client = client;
    }

    @Override
    public String pay(double amount) {
        boolean ok = client.sendPayment(amount);
        return ok ? "Paiement via PayPal réussi: " + amount + "DHS" : "Erreur paiement PayPal";
    }
}
