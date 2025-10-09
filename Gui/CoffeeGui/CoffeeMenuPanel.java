package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

import Gui.MainGui.*;
import Gui.CustomGui.SegmentedTab;

public class CoffeeMenuPanel extends JPanel {

    private final MaeveCoffeeUI ui;

    private static final int ITEMS_PER_PAGE = 4;
    private int pageIndex = 0;
    private int totalPages = 1;

    private JPanel grid;
    private JPanel bottom;

    public CoffeeMenuPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Header =====
        JLabel header = new JLabel("MENU", SwingConstants.LEFT);
        header.setForeground(Ui.TITLE_DARK);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 20, 0, 0));
        add(header, BorderLayout.NORTH);

        // ===== Content margin =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(15, 20, 20, 20));

        // ===== Rounded Content =====
        Ui.BottomRoundedBorderPanel content = new Ui.BottomRoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, 0,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        content.setLayout(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // ===== Catalog Bar (เวอร์ชันโค้งรายปุ่ม + กรอบรอบนอก) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // ขนาด/มุม/ระยะ
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
        Color selectedBg = new Color(0xcfc8b4); // ปรับตามใจ
        Color selectedText = Ui.C_BG;
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
        sMilk.setPreferredSize(new Dimension(179, TAB_H - PAD * 2));
        catGroup.add(sMilk);

        tabsRow.add(sCoffee);
        tabsRow.add(makeDividerCrop(PAD));
        tabsRow.add(sTea);
        tabsRow.add(makeDividerCrop(PAD));
        tabsRow.add(sMilk);

        topPanel.add(stripBox, BorderLayout.WEST);
        topPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentMargin.add(topPanel, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);
        content.add(grid, BorderLayout.CENTER);

        bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 1, 0, 0));
        content.add(bottom, BorderLayout.SOUTH);

        contentMargin.add(content, BorderLayout.CENTER);
        add(contentMargin, BorderLayout.CENTER);

        List<MaeveCoffeeUI.MenuDrink> list = ui.getCoffeeMenu();
        totalPages = Math.max(1, (int) Math.ceil(list.size() / (double) ITEMS_PER_PAGE));

        refreshGrid();
    }

    private JPanel makeDividerCrop(int topPad) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(1, 60 - 0));
        line.setBackground(Ui.TITLE_DARK);
        wrap.add(line, BorderLayout.SOUTH);
        return wrap;
    }

    private void refreshGrid() {
        if (grid == null || bottom == null)
            return;

        grid.removeAll();
        bottom.removeAll();

        List<MaeveCoffeeUI.MenuDrink> list = ui.getCoffeeMenu();
        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, list.size());

        for (int i = start; i < end; i++) {
            MaeveCoffeeUI.MenuDrink mc = list.get(i);
            JButton btn = Ui.makeMenuSquare(
                    mc.imagePath, mc.name, Ui.SQUARE_ITEM,
                    () -> {
                        ui.setSelectedDrink(mc);
                        ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
                        ui.setExtraShots(0);
                        ui.show("COFFEE_ADDON");
                    });
            grid.add(btn);
        }

        for (int k = end; k < start + ITEMS_PER_PAGE; k++) {
            JPanel placeholder = new JPanel();
            placeholder.setOpaque(false);
            placeholder.setPreferredSize(new Dimension(Ui.SQUARE_ITEM, Ui.SQUARE_ITEM));
            grid.add(placeholder);
        }

        if (pageIndex > 0) {
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            left.setOpaque(false);

            JButton prev = Ui.makePrimaryButton("PREVIOUS", 120, 40);
            prev.addActionListener(e -> {
                pageIndex--;
                refreshGrid();
            });

            left.add(prev);
            bottom.add(left, BorderLayout.WEST);
        }

        if (pageIndex < totalPages - 1) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
            right.setOpaque(false);

            JButton next = Ui.makePrimaryButton("NEXT", 120, 50);
            next.addActionListener(e -> {
                pageIndex++;
                refreshGrid();
            });

            right.add(next);
            bottom.add(right, BorderLayout.EAST);
        }

        grid.revalidate();
        grid.repaint();
        bottom.revalidate();
        bottom.repaint();
    }
}
