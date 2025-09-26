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

    public SignupPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Title =====
        JLabel title = new JLabel("SIGNUP", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
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
        mainPanel.setMaximumSize(new Dimension(420, 420));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Input Fields =====
        usernameField = makeTextField();
        JPanel usernamePanel = wrapField(usernameField, "USERNAME");

        passwordField = makePasswordField();
        JPanel passwordPanel = wrapPasswordField(passwordField, "PASSWORD", true);

        confirmPasswordField = makePasswordField();
        JPanel confirmPanel = wrapPasswordField(confirmPasswordField, "CONFIRMPASSWORD", false);

        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(confirmPanel);

        centerPanel.add(mainPanel);
        centerPanel.add(Box.createVerticalStrut(40));

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(420, 60));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ใช้ปุ่มจาก Ui.java
        JButton backBtn = Ui.makeSecondaryButton("BACK", 130, 50, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton signupBtn = Ui.makePrimaryButton("SIGNUP", 130, 50);
        signupBtn.addActionListener(e -> attemptSignup());

        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(signupBtn);

        centerPanel.add(buttonPanel);


        add(centerPanel, BorderLayout.CENTER);
    }


    // ===== Field Generators =====
    private JTextField makeTextField() {
        JTextField field = new Ui.RoundedTextField(20);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setMinimumSize(new Dimension(100, 45));
        field.setPreferredSize(new Dimension(0, 45));
        return field;
    }

    private JPasswordField makePasswordField() {
        JPasswordField field = new Ui.RoundedPasswordField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setMinimumSize(new Dimension(100, 45));
        field.setPreferredSize(new Dimension(0, 45));
        field.setEchoChar('•');
        return field;
    }

    // ===== Field Wrappers =====
    private JPanel wrapField(JTextField field, String label) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(new Color(0xd9d9d9));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.add(lbl);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(field);

        return fieldPanel;
    }

    private JPanel wrapPasswordField(JPasswordField field, String label, boolean isPassword) {
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

        inputPanel.add(Box.createHorizontalStrut(5));
        inputPanel.add(toggleBtn);

        fieldPanel.add(lbl);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(inputPanel);

        return fieldPanel;
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

                if (file.length() == 0) {
                    out.println("username,password");
                }

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