import FactoryMethodPattern.*;
import DecoratorMethodPattern.*;
import StrategyPattern.*;

public class Test {
    public static void main(String[] args) {
        DiscountStrategy discountedPrice = new PointToCashDiscount();
        double finalPrice = 0;

        Coffee coffee = FactoryMethodPattern.CoffeeFactory.createCoffee("hot", "Espresso", 50);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.WhippingcreamDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.MarshmallowDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        finalPrice = discountedPrice.applyDiscount(coffee.getPrice(), 300); // 300 points
        System.out.println("Final Price after discount: " + finalPrice + " baht\n");

        Coffee iceCoffee = FactoryMethodPattern.CoffeeFactory.createCoffee("ice", "Latte", 60);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");
        iceCoffee = new DecoratorMethodPattern.MarshmallowDecorator(iceCoffee);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");
        finalPrice = discountedPrice.applyDiscount(iceCoffee.getPrice(), 500); // 500 points
        System.out.println("Final Price after discount: " + finalPrice + " baht");
        
    }
}