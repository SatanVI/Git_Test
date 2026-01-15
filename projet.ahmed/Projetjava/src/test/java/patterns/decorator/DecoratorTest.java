package patterns.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DecoratorTest {

    @Test
    public void taxReceipt_calculatesTax() {
        Receipt base = new BaseReceipt("Total: 100");
        Receipt taxed = new TaxReceipt(base, 0.2);
        String out = taxed.render();
        assertTrue(out.contains("Taxe"));
        assertTrue(out.contains("20.00"));
    }

    @Test
    public void footerReceipt_appendsFooter() {
        Receipt base = new BaseReceipt("Total: 50");
        Receipt foot = new FooterReceipt(base);
        String out = foot.render();
        assertTrue(out.contains("Merci pour votre achat"));
        assertTrue(out.startsWith("Total: 50"));
    }

    @Test
    public void combined_decorator_order() {
        Receipt base = new BaseReceipt("Total: 200");
        Receipt taxed = new TaxReceipt(base, 0.1);
        Receipt foot = new FooterReceipt(taxed);
        String out = foot.render();
        assertTrue(out.contains("Taxe"));
        assertTrue(out.contains("20.00"));
        assertTrue(out.contains("Merci pour votre achat"));
    }
}
