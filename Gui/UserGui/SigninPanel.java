package Gui.UserGui;

import Gui.MainGui.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class SigninPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean showPassword = false;

    // ค่าควบคุมระยะและความสูงช่อง
    private final int fieldHeight = 45;
    private final int labelToFieldGap = 20;
    private final int fieldToNextLabelGap = 40;

    public SigninPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== Title =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 0, 20));
        
        JLabel title = new JLabel("SIGN IN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Ui.BROWN);
        title.setBorder(new EmptyBorder(30, 0, 0, 0));
        header.add(title, BorderLayout.SOUTH);

        // ===== Close Button =====
        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setOpaque(false);
        closeBtn.setForeground(Ui.BROWN);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> ui.show("COFFEE_MENU"));
        JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeWrap.setOpaque(false);
        closeWrap.add(closeBtn);
        header.add(closeWrap, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== Main =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(-75, 35, 35, 35));
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
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("USERNAME"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, fieldToNextLabelGap, 20);
        usernameField = makeTextField();
        mainPanel.add(usernameField, gbc);

        // ===== PASSWORD =====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 0, 20);
        passwordField = makePasswordField();
        mainPanel.add(makePasswordPanel(passwordField), gbc);

        // ===== Button Panel =====
        JButton SignInBtn = Ui.makePrimaryButton("SIGN IN", 130, 45);
        SignInBtn.addActionListener(e -> attemptLogin());
        gbc.gridy = row++;
        gbc.insets = new Insets(40, 20, 0, 20);
        mainPanel.add(SignInBtn, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // ===== Footer SignInBtn =====
        gbc.gridy = row++;
        gbc.insets = new Insets(30, 0, 0, 0);
        JLabel noAccountLbl = new JLabel("Don't have an account?", SwingConstants.CENTER);
        noAccountLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noAccountLbl.setForeground(Ui.BROWN);
        noAccountLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        noAccountLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ui.show("SIGNUP");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                noAccountLbl.setForeground(Ui.BROWN_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                noAccountLbl.setForeground(Ui.BROWN);
            }
        });
        mainPanel.add(noAccountLbl, gbc);
        
    }

    // ===== Helper Components =====
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(Ui.BROWN);
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
        toggleBtn.setBackground(Ui.BROWN);
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleBtn.setIcon(new Ui.EyeIcon(Ui.WHITE, 22, false));

        toggleBtn.addActionListener(e -> {
            showPassword = !showPassword;
            field.setEchoChar(showPassword ? (char) 0 : '•');
            toggleBtn.setIcon(new Ui.EyeIcon(Ui.WHITE, 22, showPassword));
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