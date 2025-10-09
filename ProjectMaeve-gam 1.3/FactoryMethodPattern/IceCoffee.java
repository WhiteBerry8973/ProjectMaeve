package FactoryMethodPattern;

public class IceCoffee implements Coffee {
    private String name;
    private double price;
    private final double ICE_SURCHARGE = 10;

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
        return price + ICE_SURCHARGE;
    }

}
