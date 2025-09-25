package Gui;

import java.io.*;
import java.util.*;

import model.*;

public class JframeCreator {
    public static List<ShowCoffee> loadFromCSV(String filePath) {
        List<ShowCoffee> coffees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    String img = parts[2].trim();
                    coffees.add(new ShowCoffee(name, price, img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coffees;
    }
}
