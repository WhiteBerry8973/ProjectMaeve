package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import StrategyPattern.*;

/**
 * SummaryPanel
 * - ดึงรายการล่าสุดจาก MaeveCoffeeUI.getLastOrder()
 * - Redeem แต้ม ด้วยกติกา PointPricingStrategy: 100 แต้ม = 1 บาท
 * - ปุ่ม Total จะบันทึกส่วนลดเป็นบรรทัดติดลบแล้วย้ายไปหน้า BILL
 */
public class SummaryPanel extends JPanel {
    private final MaeveCoffeeUI ui;
    private final DiscountStrategy redeemStrategy = new PointPricingStrategy();

    private JPanel itemsBox;
    private JLabel pointsLbl, discountLbl, totalLbl;
    private JSpinner redeemSpinner;

    public SummaryPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(0xE9E1CF));

        // Paper
        JPanel paper = new JPanel(new BorderLayout());
        paper.setOpaque(true);
        paper.setBackground(new Color(0xF5EEDB));
        paper.setBorder(new EmptyBorder(16, 16, 16, 16));
        add(paper, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Ui.BROWN);
        header.setBorder(new EmptyBorder(10, 12, 0, 12));

        // Title
        JLabel title = new JLabel("PRICE SUMMARY", SwingConstants.CENTER);
        title.setForeground(Ui.WHITE); // ตัวอักษรสีขาวบนพื้นเข้ม
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setBorder(new EmptyBorder(20, 0, 25, 0));
        header.add(title, BorderLayout.CENTER);

        // Back
        JButton back = makeHeaderButton("◀");
        back.setFont(new Font("SansSerif", Font.BOLD, 30));
        back.setBorder(new EmptyBorder(20,25,0,0));
        back.addActionListener(e -> ui.show("COFFEE_MENU"));
        header.add(back, BorderLayout.WEST);

        // ปุ่ม Close (ขวา)
        JButton close = makeHeaderButton("✕");
        close.setFont(new Font("SansSerif", Font.BOLD, 30));
        close.setBorder(new EmptyBorder(20,0,0,25));
        close.addActionListener(e -> ui.show("HOME_PAGE"));
        header.add(close, BorderLayout.EAST);

        
        // ปุ่ม Back, Close (ซ้าย-ขวา) ซ้อนกันอีกชั้น เพื่อให้ปุ่มชิดขอบ
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(back);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(close);
        header.add(right, BorderLayout.EAST);

        paper.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(8, 0, 8, 0));
        paper.add(body, BorderLayout.CENTER);

        // รายการสินค้า
        itemsBox = new JPanel();
        itemsBox.setOpaque(false);
        itemsBox.setLayout(new BoxLayout(itemsBox, BoxLayout.Y_AXIS));
        body.add(itemsBox);
        body.add(space(12));

        // แสดงแต้มที่มี (ก่อน redeem) โดยดึงแต้มจาก getCurrentPoints()
        JPanel midder = new JPanel(new BorderLayout());
        midder.setBackground(Ui.BROWN);
        midder.setBorder(new EmptyBorder(10, 12, 0, 12));
        body.add(midder);

        pointsLbl = new JLabel();
        pointsLbl.setFont(new Font("SansSerif", Font.BOLD, 30));
        pointsLbl.setForeground(Ui.WHITE);
        midder.add(pointsLbl, BorderLayout.WEST);

        // Redeem
        JPanel redeemWrap = new JPanel(new GridBagLayout());
        redeemWrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,4,4,4);
        gc.gridy = 0; gc.gridx = 0; gc.anchor = GridBagConstraints.WEST;
        redeemWrap.add(makeLabel("Redeem Point", 30, Font.BOLD, 0x4A2D1B), gc);
        gc.gridy = 1; gc.gridx = 0;
        redeemWrap.add(makeLabel("100 Point = 1 Baht", 20, Font.BOLD, 0x6B4A35), gc);
        gc.gridy = 0; gc.gridx = 1; gc.gridheight = 2; gc.anchor = GridBagConstraints.EAST;

        // Redeem Spinner
        redeemSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 100)); // max จะตั้งตอน populate()
        Dimension spinSize = new Dimension(96, 36);
        redeemSpinner.setPreferredSize(spinSize);
        redeemSpinner.setFont(new Font("SansSerif", Font.BOLD, 18));
        redeemWrap.add(redeemSpinner, gc);
        body.add(redeemWrap);

        // ส่วนลด
        JPanel disRow = new JPanel(new BorderLayout());
        disRow.setOpaque(false);
        disRow.add(makeLabel("Discount", 16, Font.PLAIN, 0x4A2D1B), BorderLayout.WEST);
        discountLbl = makeLabel("0 ฿", 18, Font.BOLD, 0x4A2D1B);
        disRow.add(discountLbl, BorderLayout.EAST);
        body.add(disRow);

        body.add(space(8));
        body.add(dash());

        // Total
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.add(makeLabel("Total", 18, Font.BOLD, 0x4A2D1B), BorderLayout.WEST);
        totalLbl = makeLabel("0 ฿", 20, Font.BOLD, 0x4A2D1B);
        totalRow.add(totalLbl, BorderLayout.EAST);
        body.add(totalRow);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JButton btnPay = new JButton("Total");
        btnPay.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnPay.setBackground(new Color(0x4A2D1B));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFocusPainted(false);
        btnPay.addActionListener(e -> finalizeAndGoBill());
        footer.add(btnPay, BorderLayout.EAST);
        paper.add(footer, BorderLayout.SOUTH);

        // events
        redeemSpinner.addChangeListener(e -> recalcDiscount());

        // populate on show
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) { populate(); }
        });
    }

    private void populate() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null) return;

        // เติมรายการ
        itemsBox.removeAll();
        for (MaeveCoffeeUI.OrderItem it : o.items) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(4,0,4,0));
            row.add(makeLabel(it.qty + " " + it.label, 18, Font.BOLD, 0x3A2A1F), BorderLayout.WEST);
            row.add(makeLabel(it.lineTotal() + " ฿", 18, Font.BOLD, 0x3A2A1F), BorderLayout.EAST);
            itemsBox.add(row);
        }
        itemsBox.revalidate();
        itemsBox.repaint();

        // แต้มที่มี ก่อน redeem
        pointsLbl.setText("YOUR POINT : " + ui.getCurrentPoints() + " POINT");

        // จำกัดค่าสูงสุดของ redeem: min(points, subtotal*100)
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
        if (o == null) return;
        int subtotal = o.total();

        int redeem = (Integer) redeemSpinner.getValue();      // แต้มที่จะแลก
        PricingContext ctx = new PricingContext(ui.getCurrentUser(), redeem);

        double netD = redeemStrategy.apply(subtotal, ctx);    // net หลังส่วนลดจาก Strategy
        int net = (int)Math.round(netD);
        int discount = subtotal - net;
        if (net < 0) net = 0;
        if (discount < 0) discount = 0;

        discountLbl.setText(discount + " ฿");
        totalLbl.setText(net + " ฿");
    }

    private void finalizeAndGoBill() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null) return;

        int subtotal = o.total();
        int redeem = (Integer) redeemSpinner.getValue();
        PricingContext ctx = new PricingContext(ui.getCurrentUser(), redeem);

        int net = (int)Math.round(redeemStrategy.apply(subtotal, ctx));
        int discount = subtotal - net;
        if (discount < 0) discount = 0;

        // ลบบรรทัดส่วนลดเก่าถ้ามี
        o.items.removeIf(it -> it.label.startsWith("Discount (Redeem "));

        if (discount > 0) {
            o.items.add(new MaeveCoffeeUI.OrderItem("Discount (Redeem " + redeem + " pts)", 1, -discount));
            // ตัดแต้มออกจากยอดสะสม
            ui.setCurrentPoints(Math.max(0, ui.getCurrentPoints() - redeem));
        }
        ui.show("BILL");
    }

    private JLabel makeLabel(String t, int sz, int style, int rgb) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", style, sz));
        l.setForeground(new Color(rgb));
        return l;
    }

    private Component space(int h) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(1,h));
        return p;
    }

    private JComponent dash() {
        JComponent c = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xCCBDA7));
                int y = getHeight()/2;
                g2.drawLine(0,y,getWidth(),y);
                g2.dispose();
            }
        };
        c.setPreferredSize(new Dimension(1, 12));
        return c;
    }

    // ปุ่มใน header (back, close) style เดียวกัน
    private JButton makeHeaderButton(String txt) {
        JButton b = new JButton(txt);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(Ui.WHITE); // ปุ่มบนพื้นเข้ม → ตัวอักษรสีขาว
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

}