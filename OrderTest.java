import FactoryMethodPattern.*;
import StrategyPattern.*;
import DecoratorMethodPattern.*;

public class OrderTest {
    public static void main(String[] args) {
        DiscountStrategy discountedPrice = new PointToCashDiscount();
        double finalPrice = 0;

        System.out.println("------------------------------------------------------------------------");

        Coffee coffee = FactoryMethodPattern.CoffeeFactory.createCoffee("hot", "Espresso", 50);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.WhippingcreamDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.MarshmallowDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        finalPrice = discountedPrice.applyDiscount(coffee.getPrice(), 300); // 300 points
        System.out.println("Final Price after discount: " + finalPrice + " baht");

        System.out.println("------------------------------------------------------------------------");

        Coffee iceCoffee = FactoryMethodPattern.CoffeeFactory.createCoffee("ice", "Latte", 60);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");
        iceCoffee = new DecoratorMethodPattern.MarshmallowDecorator(iceCoffee);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");
        finalPrice = discountedPrice.applyDiscount(iceCoffee.getPrice(), 550); // 550 points
        System.out.println("Final Price after discount: " + finalPrice + " baht");

        System.out.println("------------------------------------------------------------------------");



    }
}