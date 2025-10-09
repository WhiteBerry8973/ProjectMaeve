package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoffeeCatalogueGUI {

    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Maeve Coffee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the frame on screen
        frame.setLayout(new BorderLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("MAEVE COFFEE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(44, 47, 54));
        titleLabel.setOpaque(true);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create the catalog label
        JLabel catalogLabel = new JLabel("CATALOGUE", JLabel.CENTER);
        catalogLabel.setFont(new Font("Arial", Font.BOLD, 14));
        catalogLabel.setForeground(Color.WHITE);
        catalogLabel.setBackground(new Color(44, 47, 54));
        catalogLabel.setOpaque(true);
        frame.add(catalogLabel, BorderLayout.CENTER);

        // Create a panel to hold the buttons for Coffee, Tea, and Milk
        JPanel panel = new JPanel();
        panel.setBackground(new Color(44, 47, 54)); // Dark background color
        panel.setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns, with spacing

        // Add buttons for Coffee, Tea, and Milk
        JButton coffeeButton = createButton("Coffee", "/path/to/coffee_image.png");
        JButton teaButton = createButton("Tea", "/path/to/tea_image.png");
        JButton milkButton = createButton("Milk", "/path/to/milk_image.png");

        panel.add(coffeeButton);
        panel.add(teaButton);
        panel.add(milkButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Set the frame's background color to match the image
        frame.getContentPane().setBackground(new Color(44, 47, 54));

        // Make the frame visible
        frame.setVisible(true);
    }

    // Function to create a button with an image and text
    private static JButton createButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(233, 233, 233)); // Light gray button background
        button.setFocusPainted(false);

        // Set the button's icon (you should replace imagePath with the correct path to your image)
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Scale image
        button.setIcon(new ImageIcon(image));

        // Action listener for button click
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You selected " + text);
            }
        });

        return button;
    }
}