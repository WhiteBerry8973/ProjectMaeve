package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.List;

import Gui.MainGui.*;
import Gui.CustomGui.SegmentedTab;
import Gui.CustomGui.TopRoundedPanel;

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
        header.setForeground(Ui.TITLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 20, 6, 0));
        add(header, BorderLayout.NORTH);

        // ===== Content margin =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        // ===== Rounded Content =====
        Ui.BottomRoundedBorderPanel content = new Ui.BottomRoundedBorderPanel(Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        content.setLayout(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        // ===== Catalog Bar =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // White Strip Container
        TopRoundedPanel strip = new TopRoundedPanel(Ui.ARC);
        strip.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        strip.setBackground(Color.WHITE);
        strip.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Segments
        ButtonGroup catGroup = new ButtonGroup();

        SegmentedTab sCoffee = new SegmentedTab("Coffee", true, false, false, false, Ui.ARC);
        sCoffee.setFont(new Font("SansSerif", Font.BOLD, 20));
        sCoffee.setPreferredSize(new Dimension(165, 50));
        sCoffee.setSelected(true);
        catGroup.add(sCoffee);

        SegmentedTab sTea = new SegmentedTab("TEA", false, false, false, false, Ui.ARC);
        sTea.setFont(new Font("SansSerif", Font.BOLD, 20));
        sTea.setPreferredSize(new Dimension(165, 50));
        catGroup.add(sTea);

        SegmentedTab sMilk = new SegmentedTab("MILK", false, false, false, false, Ui.ARC);
        sMilk.setFont(new Font("SansSerif", Font.BOLD, 20));
        sMilk.setPreferredSize(new Dimension(165, 50));
        catGroup.add(sMilk);

        SegmentedTab sNext = new SegmentedTab("\u203A", false, true, false, false, Ui.ARC);
        sNext.setFont(new Font("SansSerif", Font.BOLD, 20));
        sNext.setPreferredSize(new Dimension(49, 50));
        catGroup.add(sNext);

        strip.add(sCoffee);
        strip.add(sTea);
        strip.add(sMilk);
        strip.add(sNext);

        topPanel.add(strip, BorderLayout.CENTER);

        contentMargin.add(topPanel, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);
        content.add(grid, BorderLayout.CENTER);

        // ===== Bottom =====
        bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(0, 0, 0, 0));
        content.add(bottom, BorderLayout.SOUTH);

        contentMargin.add(content, BorderLayout.CENTER);
        add(contentMargin, BorderLayout.CENTER);

        List<MaeveCoffeeUI.MenuDrink> list = ui.getCoffeeMenu();
        totalPages = Math.max(1, (int) Math.ceil(list.size() / (double) ITEMS_PER_PAGE));
        refreshGrid();
    }

    private void refreshGrid() {
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
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);

            JButton next = Ui.makePrimaryButton("NEXT", 120, 40);
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
