package patterns.factory;

import patterns.adapter.ExternalPaypalClient;
import patterns.adapter.PaypalAdapter;

class CardPayment implements Payment {
    public String pay(double amount) { return "Paiement par CARTE: " + amount + "€"; }
}

class CashPayment implements Payment {
    public String pay(double amount) { return "Paiement en ESPECES: " + amount + "€"; }
}

class PaypalPayment implements Payment {
    public String pay(double amount) { return "Paiement via PAYPAL (simulé): " + amount + "€"; }
}

public class PaymentFactory {
    public static Payment create(String type) {
        switch (type.toLowerCase()) {
            case "carte": return new CardPayment();
            case "especes": return new CashPayment();
            case "paypal": return new PaypalPayment();
      
            case "paypal-sdk": return new PaypalAdapter(new ExternalPaypalClient("MERCHANT_123"));
            default: return new CashPayment();
        }
    }
}