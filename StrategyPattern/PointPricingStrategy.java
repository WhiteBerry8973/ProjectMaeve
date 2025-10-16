package StrategyPattern;

public class PointPricingStrategy implements DiscountStrategy {
    @Override
    public double apply(double subtotal, PricingContext ctx) {
        if (subtotal <= 0) return 0;
        if (ctx == null) return subtotal;

        int pts = Math.max(0, ctx.userPoints);

        int unitsFromPoints = pts / 100;
        if (unitsFromPoints <= 0) {
            ctx.pointsToDeduct = 0;
            return subtotal;
        }

        int unitsCapBySubtotal = (int) Math.floor(subtotal);

        int unitsToRedeem = Math.min(unitsFromPoints, unitsCapBySubtotal);

        double discount = unitsToRedeem * 1.0;
        ctx.pointsToDeduct = unitsToRedeem * 100;

        double total = subtotal - discount;
        return total < 0 ? 0 : total;
    }
}
