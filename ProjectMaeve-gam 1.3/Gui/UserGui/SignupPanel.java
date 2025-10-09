package Gui.UserGui;

import Gui.MainGui.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class SignupPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private boolean showPassword = false;
    private boolean showConfirm = false;

    private final int labelToFieldGap = 20;
    private final int fieldToNextLabelGap = 40;

    public SignupPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Title =====
        JLabel title = new JLabel("SIGNUP", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(new Color(0xd9d9d9));
        title.setBorder(new EmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // ===== Main Center Panel =====
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
        gbc.insets = new Insets(0, 0, fieldToNextLabelGap, 0);
        passwordField = makePasswordField();
        mainPanel.add(makePasswordPanel(passwordField, true), gbc);

        // ===== CONFIRM PASSWORD =====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, labelToFieldGap, 0);
        mainPanel.add(makeLabel("CONFIRM PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        confirmPasswordField = makePasswordField();
        mainPanel.add(makePasswordPanel(confirmPasswordField, false), gbc);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        buttonPanel.setOpaque(false);

        JButton backBtn = Ui.makeSecondaryButton("BACK", 130, 50, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton signupBtn = Ui.makePrimaryButton("SIGNUP", 130, 50);
        signupBtn.addActionListener(e -> attemptSignup());

        buttonPanel.add(backBtn);
        buttonPanel.add(signupBtn);

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
        field.setPreferredSize(new Dimension(0, 45));
        return field;
    }

    private JPasswordField makePasswordField() {
        JPasswordField field = new Ui.RoundedPasswordField(20);
        field.setPreferredSize(new Dimension(0, 45));
        field.setEchoChar('•');
        return field;
    }

    private JPanel makePasswordPanel(JPasswordField field, boolean isPassword) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);

        JButton toggleBtn = new JButton();
        toggleBtn.setPreferredSize(new Dimension(45, 45));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorder(BorderFactory.createEmptyBorder());
        toggleBtn.setBackground(new Color(20, 20, 20));
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleBtn.setIcon(new Ui.EyeIcon(new Color(0xd9d9d9), 22, false));

        toggleBtn.addActionListener(e -> {
            if (isPassword) {
                showPassword = !showPassword;
                field.setEchoChar(showPassword ? (char) 0 : '•');
                toggleBtn.setIcon(new Ui.EyeIcon(new Color(0xd9d9d9), 22, showPassword));
            } else {
                showConfirm = !showConfirm;
                field.setEchoChar(showConfirm ? (char) 0 : '•');
                toggleBtn.setIcon(new Ui.EyeIcon(new Color(0xd9d9d9), 22, showConfirm));
            }
        });

        panel.add(field, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);
        return panel;
    }

    // ===== Signup Logic =====
    private void attemptSignup() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        File file = new File("files/users.csv");
        file.getParentFile().mkdirs();

        try {
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2 && parts[0].equalsIgnoreCase(user)) {
                            JOptionPane.showMessageDialog(this, "This username already exists. Please choose another.");
                            return;
                        }
                    }
                }
            }

            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                if (file.length() == 0) out.println("username,password");
                out.println(user + "," + pass);
            }

            JOptionPane.showMessageDialog(this, "Signup successful for " + user);
            ui.setCurrentUser(user, 0);
            ui.show("COFFEE_MENU");

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user data: " + e.getMessage());
        }
    }
}