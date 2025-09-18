package FactoryMethodPattern;

public class IceCoffee implements Coffee {
    private String name;
    private int price;

    public IceCoffee(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price + 10;
    }

}
