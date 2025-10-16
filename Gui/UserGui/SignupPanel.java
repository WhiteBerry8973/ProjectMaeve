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

    private JLabel userErrLbl;
    private JLabel passErrLbl;
    private JLabel confirmErrLbl;

    private final int fieldHeight = 45;
    private final int labelToFieldGap = 20;
    private final int fieldToNextLabelGap = 40;

    public SignupPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== HEADER =====
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
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(35, 35, 35, 35));
        mainPanel.setOpaque(false);
        mainPanel.setMaximumSize(new Dimension(420, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        int row = 0;

        // ==== USER ====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("USERNAME"), gbc);

        // Field
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 6, 20);
        usernameField = makeTextField();
        ((AbstractDocument) usernameField.getDocument())
                .setDocumentFilter(new NotifyingFilter(
                        16, "[A-Za-z0-9]*",
                        msg -> setError(userErrLbl, msg),
                        "Username must have only 16 characters.",
                        "Only letters and digits (A–Z, a–z, 0–9)."
                ));
        mainPanel.add(usernameField, gbc);

        // Error
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 22, fieldToNextLabelGap - 10, 20);
        userErrLbl = makeErrLabel();
        mainPanel.add(userErrLbl, gbc);

        // ==== PASSWORD ====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("PASSWORD"), gbc);

        // Field
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 6, 20);
        passwordField = makePasswordField();
        ((AbstractDocument) passwordField.getDocument())
                .setDocumentFilter(new NotifyingFilter(
                        8, "[A-Za-z0-9_\\-/.]*",
                        msg -> setError(passErrLbl, msg),
                        "Password must have only 8 characters.",
                        "Only letters, digits and _ - / ."
                ));
        mainPanel.add(makePasswordPanel(passwordField, true), gbc);

        // Error
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 22, fieldToNextLabelGap - 10, 20);
        passErrLbl = makeErrLabel();
        mainPanel.add(passErrLbl, gbc);

        // ==== CONFIRM ====
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, labelToFieldGap, 20);
        mainPanel.add(makeLabel("CONFIRM PASSWORD"), gbc);

        // Field
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 20, 6, 20);
        confirmPasswordField = makePasswordField();
        ((AbstractDocument) confirmPasswordField.getDocument())
                .setDocumentFilter(new NotifyingFilter(
                        8, "[A-Za-z0-9_\\-/.]*",
                        msg -> setError(confirmErrLbl, msg),
                        "Password must have only 8 characters.",
                        "Only letters, digits and _ - / ."
                ));
        mainPanel.add(makePasswordPanel(confirmPasswordField, false), gbc);

        // Error
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 22, 0, 20);
        confirmErrLbl = makeErrLabel();
        mainPanel.add(confirmErrLbl, gbc);

        // Signin
        JButton SignInBtn = Ui.makePrimaryButton("SIGN UP", 130, 45);
        SignInBtn.addActionListener(e -> attemptSignup());
        confirmPasswordField.addActionListener(e -> attemptSignup()); // Enter = sign up
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
            public void mouseClicked(java.awt.event.MouseEvent evt) { ui.show("SIGNIN"); }
            public void mouseEntered(java.awt.event.MouseEvent evt) { haveAccountLbl.setForeground(Ui.BROWN_DARK); }
            public void mouseExited (java.awt.event.MouseEvent evt) { haveAccountLbl.setForeground(Ui.BROWN); }
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

    private JLabel makeErrLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(new Color(180, 40, 40));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setVisible(false);
        return lbl;
    }

    private void setError(JLabel label, String msg) {
        if (label == null) return;
        if (msg == null) {
            label.setText(" ");
            label.setVisible(false);
        } else {
            label.setText(msg);
            label.setVisible(true);
        }
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

        if (store.hasUser(user)) {
            JOptionPane.showMessageDialog(this, "This username is already taken.");
            return;
        }

        store.upsertUser(user, pass, 0);
        ui.setCurrentUser(user, 0);
        JOptionPane.showMessageDialog(this, "Signup successful for " + user);
        ui.show("COFFEE_MENU");
    }

    // ===== ERROR =====
    static class NotifyingFilter extends DocumentFilter {
        interface RejectHandler { void onReject(String message); }
        private final int max;
        private final String regex;
        private final RejectHandler handler;
        private final String tooLongMsg;
        private final String invalidCharMsg;

        NotifyingFilter(int max, String regex, RejectHandler handler,
                        String tooLongMsg, String invalidCharMsg) {
            this.max = max;
            this.regex = regex;
            this.handler = handler;
            this.tooLongMsg = tooLongMsg;
            this.invalidCharMsg = invalidCharMsg;
        }

        @Override public void insertString(FilterBypass fb, int off, String s, AttributeSet a)
                throws BadLocationException {
            if (s == null) return;
            apply(fb, off, 0, s, a);
        }

        @Override public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a)
                throws BadLocationException {
            apply(fb, off, len, s == null ? "" : s, a);
        }

        private void apply(FilterBypass fb, int off, int len, String s, AttributeSet a)
                throws BadLocationException {
            String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
            String cand = new StringBuilder(cur).replace(off, off + len, s).toString();

            if (cand.length() > max) {
                if (handler != null) handler.onReject(tooLongMsg);
                return;
            }
            if (!cand.matches(regex)) {
                if (handler != null) handler.onReject(invalidCharMsg);
                return;
            }

            if (handler != null) handler.onReject(null);
            super.replace(fb, off, len, s, a);
        }
    }
}
