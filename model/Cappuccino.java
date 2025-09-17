package model;

public class Cappuccino implements Coffee {
    @Override
    public String getName() {
        return "Cappuccino";
    }

    @Override
    public int getPrice() {
        return 60;
    }

    @Override
    public String getDescription() {
        return "A classic cappuccino with a perfect balance of espresso, steamed milk, and froth.";
    }
    
    @Override
    public String getImagePath() {
        return "Imgs/cappuccino.png";
    }
}
