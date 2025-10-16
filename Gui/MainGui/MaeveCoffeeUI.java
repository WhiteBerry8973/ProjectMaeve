package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import Gui.CoffeeGui.*;
import Gui.MainGui.MenuCatalogPanel.Catalog;
import Gui.TeaGui.*;
import Gui.UserGui.*;
import Lib.*;
import Gui.AdminGui.*;

public class MaeveCoffeeUI {

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    public MenuCatalogPanel coffeePanel;
    public MenuCatalogPanel teaPanel;
    public MenuCatalogPanel sodaPanel;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    private static final String CSV_COFFEE = "files/coffee_menus.csv";
    private static final String CSV_TEA = "files/tea_menus.csv";
    private static final String CSV_SODA = "files/soda_menus.csv";

    private PricingService pricingService = new PricingService("files/size.csv", "files/topping.csv");

    private List<MenuDrink> coffeeMenu = new ArrayList<>();
    private List<MenuDrink> teaMenu = new ArrayList<>();
    private List<MenuDrink> sodaMenu = new ArrayList<>();

    // ===== SHARED STATE =====
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

    private MenuDrink selectedDrink;

    public enum DrinkType {
        HOT, ICED
    }

    private DrinkType selectedType = DrinkType.HOT;
    private int extraShots = 0;
    private List<String> selectedToppings = new ArrayList<>();
    private String selectedSweetness = null;

    // ===== USER =====
    private final User user = new User("files/users.csv");
    private String currentUser;
    private int currentPoints = 0;

    public MaeveCoffeeUI() {
        coffeeMenu = loadMenuFromCSV(CSV_COFFEE);
        teaMenu = loadMenuFromCSV(CSV_TEA);
        sodaMenu = loadMenuFromCSV(CSV_SODA);
    }

    // ===== PAGES =====
    private void createAndShowGUI() {
        frame = new JFrame("Maeve Coffee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 820);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new StartPanel(this), "HOME_PAGE");
        cards.add(new SignupPanel(this), "SIGNUP");
        cards.add(new SigninPanel(this), "SIGNIN");
        cards.add(new AdminLoginPanel(this), "ADMIN_LOGIN");
        cards.add(new AdminCatalogPanel(this), "ADMIN_CATALOG");

        coffeePanel = new MenuCatalogPanel(this, MenuCatalogPanel.Catalog.COFFEE);
        teaPanel = new MenuCatalogPanel(this, MenuCatalogPanel.Catalog.TEA);
        sodaPanel = new MenuCatalogPanel(this, MenuCatalogPanel.Catalog.SODA);
        cards.add(coffeePanel, MenuCatalogPanel.COFFEE);
        cards.add(teaPanel, MenuCatalogPanel.TEA);
        cards.add(sodaPanel, MenuCatalogPanel.SODA);

        cards.add(new CoffeeAddonPanel(this), MenuCatalogPanel.COFFEE_ADDON);
        cards.add(new TeaAddonPanel(this), MenuCatalogPanel.TEA_ADDON);
        cards.add(new ReceiptDialog(this), "BILL");
        cards.add(new SummaryPanel(this), "SUMMARY");

        frame.add(cards, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        show("HOME_PAGE");
    }

    // ===== ORDER ITEM =====
    public static class OrderItem {
        public final String label;
        public final int qty;
        public final int price;

        public OrderItem(String label, int qty, int price) {
            this.label = label;
            this.qty = qty;
            this.price = price;
        }

        public int lineTotal() {
            return qty * price;
        }
    }

    public static class OrderSummary {
        public String username;
        public int pointsBefore;
        public int pointsEarned;
        public java.time.LocalDate date;
        public java.time.LocalTime time;
        public java.util.List<OrderItem> items = new java.util.ArrayList<>();

        public int total() {
            return items.stream().mapToInt(OrderItem::lineTotal).sum();
        }
    }

    private OrderSummary lastOrder;

    // ===== STATE GETTERS/SETTERS =====

    public String getMenuCsvPath(Catalog c) {
        switch (c) {
            case COFFEE:
                return CSV_COFFEE;
            case TEA:
                return CSV_TEA;
            case SODA:
                return CSV_SODA;
            default:
                return null;
        }
    }

    public Map<String, Boolean> getStockMap(MenuCatalogPanel.Catalog c) {
        String filePath = getMenuCsvPath(c);
        Map<String, Boolean> out = new LinkedHashMap<>();
        if (filePath == null)
            return out;
        Path p = Paths.get(filePath);
        if (!Files.exists(p))
            return out;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String first = br.readLine();
            if (first == null)
                return out;
            String[] cols = first.split(",", -1);
            boolean hasHeader = false;
            for (String h : cols)
                if ("name".equalsIgnoreCase(h == null ? "" : h.trim())) {
                    hasHeader = true;
                    break;
                }
            int nameIdx = hasHeader ? indexOf(cols, "name") : 0;
            int stockIdx = hasHeader ? indexOf(cols, "stock") : -1;

            if (!hasHeader) {
                return out;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#"))
                    continue;
                String[] f = line.split(",", -1);
                String name = (nameIdx >= 0 && nameIdx < f.length) ? f[nameIdx].trim() : "";
                if (name.isEmpty())
                    continue;
                boolean in = (stockIdx >= 0 && stockIdx < f.length)
                        ? "1".equals(f[stockIdx].trim()) || "true".equalsIgnoreCase(f[stockIdx].trim())
                        : true;
                out.put(name, in);
            }
        } catch (IOException ignore) {
        }
        return out;
    }

    private static int indexOf(String[] a, String key) {
        for (int i = 0; i < a.length; i++)
            if (key.equalsIgnoreCase(a[i] == null ? "" : a[i].trim()))
                return i;
        return -1;
    }

    public PricingService getPricingService() {
        return pricingService;
    }

    public OrderSummary getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(OrderSummary o) {
        lastOrder = o;
    }

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

    // User

    public User getUser() {
        return user;
    }

    public boolean isSignedIn() {
        return currentUser != null && !currentUser.trim().isEmpty();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public int getCurrentPoints() {
        return isSignedIn() ? user.getPoints(currentUser) : 0;
    }

    public void setCurrentUser(String name, int points) {
        this.currentUser = name;
        this.currentPoints = points;

        if (coffeePanel != null)
            coffeePanel.updateHeaderUser();
        if (teaPanel != null)
            teaPanel.updateHeaderUser();
        if (sodaPanel != null)
            sodaPanel.updateHeaderUser();
    }

    public void addPoints(int delta) {
        if (!isSignedIn())
            return;
        user.addPoints(currentUser, delta);
    }

    public void setCurrentPoints(int pts) {
        if (!isSignedIn())
            return;
        user.setPoints(currentUser, pts);
    }

    // Show Page
    public void show(String name) {
        cardLayout.show(cards, name);
    }

    public java.util.List<MenuDrink> getMenuByCatalog(MenuCatalogPanel.Catalog cat) {
        switch (cat) {
            case TEA:
                return teaMenu;
            case SODA:
                return sodaMenu;
            case COFFEE:
            default:
                return coffeeMenu;
        }
    }

    // ===== CSV LOADER =====
    private static List<MenuDrink> loadMenuFromCSV(String filePath) {
        List<MenuDrink> menuList = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("CSV not found: " + filePath);
            return menuList;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            br.readLine();
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
}