package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptDialog extends JDialog {

    private final MaeveCoffeeUI ui;

    private JLabel nameLeft, pointLeft, dateLbl, timeLbl, totalLbl, rewardLbl;
    private JPanel itemsBox;

    // Realtime
    private MaeveCoffeeUI.PointsListener pointsListener;

    public ReceiptDialog(Frame owner, MaeveCoffeeUI ui) {
        super(owner, "Receipt", true);
        this.ui = ui;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.WHITE);
        setResizable(false);

        // ===== Paper =====
        JPanel paper = new JPanel(new BorderLayout());
        paper.setOpaque(true);
        paper.setBackground(Color.WHITE);
        paper.setBorder(new EmptyBorder(16, 20, 16, 20));
        setContentPane(paper);

        JPanel frame = new JPanel();
        frame.setOpaque(false);
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

        // ==== HEADER ====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 30, 8, 0));

        JLabel title = new JLabel("MAEVE COFFEE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Ui.BROWN_DARK);
        header.add(title, BorderLayout.CENTER);

        // Close
        JButton btnClose = new JButton("\u2715");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setOpaque(false);
        btnClose.setForeground(Ui.BROWN_DARK);
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeWrap.setOpaque(false);
        closeWrap.add(btnClose);
        header.add(closeWrap, BorderLayout.EAST);

        frame.add(header);

        // ==== USER, TIME ====
        JPanel topInfo = new JPanel(new GridBagLayout());
        topInfo.setOpaque(false);

        nameLeft = makeLabel("GUEST", 18, Font.BOLD, Ui.BROWN_DARK);
        pointLeft = makeLabel("", 16, Font.PLAIN, Ui.BROWN_DARK);
        dateLbl = makeLabel("--/--/--", 16, Font.PLAIN, Ui.BROWN_DARK);
        timeLbl = makeLabel("--:--", 16, Font.PLAIN, Ui.BROWN_DARK);

        int vgap = 6;

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 0, 0, 0);
        gc.gridy = 0;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.weightx = 1.0;
        topInfo.add(nameLeft, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0;
        topInfo.add(dateLbl, gc);

        gc.gridy = 1;
        gc.insets = new Insets(vgap, 0, 0, 0);

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.weightx = 1.0;
        topInfo.add(pointLeft, gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0;
        topInfo.add(timeLbl, gc);

        frame.add(topInfo);
        frame.add(dash());

        paper.add(frame, BorderLayout.NORTH);

        // ==== ITEM LIST ====
        itemsBox = new JPanel();
        itemsBox.setOpaque(false);
        itemsBox.setLayout(new BoxLayout(itemsBox, BoxLayout.Y_AXIS));
        itemsBox.setBorder(new EmptyBorder(8, 0, 15, 0));

        JPanel listHolder = new JPanel(new BorderLayout());
        listHolder.setOpaque(false);
        listHolder.add(itemsBox, BorderLayout.NORTH);

        JScrollPane centerScroll = new JScrollPane(listHolder);
        centerScroll.setBorder(BorderFactory.createEmptyBorder());
        centerScroll.setOpaque(false);
        centerScroll.getViewport().setOpaque(false);
        centerScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        paper.add(centerScroll, BorderLayout.CENTER);

        JPanel southframe = new JPanel();
        southframe.setOpaque(false);
        southframe.setLayout(new BoxLayout(southframe, BoxLayout.Y_AXIS));

        // Total
        JPanel rowTotal = new JPanel(new BorderLayout());
        rowTotal.setOpaque(false);
        rowTotal.setBorder(new EmptyBorder(8, 0, 8, 0));
        rowTotal.add(makeLabel("Total", 18, Font.BOLD, Ui.BROWN_DARK), BorderLayout.WEST);
        totalLbl = makeLabel("0 ฿", 18, Font.BOLD, Ui.BROWN_DARK);
        rowTotal.add(totalLbl, BorderLayout.EAST);
        southframe.add(rowTotal);

        southframe.add(dash());

        rewardLbl = makeLabel("Received 0 Point", 14, Font.PLAIN, Ui.BROWN_DARK);
        rewardLbl.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel rewardRow = new JPanel(new BorderLayout());
        rewardRow.setOpaque(false);
        rewardRow.setBorder(new EmptyBorder(8, 0, 0, 0));
        rewardRow.add(rewardLbl, BorderLayout.CENTER);
        southframe.add(rewardRow);

        paper.add(southframe, BorderLayout.SOUTH);

        pointsListener = newPts -> SwingUtilities.invokeLater(() -> pointLeft.setText("Current Point : " + newPts));
        ui.addPointsListener(pointsListener);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                populate();
            }
        });

        setSize(520, 720);
        setLocationRelativeTo(owner);
    }

    public void populate() {

        String uname = ui.getCurrentUser();
        nameLeft.setText(uname == null || uname.trim().isEmpty() ? "GUEST" : uname);
        pointLeft.setText("Current Point : " + ui.getCurrentPoints());

        LocalDateTime now = LocalDateTime.now();
        dateLbl.setText(
                String.format("%02d/%02d/%02d", now.getDayOfMonth(), now.getMonthValue(), (now.getYear() % 100)));
        timeLbl.setText(String.format("%02d:%02d", now.getHour(), now.getMinute()));

        itemsBox.removeAll();

        MaeveCoffeeUI.OrderSummary o = ui.getLastOrder();
        if (o != null) {

            List<MaeveCoffeeUI.OrderItem> items = o.items;
            for (MaeveCoffeeUI.OrderItem it : items) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setBorder(new EmptyBorder(10, 0, 10, 0));

                JLabel left = makeLabel(it.qty + " " + it.label, 16, Font.PLAIN, Ui.BROWN_DARK);
                JLabel right = makeLabel(it.lineTotal() + " ฿", 16, Font.BOLD, Ui.BROWN_DARK);

                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);
                itemsBox.add(row);
            }
            totalLbl.setText(o != null ? o.total() + " ฿" : "0 ฿");

            int earned = (o != null) ? Math.max(0, o.pointsEarned) : 0;

            if (ui.isSignedIn()) {
                rewardLbl.setText("Received " + earned + " Point" + (earned == 1 ? "" : "s"));
            } else {
                rewardLbl.setText("You may take this bill to cashier counter");
            }

        }

        itemsBox.revalidate();
        itemsBox.repaint();
    }

    // ========= helper =========
    private JLabel makeLabel(String t, int sz, int style, Color color) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", style, sz));
        l.setForeground(color);
        return l;
    }

    private JLabel makeLabel(String t, int sz, int style, int rgb) {
        return makeLabel(t, sz, style, new Color(rgb));
    }

    private JComponent dash() {
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xCCBDA7));
                int y = getHeight() / 2;
                g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };
        c.setPreferredSize(new Dimension(1, 12));
        c.setOpaque(false);
        return c;
    }
}
