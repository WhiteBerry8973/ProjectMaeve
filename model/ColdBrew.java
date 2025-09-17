package model;

public class ColdBrew implements Coffee {
    @Override
    public String getName() {
        return "Cold Brew";
    }

    @Override
    public int getPrice() {
        return 75;
    }

    @Override
    public String getDescription() {
        return "Smooth and refreshing cold brew coffee.";
    }
    
    @Override
    public String getImagePath() {
        return "Imgs/cold_brew.png";
    }
}
