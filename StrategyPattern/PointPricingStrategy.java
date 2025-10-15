package StrategyPattern;

/**
 * ส่วนลดจากแต้ม: 100 แต้ม = ลด 1 บาท
 * ถ้าคะแนนไม่ถึง 100 จะไม่ได้ส่วนลด และจะไม่หักราคาติดลบ
 */
public class PointPricingStrategy implements DiscountStrategy {
    @Override
    public double apply(double subtotal, PricingContext ctx) {
        if (ctx == null)
            return subtotal;
        int pts = Math.max(0, ctx.userPoints);
        int units = pts / 100;
        double pointDiscount = units * 1.0;
        double discount = Math.min(subtotal, pointDiscount);
        return subtotal - discount;
    }
}
