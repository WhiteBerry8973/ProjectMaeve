package Gui.UserGui;

import Gui.MainGui.*;
import Lib.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SigninPanel extends JPanel {

    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean showPassword = false;

    private final int fieldHeight = 45;
    private final int labelToFieldGap = 20;
    private final int fieldToNextLabelGap = 40;

    public SigninPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 0, 20));

        JLabel title = new JLabel("SIGN IN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Ui.BROWN);
        title.setBorder(new EmptyBorder(30, 0, 0, 0));
        header.add(title, BorderLayout.SOUTH);

        // Close
        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setOpaque(false);
        closeBtn.setForeground(Ui.BROWN);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> ui.show(MenuCatalogPanel.COFFEE));
        JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeWrap.setOpaque(false);
        closeWrap.add(closeBtn);
        header.add(closeWrap, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(-75, 35, 35, 35));
        mainPanel.setOpaque(false);
        mainPanel.setMaximumSize(new Dimension(420, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        int row = 0;

        // Username
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("USERNAME"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, fieldToNextLabelGap, 20);
        usernameField = makeTextField();
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 0, 20);
        passwordField = makePasswordField();
        mainPanel.add(makePasswordPanel(passwordField), gbc);

        // Sign In
        JButton signInBtn = Ui.makePrimaryButton("SIGN IN", 130, 45);
        signInBtn.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin()); // Enter = login
        gbc.gridy = row++;
        gbc.insets = new Insets(40, 20, 0, 20);
        mainPanel.add(signInBtn, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
        gbc.gridy = row++;
        gbc.insets = new Insets(30, 0, 0, 0);
        JLabel noAccountLbl = new JLabel("Don't have an account?", SwingConstants.CENTER);
        noAccountLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        noAccountLbl.setForeground(Ui.BROWN);
        noAccountLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        noAccountLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { ui.show("SIGNUP"); }
            public void mouseEntered(java.awt.event.MouseEvent evt) { noAccountLbl.setForeground(Ui.BROWN_DARK); }
            public void mouseExited (java.awt.event.MouseEvent evt) { noAccountLbl.setForeground(Ui.BROWN); }
        });
        mainPanel.add(noAccountLbl, gbc);
    }

    // ===== HELPER =====
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

    // ===== SIGNIN LOGIC =====
    private void attemptLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username/password.");
            return;
        }

        User store = ui.getUser();
        if (store.authenticate(user, pass)) {
            ui.setCurrentUser(user, 0);
            JOptionPane.showMessageDialog(this, "Welcome back, " + user + "!");
            ui.show(MenuCatalogPanel.COFFEE);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }
}
