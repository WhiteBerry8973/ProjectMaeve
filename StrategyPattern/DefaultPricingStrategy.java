package StrategyPattern;

public class DefaultPricingStrategy implements DiscountStrategy {
    @Override
    public double apply(double subtotal, PricingContext ctx) {
        return subtotal;
    }
}
