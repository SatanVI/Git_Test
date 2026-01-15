package patterns.strategy;

public class TeacherPricing implements PricingStrategy {
    @Override
    public double apply(double basePrice) { return basePrice * 0.90; } 
}
