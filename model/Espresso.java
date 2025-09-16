package model;

public class Espresso implements Coffee {
    @Override
    public String getName() {
        return "Espresso";
    }

    @Override
    public int getPrice() {
        return 45;
    }

    @Override
    public String getDescription() {
        return "Strong and bold espresso shot.";
    }
    
}
