package FactoryMethodPattern;

public class HotCoffee implements Coffee {
    private String name;
    private double price;

    public HotCoffee(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }
    
}
