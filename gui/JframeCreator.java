package gui;

import java.io.*;
import java.util.*;

import model.*;

public class JframeCreator {
    public static List<Coffee> loadFromCSV(String filePath) {
        List<Coffee> coffees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    String desc = parts[2].trim();
                    String img = parts[3].trim();
                    coffees.add(new Coffee(name, price, desc, img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coffees;
    }
}
