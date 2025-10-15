package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import Gui.CustomGui.TopRoundedPanel;
import Gui.CustomGui.*;
import Lib.*;
import StrategyPattern.*;

public class SummaryPanel extends JPanel {
    private final MaeveCoffeeUI ui;

    public SummaryPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    private JComponent buildHeader() {

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Ui.BROWN);
        bar.setBorder(new EmptyBorder(10, 12, 30, 12));

        // Back
        JButton back = makeHeaderButton("◀");
        back.addActionListener(e -> ui.show("COFFEE_MENU"));


        JLabel title = new JLabel("SUMMARY", SwingConstants.CENTER);
        title.setForeground(Ui.WHITE); // ตัวอักษรสีขาวบนพื้นเข้ม
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setBorder(new EmptyBorder(25, 0, 0, 0));

        // ปุ่ม Close (ขวา)
        JButton close = makeHeaderButton("✕");
        close.addActionListener(e -> ui.show("HOME_PAGE"));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(back);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(close);

        bar.add(left, BorderLayout.WEST);
        bar.add(title, BorderLayout.CENTER);
        bar.add(right, BorderLayout.EAST);

        // เส้นคั่นบาง ๆ ใต้แถบหัว
        JComponent divider = new JComponent() {
            {
                setPreferredSize(new Dimension(1, 1));
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 40)); // เงาบาง ๆ
                g.fillRect(0, 0, getWidth(), 1);
            }
        };

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(bar, BorderLayout.NORTH);
        header.add(divider, BorderLayout.SOUTH);
        return header;
    }

    // ==== BODY ====
    private JComponent buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(12, 16, 16, 16));

        // Items
        JPanel items = new JPanel();
        items.setOpaque(false);
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        body.add(items, BorderLayout.NORTH);

        // Bottom
        JPanel pointBar = new JPanel(new BorderLayout());
        pointBar.setBackground(Ui.BROWN);
        pointBar.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel yourPoint = makeLabel("Your Point : ", 18, Font.BOLD, new Color(0xF3D090));
        JLabel pointNum = makeLabel("500", 22, Font.BOLD, new Color(0xFFC23D));
        JLabel pointTail = makeLabel(" Point", 18, Font.BOLD, Color.WHITE);

        JPanel pointWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pointWrap.setOpaque(false);
        pointWrap.add(yourPoint);
        pointWrap.add(Box.createHorizontalStrut(6));
        pointWrap.add(pointNum);
        pointWrap.add(Box.createHorizontalStrut(6));
        pointWrap.add(pointTail);
        pointBar.add(pointWrap, BorderLayout.WEST);

        // Redeem
        JPanel redeem = new JPanel(new BorderLayout());
        redeem.setOpaque(false);
        redeem.setBorder(new EmptyBorder(16, 0, 12, 0));

        JLabel redeemTitle = makeLabel("Redeem Point", 20, Font.BOLD, Ui.BROWN);
        JLabel hint = makeLabel("100 Point = 1 Bath", 12, Font.PLAIN, new Color(0x9D715C));

        JPanel redeemLeft = new JPanel();
        redeemLeft.setOpaque(false);
        redeemLeft.setLayout(new BoxLayout(redeemLeft, BoxLayout.Y_AXIS));
        redeemLeft.add(redeemTitle);
        redeemLeft.add(Box.createVerticalStrut(4));
        redeemLeft.add(hint);

        JTextField input = new JTextField("0");
        input.setHorizontalAlignment(SwingConstants.CENTER);
        input.setEditable(false);
        input.setPreferredSize(new Dimension(120, 40));
        input.setFont(input.getFont().deriveFont(Font.BOLD, 16f));
        input.setBorder(BorderFactory.createLineBorder(Ui.BROWN, 2, true));
        input.setBackground(Ui.WHITE);
        input.setForeground(Ui.BROWN);

        redeem.add(redeemLeft, BorderLayout.WEST);
        redeem.add(input, BorderLayout.EAST);

        JComponent divider = makeDivider(); // เส้น

        // Discount & Total
        JPanel totals = new JPanel();
        totals.setOpaque(false);
        totals.setLayout(new BoxLayout(totals, BoxLayout.Y_AXIS));
        totals.add(valueRow("Discount", "0 B", false));
        totals.add(Box.createVerticalStrut(8));
        totals.add(valueRow("Total", "40B", true));

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(pointBar);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(redeem);
        bottom.add(divider);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(totals);

        body.add(bottom, BorderLayout.SOUTH);
        return body;
    }

    // ---------- Small builders (ผูกสี/ฟอนต์กับ Ui) ----------

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

    private JPanel itemRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 4, 6, 4));

        JLabel l = makeLabel(label, 20, Font.PLAIN, Ui.BROWN);
        JLabel v = makeLabel(value, 20, Font.BOLD, Ui.BROWN);
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JPanel valueRow(String label, String value, boolean emphasize) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 4, 6, 4));

        JLabel l = makeLabel(label, emphasize ? 22 : 18, emphasize ? Font.BOLD : Font.PLAIN, Ui.BROWN);
        JLabel v = makeLabel(value, emphasize ? 24 : 18, Font.BOLD, Ui.BROWN);
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JLabel makeLabel(String txt, int size, int style, Color color) {
        JLabel lb = new JLabel(txt);
        lb.setForeground(color);
        lb.setFont(new Font("SansSerif", style, size));
        return lb;
    }

    private JButton makeIconButton(String txt) {
        JButton b = new JButton(txt);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(Ui.BROWN); // ✅ สีไอคอนตามธีม
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JComponent makeDivider() {
        return new JComponent() {
            {
                setPreferredSize(new Dimension(1, 1));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Ui.BROWN); // ✅ ใช้สีขอบจาก Ui
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
    }
}
