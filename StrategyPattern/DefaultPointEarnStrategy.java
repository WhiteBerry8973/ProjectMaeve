package StrategyPattern;
public class DefaultPointEarnStrategy implements PointEarnStrategy {
    @Override public int computeEarnedPoints(int baseAfterDiscount) {
        if (baseAfterDiscount <= 0) return 0;
        return baseAfterDiscount * 2; // 1 บาท = 2 แต้ม
    }
}
