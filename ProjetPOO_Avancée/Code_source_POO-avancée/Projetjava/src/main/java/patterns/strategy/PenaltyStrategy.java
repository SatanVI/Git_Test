package patterns.strategy;

public interface PenaltyStrategy {
    double compute(int daysLate);
}

class FixedPenalty implements PenaltyStrategy {
    private final double perDay;
    public FixedPenalty(double perDay) { this.perDay = perDay; }
    @Override
    public double compute(int daysLate) {
        int effective = Math.max(0, daysLate);
        return effective * perDay;
    }
}
