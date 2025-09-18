package StrategyPattern;

public class PointToCashDiscount implements DiscountStrategy {
    private double cashPerPoint = 0.01; // 1 point = 0.01 baht , 100 points = 1 baht

    @Override
    public double applyDiscount(double price, int points) {
        double discount = points * cashPerPoint;
        return Math.max(0, price - discount);
    }
}
