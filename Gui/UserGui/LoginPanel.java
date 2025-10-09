package Gui.UserGui;

import Gui.MainGui.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class LoginPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean showPassword = false;

    // ค่าควบคุมระยะและความสูงช่อง
    private final int fieldHeight = 60;          // ความสูงช่องกรอก
    private final int labelToFieldGap = 20;       // label → ช่องกรอก
    private final int fieldToNextLabelGap = 40;  // ช่องกรอก → label ถัดไป

    public LoginPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Title =====
        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(new Color(0xd9d9d9));
        title.setBorder(new EmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // ===== Main Rounded Panel =====
        JPanel mainPanel = new Ui.RoundedPanel(20, new Color(40, 40, 40));
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(35, 35, 35, 35));
        mainPanel.setOpaque(false);
        mainPanel.setMaximumSize(new Dimension(420, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        int row = 0;

        // ===== USERNAME =====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, labelToFieldGap, 0);
        mainPanel.add(makeLabel("USERNAME"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, fieldToNextLabelGap, 0);
        usernameField = makeTextField();
        mainPanel.add(usernameField, gbc);

        // ===== PASSWORD =====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, labelToFieldGap, 0);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        passwordField = makePasswordField();
        mainPanel.add(makePasswordPanel(passwordField), gbc);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        buttonPanel.setOpaque(false);

        JButton backBtn = Ui.makeSecondaryButton("BACK", 130, 50, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton loginBtn = Ui.makePrimaryButton("LOGIN", 130, 50);
        loginBtn.addActionListener(e -> attemptLogin());

        buttonPanel.add(backBtn);
        buttonPanel.add(loginBtn);

        // ===== Wrap =====
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.add(mainPanel);
        wrapper.add(Box.createVerticalStrut(30));
        wrapper.add(buttonPanel);

        add(wrapper, BorderLayout.CENTER);
    }

    // ===== Helper Components =====
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(0xd9d9d9));
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField field = new Ui.RoundedTextField(20);
        field.setPreferredSize(new Dimension(0, fieldHeight));
        return field;
    }

    private JPasswordField makePasswordField() {
        JPasswordField field = new Ui.RoundedPasswordField(20);
        field.setPreferredSize(new Dimension(0, fieldHeight));
        field.setEchoChar('•');
        return field;
    }

    private JPanel makePasswordPanel(JPasswordField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);

        JButton toggleBtn = new JButton();
        toggleBtn.setPreferredSize(new Dimension(fieldHeight, fieldHeight));
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

        panel.add(field, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);
        return panel;
    }

    // ===== Login Logic =====
    private void attemptLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        File file = new File("files/users.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No users found. Please sign up first.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equalsIgnoreCase(user) && parts[1].equals(pass)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(this, "Welcome back, " + user + "!");
                ui.setCurrentUser(user, 0);
                ui.show("COFFEE_MENU");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading user data: " + e.getMessage());
        }
    }
}