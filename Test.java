import FactoryMethodPattern.*;

public class Test {
    public static void main(String[] args) {
        Coffee coffee = new HotCoffee("Espresso", 45);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.WhippingcreamDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");
        coffee = new DecoratorMethodPattern.MarshmallowDecorator(coffee);
        System.out.println("Coffee: " + coffee.getName() + ", Price: " + coffee.getPrice() +" baht");

        Coffee iceCoffee = new IceCoffee("Iced Latte", 55);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");
        iceCoffee = new DecoratorMethodPattern.MarshmallowDecorator(iceCoffee);
        System.out.println("Coffee: " + iceCoffee.getName() + ", Price: " + iceCoffee.getPrice() +" baht");

    }
}