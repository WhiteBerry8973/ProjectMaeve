package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.List;

import Gui.MainGui.*;

public class CoffeeMenuPanel extends JPanel {

    private final MaeveCoffeeUI ui;

    private static final int ITEMS_PER_PAGE = 4;
    private int pageIndex = 0;
    private int totalPages = 1;

    private JPanel grid;
    private JLabel title;
    private JPanel bottom;

    public CoffeeMenuPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Header =====
        JLabel header = new JLabel("MAEVE COFFEE", SwingConstants.LEFT);
        header.setForeground(Ui.TITLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 20, 6, 0));
        add(header, BorderLayout.NORTH);

        // ===== Content margin =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        // ===== Rounded content =====
        Ui.RoundedBorderPanel content = new Ui.RoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        content.setLayout(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        title = new JLabel("MENU", SwingConstants.CENTER);
        title.setForeground(Ui.TITLE);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setBorder(new EmptyBorder(6, 0, 10, 0));
        content.add(title, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);
        content.add(grid, BorderLayout.CENTER);

        // ===== Bottom nav (เก็บเป็นฟิลด์) =====
        bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 0, 0, 0));
        content.add(bottom, BorderLayout.SOUTH);

        contentMargin.add(content, BorderLayout.CENTER);
        add(contentMargin, BorderLayout.CENTER);

        // init
        List<MaeveCoffeeUI.MenuDrink> list = ui.getCoffeeMenu();
        totalPages = Math.max(1, (int) Math.ceil(list.size() / (double) ITEMS_PER_PAGE));
        refreshGrid();
    }

    private void refreshGrid() {
        grid.removeAll();
        bottom.removeAll(); // reset ปุ่มทุกครั้ง

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

        // placeholders
        for (int k = end; k < start + ITEMS_PER_PAGE; k++) {
            JPanel placeholder = new JPanel();
            placeholder.setOpaque(false);
            placeholder.setPreferredSize(new Dimension(Ui.SQUARE_ITEM, Ui.SQUARE_ITEM));
            grid.add(placeholder);
        }

        // ปุ่ม PREVIOUS ถ้าไม่ใช่หน้าแรก
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

        // ปุ่ม NEXT ถ้าไม่ใช่หน้าสุดท้าย
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
