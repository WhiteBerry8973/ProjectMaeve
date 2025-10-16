package Gui.UserGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

import Gui.MainGui.*;
import Lib.*;

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
        // ---- block typing/paste: ≤16 and only A–Z a–z 0–9 ----
        ((AbstractDocument) usernameField.getDocument())
                .setDocumentFilter(new SimpleFilter(16, "[A-Za-z0-9]*")); // NEW
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 0, 20);
        passwordField = makePasswordField();
        // ---- block typing/paste: ≤8 and only A–Z a–z 0–9 _ - / . ----
        ((AbstractDocument) passwordField.getDocument())
                .setDocumentFilter(new SimpleFilter(8, "[A-Za-z0-9_\\-/.]*")); // NEW
        mainPanel.add(makePasswordPanel(passwordField), gbc);

        // Sign In
        JButton signInBtn = Ui.makePrimaryButton("SIGN IN", 130, 45);
        signInBtn.addActionListener(e -> attemptSignin());
        passwordField.addActionListener(e -> attemptSignin()); // Enter = login
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
    private void attemptSignin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        // User
        if (user.length() > 16 || !user.matches("[A-Za-z0-9]+")) {
            JOptionPane.showMessageDialog(this, "Invalid username.");
            return;
        }
        // Pass
        if (pass.length() > 8 || !pass.matches("[A-Za-z0-9_\\-/.]+")) {
            JOptionPane.showMessageDialog(this, "Invalid password format.");
            return;
        }

        User store = ui.getUser();

        if (!store.authenticate(user, pass)) {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
            return;
        }

        int pts = store.getPoints(user);
        ui.setCurrentUser(user, pts);
        JOptionPane.showMessageDialog(this, "Welcome back, " + user + "!");
        ui.show("COFFEE_MENU");
    }

    static class SimpleFilter extends DocumentFilter {
        private final int max;
        private final String regex;

        SimpleFilter(int max, String regex) {
            this.max = max;
            this.regex = regex;
        }

        @Override
        public void insertString(FilterBypass fb, int off, String s, AttributeSet a)
                throws BadLocationException {
            if (s == null)
                return;
            String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
            String cand = new StringBuilder(cur).insert(off, s).toString();
            if (cand.length() <= max && cand.matches(regex)) {
                super.insertString(fb, off, s, a);
            }
        }

        @Override
        public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a)
                throws BadLocationException {
            String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder sb = new StringBuilder(cur).replace(off, off + len, s == null ? "" : s);
            String cand = sb.toString();
            if (cand.length() <= max && cand.matches(regex)) {
                super.replace(fb, off, len, s, a);
            }
        }
    }
}
