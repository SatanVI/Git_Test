package service;

import domain.User;
import patterns.factory.Payment;
import patterns.factory.PaymentFactory;
import patterns.observer.EventBus;

public class PaymentService {
    private final EventBus bus;

    public PaymentService(EventBus bus) {
        this.bus = bus;
    }

    public String chargePenalty(User user, double amount, String paymentType) {
        Payment payment = PaymentFactory.create(paymentType);
        String msg = payment.pay(amount);
        bus.publish("PENALTY_CHARGED", "Pénalité facturée à " + user.getId() + ": " + amount + "DHS via " + paymentType);
        return msg;
    }
}
