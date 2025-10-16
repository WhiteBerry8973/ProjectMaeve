package Gui.TeaGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.*;

import Gui.MainGui.*;
import Lib.*;
import Gui.CustomGui.*;

public class TeaAddonPanel extends JPanel {

    private static final String IMG_TOPPING1 = "Imgs/toppings/black_pearl.png";
    private static final String IMG_TOPPING2 = "Imgs/toppings/konjac_pearl.png";
    private static final String IMG_TOPPING3 = "Imgs/toppings/golden_pearl.png";

    private static final String[] SIZE_CODES = { "S001", "S002", "S003" };

    private final MaeveCoffeeUI ui;

    private JLabel lblDrinkTitle;
    private JButton lblTotal;
    private JPanel totalWrap;
    private boolean totalSolid = false;

    private final Set<JToggleButton> toppingButtons = new HashSet<>();
    private final Set<JToggleButton> sweetnessButtons = new HashSet<>();
    private final ButtonGroup sweetnessGroup = new ButtonGroup();

    private final ButtonGroup typeGroup = new ButtonGroup();
    private SegmentedTab hotToggle, icedToggle;

    private int selectedSize = 0;

    public TeaAddonPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.WHITE);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 20, 10, 20));

        lblDrinkTitle = new JLabel(" ");
        lblDrinkTitle.setForeground(Ui.BROWN);
        lblDrinkTitle.setFont(new Font("SansSerif", Font.BOLD, 40));
        header.add(lblDrinkTitle, BorderLayout.WEST);

        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setOpaque(false);
        closeBtn.setForeground(Ui.BROWN);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        closeBtn.addActionListener(e -> {
            resetSelections();
            ui.show("TEA_MENU");
        });
        JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeWrap.setOpaque(false);
        closeWrap.add(closeBtn);
        header.add(closeWrap, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== CONTENT =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(10, 20, 0, 20));

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), r = 18;
                g2.setColor(Ui.WHITE);
                g2.fillRoundRect(0, 0, w, h, r, r);
                g2.setColor(Ui.BROWN);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, r, r);
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Type
        JPanel typeRow = new JPanel(new BorderLayout());
        typeRow.setOpaque(false);
        typeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel typeTitle = makeTitle("TYPE", 28);
        typeTitle.setForeground(Ui.BROWN);
        typeRow.add(typeTitle, BorderLayout.WEST);

        hotToggle = new SegmentedTab("HOT --฿", true, false, true, false, 14);
        icedToggle = new SegmentedTab("ICED --฿", false, true, false, true, 14);
        hotToggle.setOpaque(false);
        icedToggle.setOpaque(false);
        hotToggle.setFont(new Font("SansSerif", Font.BOLD, 16));
        icedToggle.setFont(new Font("SansSerif", Font.BOLD, 16));
        hotToggle.setBorder(new EmptyBorder(0, 16, 0, 16));
        icedToggle.setBorder(new EmptyBorder(0, 16, 0, 16));
        typeGroup.add(hotToggle);
        typeGroup.add(icedToggle);

        JPanel typePill = new JPanel(new GridLayout(1, 2, 0, 0)) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), r = 22;
                g2.setColor(Ui.BROWN);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, r, r);
                int cx = (w - 2) / 2 + 1;
                g2.fillRect(cx, 2, 1, h - 4);
                g2.dispose();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), r = 22;
                g2.setColor(Ui.WHITE);
                g2.fillRoundRect(0, 0, w, h, r, r);
                g2.dispose();
            }
        };
        typePill.setOpaque(false);
        typePill.setPreferredSize(new Dimension(300, 44));
        typePill.add(hotToggle);
        typePill.add(icedToggle);

        JPanel typeWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typeWrap.setOpaque(false);
        typeWrap.add(typePill);
        typeRow.add(Box.createHorizontalStrut(16), BorderLayout.CENTER);
        typeRow.add(typeWrap, BorderLayout.EAST);
        content.add(typeRow);
        content.add(Box.createVerticalStrut(20));

        // Size
        JPanel sizeRow = new JPanel();
        sizeRow.setOpaque(false);
        sizeRow.setLayout(new BoxLayout(sizeRow, BoxLayout.X_AXIS));

        JLabel sizeLabel = new JLabel("SIZE");
        sizeLabel.setForeground(Ui.BROWN);
        sizeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        sizeLabel.setAlignmentY(0.5f);
        sizeRow.add(sizeLabel);
        sizeRow.add(Box.createHorizontalStrut(16));

        JPanel sizeBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        sizeBtns.setOpaque(false);
        sizeBtns.setAlignmentY(0.5f);
        sizeBtns.setBorder(new EmptyBorder(0, 70, 0, 0));

        ButtonGroup sizeGroup = new ButtonGroup();
        JToggleButton btnS = makeSizeIcon("Imgs/size_s.png", 48, 60, 50, 62);
        JToggleButton btnM = makeSizeIcon("Imgs/size_m.png", 56, 70, 58, 72);
        JToggleButton btnL = makeSizeIcon("Imgs/size_l.png", 64, 80, 66, 82);

        sizeGroup.add(btnS);
        sizeGroup.add(btnM);
        sizeGroup.add(btnL);
        btnM.setSelected(true);
        selectedSize = 0;

        ItemListener sizeChanged = e -> {
            if (((AbstractButton) e.getItem()).isSelected()) {
                Object src = e.getItemSelectable();
                if (src == btnS)
                    selectedSize = 0;
                else if (src == btnM)
                    selectedSize = 1;
                else if (src == btnL)
                    selectedSize = 2;
                recalcTotal();
            }
        };
        btnS.addItemListener(sizeChanged);
        btnM.addItemListener(sizeChanged);
        btnL.addItemListener(sizeChanged);

        sizeBtns.add(wrapBottom(btnS, 72, 96));
        sizeBtns.add(wrapBottom(btnM, 80, 96));
        sizeBtns.add(wrapBottom(btnL, 88, 96));
        sizeRow.add(sizeBtns);
        content.add(sizeRow);
        content.add(Box.createVerticalStrut(12));

        // Topping
        JLabel tTitle = makeTitle("TOPPING", 30);
        tTitle.setForeground(Ui.BROWN);
        tTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        tTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        content.add(tTitle);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        topRow.setOpaque(false);
        JToggleButton t1 = Ui.makeToppingToggle(IMG_TOPPING1, 140, 5, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t2 = Ui.makeToppingToggle(IMG_TOPPING2, 140, 10, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t3 = Ui.makeToppingToggle(IMG_TOPPING3, 140, 10, Ui.Orientation.TOP_BOTTOM);
        t1.setActionCommand("Black Pearl");
        t2.setActionCommand("Konjac Pearl");
        t3.setActionCommand("Golden Pearl");

        toppingButtons.clear();
        toppingButtons.add(t1);
        toppingButtons.add(t2);
        toppingButtons.add(t3);
        topRow.add(t1);
        topRow.add(t2);
        topRow.add(t3);
        content.add(topRow);
        content.add(Box.createVerticalStrut(20));

        // Sweetness
        JLabel sTitle = makeTitle("SWEETNESS", 30);
        sTitle.setForeground(Ui.BROWN);
        sTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        content.add(sTitle);

        JPanel sweetBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 19, 10));
        sweetBtns.setOpaque(false);

        JToggleButton bsw0 = Ui.makeTextToggle("0%", 100, 50, Ui.Orientation.LEFT_RIGHT);
        JToggleButton bsw50 = Ui.makeTextToggle("50%", 100, 50, Ui.Orientation.TOP_BOTTOM);
        JToggleButton bsw100 = Ui.makeTextToggle("100%", 100, 50, Ui.Orientation.TOP_BOTTOM);
        JToggleButton bsw120 = Ui.makeTextToggle("120%", 100, 50, Ui.Orientation.RIGHT_LEFT);

        sweetnessGroup.add(bsw0);
        sweetnessGroup.add(bsw50);
        sweetnessGroup.add(bsw100);
        sweetnessGroup.add(bsw120);
        sweetnessButtons.clear();
        sweetnessButtons.add(bsw0);
        sweetnessButtons.add(bsw50);
        sweetnessButtons.add(bsw100);
        sweetnessButtons.add(bsw120);

        sweetBtns.add(bsw0);
        sweetBtns.add(bsw50);
        sweetBtns.add(bsw100);
        sweetBtns.add(bsw120);
        content.add(sweetBtns);

        contentMargin.add(content, BorderLayout.CENTER);
        add(contentMargin, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(16, 20, 16, 20));
        add(bottom, BorderLayout.SOUTH);

        totalWrap = new JPanel(new GridLayout(1, 1));
        totalWrap.setOpaque(false);
        bottom.add(totalWrap, BorderLayout.CENTER);
        rebuildTotalButton(false);

        // ===== LISTENERS =====
        ItemListener addonListener = e -> recalcTotal();
        toppingButtons.forEach(b -> b.addItemListener(addonListener));
        sweetnessButtons.forEach(b -> b.addItemListener(addonListener));
        hotToggle.addItemListener(addonListener);
        icedToggle.addItemListener(addonListener);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshFromSelection();
            }
        });
    }

    // ===== HELPER =====
    private JLabel makeTitle(String text, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(Ui.BROWN_DARK);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private void refreshFromSelection() {
        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        lblDrinkTitle.setText(d.name);
        hotToggle.setText("HOT " + (int) d.hotPrice + "฿");
        hotToggle.setEnabled(true);
        if (d.icedAvailable) {
            icedToggle.setEnabled(true);
            icedToggle.setText("ICED " + (int) d.icedPrice + "฿");
        } else {
            icedToggle.setEnabled(false);
            icedToggle.setText("ICED");
        }

        if (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable) {
            typeGroup.setSelected(icedToggle.getModel(), true);
        } else {
            typeGroup.setSelected(hotToggle.getModel(), true);
            ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
        }

        toppingButtons.forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.forEach(b -> b.setSelected(false));

        recalcTotal();
    }

    // Cal
    private void recalcTotal() {
        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        boolean iced = icedToggle.isSelected() && d.icedAvailable;
        double base = iced ? d.icedPrice : d.hotPrice;

        PricingService ps = ui.getPricingService();
        String sizeCode = SIZE_CODES[selectedSize];

        // Toppings List
        java.util.List<String> tops = new java.util.ArrayList<>();
        for (AbstractButton b : toppingButtons) {
            if (b.isSelected() && b.getActionCommand() != null)
                tops.add(b.getActionCommand());
        }

        // Total Cal
        StrategyPattern.PricingContext ctx = new StrategyPattern.PricingContext(ui.getCurrentUser(),
                ui.getCurrentPoints());
        StrategyPattern.DiscountStrategy st = new StrategyPattern.DefaultPricingStrategy();
        int total = ps.calcTeaTotal(base, sizeCode, tops, st, ctx);

        if (lblTotal != null) {
            lblTotal.setText("Total : " + total + "฿");
            boolean ready = isSweetSelected() && total > 0;
            if (ready != totalSolid)
                rebuildTotalButton(ready);
        }
    }

    private boolean isSweetSelected() {
        return sweetnessButtons.stream().anyMatch(AbstractButton::isSelected);
    }

    private void handleTotalClick() {
        if (!isSweetSelected()) {
            JOptionPane.showMessageDialog(this, "Please select sweetness level", "Not Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ans = JOptionPane.showConfirmDialog(this, "Confirm to order this item?", "Confirm Order",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans != JOptionPane.YES_OPTION) {
            resetSelections();
            ui.show("TEA_MENU");
            return;
        }

        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        MaeveCoffeeUI.OrderSummary o = new MaeveCoffeeUI.OrderSummary();
        o.username = ui.getCurrentUser();
        o.pointsBefore = ui.getCurrentPoints();
        o.date = java.time.LocalDate.now();
        o.time = java.time.LocalTime.now();

        boolean icedOk = (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable);
        int base = (int) Math.round(icedOk ? d.icedPrice : d.hotPrice);
        o.items.add(new MaeveCoffeeUI.OrderItem(d.name + (icedOk ? " (Iced)" : " (Hot)"), 1, base));

        PricingService ps = ui.getPricingService();
        String sizeCode = SIZE_CODES[selectedSize];
        int sizePrice = ps.toInt(ps.getSizePrice(sizeCode));
        if (sizePrice > 0) {
            String sizeLabel = (selectedSize == 0 ? "Size S" : selectedSize == 1 ? "Size M" : "Size L");
            o.items.add(new MaeveCoffeeUI.OrderItem(sizeLabel, 1, sizePrice));
        }

        for (AbstractButton b : toppingButtons) {
            if (b.isSelected()) {
                String code = b.getActionCommand();
                int tp = ps.toInt(ps.getToppingPrice(code));
                String label = (String) b.getClientProperty("label");
                if (label == null || label.isBlank())
                    label = code;
                o.items.add(new MaeveCoffeeUI.OrderItem(label, 1, tp));
            }
        }

        ui.setLastOrder(o);
        ui.show("SUMMARY");
    }

    private void resetSelections() {
        toppingButtons.forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.forEach(b -> b.setSelected(false));
        typeGroup.clearSelection();
        selectedSize = 1;
        rebuildTotalButton(false);
        recalcTotal();
    }

    private void rebuildTotalButton(boolean solid) {
        if (totalWrap == null)
            return;
        String text = (lblTotal != null) ? lblTotal.getText() : "Total : 0฿";

        totalWrap.removeAll();
        totalSolid = solid;

        JButton newBtn = solid ? Ui.makePrimaryButton(text, 10, 56) : Ui.makeLightCapsuleButton(text, 10, 56);
        newBtn.setFont(newBtn.getFont().deriveFont(Font.BOLD, 18f));
        newBtn.addActionListener(e -> handleTotalClick());

        lblTotal = newBtn;
        totalWrap.add(lblTotal);
        totalWrap.revalidate();
        totalWrap.repaint();
    }

    // ===== UI HELPER =====
    private static Icon scaleIcon(String path, int w, int h) {
        ImageIcon raw = new ImageIcon(path);
        Image img = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private JToggleButton makeSizeIcon(String imgPath, int w, int h, int wSel, int hSel) {
        JToggleButton b = new JToggleButton();
        ImageIcon normal = (ImageIcon) scaleIcon(imgPath, w, h);
        ImageIcon larger = (ImageIcon) scaleIcon(imgPath, wSel, hSel);
        b.setIcon(normal);
        b.setSelectedIcon(larger);
        b.setRolloverIcon(larger);
        b.setRolloverSelectedIcon(larger);
        b.setPressedIcon(larger);
        b.setText("");
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setVerticalAlignment(SwingConstants.BOTTOM);
        b.setPreferredSize(new Dimension(wSel + 16, hSel + 16));
        b.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JComponent wrapBottom(AbstractButton inner, int w, int h) {
        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);
        box.setPreferredSize(new Dimension(w, h));
        box.add(inner, BorderLayout.SOUTH);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.setFocusable(false);
        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                inner.dispatchEvent(SwingUtilities.convertMouseEvent(box, e, inner));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                inner.dispatchEvent(SwingUtilities.convertMouseEvent(box, e, inner));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                inner.dispatchEvent(SwingUtilities.convertMouseEvent(box, e, inner));
            }
        });
        return box;
    }
}