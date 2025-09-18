package DecoratorMethodPattern;
import FactoryMethodPattern.Coffee;

public class MarshmallowDecorator extends CoffeeDecorator {

    public MarshmallowDecorator(Coffee wrappedCoffee) {
        super(wrappedCoffee);
    }

    @Override
    public String getName() {
        return wrappedCoffee.getName() + " with Marshmallow";
    }

    @Override
    public int getPrice() {
        return wrappedCoffee.getPrice() + 5;
    }
    
}
