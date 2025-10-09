package FactoryMethodPattern;

public class SodaFactory {

        public static Soda createSoda(String type, String name, double price) {
        if (type.equalsIgnoreCase("ice")) {
            return new IceSoda(name, price);
        } else if (type.equalsIgnoreCase("Smoothie")) {
            return new Smoothie(name, price);
        }
        throw new IllegalArgumentException("Unknown coffee type: " + type);
    }
}