package Lib;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import Gui.MainGui.MaeveCoffeeUI;

public class LoyaltyService {
    private final Supplier<Boolean> isSignedIn;
    private final IntConsumer addPoints;

    public LoyaltyService(Supplier<Boolean> isSignedIn, IntConsumer addPoints) {
        this.isSignedIn = isSignedIn;
        this.addPoints = addPoints;
    }

    // ให้แต้มครั้งเดียว/ออเดอร์
    public int grantIfEligible(MaeveCoffeeUI.OrderSummary o, int earned) {
        if (o == null) return 0;
        if (!isSignedIn.get()) { o.pointsEarned = 0; o.pointsGranted = true; return 0; }
        if (o.pointsGranted) return 0;

        int grant = Math.max(0, earned);
        if (grant > 0) addPoints.accept(grant);

        o.pointsEarned = grant;
        o.pointsGranted = true;
        return grant;
    }

    // ตัดแต้มที่ใช้แลกส่วนลด
    public void redeemPointsIfAny(int usedPts) {
        if (!isSignedIn.get()) return;
        if (usedPts > 0) addPoints.accept(-usedPts);
    }
}
