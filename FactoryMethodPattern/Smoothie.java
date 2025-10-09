package FactoryMethodPattern;

public class Smoothie implements Soda {
    private String name ;
    private double price ;
    private final double SMOOTHIE_EXTRA_COST = 10;

    public Smoothie(String name, double price) {
        this.name = name;
        this.price = price;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price + SMOOTHIE_EXTRA_COST;
    }
    
}
