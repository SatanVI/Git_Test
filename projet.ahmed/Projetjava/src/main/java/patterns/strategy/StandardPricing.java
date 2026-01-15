package patterns.strategy;

public class StandardPricing implements PricingStrategy {
    @Override
    public double apply(double basePrice) { return basePrice; }
}
