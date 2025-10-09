package FactoryMethodPattern;

public class Smoothie implements Soda {
    private String name ;
    private double price ;

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
        return price + 10;
    }
    
}
