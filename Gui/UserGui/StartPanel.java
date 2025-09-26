package Gui.UserGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Gui.MainGui.MaeveCoffeeUI;
import Gui.MainGui.Ui;

public class StartPanel extends JPanel {

    public StartPanel(MaeveCoffeeUI ui) {
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Title =====
        JLabel title = new JLabel("MAEVE COFFEE", SwingConstants.CENTER);
        title.setForeground(Ui.TITLE);
        title.setFont(new Font("SansSerif", Font.BOLD, 42)); // 🔹 ใหญ่ขึ้น
        title.setBorder(new EmptyBorder(60, 0, 40, 0)); // 🔹 เว้นระยะมากขึ้น
        add(title, BorderLayout.NORTH);

        // ===== Rounded content =====
        Ui.RoundedBorderPanel box = new Ui.RoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        box.setLayout(new GridLayout(3, 1, 0, 30)); // 🔹 ระยะห่างปุ่มเยอะขึ้น
        box.setBorder(new EmptyBorder(60, 80, 60, 80));

        JButton signup = Ui.makePrimaryButton("SIGNUP", 240, 60); // 🔹 ปุ่มใหญ่ขึ้น
        signup.addActionListener(e -> ui.show("SIGNUP"));

        JButton login = Ui.makeSecondaryButton("LOGIN", 240, 60, Ui.Orientation.TOP_BOTTOM);
        login.addActionListener(e -> ui.show("LOGIN"));

        JButton guest = Ui.makeSecondaryButton("GUEST", 240, 60, Ui.Orientation.TOP_BOTTOM);
        guest.addActionListener(e -> {
            ui.setCurrentUser("GUEST", 0);
            ui.show("COFFEE_MENU");
        });

        box.add(signup);
        box.add(login);
        box.add(guest);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(box);
        add(center, BorderLayout.CENTER);

        // ===== Adminster link =====
        JLabel admin = new JLabel("Adminster", SwingConstants.RIGHT);
        admin.setForeground(new Color(150, 150, 150));
        admin.setFont(new Font("SansSerif", Font.PLAIN, 14));
        admin.setBorder(new EmptyBorder(0, 0, 20, 20));
        admin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ui.show("ADMIN_LOGIN");
            }
        });
        add(admin, BorderLayout.SOUTH);
    }
}
