package patterns.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PenaltyStrategyTest {

    @Test
    public void cappedPenalty_underCap() {
        CappedPenalty p = new CappedPenalty(2.0, 10.0);
        assertEquals(6.0, p.compute(3), 0.0001);
    }

    @Test
    public void cappedPenalty_overCap() {
        CappedPenalty p = new CappedPenalty(2.0, 10.0);
        assertEquals(10.0, p.compute(10), 0.0001);
    }

    @Test
    public void cappedPenalty_negativeDays() {
        CappedPenalty p = new CappedPenalty(2.0, 10.0);
        assertEquals(-2.0, p.compute(-1), 0.0001);
    }
}
