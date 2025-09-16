package model;

public class Latte implements Coffee {
    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public int getPrice() {
        return 60;
    }

    @Override
    public String getDescription() {
        return "A creamy blend of espresso and steamed milk.";
    }
    
}
