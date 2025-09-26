package FactoryMethodPattern;

public class IceCoffee implements Coffee {
    private String name;
    private double price;

    public IceCoffee(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price + 10;
    }

}
