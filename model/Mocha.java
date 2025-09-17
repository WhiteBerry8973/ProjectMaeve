package model;

public class Mocha implements Coffee {
    @Override
    public String getName() {
        return "Mocha";
    }

    @Override
    public int getPrice() {
        return 65;
    }

    @Override
    public String getDescription() {
        return "A delightful blend of espresso, steamed milk, and chocolate syrup.";
    }
    
    @Override
    public String getImagePath() {
        return "Imgs/mocha.png";
    }
}
