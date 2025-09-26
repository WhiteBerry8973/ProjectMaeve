package Gui.AdminGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Gui.MainGui.MaeveCoffeeUI;
import Gui.MainGui.Ui;

public class AdminLoginPanel extends JPanel {
    private MaeveCoffeeUI ui;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        JLabel title = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
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

        box.add(makeLabeled("USERNAME", usernameField));
        box.add(Box.createVerticalStrut(20));
        box.add(makeLabeled("PASSWORD", Ui.makePasswordFieldWithToggle(passwordField, 240, 40)));
        box.add(Box.createVerticalStrut(30));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRow.setOpaque(false);

        JButton backBtn = Ui.makeSecondaryButton("BACK", 120, 44, Ui.Orientation.LEFT_RIGHT);
        backBtn.addActionListener(e -> ui.show("HOME_PAGE"));

        JButton loginBtn = Ui.makePrimaryButton("LOGIN", 120, 44);
        loginBtn.addActionListener(e -> attemptAdminLogin());

        btnRow.add(backBtn);
        btnRow.add(loginBtn);
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

    private void attemptAdminLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        // 🔑 แบบง่าย hardcode ก่อน
        if (user.equals("admin") && pass.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Welcome Admin!");
            ui.show("ADMIN_DASHBOARD"); // <- สร้าง panel ใหม่สำหรับ admin ต่อ
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials");
        }
    }
}
