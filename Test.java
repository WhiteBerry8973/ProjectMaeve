import model.*;

public class Test {
    public static void main(String[] args) {
        Coffee coffee = new ConcreteCoffee("Latte", 5);
        System.out.println(coffee.getName()); // Latte
        System.out.println(coffee.getPrice()); // 5
        System.out.println(coffee.getDescription()); // This is a delicious Latte coffee.
    }
}
