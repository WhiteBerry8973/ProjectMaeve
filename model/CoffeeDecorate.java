package model;

public class CoffeeDecorate extends ConcreteCoffee {
    private Coffee coffee;

    public CoffeeDecorate(Coffee coffee) {
        super(coffee.getName(), coffee.getPrice());
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }
    
}
