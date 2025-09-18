package model;

public class ConcreteCoffee implements Coffee {
    private String name;
    private int price;

    public ConcreteCoffee(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return "This is a delicious " + name + " coffee.";
    }
    
}
