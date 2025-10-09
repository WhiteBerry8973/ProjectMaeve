package Gui.MainGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import Gui.MainGui.MaeveCoffeeUI;
import Gui.CustomGui.SegmentedTab;

public class MenuCatalogPanel extends JPanel {

    public enum Catalog {
        COFFEE, TEA, MILK
    }

    public static final String CARD_COFFEE = "COFFEE_MENU";
    public static final String CARD_TEA = "TEA_MENU";
    public static final String CARD_MILK = "MILK_MENU";

    private final MaeveCoffeeUI ui;
    private final Catalog catalog;

    private static final int ITEMS_PER_PAGE = 4;
    private int pageIndex = 0;
    private int totalPages = 1;

    private JPanel grid;
    private JPanel bottomOuter;

    public MenuCatalogPanel(MaeveCoffeeUI ui, Catalog catalog) {
        this.ui = ui;
        this.catalog = catalog;

        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Header =====
        JLabel header = new JLabel("MENU", SwingConstants.LEFT);
        header.setForeground(Ui.TITLE_DARK);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));

        JButton signInBtn = Ui.makeLightCapsuleButton("SIGN IN", 110, 40);

        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(false);
        headerBar.setBorder(new EmptyBorder(14, 20, 0, 20));

        headerBar.add(header, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(14, 0, 0, 0));
        right.add(signInBtn);

        headerBar.add(right, BorderLayout.EAST);

        add(headerBar, BorderLayout.NORTH);

        signInBtn.addActionListener(e -> ui.show("LOGIN"));

        // ===== Content =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(15, 20, 8, 20));
        add(contentMargin, BorderLayout.CENTER);

        // ===== Catalog Bar =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        final int TAB_H = 60;
        final int ARC = Ui.ARC;
        final int PAD = 1;
        final float STK = 2f;

        JPanel stripBox = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 1, h = TAB_H - 1;

                g2.setColor(Ui.BG);
                g2.fillRect(0, 0, getWidth(), TAB_H);

                Path2D p = new Path2D.Double();
                p.moveTo(ARC, 0);
                p.quadTo(0, 0, 0, ARC);
                p.lineTo(0, h);
                p.lineTo(w, h);
                p.lineTo(w, ARC);
                p.quadTo(w, 0, w - ARC, 0);
                p.closePath();

                g2.setStroke(new BasicStroke(STK));
                g2.setColor(Ui.TITLE_DARK);
                g2.draw(p);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TAB_H);
            }
        };
        stripBox.setOpaque(false);

        JPanel tabsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsRow.setOpaque(false);
        tabsRow.setBorder(new EmptyBorder(PAD, PAD, PAD, PAD));
        stripBox.add(tabsRow, BorderLayout.CENTER);

        ButtonGroup catGroup = new ButtonGroup();

        SegmentedTab sCoffee = new SegmentedTab("COFFEE",
                true, false, false, false, ARC);
        sCoffee.setFont(new Font("SansSerif", Font.BOLD, 20));
        sCoffee.setPreferredSize(new Dimension(180, TAB_H - PAD * 2));
        sCoffee.setSelected(true);
        catGroup.add(sCoffee);

        SegmentedTab sTea = new SegmentedTab("TEA",
                false, false, false, false, ARC);
        sTea.setFont(new Font("SansSerif", Font.BOLD, 20));
        sTea.setPreferredSize(new Dimension(180, TAB_H - PAD * 2));
        catGroup.add(sTea);

        SegmentedTab sMilk = new SegmentedTab("MILK",
                false, true, false, false, ARC);
        sMilk.setFont(new Font("SansSerif", Font.BOLD, 20));
        sMilk.setPreferredSize(new Dimension(180, TAB_H - PAD * 2));
        catGroup.add(sMilk);

        sCoffee.setSelected(catalog == Catalog.COFFEE);
        sTea.setSelected(catalog == Catalog.TEA);
        sMilk.setSelected(catalog == Catalog.MILK);

        sCoffee.addActionListener(e -> ui.show(CARD_COFFEE));
        sTea.addActionListener(e -> ui.show(CARD_TEA));
        sMilk.addActionListener(e -> ui.show(CARD_MILK));

        tabsRow.add(sCoffee);
        tabsRow.add(makeDividerCrop());
        tabsRow.add(sTea);
        tabsRow.add(makeDividerCrop());
        tabsRow.add(sMilk);

        topPanel.add(stripBox, BorderLayout.WEST);
        topPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentMargin.add(topPanel, BorderLayout.NORTH);

        // ===== Menu =====
        JPanel whiteCard = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

                int w = getWidth();
                int h = getHeight();
                int r = 18;

                Path2D bg = new Path2D.Double();
                bg.moveTo(0, 0);
                bg.lineTo(w, 0);
                bg.lineTo(w, h - r);
                bg.quadTo(w, h, w - r, h);
                bg.lineTo(r, h);
                bg.quadTo(0, h, 0, h - r);
                bg.closePath();

                g2.setColor(Ui.BG);
                g2.fill(bg);

                g2.translate(0.5, 0.5);
                Path2D border = new Path2D.Double();
                border.moveTo(0, 0);
                border.lineTo(w - 1, 0);
                border.lineTo(w - 1, h - 1 - r);
                border.quadTo(w - 1, h - 1, w - 1 - r, h - 1);
                border.lineTo(0 + r, h - 1);
                border.quadTo(0, h - 1, 0, h - 1 - r);
                border.closePath();

                g2.setColor(Ui.TITLE_DARK);
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(border);

                g2.dispose();
            }
        };

        whiteCard.setOpaque(false);
        whiteCard.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentMargin.add(whiteCard, BorderLayout.CENTER);

        grid = new JPanel(new GridLayout(2, 2, 40, 36));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(12, 16, 16, 16));
        whiteCard.add(grid, BorderLayout.CENTER);

        bottomOuter = new JPanel(new BorderLayout());
        bottomOuter.setOpaque(false);
        bottomOuter.setBorder(new EmptyBorder(8, 15, 16, 15));
        add(bottomOuter, BorderLayout.SOUTH);

        List<MaeveCoffeeUI.MenuDrink> list = ui.getMenuByCatalog(catalog);
        totalPages = Math.max(1, (int) Math.ceil(list.size() / (double) ITEMS_PER_PAGE));

        refreshGrid();
    }

    private JPanel makeDividerCrop() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(1, 60));
        line.setBackground(Ui.TITLE_DARK);
        wrap.add(line, BorderLayout.SOUTH);
        return wrap;
    }

    private void refreshGrid() {
        if (grid == null || bottomOuter == null)
            return;

        grid.removeAll();
        bottomOuter.removeAll();

        List<MaeveCoffeeUI.MenuDrink> list = ui.getMenuByCatalog(catalog);
        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, list.size());

        for (int i = start; i < end; i++) {
            grid.add(makePlainMenuItem(list.get(i)));
        }
        for (int k = end; k < start + ITEMS_PER_PAGE; k++) {
            JPanel placeholder = new JPanel();
            placeholder.setOpaque(false);
            grid.add(placeholder);
        }

        if (totalPages <= 1) {
            bottomOuter.add(makeFullWidthButton(Ui.makeLightCapsuleButton("Next", 100, 54), false),
                    BorderLayout.CENTER);
        } else if (pageIndex == 0) {
            JButton next = Ui.makeLightCapsuleButton("Next", 150, 54);
            next.addActionListener(e -> {
                pageIndex++;
                refreshGrid();
            });
            bottomOuter.add(makeFullWidthButton(next, true), BorderLayout.CENTER);
        } else if (pageIndex == totalPages - 1) {
            JButton prev = Ui.makeLightCapsuleButton("Previous", 100, 54);
            prev.addActionListener(e -> {
                pageIndex--;
                refreshGrid();
            });
            bottomOuter.add(makeFullWidthButton(prev, true), BorderLayout.CENTER);
        } else {
            JPanel two = new JPanel(new GridLayout(1, 2, 24, 0));
            two.setOpaque(false);

            JButton prev = Ui.makeLightCapsuleButton("Previous", 100, 54);
            JButton next = Ui.makeLightCapsuleButton("Next", 100, 54);
            prev.addActionListener(e -> {
                pageIndex--;
                refreshGrid();
            });
            next.addActionListener(e -> {
                pageIndex++;
                refreshGrid();
            });

            two.add(prev);
            two.add(next);

            bottomOuter.add(two, BorderLayout.CENTER);
        }

        grid.revalidate();
        grid.repaint();
        bottomOuter.revalidate();
        bottomOuter.repaint();
    }

    private JComponent makeFullWidthButton(JButton btn, boolean enabled) {
        btn.setEnabled(enabled);
        btn.setPreferredSize(new Dimension(0, 54));
        btn.setMinimumSize(new Dimension(0, 54));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(0, 6, 0, 6));
        box.add(btn, BorderLayout.CENTER);
        return box;
    }

    private JComponent makePlainMenuItem(MaeveCoffeeUI.MenuDrink d) {
        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);

        JLabel pic = new JLabel();
        pic.setHorizontalAlignment(SwingConstants.CENTER);
        pic.setVerticalAlignment(SwingConstants.BOTTOM);
        pic.setPreferredSize(new Dimension(200, 170));
        if (d.imagePath != null && !d.imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(d.imagePath);
            Image scaled = icon.getImage().getScaledInstance(180, 140, Image.SCALE_SMOOTH);
            pic.setIcon(new ImageIcon(scaled));
        }
        box.add(pic, BorderLayout.CENTER);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(d.name);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setForeground(Ui.TITLE_DARK);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 20f));

        double price = (d.hotPrice > 0) ? d.hotPrice : d.icedPrice;
        JLabel priceLbl = new JLabel(((int) price) + "B");
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLbl.setForeground(Ui.TITLE_DARK);
        priceLbl.setFont(priceLbl.getFont().deriveFont(Font.BOLD, 17f));

        text.add(Box.createVerticalStrut(8));
        text.add(name);
        text.add(Box.createVerticalStrut(6));
        text.add(priceLbl);

        box.add(text, BorderLayout.SOUTH);

        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ui.setSelectedDrink(d);
                ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
                ui.setExtraShots(0);
                ui.show("COFFEE_ADDON");
            }
        });

        return box;
    }
}
