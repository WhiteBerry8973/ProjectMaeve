package Gui.CoffeeGui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ItemListener;
import java.util.*;

import Gui.MainGui.*;

public class CoffeeAddonPanel extends JPanel {

    private static final String IMG_TOPPING1 = "Imgs/toppings/whipping_cream.png";
    private static final String IMG_TOPPING2 = "Imgs/toppings/chocolate.png";
    private static final String IMG_TOPPING3 = "Imgs/toppings/marshmallow.png";

    private final MaeveCoffeeUI ui;

    private JLabel lblCoffeeName;
    private JLabel lblTotal;
    private JLabel lblShotNote;
    private JLabel lblShotCount;

    private final Set<JToggleButton> toppingButtons = new HashSet<>();
    private final Map<AbstractButton, Double> toppingPriceMap = new HashMap<>();

    private final Set<JToggleButton> sweetnessButtons = new HashSet<>();
    private final ButtonGroup sweetnessGroup = new ButtonGroup();

    private final ButtonGroup typeGroup = new ButtonGroup();
    private JToggleButton hotToggle, icedToggle;

    private JButton confirmBtn;

    private int extraShots = 0;

    public CoffeeAddonPanel(MaeveCoffeeUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(22, 20, 6, 20));

        JLabel title = new JLabel("ADDON");
        title.setForeground(Ui.TITLE);
        title.setFont(new Font("SansSerif", Font.BOLD, 52));

        lblCoffeeName = new JLabel(" ");
        lblCoffeeName.setForeground(Ui.TITLE);
        lblCoffeeName.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JPanel addonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        addonRow.setOpaque(false);
        addonRow.add(title);
        addonRow.add(lblCoffeeName);

        // total badge (ขวาบน)
        Ui.RoundedBorderPanel totalBadge = new Ui.RoundedBorderPanel(
                Ui.ITEM_FILL, Ui.ARC, 1f,
                Ui.ITEM_BORDER_TOP, Ui.ITEM_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        totalBadge.setOpaque(false);
        totalBadge.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        lblTotal = new JLabel("Total : 0฿");
        lblTotal.setForeground(Ui.TITLE);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setBorder(new EmptyBorder(20, 24, 5, 24));
        totalBadge.add(lblTotal);

        header.add(addonRow, BorderLayout.WEST);
        header.add(totalBadge, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== Content margin =====
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(10, 20, 20, 20));

        // ===== Rounded content =====
        Ui.RoundedBorderPanel content = new Ui.RoundedBorderPanel(
                Ui.PANEL_FILL, Ui.ARC, Ui.BORDER_STROKE,
                Ui.PANEL_BORDER_TOP, Ui.PANEL_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== TYPE =====
        JPanel typeRow = new JPanel(new BorderLayout());
        typeRow.setOpaque(false);
        typeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel typeTitle = makeTitle("TYPE", 22);
        typeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        typeRow.add(typeTitle, BorderLayout.WEST);

        JPanel typeBtns = new JPanel(new GridLayout(1, 2, 0, 0));
        typeBtns.setOpaque(false);
        typeBtns.setPreferredSize(new Dimension(270, 40));

        hotToggle = Ui.makeTextRadio("HOT --฿", 135, 40, Ui.Orientation.TOP_BOTTOM, typeGroup);
        icedToggle = Ui.makeTextRadio("ICED --฿", 135, 40, Ui.Orientation.TOP_BOTTOM, typeGroup);

        typeBtns.add(hotToggle);
        typeBtns.add(icedToggle);
        typeRow.add(typeBtns, BorderLayout.EAST);

        content.add(typeRow);
        content.add(Box.createVerticalStrut(20));

        // ===== EXTRA SHOT =====
        JPanel shotRow = new JPanel(new BorderLayout());
        shotRow.setOpaque(false);
        shotRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel shotTitle = makeTitle("EXTRA SHOT", 22);
        shotTitle.setHorizontalAlignment(SwingConstants.LEFT);
        shotRow.add(shotTitle, BorderLayout.WEST);

        JPanel shotBtns = new JPanel(new GridBagLayout());
        shotBtns.setOpaque(false);
        shotBtns.setMaximumSize(new Dimension(270, 40));

        JButton minus = Ui.makePrimaryButton("-", 50, 40);
        JButton plus = Ui.makePrimaryButton("+", 50, 40);
        lblShotCount = new JLabel("1", SwingConstants.CENTER);
        lblShotCount.setForeground(Ui.TITLE);
        lblShotCount.setFont(new Font("SansSerif", Font.BOLD, 18));

        Ui.RoundedBorderPanel countBg = new Ui.RoundedBorderPanel(
                Ui.ITEM_FILL, Ui.ARC, 1f,
                Ui.ITEM_BORDER_TOP, Ui.ITEM_BORDER_BOT, Ui.Orientation.TOP_BOTTOM);
        countBg.setLayout(new BorderLayout());
        countBg.add(lblShotCount, BorderLayout.CENTER);
        countBg.setPreferredSize(new Dimension(170, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.15;
        shotBtns.add(minus, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        shotBtns.add(countBg, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        shotBtns.add(plus, gbc);

        shotRow.add(shotBtns, BorderLayout.EAST);

        content.add(shotRow);
        lblShotNote = new JLabel("+ 5฿ / 1 Shot");
        lblShotNote.setForeground(new Color(150, 150, 150));
        lblShotNote.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblShotNote.setBorder(new EmptyBorder(2, 0, 10, 0));
        shotRow.add(lblShotNote, BorderLayout.SOUTH);
        content.add(Box.createVerticalStrut(10));

        // ===== TOPPING =====
        JLabel tTitle = makeTitle("TOPPING", 28);
        tTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        tTitle.setBorder(new EmptyBorder(0, 0, 30, 0));
        content.add(tTitle);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        topRow.setOpaque(false);
        // ราคา: 15 / 10 / 10 เหมือนเดิม
        JToggleButton t1 = Ui.makeToppingToggle(IMG_TOPPING1, 140, 15, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t2 = Ui.makeToppingToggle(IMG_TOPPING2, 140, 10, Ui.Orientation.TOP_BOTTOM);
        JToggleButton t3 = Ui.makeToppingToggle(IMG_TOPPING3, 140, 10, Ui.Orientation.TOP_BOTTOM);
        toppingButtons.clear();
        toppingButtons.add(t1);
        toppingButtons.add(t2);
        toppingButtons.add(t3);
        toppingPriceMap.put(t1, 15.0);
        toppingPriceMap.put(t2, 10.0);
        toppingPriceMap.put(t3, 10.0);
        topRow.add(t1);
        topRow.add(t2);
        topRow.add(t3);
        content.add(topRow);
        content.add(Box.createVerticalStrut(20));

        // ===== SWEETNESS =====
        JLabel sTitle = makeTitle("SWEETNESS", 28);
        sTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sTitle.setBorder(new EmptyBorder(0, 0, 30, 0));
        content.add(sTitle);

        JPanel sweetBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10));
        sweetBtns.setOpaque(false);

        JToggleButton bsw0 = Ui.makeTextToggle("0%", 76, 40, Ui.Orientation.LEFT_RIGHT);
        JToggleButton bsw50 = Ui.makeTextToggle("50%", 76, 40, Ui.Orientation.TOP_BOTTOM);
        JToggleButton bsw100 = Ui.makeTextToggle("100%", 76, 40, Ui.Orientation.TOP_BOTTOM);
        JToggleButton bsw120 = Ui.makeTextToggle("120%", 76, 40, Ui.Orientation.RIGHT_LEFT);

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

        // ===== Bottom (Confirm/Cancel) =====
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        confirmBtn = Ui.makeSecondaryButton("CONFIRM", 150, 44, Ui.Orientation.LEFT_RIGHT);
        confirmBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Please select Sweetness and Drink Type (Hot/Iced)",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
        });

        JButton cancel = Ui.makeSecondaryButton("CANCEL", 150, 44, Ui.Orientation.RIGHT_LEFT);
        cancel.addActionListener(e -> {
            resetSelections();
            ui.show("COFFEE_MENU");
        });

        bottomBar.add(confirmBtn);
        bottomBar.add(cancel);
        add(bottomBar, BorderLayout.SOUTH);

        // ===== Listeners =====
        ItemListener addonListener = e -> {
            updateAddonConfirmEnabled(bottomBar);
            updateTotalBadge();
        };
        toppingButtons.forEach(b -> b.addItemListener(addonListener));
        sweetnessButtons.forEach(b -> b.addItemListener(addonListener));

        hotToggle.addItemListener(addonListener);
        icedToggle.addItemListener(addonListener);

        minus.addActionListener(e -> {
            if (extraShots > 0) {
                extraShots--;
                ui.setExtraShots(extraShots);
                lblShotCount.setText(String.valueOf(extraShots));
                updateTotalBadge();
            } else {
                JOptionPane.showMessageDialog(this, "at least 0 shot", "Limit", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        plus.addActionListener(e -> {
            if (extraShots < 3) {
                extraShots++;
                ui.setExtraShots(extraShots);
                lblShotCount.setText(String.valueOf(extraShots));
                updateTotalBadge();
            } else {
                JOptionPane.showMessageDialog(this, "at most 3 shots", "Limit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // เมื่อ panel ถูกแสดง ให้ refresh จาก drink ที่เลือกไว้
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshFromSelection();
                updateAddonConfirmEnabled(bottomBar);
            }
        });
    }

    private JLabel makeTitle(String text, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(Ui.TITLE);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private void refreshFromSelection() {
        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        lblCoffeeName.setText(" " + d.name);

        hotToggle.setText("HOT " + (int) d.hotPrice + "฿");
        hotToggle.setEnabled(true);

        icedToggle.setText("ICED " + (int) d.icedPrice + "฿");
        icedToggle.setEnabled(d.icedAvailable);

        // default select HOT (หรือคงค่าจาก ui)
        if (ui.getSelectedType() == MaeveCoffeeUI.DrinkType.ICED && d.icedAvailable) {
            typeGroup.setSelected(icedToggle.getModel(), true);
        } else {
            typeGroup.setSelected(hotToggle.getModel(), true);
            ui.setSelectedType(MaeveCoffeeUI.DrinkType.HOT);
        }

        // โชว์ราคา shot/จำนวน shot
        lblShotNote.setText("+ " + (int) d.shotPrice + "฿ / 1 Shot");
        extraShots = Math.max(0, Math.min(3, ui.getExtraShots() == 0 ? 1 : ui.getExtraShots()));
        ui.setExtraShots(extraShots);
        lblShotCount.setText(String.valueOf(extraShots));

        // clear topping/sweetness เมื่อเข้าหน้านี้ใหม่
        toppingButtons.forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.forEach(b -> b.setSelected(false));

        updateTotalBadge();
    }

    private void updateAddonConfirmEnabled(JPanel bottomBar) {
        boolean typeSelected = hotToggle.isSelected() || icedToggle.isSelected();
        boolean sweetSelected = sweetnessButtons.stream().anyMatch(AbstractButton::isSelected);

        int idx = bottomBar.getComponentZOrder(confirmBtn);
        bottomBar.remove(confirmBtn);

        if (typeSelected && sweetSelected) {
            // ปุ่ม primary + ไป payment
            confirmBtn = Ui.makePrimaryButton("CONFIRM", 150, 44);
            confirmBtn.addActionListener(ev -> {
                // keep selections; ไปหน้า payment
                ui.show("COFFEE_PAYMENT");
            });
        } else {
            // ปุ่ม secondary + เตือน
            confirmBtn = Ui.makeSecondaryButton("CONFIRM", 150, 44, Ui.Orientation.LEFT_RIGHT);
            confirmBtn.addActionListener(ev -> {
                JOptionPane.showMessageDialog(this,
                        "Please select Sweetness and Drink Type (Hot/Iced)",
                        "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
            });
        }

        bottomBar.add(confirmBtn, idx);
        bottomBar.revalidate();
        bottomBar.repaint();
    }

    private void updateTotalBadge() {
        MaeveCoffeeUI.MenuDrink d = ui.getSelectedDrink();
        if (d == null)
            return;

        double base = (icedToggle.isSelected() && d.icedAvailable) ? d.icedPrice : d.hotPrice;
        ui.setSelectedType(icedToggle.isSelected() && d.icedAvailable
                ? MaeveCoffeeUI.DrinkType.ICED
                : MaeveCoffeeUI.DrinkType.HOT);

        double shotSum = ui.getExtraShots() * d.shotPrice;
        double topSum = toppingButtons.stream()
                .filter(AbstractButton::isSelected)
                .mapToDouble(b -> toppingPriceMap.getOrDefault(b, 0.0))
                .sum();

        double total = base + shotSum + topSum;
        lblTotal.setText("Total : " + (int) total + "฿");
    }

    private void resetSelections() {
        toppingButtons.forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.forEach(b -> b.setSelected(false));
        typeGroup.clearSelection();
        ui.setExtraShots(0);
        extraShots = 0;
        lblShotCount.setText("0");
        updateTotalBadge();
    }
}
