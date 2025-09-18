package DecoratorMethodPattern;
import FactoryMethodPattern.Coffee;

public class WhippingcreamDecorator extends CoffeeDecorator {

    public WhippingcreamDecorator(Coffee wrappedCoffee) {
        super(wrappedCoffee);
    }

    @Override
    public String getName() {
        return wrappedCoffee.getName() + " with Whipping Cream";
    }

    @Override
    public int getPrice() {
        return wrappedCoffee.getPrice() + 5;
    }
    
}