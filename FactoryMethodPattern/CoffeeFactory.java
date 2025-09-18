package FactoryMethodPattern;

public class CoffeeFactory {
    public static Coffee createCoffee(String type, String name, int price) {
        if (type.equalsIgnoreCase("hot")) {
            return new HotCoffee(name, price);
        } else if (type.equalsIgnoreCase("ice")) {
            return new IceCoffee(name, price);
        }
        throw new IllegalArgumentException("Unknown coffee type: " + type);
    }
}
