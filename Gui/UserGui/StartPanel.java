package Gui.UserGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import Gui.MainGui.*;

public class StartPanel extends JPanel {

    public StartPanel(MaeveCoffeeUI ui) {
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== Title =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 0, 20));
        
        JLabel title = new JLabel("MAEVE COFFEE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Ui.BROWN);
        title.setBorder(new EmptyBorder(30, 0, 0, 0));
        header.add(title, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        
        // ===== Main Center Panel =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(0, 35, 35, 35));
        mainPanel.setOpaque(false);
        mainPanel.setMaximumSize(new Dimension(420, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        int row = 0;

        // ===== Image =====
        ImageIcon icon = new ImageIcon("Imgs/logo.png");
        Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(imageLabel, gbc);

        // ===== Order Now Button =====
        JButton OrderNow = Ui.makePrimaryButton("ORDER NOW!", 130, 45);
        OrderNow.addActionListener(e -> ui.show("COFFEE_MENU"));
        gbc.gridy = row++;
        gbc.insets = new Insets(60, 20, 0, 20);
        mainPanel.add(OrderNow, gbc);

        add(mainPanel, BorderLayout.CENTER);


        // ===== Adminster link =====
        JLabel admin = new JLabel("Adminster", SwingConstants.RIGHT);
        admin.setForeground(Ui.BROWN_DARK);
        admin.setFont(new Font("SansSerif", Font.PLAIN, 14));
        admin.setBorder(new EmptyBorder(0, 0, 20, 20));
        admin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ui.show("ADMIN_LOGIN");
            }
        });
        add(admin, BorderLayout.SOUTH);
    }
}
