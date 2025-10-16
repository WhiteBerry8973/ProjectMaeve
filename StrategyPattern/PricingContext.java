package StrategyPattern;

public class PricingContext {
    public final String username;
    public final int userPoints;

    public int pointsToDeduct = 0;

    public PricingContext(String username, int userPoints) {
        this.username = username;
        this.userPoints = userPoints;
    }
}
