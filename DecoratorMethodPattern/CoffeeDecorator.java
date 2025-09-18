package DecoratorMethodPattern;
import FactoryMethodPattern.Coffee;

public class CoffeeDecorator implements Coffee {
    protected Coffee wrappedCoffee;

    public CoffeeDecorator(Coffee wrappedCoffee) {
        this.wrappedCoffee = wrappedCoffee;
    }

    @Override
    public String getName() {
        return wrappedCoffee.getName();
    }

    @Override
    public double getPrice() {
        return wrappedCoffee.getPrice();
    }


}