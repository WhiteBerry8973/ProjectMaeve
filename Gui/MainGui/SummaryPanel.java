package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import StrategyPattern.*;

public class SummaryPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private final DiscountStrategy redeemStrategy = new PointPricingStrategy();

    private JPanel itemsBox;
    private JLabel pointsLbl, discountLbl, totalLbl;
    private JSpinner redeemSpinner;

    private final PointEarnStrategy pointEarnStrategy = new StrategyPattern.DefaultPointEarnStrategy();
    private final Lib.LoyaltyService loyalty;

    public SummaryPanel(MaeveCoffeeUI ui) {
        this.ui = java.util.Objects.requireNonNull(ui, "ui must not be null");
        this.loyalty = new Lib.LoyaltyService(ui::isSignedIn, ui::addPoints);
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Ui.WHITE);

        // Paper
        JPanel paper = new JPanel(new BorderLayout());
        paper.setOpaque(true);
        paper.setBackground(Ui.WHITE);
        paper.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(paper, BorderLayout.CENTER);

        // ==== HEADER ====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Ui.BROWN);
        header.setBorder(new EmptyBorder(10, 12, 0, 12));

        JLabel title = new JLabel("PRICE SUMMARY", SwingConstants.CENTER);
        title.setForeground(Ui.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setBorder(new EmptyBorder(20, 0, 25, 0));
        header.add(title, BorderLayout.CENTER);

        // Back
        JButton back = makeHeaderButton("◀");
        back.setFont(new Font("SansSerif", Font.BOLD, 30));
        back.setBorder(new EmptyBorder(20, 25, 0, 0));
        back.addActionListener(e -> ui.show("COFFEE_MENU"));

        // Close
        JButton close = makeHeaderButton("✕");
        close.setFont(new Font("SansSerif", Font.BOLD, 30));
        close.setBorder(new EmptyBorder(20, 0, 0, 25));
        close.addActionListener(e -> ui.show("HOME_PAGE"));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(back);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(close);
        header.add(right, BorderLayout.EAST);

        paper.add(header, BorderLayout.NORTH);

        // ==== ITEM LIST ====
        JPanel listWrap = new JPanel(new BorderLayout());
        listWrap.setOpaque(false);

        itemsBox = new JPanel();
        itemsBox.setOpaque(false);
        itemsBox.setLayout(new BoxLayout(itemsBox, BoxLayout.Y_AXIS));
        itemsBox.setBorder(new EmptyBorder(20, 50, 10, 50));

        JPanel listHolder = new JPanel(new BorderLayout());
        listHolder.setOpaque(false);
        listHolder.add(itemsBox, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listHolder);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listWrap.add(scroll, BorderLayout.CENTER);

        paper.add(listWrap, BorderLayout.CENTER);

        JPanel frame = new JPanel();
        frame.setOpaque(false);
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

        // Your Point
        JPanel midder = new JPanel(new BorderLayout());
        midder.setBackground(Ui.BROWN);
        midder.setBorder(new EmptyBorder(30, 50, 30, 50));
        pointsLbl = new JLabel();
        pointsLbl.setFont(new Font("SansSerif", Font.BOLD, 30));
        pointsLbl.setForeground(Ui.WHITE);
        midder.add(pointsLbl, BorderLayout.WEST);
        frame.add(midder);

        // Redeem Block
        JPanel redeemWrap = new JPanel(new GridBagLayout());
        redeemWrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 50, 0, 50);

        // Label 1
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        redeemWrap.add(makeLabel("Redeem Point", 30, Font.BOLD, Ui.BROWN), gc);

        // Label 2
        gc.gridy = 1;
        gc.insets = new Insets(5, 50, 15, 50);
        redeemWrap.add(makeLabel("100 Point = 1 Baht", 20, Font.PLAIN, Ui.BROWN_LIGHT), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;
        redeemSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 100));
        Dimension spinSize = new Dimension(100, 50);
        redeemSpinner.setPreferredSize(spinSize);
        redeemSpinner.setFont(new Font("SansSerif", Font.BOLD, 18));
        redeemWrap.add(redeemSpinner, gc);

        frame.add(redeemWrap);

        Color LIGHT_BOX = Ui.WHITE_DARK;
        JPanel bottomBox = new JPanel();
        bottomBox.setOpaque(true);
        bottomBox.setBackground(LIGHT_BOX);
        bottomBox.setLayout(new BoxLayout(bottomBox, BoxLayout.Y_AXIS));
        bottomBox.setBorder(new EmptyBorder(12, 40, 12, 40));

        JPanel disRow = new JPanel(new BorderLayout());
        disRow.setOpaque(false);
        disRow.add(makeLabel("Discount", 25, Font.BOLD, Ui.BROWN_LIGHT), BorderLayout.WEST);
        discountLbl = makeLabel("0 ฿", 25, Font.BOLD, Ui.BROWN_LIGHT);
        disRow.add(discountLbl, BorderLayout.EAST);
        disRow.setBorder(new EmptyBorder(4, 10, 4, 10));
        bottomBox.add(disRow);

        // Total
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.add(makeLabel("Total", 25, Font.BOLD, Ui.BROWN), BorderLayout.WEST);
        totalLbl = makeLabel("0 ฿", 25, Font.BOLD, Ui.BROWN);
        totalRow.add(totalLbl, BorderLayout.EAST);
        totalRow.setBorder(new EmptyBorder(10, 10, 15, 10));
        bottomBox.add(totalRow);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setBorder(new EmptyBorder(0, 0, 10, 0));
        btnRow.setOpaque(false);

        JButton btnPay = Ui.makePrimaryButton("CHECK BILL", 500, 45);
        btnPay.addActionListener(e -> finalizeAndGoBill());

        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnPay.getPreferredSize().height));
        btnRow.add(btnPay);
        bottomBox.add(btnRow);

        frame.add(bottomBox);
        paper.add(frame, BorderLayout.SOUTH);

        redeemSpinner.addChangeListener(e -> recalcDiscount());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                populate();
            }
        });
    }

    private void populate() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null)
            return;

        // Add
        itemsBox.removeAll();
        for (MaeveCoffeeUI.OrderItem it : o.items) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(0, 0, 25, 0));
            row.add(makeLabel(it.qty + " " + it.label, 22, Font.BOLD, Ui.BROWN), BorderLayout.WEST);
            row.add(makeLabel(it.lineTotal() + " ฿", 22, Font.BOLD, Ui.BROWN), BorderLayout.EAST);
            itemsBox.add(row);
        }
        itemsBox.revalidate();
        itemsBox.repaint();

        // Point
        pointsLbl.setText("YOUR POINT : " + ui.getCurrentPoints() + " POINT");

        int subtotal = o.total();
        int maxRedeemByPrice = Math.max(0, subtotal) * 100;
        int max = Math.max(0, Math.min(ui.getCurrentPoints(), maxRedeemByPrice));

        SpinnerNumberModel m = (SpinnerNumberModel) redeemSpinner.getModel();
        m.setMaximum(max);
        m.setMinimum(0);
        m.setStepSize(100);
        redeemSpinner.setValue(0);

        recalcDiscount();
    }

    private void recalcDiscount() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null)
            return;
        int subtotal = o.total();

        int redeem = (Integer) redeemSpinner.getValue();
        PricingContext ctx = new PricingContext(ui.getCurrentUser(), redeem);

        double netD = redeemStrategy.apply(subtotal, ctx);
        int net = (int) Math.round(netD);
        int discount = subtotal - net;
        if (net < 0)
            net = 0;
        if (discount < 0)
            discount = 0;

        discountLbl.setText(discount + " ฿");
        totalLbl.setText(net + " ฿");
    }

    private void finalizeAndGoBill() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null)
            return;

        int subtotal = o.total();
        int redeem = (Integer) redeemSpinner.getValue();

        PricingContext ctx = new PricingContext(ui.getCurrentUser(), redeem);
        int net = (int) Math.round(redeemStrategy.apply(subtotal, ctx));
        int discount = subtotal - net;
        if (discount < 0)
            discount = 0;

        int usedPts = ctx.pointsToDeduct;

        o.items.removeIf(it -> it.label.startsWith("Discount (Redeem "));
        if (discount > 0) {
            o.items.add(new MaeveCoffeeUI.OrderItem("Discount (Redeem " + usedPts + " pts)", 1, -discount));
            loyalty.redeemPointsIfAny(usedPts);
        }

        int earned = pointEarnStrategy.computeEarnedPoints(subtotal);
        loyalty.grantIfEligible(o, earned);

        ui.showBillDialog(this);
    }

    private JLabel makeLabel(String t, int sz, int style, Color color) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", style, sz));
        l.setForeground(color);
        return l;
    }

    private JButton makeHeaderButton(String txt) {
        JButton b = new JButton(txt);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(Ui.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}