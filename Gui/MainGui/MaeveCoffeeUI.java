package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import Gui.CoffeeGui.*;
import Gui.UserGui.*;
import Gui.AdminGui.*;

public class MaeveCoffeeUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    // ===== Shared State =====
    public static class MenuDrink {
        public final String name;
        public final String imagePath;
        public final double hotPrice;
        public final double icedPrice;
        public final boolean icedAvailable;
        public final double shotPrice;

        public MenuDrink(String name, double hot, double iced, boolean icedAvail,
                double shotPrice, String img) {
            this.name = name;
            this.hotPrice = hot;
            this.icedPrice = iced;
            this.icedAvailable = icedAvail;
            this.shotPrice = shotPrice;
            this.imagePath = img;
        }
    }

    private List<MenuDrink> coffeeMenu = new ArrayList<>();

    private MenuDrink selectedDrink;

    // Addon state
    public enum DrinkType {
        HOT, ICED
    }

    private DrinkType selectedType = DrinkType.HOT;
    private int extraShots = 0;
    private List<String> selectedToppings = new ArrayList<>();
    private String selectedSweetness = null;

    // ===== User state =====
    private String currentUser = "GUEST";
    private int currentPoints = 0;

    public MaeveCoffeeUI() {
        coffeeMenu = loadMenuFromCSV("files/coffee_menus.csv");
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Maeve Coffee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 820);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new StartPanel(this), "HOME_PAGE");
        cards.add(new SignupPanel(this), "SIGNUP");
        cards.add(new LoginPanel(this), "LOGIN");
        cards.add(new AdminLoginPanel(this), "ADMIN_LOGIN");
        cards.add(new CoffeeMenuPanel(this), "COFFEE_MENU");
        cards.add(new CoffeeAddonPanel(this), "COFFEE_ADDON");
        cards.add(new CoffeePaymentPanel(this), "COFFEE_PAYMENT");

        frame.add(cards, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        show("HOME_PAGE");
    }

    // ===== State getters/setters =====
    public List<MenuDrink> getCoffeeMenu() {
        return coffeeMenu;
    }

    public void setSelectedDrink(MenuDrink d) {
        this.selectedDrink = d;
    }

    public MenuDrink getSelectedDrink() {
        return selectedDrink;
    }

    public void setSelectedType(DrinkType t) {
        this.selectedType = t;
    }

    public DrinkType getSelectedType() {
        return selectedType;
    }

    public void setExtraShots(int n) {
        this.extraShots = n;
    }

    public int getExtraShots() {
        return extraShots;
    }

    public void setSelectedToppings(List<String> t) {
        this.selectedToppings = t;
    }

    public List<String> getSelectedToppings() {
        return selectedToppings;
    }

    public void setSelectedSweetness(String s) {
        this.selectedSweetness = s;
    }

    public String getSelectedSweetness() {
        return selectedSweetness;
    }

    // ===== User =====
    public void setCurrentUser(String username, int points) {
        this.currentUser = username;
        this.currentPoints = points;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int points) {
        this.currentPoints = points;
    }

    // ===== Navigation =====
    public void show(String name) {
        cardLayout.show(cards, name);
    }

    // ===== CSV Loader =====
    private static List<MenuDrink> loadMenuFromCSV(String filePath) {
        List<MenuDrink> menuList = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("CSV not found: " + filePath);
            return menuList;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String name = parts[0].trim();
                    double hot = Double.parseDouble(parts[1].trim());
                    double iced = Double.parseDouble(parts[2].trim());
                    boolean icedAvail = parts[3].trim().equals("1");
                    double shotPrice = Double.parseDouble(parts[4].trim());
                    String img = parts[5].trim();

                    menuList.add(new MenuDrink(name, hot, iced, icedAvail, shotPrice, img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuList;
    }

    // ===== Helper: get topping price (dummy 10฿ for example) =====
    public int getToppingPrice(String t) {
        if (t.toLowerCase().contains("whip"))
            return 15;
        if (t.toLowerCase().contains("choco"))
            return 10;
        if (t.toLowerCase().contains("marsh"))
            return 10;
        return 10;
    }
}
