package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Gui.MainGui.MaeveCoffeeUI;

public class ReceiptDialog extends JPanel {
    private final MaeveCoffeeUI ui;

    private JLabel nameLeft, pointRight, dateLbl, timeLbl, totalLbl, rewardLbl;
    private JPanel itemsBox;

    public ReceiptDialog(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        // แผ่นขาวเต็มกรอบ
        JPanel paper = new JPanel(new BorderLayout());
        paper.setBackground(Color.WHITE);
        paper.setBorder(new EmptyBorder(28, 32, 28, 32));
        add(paper, BorderLayout.CENTER);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 6, 0));

        JLabel title = new JLabel("MAEVE COFFEE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(0x1F1F1F));
        header.add(title, BorderLayout.CENTER);

        JButton btnClose = new JButton("\u2715");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setOpaque(false);
        btnClose.setForeground(new Color(0x1F1F1F));
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(
                    this,
                    "Cancel and return to menu?",
                    "Cancel receipt",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.YES_OPTION) ui.show("COFFEE_MENU");
        });
        JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeWrap.setOpaque(false);
        closeWrap.add(btnClose);
        header.add(closeWrap, BorderLayout.EAST);

        paper.add(header, BorderLayout.NORTH);

        // ===== Body =====
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(10, 0, 5, 0));

        // user row
        JPanel rowUser = new JPanel(new BorderLayout());
        rowUser.setOpaque(false);
        nameLeft = makeLabel("GUEST", 18, Font.BOLD, 0x1F1F1F);
        pointRight = makeLabel("", 16, Font.PLAIN, 0x1F1F1F);
        rowUser.add(nameLeft, BorderLayout.WEST);
        rowUser.add(pointRight, BorderLayout.EAST);

        // datetime row
        JPanel rowDT = new JPanel(new BorderLayout());
        rowDT.setOpaque(false);
        dateLbl = makeLabel("--/--/--", 20, Font.PLAIN, 0x444444);
        timeLbl = makeLabel("--:--", 20, Font.PLAIN, 0x444444);
        rowDT.add(dateLbl, BorderLayout.WEST);
        rowDT.add(timeLbl, BorderLayout.EAST);

        // ทำเลย์เอาต์นิ่ง: ล็อกความสูงแถว + จองความกว้าง pointRight
        final int H_ROW = 32;
        String sample = "Your Point : 9999";
        FontMetrics pfm = getFontMetrics(pointRight.getFont());
        int pointW = pfm.stringWidth(sample) + 6;
        Dimension POINT_DIM = new Dimension(pointW, H_ROW);
        pointRight.setPreferredSize(POINT_DIM);
        pointRight.setMinimumSize(POINT_DIM);
        pointRight.setMaximumSize(POINT_DIM);

        Dimension ROW_DIM = new Dimension(Integer.MAX_VALUE, H_ROW);
        rowUser.setPreferredSize(ROW_DIM);
        rowUser.setMinimumSize(ROW_DIM);
        rowUser.setMaximumSize(ROW_DIM);
        rowDT.setPreferredSize(ROW_DIM);
        rowDT.setMinimumSize(ROW_DIM);
        rowDT.setMaximumSize(ROW_DIM);

        body.add(rowUser);
        body.add(rowDT);
        body.add(dash());
        body.add(space(6));

        itemsBox = new JPanel();
        itemsBox.setOpaque(false);
        itemsBox.setLayout(new BoxLayout(itemsBox, BoxLayout.Y_AXIS));
        itemsBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        body.add(itemsBox);

        body.add(space(8));
        JPanel rowTotal = new JPanel(new BorderLayout());
        rowTotal.setOpaque(false);
        rowTotal.add(makeLabel("Total", 20, Font.BOLD, 0x1F1F1F), BorderLayout.WEST);
        totalLbl = makeLabel("0 ฿", 20, Font.BOLD, 0x1F1F1F);
        rowTotal.add(totalLbl, BorderLayout.EAST);
        body.add(rowTotal);

        body.add(space(8));
        body.add(dash());
        body.add(space(8));

        rewardLbl = makeLabel("You may take this bill to cashier counter", 14, Font.PLAIN, 0x666666);
        rewardLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(rewardLbl);

        body.add(space(8));
        body.add(dash());

        paper.add(body, BorderLayout.CENTER);

        // Refresh เมื่อถูกโชว์
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) { populate(); }
        });
    }

    private void populate() {
        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o == null) return;

        String uname = (o.username == null || o.username.isBlank()) ? "GUEST" : o.username;
        nameLeft.setText(uname.toUpperCase());

        if (!"GUEST".equalsIgnoreCase(uname)) {
            pointRight.setText("Your Point : " + (o.pointsBefore + o.pointsEarned));
        } else {
            pointRight.setText("\u00A0");
        }

        dateLbl.setText(String.format("%d/%d/%02d",
                o.date.getDayOfMonth(), o.date.getMonthValue(), o.date.getYear() % 100));
        timeLbl.setText(String.format("%02d:%02d", o.time.getHour(), o.time.getMinute()));

        itemsBox.removeAll();
        for (MaeveCoffeeUI.OrderItem it : o.items) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(8, 0, 8, 0));
            row.add(makeLabel(it.qty + " " + it.label, 20, Font.PLAIN, 0x222222), BorderLayout.WEST);
            row.add(makeLabel(it.lineTotal() + "  ฿", 20, Font.BOLD, 0x111111), BorderLayout.EAST);
            itemsBox.add(row);
        }
        totalLbl.setText(o.total() + "  ฿");

        if (!"GUEST".equalsIgnoreCase(uname))
            rewardLbl.setText("Received " + o.pointsEarned + " Point" + (o.pointsEarned > 1 ? "s" : "") + " and 1 Ticket");
        else
            rewardLbl.setText("You may take this bill to cashier counter");

        itemsBox.revalidate();
        itemsBox.repaint();
    }

    // Helpers
    private JLabel makeLabel(String t, int sz, int style, int rgb) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", style, sz));
        l.setForeground(new Color(rgb));
        return l;
    }
    private Component space(int h) { JPanel p = new JPanel(); p.setOpaque(false); p.setPreferredSize(new Dimension(1,h)); return p; }
    private JComponent dash() {
        return new JComponent(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;
                g2.setColor(new Color(0x1F1F1F));
                float[] ds={6f,6f};
                g2.setStroke(new BasicStroke(2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,10f,ds,0f));
                int y=getHeight()/2;
                g2.drawLine(0,y,getWidth(),y);
            }
            @Override public Dimension getPreferredSize(){return new Dimension(10,12);}
        };
    }
}
