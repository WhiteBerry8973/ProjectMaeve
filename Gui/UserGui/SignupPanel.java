package Gui.UserGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import Gui.MainGui.MaeveCoffeeUI;
import Gui.MainGui.Ui;

public class SignupPanel extends JPanel {
    private MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmField;

    public SignupPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        JLabel title = new JLabel("SIGNUP", SwingConstants.CENTER);
        title.setForeground(Ui.TITLE);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setBorder(new EmptyBorder(40, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        Ui.RoundedBorderPanel box = new Ui.RoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(40, 40, 40, 40));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmField = new JPasswordField();

        box.add(makeLabeled("USERNAME", Ui.makeTextFieldWithPadding(usernameField, 240, 40)));
        box.add(Box.createVerticalStrut(20));
        box.add(makeLabeled("PASSWORD", Ui.makePasswordFieldWithToggle(passwordField, 240, 40)));
        box.add(Box.createVerticalStrut(20));
        box.add(makeLabeled("CONFIRM PASSWORD", Ui.makePasswordFieldWithToggle(confirmField, 240, 40)));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRow.setOpaque(false);

        JButton backBtn = Ui.makeSecondaryButton("BACK", 120, 44, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton signupBtn = Ui.makePrimaryButton("SIGNUP", 120, 44);
        signupBtn.addActionListener(e -> attemptSignup());

        btnRow.add(backBtn);
        btnRow.add(signupBtn);
        box.add(btnRow);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(box);
        add(center, BorderLayout.CENTER);
    }

    private JPanel makeLabeled(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(Ui.TITLE);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void attemptSignup() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("files/users.csv", true))) {
            bw.write(user + "," + pass + ",0\n");
            JOptionPane.showMessageDialog(this, "Signup success, now login!");
            ui.show("LOGIN");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
