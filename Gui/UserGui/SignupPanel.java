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

    private final int fieldHeight = 45;
    private final int labelToFieldGap = 20;
    private final int fieldToNextLabelGap = 40;

    public SignupPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== Title =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 10, 20));
        
        JLabel title = new JLabel("SIGN UP", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Ui.BROWN);
        title.setBorder(new EmptyBorder(30, 0, 30, 0));
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

        // ===== Main Center Panel =====
        JPanel mainPanel = new JPanel();
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
        gbc.insets = new Insets(0, 20, fieldToNextLabelGap, 20);
        passwordField = makePasswordField();
        mainPanel.add(makePasswordPanel(passwordField, true), gbc);

        // ===== CONFIRM PASSWORD =====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("CONFIRM PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 0, 20);
        confirmPasswordField = makePasswordField();
        mainPanel.add(makePasswordPanel(confirmPasswordField, false), gbc);

        // ===== Button Panel =====
        JButton SignInBtn = Ui.makePrimaryButton("SIGN UP", 130, 45);
        SignInBtn.addActionListener(e -> attemptSignup());
        gbc.gridy = row++;
        gbc.insets = new Insets(40, 20, 0, 20);
        mainPanel.add(SignInBtn, gbc);
        
        add(mainPanel, BorderLayout.CENTER);

        // ===== Footer SignUpBtn =====
        gbc.gridy = row++;
        gbc.insets = new Insets(30, 20, 20, 20);
        JLabel haveAccountLbl = new JLabel("Already have an account?", SwingConstants.CENTER);
        haveAccountLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        haveAccountLbl.setForeground(Ui.BROWN);
        haveAccountLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        haveAccountLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ui.show("SIGNIN");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                haveAccountLbl.setForeground(Ui.BROWN_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                haveAccountLbl.setForeground(Ui.BROWN);
            }
        });
        mainPanel.add(haveAccountLbl, gbc);

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

    private JPanel makePasswordPanel(JPasswordField field, boolean isPassword) {
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
            if (isPassword) {
                showPassword = !showPassword;
                field.setEchoChar(showPassword ? (char) 0 : '•');
                toggleBtn.setIcon(new Ui.EyeIcon(Ui.WHITE, 22, showPassword));
            } else {
                showConfirm = !showConfirm;
                field.setEchoChar(showConfirm ? (char) 0 : '•');
                toggleBtn.setIcon(new Ui.EyeIcon(Ui.WHITE, 22, showConfirm));
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