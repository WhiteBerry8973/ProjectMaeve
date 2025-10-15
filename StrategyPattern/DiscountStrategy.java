package StrategyPattern;


public interface DiscountStrategy {

    double apply(double subtotal, PricingContext ctx);
}
