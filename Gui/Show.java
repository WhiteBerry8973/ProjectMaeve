package Gui;

public class Show {
    private String name;
    private double price;
    private String imagePath;

    public Show(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }


    public String getImagePath() {
        return imagePath;
    }
}
