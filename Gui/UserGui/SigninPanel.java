package Gui.UserGui;

import Gui.MainGui.*;
import Lib.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class SigninPanel extends JPanel {

    private final MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean showPassword = false;

    // error labels
    private JLabel userErrLbl;
    private JLabel passErrLbl;

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
        mainPanel.add(makePasswordPanel(passwordField), gbc);

        // Error
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 22, 0, 20);
        passErrLbl = makeErrLabel();
        mainPanel.add(passErrLbl, gbc);

        // Signin
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
