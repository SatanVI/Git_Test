package patterns.strategy;

public class StudentPricing implements PricingStrategy {
    @Override
    public double apply(double basePrice) { return basePrice * 0.85; } 
}
