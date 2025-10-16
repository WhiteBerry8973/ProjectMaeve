package Gui.UserGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

import Gui.MainGui.*;
import Lib.*;

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

        // Close
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

        // ===== MAIN ====
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

        // Username
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("USERNAME"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, fieldToNextLabelGap, 20);
        usernameField = makeTextField();
        // พิมพ์ได้ ≤16 และเฉพาะ A–Z a–z 0–9
        ((AbstractDocument) usernameField.getDocument())
                .setDocumentFilter(new SimpleFilter(16, "[A-Za-z0-9]*"));
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, fieldToNextLabelGap, 20);
        passwordField = makePasswordField();
        // พิมพ์ได้ ≤8 และเฉพาะ A–Z a–z 0–9 _ - / .
        ((AbstractDocument) passwordField.getDocument())
                .setDocumentFilter(new SimpleFilter(8, "[A-Za-z0-9_\\-/.]*"));
        mainPanel.add(makePasswordPanel(passwordField, true), gbc);

        // Confirm Password
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("CONFIRM PASSWORD"), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 0, 20);
        confirmPasswordField = makePasswordField();
        // กติกาเหมือน password
        ((AbstractDocument) confirmPasswordField.getDocument())
                .setDocumentFilter(new SimpleFilter(8, "[A-Za-z0-9_\\-/.]*"));
        mainPanel.add(makePasswordPanel(confirmPasswordField, false), gbc);

        // Button Panel
        JButton SignInBtn = Ui.makePrimaryButton("SIGN UP", 130, 45);
        SignInBtn.addActionListener(e -> attemptSignup());
        // กด Enter บน confirm = สมัคร
        confirmPasswordField.addActionListener(e -> attemptSignup());
        gbc.gridy = row++;
        gbc.insets = new Insets(40, 20, 0, 20);
        mainPanel.add(SignInBtn, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
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

    // ===== SIGNUP LOGIC =====
    private void attemptSignup() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        // User
        if (user.length() > 16) {
            JOptionPane.showMessageDialog(this, "Username must be at most 16 characters.");
            return;
        }
        if (!user.matches("[A-Za-z0-9]+")) {
            JOptionPane.showMessageDialog(this, "Username allows only letters and digits (A-Z, a-z, 0-9).");
            return;
        }

        // Pass
        if (pass.length() > 8) {
            JOptionPane.showMessageDialog(this, "Password must be at most 8 characters.");
            return;
        }
        if (!pass.matches("[A-Za-z0-9_\\-/.]+")) {
            JOptionPane.showMessageDialog(this, "Password allows letters/digits and only _ - / .");
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        User store = ui.getUser();

        try {
            if (store.hasUser(user)) {
                JOptionPane.showMessageDialog(this, "This username is already taken.");
                return;
            }
        } catch (Throwable ignored) {
        }

        store.upsertUser(user, pass, 0);
        ui.setCurrentUser(user, 0);
        JOptionPane.showMessageDialog(this, "Signup successful for " + user);
        ui.show("COFFEE_MENU");
    }

    static class SimpleFilter extends DocumentFilter {
        private final int max;
        private final String regex;

        SimpleFilter(int max, String regex) {
            this.max = max;
            this.regex = regex;
        }

        @Override public void insertString(FilterBypass fb, int off, String s, AttributeSet a)
                throws BadLocationException {
            if (s == null) return;
            String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
            String cand = new StringBuilder(cur).insert(off, s).toString();
            if (cand.length() <= max && cand.matches(regex)) {
                super.insertString(fb, off, s, a);
            }
        }

        @Override public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a)
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
