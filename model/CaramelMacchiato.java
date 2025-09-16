package model;

public class CaramelMacchiato implements Coffee {
    @Override
    public String getName() {
        return "Caramel Macchiato";
    }

    @Override
    public int getPrice() {
        return 65;
    }

    @Override
    public String getDescription() {
        return "A delightful blend of espresso, steamed milk, vanilla syrup, and caramel drizzle.";
    }

    
}