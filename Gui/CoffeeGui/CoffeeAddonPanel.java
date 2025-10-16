package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.geom.Path2D;
import java.util.*;

import Gui.MainGui.*;
import Lib.*;
import Gui.CustomGui.*;

public class CoffeeAddonPanel extends JPanel {

    private static final String IMG_TOPPING1 = "Imgs/toppings/whipping_cream.png";
    private static final String IMG_TOPPING2 = "Imgs/toppings/chocolate.png";
    private static final String IMG_TOPPING3 = "Imgs/toppings/marshmallow.png";

    private final MaeveCoffeeUI ui;

    private JLabel lblDrinkTitle;
    private JButton lblTotal;
    private JLabel lblShotNote;
    private JLabel lblShotCount;
    private JPanel totalWrap;
    private boolean totalSolid = false;

    private final Set<JToggleButton> toppingButtons = new HashSet<>();
    private final Set<JToggleButton> sweetnessButtons = new HashSet<>();
    private final ButtonGroup sweetnessGroup = new ButtonGroup();

    private final ButtonGroup typeGroup = new ButtonGroup();
    private SegmentedTab hotToggle, icedToggle;

    private int extraShots = 0;

    public CoffeeAddonPanel(MaeveCoffeeUI ui) {
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
            ui.show("COFFEE_MENU");
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
        content.add(Box.createVerticalStrut(40));

        // Extra Shot
        JPanel shotRow = new JPanel(new BorderLayout());
        shotRow.setOpaque(false);
        shotRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel shotTitle = makeTitle("EXTRA SHOT", 30);
        shotTitle.setForeground(Ui.BROWN);
        shotRow.add(shotTitle, BorderLayout.WEST);

        JPanel shotBtns = new JPanel(new GridBagLayout());
        shotBtns.setOpaque(false);
        shotBtns.setMaximumSize(new Dimension(320, 44));

        JButton minus = createStepButton("-", true, false, 56, 44);
        JButton plus = createStepButton("+", false, true, 56, 44);

        lblShotCount = new JLabel("0", SwingConstants.CENTER);
        lblShotCount.setForeground(Ui.BROWN);
        lblShotCount.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel countMid = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Ui.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Ui.BROWN);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRect(1, 0, getWidth() - 2, getHeight() - 2);
                g2.dispose();
            }
        };

        countMid.setOpaque(false);
        countMid.setPreferredSize(new Dimension(88, 44));
        countMid.add(lblShotCount, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, -2, 0, 0);
        gbc.gridx = 0;
        gbc.weightx = 0;
        shotBtns.add(minus, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        shotBtns.add(countMid, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        shotBtns.add(plus, gbc);

        shotRow.add(Box.createHorizontalStrut(24), BorderLayout.CENTER);
        shotRow.add(shotBtns, BorderLayout.EAST);
        content.add(shotRow);

        lblShotNote = new JLabel("+ 5฿ / 1 Shot");
        lblShotNote.setForeground(Ui.BROWN);
        lblShotNote.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblShotNote.setBorder(new EmptyBorder(2, 0, 10, 0));
        shotRow.add(lblShotNote, BorderLayout.SOUTH);

        content.add(Box.createVerticalStrut(16));

        // Topping
        JLabel tTitle = makeTitle("TOPPING", 30);
        tTitle.setForeground(Ui.BROWN);
        tTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        tTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        content.add(tTitle);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        topRow.setOpaque(false);

        JToggleButton t1 = Ui.makeToppingToggle(IMG_TOPPING1, 140, 15, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t2 = Ui.makeToppingToggle(IMG_TOPPING2, 140, 10, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t3 = Ui.makeToppingToggle(IMG_TOPPING3, 140, 10, Ui.Orientation.TOP_BOTTOM);
        t1.setActionCommand("Whipping Cream");
        t2.setActionCommand("Chocolate");
        t3.setActionCommand("Marshmallow");


        toppingButtons.clear();
        toppingButtons.add(t1);
        toppingButtons.add(t2);
        toppingButtons.add(t3);
        topRow.add(t1);
        topRow.add(t2);
        topRow.add(t3);
        content.add(topRow);

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

        // Stepper
        minus.addActionListener(e -> {
            if (extraShots > 0) {
                extraShots--;
                ui.setExtraShots(extraShots);
                lblShotCount.setText(String.valueOf(extraShots));
                recalcTotal();
            } else {
                JOptionPane.showMessageDialog(this, "at least 0 shot", "Limit", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        plus.addActionListener(e -> {
            if (extraShots < 3) {
                extraShots++;
                ui.setExtraShots(extraShots);
                lblShotCount.setText(String.valueOf(extraShots));
                recalcTotal();
            } else {
                JOptionPane.showMessageDialog(this, "at most 3 shots", "Limit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

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
            icedToggle.setSelected(false);
            icedToggle.setText("ICED");

            typeGroup.setSelected(hotToggle.getModel(), true);
            hotToggle.setSelected(true);
            ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
        }

        if (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable) {
            typeGroup.setSelected(icedToggle.getModel(), true);
            icedToggle.setSelected(true);
            hotToggle.setSelected(false);
        } else {
            typeGroup.setSelected(hotToggle.getModel(), true);
            hotToggle.setSelected(true);
            icedToggle.setSelected(false);
            ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
        }

        // Shot Price
        lblShotNote.setText("+ " + (int) d.shotPrice + "฿ / 1 Shot");
        extraShots = Math.max(0, Math.min(3, ui.getExtraShots() == 0 ? 0 : ui.getExtraShots()));
        ui.setExtraShots(extraShots);
        lblShotCount.setText(String.valueOf(extraShots));

        // Reset elections
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

        // Toppings list
        java.util.List<String> tops = new java.util.ArrayList<>();
        for (AbstractButton b : toppingButtons) {
            if (b.isSelected() && b.getActionCommand() != null)
                tops.add(b.getActionCommand());
        }

        int shots = Math.max(0, ui.getExtraShots());

        // Total Cal
        StrategyPattern.PricingContext ctx = new StrategyPattern.PricingContext(ui.getCurrentUser(),
                ui.getCurrentPoints());
        StrategyPattern.DiscountStrategy st = new StrategyPattern.DefaultPricingStrategy();
        int total = ps.calcCoffeeTotal(base, shots, d.shotPrice, tops, st, ctx);

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
            ui.show("COFFEE_MENU");
            return;
        }

        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        MaeveCoffeeUI.OrderSummary o = new MaeveCoffeeUI.OrderSummary();
        o.username = (ui.getCurrentUser() == null ? "GUEST" : ui.getCurrentUser());
        o.pointsBefore = ui.getCurrentPoints();
        o.date = java.time.LocalDate.now();
        o.time = java.time.LocalTime.now();

        MaeveCoffeeUI.DrinkType t = ui.getSelectedType();
        boolean icedOk = (t != null && t == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable);
        int base = (int) Math.round(icedOk ? d.icedPrice : d.hotPrice);
        o.items.add(new MaeveCoffeeUI.OrderItem(d.name + (icedOk ? " (Iced)" : " (Hot)"), 1, base));

        int shots = ui.getExtraShots();
        if (shots > 0) {
            o.items.add(new MaeveCoffeeUI.OrderItem(shots + " Extra shot(+" + (int) d.shotPrice + "฿)", 1,
                    (int) Math.round(shots * d.shotPrice)));
        }

        PricingService ps = ui.getPricingService();
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

        o.pointsEarned = "GUEST".equalsIgnoreCase(o.username) ? 0 : 0;
        if (o.pointsEarned > 0) {
            ui.addPoints(o.pointsEarned);
        }

        ui.setLastOrder(o);
        ui.show("SUMMARY");
    }

    private void resetSelections() {
        toppingButtons.forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.forEach(b -> b.setSelected(false));
        typeGroup.clearSelection();
        ui.setExtraShots(0);
        extraShots = 0;
        lblShotCount.setText("0");
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
    private JButton createStepButton(String text, boolean roundLeft, boolean roundRight, int w, int h) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int ww = getWidth(), hh = getHeight(), r = Ui.ARC;
                Shape shape = makeSideRoundedRect(0, 0, ww - 1, hh - 1, r, roundLeft, roundRight);
                g2.setColor(Ui.BROWN);
                g2.fill(shape);
                g2.setColor(Ui.WHITE);
                FontMetrics fm = g2.getFontMetrics(getFont());
                g2.drawString(getText(), (ww - fm.stringWidth(getText())) / 2,
                        (hh - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };

        b.setPreferredSize(new Dimension(w, h));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private Shape makeSideRoundedRect(int x, int y, int w, int h, int r, boolean left, boolean right) {
        Path2D.Double p = new Path2D.Double();
        if (left)
            p.moveTo(x + r, y);
        else
            p.moveTo(x, y);
        if (right) {
            p.lineTo(x + w - r, y);
            p.quadTo(x + w, y, x + w, y + r);
        } else {
            p.lineTo(x + w, y);
        }
        if (right) {
            p.lineTo(x + w, y + h - r);
            p.quadTo(x + w, y + h, x + w - r, y + h);
        } else {
            p.lineTo(x + w, y + h);
        }
        if (left) {
            p.lineTo(x + r, y + h);
            p.quadTo(x, y + h, x, y + h - r);
        } else {
            p.lineTo(x, y + h);
        }
        if (left) {
            p.lineTo(x, y + r);
            p.quadTo(x, y, x + r, y);
        } else {
            p.lineTo(x, y);
        }
        p.closePath();
        return p;
    }
}