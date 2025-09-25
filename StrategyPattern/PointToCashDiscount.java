package StrategyPattern;

/**
 * AF 100 points = 1 baht, 105 points = 1 baht and 5 points are not redeemable
 * If points are less than 100, no discount is applied
 * If discount exceeds price, throw IllegalArgumentException
 */
public class PointToCashDiscount implements DiscountStrategy {
    private double cashPer100Point = 1.0; // 100 points = 1 baht

    @Override
    public double applyDiscount(double price, int points) {
        if (points < 100) {
            return price; // No discount if less than 100 points
        }

        int redeemablePoints = (points / 100) * 100; // Only full hundreds are redeemable
        double discount = (redeemablePoints / 100) * cashPer100Point;

        if (discount > price) {
            throw new IllegalArgumentException("Discount exceeds price");
        }
        
        return price - discount;
    }
}
