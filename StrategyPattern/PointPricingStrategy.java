package StrategyPattern;

/**
 * ส่วนลดจากแต้ม: 100 แต้ม = ลด 1 บาท
 * - ปัดลงเสมอ (floor): 99 แต้ม = 0 หน่วย, 199 แต้ม = 1 หน่วย
 * - ลดได้ไม่เกินยอดสินค้า (บาท)
 * - บันทึกจำนวนแต้มที่จะหักจริงลง ctx.pointsToDeduct
 */
public class PointPricingStrategy implements DiscountStrategy {
    @Override
    public double apply(double subtotal, PricingContext ctx) {
        if (subtotal <= 0) return 0;           // กันค่าติดลบ/ศูนย์
        if (ctx == null) return subtotal;

        int pts = Math.max(0, ctx.userPoints);

        // 1) หน่วยส่วนลดจากแต้มที่มี (floor)
        int unitsFromPoints = pts / 100;       // 199 -> 1, 99 -> 0
        if (unitsFromPoints <= 0) {
            ctx.pointsToDeduct = 0;
            return subtotal;                   // ไม่มีสิทธิ์แลก
        }

        // 2) หน่วยส่วนลดสูงสุดจำกัดตามยอดสินค้า (บาท) — ห้ามลดเกินยอด
        int unitsCapBySubtotal = (int) Math.floor(subtotal);

        // 3) หน่วยส่วนลดที่ใช้จริง
        int unitsToRedeem = Math.min(unitsFromPoints, unitsCapBySubtotal);

        // 4) คำนวณส่วนลดและแต้มที่จะถูกหักจริง
        double discount = unitsToRedeem * 1.0;     // 1 หน่วย = 1 บาท
        ctx.pointsToDeduct = unitsToRedeem * 100;  // หักแต้มเท่าที่ใช้จริง

        // 5) คืนยอดสุทธิหลังหัก (ไม่ติดลบ)
        double total = subtotal - discount;
        return total < 0 ? 0 : total;
    }
}
