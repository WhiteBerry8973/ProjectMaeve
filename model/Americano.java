package model;

public class Americano implements Coffee {
    @Override
    public String getName() {
        return "Americano";
    }

    @Override
    public int getPrice() {
        return 45;
    }

    @Override
    public String getDescription() {
        return "A smooth and rich espresso diluted with hot water.";
    }

    @Override
    public String getImagePath() {
        return "Imgs/americano.png";
    }
    
}
