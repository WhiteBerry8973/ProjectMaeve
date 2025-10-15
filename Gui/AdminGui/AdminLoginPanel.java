package Gui.AdminGui;

import Gui.MainGui.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class AdminLoginPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean showPassword = false;

    public AdminLoginPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== Title =====
        JLabel title = new JLabel("LOGIN AS ADMIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(0xd9d9d9));
        title.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // ===== Rounded Main Panel =====
        JPanel mainPanel = new Ui.RoundedPanel(20, new Color(40, 40, 40));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setOpaque(false);
        mainPanel.setMaximumSize(new Dimension(420, 250));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Input Fields =====
        usernameField = new Ui.RoundedTextField(20);
        passwordField = new Ui.RoundedPasswordField(20);

        JPanel usernamePanel = wrapField(usernameField, "USERNAME");
        JPanel passwordPanel = wrapPasswordField(passwordField, "PASSWORD");

        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passwordPanel);

        centerPanel.add(mainPanel);
        centerPanel.add(Box.createVerticalStrut(40));

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(420, 60));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = Ui.makeSecondaryButton("BACK", 130, 50, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton loginBtn = Ui.makePrimaryButton("LOGIN", 130, 50);
        loginBtn.setBackground(new Color(80, 80, 200));
        loginBtn.setForeground(new Color(0xd9d9d9));
        loginBtn.addActionListener(e -> attemptLogin());

        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginBtn);

        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    // ===== Wrap Field =====
    private JPanel wrapField(JTextField field, String label) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(0xd9d9d9));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(0, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.add(lbl);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(field);

        return fieldPanel;
    }

    // ===== Wrap Password Field (with toggle) =====
    private JPanel wrapPasswordField(JPasswordField field, String label) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(0xd9d9d9));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(0, 45));
        field.setEchoChar('•');

        inputPanel.add(field);

        JButton toggleBtn = new JButton();
        toggleBtn.setPreferredSize(new Dimension(45, 45));
        toggleBtn.setMaximumSize(new Dimension(45, 45));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorder(BorderFactory.createEmptyBorder());
        toggleBtn.setBackground(new Color(20, 20, 20));
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleBtn.setIcon(new Ui.EyeIcon(new Color(0xd9d9d9), 22, false));

        toggleBtn.addActionListener(e -> {
            showPassword = !showPassword;
            field.setEchoChar(showPassword ? (char) 0 : '•');
            toggleBtn.setIcon(new Ui.EyeIcon(new Color(0xd9d9d9), 22, showPassword));
        });

        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(toggleBtn);

        fieldPanel.add(lbl);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(inputPanel);

        return fieldPanel;
    }

    // ===== Admin Login Logic =====
    private void attemptLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        File file = new File("files/admin.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Admin file not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean success = false;

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String u = parts[0].trim();
                    String p = parts[1].trim();
                    if (u.equals(user) && p.equals(pass)) {
                        success = true;
                        break;
                    }
                }
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Welcome Admin " + user);
                ui.show("ADMIN_PANEL");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin username or password");
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading admin data: " + e.getMessage());
        }
    }
}
