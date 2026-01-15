package patterns.strategy;

public class CappedPenalty implements PenaltyStrategy {
    private final double perDay;
    private final double cap;

    public CappedPenalty(double perDay, double cap) {
        this.perDay = perDay;
        this.cap = cap;
    }

    @Override
    public double compute(int daysLate) {
        return Math.min(daysLate * perDay, cap);
    }
}