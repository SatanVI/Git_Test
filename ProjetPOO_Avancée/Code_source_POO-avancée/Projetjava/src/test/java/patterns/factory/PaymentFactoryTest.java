package patterns.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PaymentFactoryTest {

    @Test
    public void createCardPayment() {
        Payment p = PaymentFactory.create("carte");
        assertNotNull(p);
        String out = p.pay(12.5);
        assertTrue(out.contains("CARTE") || out.toLowerCase().contains("carte"));
    }

    @Test
    public void createCashPayment() {
        Payment p = PaymentFactory.create("especes");
        assertNotNull(p);
        String out = p.pay(5);
        assertTrue(out.toUpperCase().contains("ESPECES") || out.toLowerCase().contains("especes"));
    }

    @Test
    public void createPaypalAdapter() {
        Payment p = PaymentFactory.create("paypal-sdk");
        assertNotNull(p);
        String out = p.pay(100);
        assertTrue(out.toLowerCase().contains("paypal") || out.toLowerCase().contains("paypal"));
    }
}
