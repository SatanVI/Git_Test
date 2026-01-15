package patterns.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PricingStrategyTest {

    @Test
    public void standardPricing_sameAsBase() {
        PricingStrategy s = new StandardPricing();
        assertEquals(100.0, s.apply(100.0));
    }

    @Test
    public void studentPricing_reduces15Percent() {
        PricingStrategy s = new StudentPricing();
        assertEquals(85.0, s.apply(100.0), 0.0001);
    }

    @Test
    public void teacherPricing_reduces10Percent() {
        PricingStrategy s = new TeacherPricing();
        assertEquals(90.0, s.apply(100.0), 0.0001);
    }
}
