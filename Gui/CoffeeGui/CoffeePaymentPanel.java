package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

import Gui.MainGui.*;
import StrategyPattern.DiscountStrategy;
import StrategyPattern.PointToCashDiscount;

public class CoffeePaymentPanel extends JPanel {

    private final MaeveCoffeeUI ui;
    private final Set<JToggleButton> paymentMethodButtons = new HashSet<>();
    private final ButtonGroup paymentGroup = new ButtonGroup();

    private JButton confirmBtn;

    private static final String IMG_PPROMPT = "Imgs/payments/promptpay.png";
    private static final String IMG_PAYPAL = "Imgs/payments/paypal.png";
    private static final String IMG_CARD = "Imgs/payments/card.png";
    private static final String IMG_CASH = "Imgs/payments/cash.png";

    public CoffeePaymentPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // Header
        JLabel header = new JLabel("PAYMENT", SwingConstants.LEFT);
        header.setForeground(Ui.TITLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 20, 6, 0));
        add(header, BorderLayout.NORTH);

        // Content margin
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        Ui.RoundedBorderPanel content = new Ui.RoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("PAYMENT METHOD", SwingConstants.CENTER);
        title.setForeground(Ui.TITLE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(6, 0, 10, 0));
        content.add(title, BorderLayout.NORTH);

        JPanel pmGrid = new JPanel(new GridLayout(2, 2, 18, 18));
        pmGrid.setOpaque(false);

        JToggleButton pm1 = makePaymentImageToggle(IMG_PPROMPT, Ui.SQUARE_PM);
        JToggleButton pm2 = makePaymentImageToggle(IMG_PAYPAL, Ui.SQUARE_PM);
        JToggleButton pm3 = makePaymentImageToggle(IMG_CARD, Ui.SQUARE_PM);
        JToggleButton pm4 = makePaymentImageToggle(IMG_CASH, Ui.SQUARE_PM);

        paymentGroup.add(pm1); paymentGroup.add(pm2);
        paymentGroup.add(pm3); paymentGroup.add(pm4);
        paymentMethodButtons.add(pm1); paymentMethodButtons.add(pm2);
        paymentMethodButtons.add(pm3); paymentMethodButtons.add(pm4);

        pmGrid.add(pm1); pmGrid.add(pm2); pmGrid.add(pm3); pmGrid.add(pm4);
        content.add(pmGrid, BorderLayout.CENTER);

        contentMargin.add(content, BorderLayout.CENTER);
        add(contentMargin, BorderLayout.CENTER);

        // Bottom
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        confirmBtn = Ui.makeSecondaryButton("CONFIRM", 150, 44, Ui.Orientation.LEFT_RIGHT);
        confirmBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Please select Payment Method completely",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
        });

        JButton cancel = Ui.makeSecondaryButton("CANCEL", 150, 44, Ui.Orientation.RIGHT_LEFT);
        cancel.addActionListener(e -> {
            resetPaymentSelections();
            ui.show("COFFEE_MENU");
        });

        bottomBar.add(confirmBtn);
        bottomBar.add(cancel);
        add(bottomBar, BorderLayout.SOUTH);

        // listeners
        ItemListener payListener = e -> updatePaymentConfirmEnabled(bottomBar);
        paymentMethodButtons.forEach(b -> b.addItemListener(payListener));
    }

    private JToggleButton makePaymentImageToggle(String imagePath, int size) {
        return new Gui.CustomGui.RoundedToggleButton(
                "",
                Ui.ARC, Ui.BORDER_STROKE, true,
                Ui.ITEM_FILL, Ui.SEL_FILL, Ui.SEL_FILL, Ui.ITEM_FILL,
                Ui.SEL_FILL, Ui.PRIMARY_FILL, Ui.SEL_FILL,
                Ui.TITLE, Ui.TITLE, Ui.TITLE, Ui.TITLE,
                Ui.TITLE, Ui.PRIMARY_TEXT, Ui.TITLE,
                Ui.ITEM_BORDER_TOP, Ui.ITEM_BORDER_BOT,
                Gui.CustomGui.RoundedToggleButton.Orientation.TOP_BOTTOM) {
            @Override
            protected void paintContent(Graphics2D g2, Color textColor) {
                int inset = 12;
                Ui.drawImageKeepRatio(g2, imagePath, inset, inset,
                        getWidth() - inset * 2, getHeight() - inset * 2);
            }
        };
    }

    private void updatePaymentConfirmEnabled(JPanel bottomBar) {
        boolean ok = paymentMethodButtons.stream().anyMatch(AbstractButton::isSelected);

        int idx = bottomBar.getComponentZOrder(confirmBtn);
        bottomBar.remove(confirmBtn);

        if (ok) {
            confirmBtn = Ui.makePrimaryButton("CONFIRM", 150, 44);
            confirmBtn.addActionListener(e -> {
                double total = calculateTotal(ui);
                double finalPrice = total;

                if (!ui.getCurrentUser().equals("GUEST")) {
                    DiscountStrategy strategy = new PointToCashDiscount();
                    finalPrice = strategy.applyDiscount(total, ui.getCurrentPoints());
                }

                ReceiptDialog dialog = new ReceiptDialog(
                        SwingUtilities.getWindowAncestor(this),
                        ui,
                        !ui.getCurrentUser().equals("GUEST"),
                        ui.getCurrentUser(),
                        ui.getCurrentPoints(),
                        total,
                        finalPrice
                );
                dialog.setVisible(true);

                if (!ui.getCurrentUser().equals("GUEST")) {
                    ui.setCurrentPoints(ui.getCurrentPoints() + 1);
                }

                ui.show("COFFEE_MENU");
            });

        } else {
            confirmBtn = Ui.makeSecondaryButton("CONFIRM", 150, 44, Ui.Orientation.LEFT_RIGHT);
            confirmBtn.addActionListener(ev -> JOptionPane.showMessageDialog(this,
                    "Please select Payment Method completely",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE));
        }

        bottomBar.add(confirmBtn, idx);
        bottomBar.revalidate();
        bottomBar.repaint();
    }

    private void resetPaymentSelections() {
        paymentMethodButtons.forEach(b -> b.setSelected(false));
        paymentGroup.clearSelection();
    }

    private double calculateTotal(MaeveCoffeeUI ui) {
        if (ui.getSelectedDrink() == null) return 0;
        double base = (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED
                && ui.getSelectedDrink().icedAvailable)
                ? ui.getSelectedDrink().icedPrice : ui.getSelectedDrink().hotPrice;
        double shotSum = ui.getExtraShots() * ui.getSelectedDrink().shotPrice;

        double topSum = 0;
        if (ui.getSelectedToppings() != null) {
            for (String t : ui.getSelectedToppings()) {
                topSum += ui.getToppingPrice(t);
            }
        }
        return base + shotSum + topSum;
    }
}
