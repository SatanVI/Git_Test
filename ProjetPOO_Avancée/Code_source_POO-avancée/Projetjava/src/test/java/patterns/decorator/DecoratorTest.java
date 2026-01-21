package patterns.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DecoratorTest {

    @Test
    public void footerReceipt_appendsFooter() {
        Receipt base = new BaseReceipt("Total: 50");
        Receipt foot = new FooterReceipt(base);
        String out = foot.render();
        assertTrue(out.contains("Merci pour votre achat"));
        assertTrue(out.startsWith("Total: 50"));
    }


}
