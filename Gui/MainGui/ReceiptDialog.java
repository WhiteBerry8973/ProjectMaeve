package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(Window parent, MaeveCoffeeUI ui,
            boolean loggedIn, String username, int points,
            double total, double finalPrice) {
        super(parent, "Receipt", ModalityType.APPLICATION_MODAL);

        setSize(350, 500);
        setLocationRelativeTo(parent);

        JTextArea receipt = new JTextArea();
        receipt.setEditable(false);
        receipt.setFont(new Font("Monospaced", Font.BOLD, 14));

        StringBuilder sb = new StringBuilder();
        // ===== Header =====
        sb.append("\n");
        sb.append(centerText("MAEVE COFFEE", 36)).append("\n\n");
        sb.append(" ---------------------------------------\n");

        if (loggedIn) {
            sb.append(username).append("   Your Point : ").append(points).append("\n");
        } else {
            sb.append("GUEST\n");
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy   HH:mm");
        sb.append("\n").append(dtf.format(now)).append("\n\n");

        sb.append(" ---------------------------------------\n");

        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d != null) {
            double base = (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable)
                    ? d.icedPrice
                    : d.hotPrice;
            sb.append(String.format("%-20s %5.0f ฿\n", "1 " + d.name, base));
        }

        int shots = ui.getExtraShots();
        if (shots > 0) {
            double shotPrice = ui.getSelectedDrink().shotPrice;
            sb.append(String.format("%-20s %5.0f ฿\n", shots + " Extrashot(+" + (int) shotPrice + "฿)",
                    shots * shotPrice));
        }

        List<String> toppings = ui.getSelectedToppings();
        if (toppings != null) {
            for (String t : toppings) {
                int price = ui.getToppingPrice(t);
                sb.append(String.format("%-20s %5d ฿\n", "1 " + t, price));
            }
        }

        if (ui.getSelectedSweetness() != null) {
            sb.append(String.format("%-20s %s\n", "Sweetness", ui.getSelectedSweetness()));
        }

        sb.append("\n");
        sb.append(String.format("%-20s %5.0f ฿\n", "Subtotal", total));

        if (loggedIn) {
            sb.append(String.format("%-20s %5.0f ฿\n", "After Discount", finalPrice));
            sb.append("Received 1 Point\n");
        }

        sb.append(" ---------------------------------------\n");
        sb.append("You may take this bill to cashier counter\n");

        receipt.setText(sb.toString());

        add(new JScrollPane(receipt), BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dispose());
        add(ok, BorderLayout.SOUTH);
    }

    private String centerText(String text, int width) {
        int pad = (width - text.length()) / 2;
        if (pad <= 0)
            return text;
        return " ".repeat(pad) + text;
    }
}
