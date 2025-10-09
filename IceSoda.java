package FactoryMethodPattern;

public class IceSoda implements Soda {
    private String name ;
    private double price ;

    public IceSoda(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price ;
    }
    
}
