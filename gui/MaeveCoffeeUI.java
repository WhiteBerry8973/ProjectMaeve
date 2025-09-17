package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Maeve Coffee UI (single file, BorderLayout + EmptyBorder)
 *
 * Color Code:
 * - Background: #1e1e1e
 * - Title: #d9d9d9
 * - Panel: #2a2a2a (border gradient #333333 -> #2a2a2a)
 * - Item: #333333 (border gradient #504e4e -> #333333)
 * - Selected: #1e1e1e
 * - Primary: #d9d9d9 (text #1e1e1e)
 * - Hover Primary: #1e1e1e
 * - Secondary: #333333 (border gradient #504e4e -> #333333, text #d9d9d9)
 * - Hover Secondary: #d9d9d9 (text #1e1e1e)
 * - Disabled: #333333 (text #d9d9d9)
 * - Hover Primary: #1e1e1e
 */
public class MaeveCoffeeUI {

    private static final String imagePath_topping1 = "gui/americano.png";
    private static final String imagePath_topping2 = "gui/americano.png";
    private static final String imagePath_topping3 = "gui/americano.png";
    private static final String imagePath_topping4 = "gui/americano.png";
    private static final String imagePath_topping5 = "gui/americano.png";
    private static final String imagePath_topping6 = "gui/americano.png";

    private static final String imagePath_payment_card = "gui/americano.png";
    private static final String imagePath_payment_paypal = "gui/americano.png";

    // ======= Colors =======
    private static final Color BG = hex("#1e1e1e");
    private static final Color TITLE = hex("#d9d9d9");

    // PANEL:
    private static final Color PANEL_FILL = hex("#2a2a2a");
    private static final Color PANEL_BORDER_TOP = hex("#333333");
    private static final Color PANEL_BORDER_BOT = hex("#2a2a2a");

    // ITEM:
    private static final Color ITEM_FILL = hex("#333333");
    private static final Color ITEM_BORDER_TOP = hex("#504e4e");
    private static final Color ITEM_BORDER_BOT = hex("#333333");

    // HOVER/SELECTED:
    private static final Color SEL_FILL = hex("#1e1e1e");

    // PRIMARY (NEXT/CONFIRM)
    private static final Color PRIMARY_FILL = hex("#d9d9d9");
    private static final Color PRIMARY_TEXT = hex("#1e1e1e");

    // SECONDARY (PREVIOUS/CANCEL)
    private static final Color SECONDARY_FILL = hex("#333333");
    private static final Color SECONDARY_TEXT = TITLE;
    private static final Color SECONDARY_LEFT = hex("#504e4e");
    private static final Color SECONDARY_RIGHT = hex("#333333");

    private static final int ARC = 18;
    private static final float BORDER_STROKE = 2f;

    // SQUARE
    private static final int SQUARE_ITEM = 140;
    private static final int SQUARE_TOPPING = 96;
    private static final int SQUARE_PM = 120;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    private List<MenuCoffee> menuItems = new ArrayList<>();
    private int totalMenuPages = 1;
    private static final int MENU_ITEMS_PER_PAGE = 4;

    // Enable/Disable Confirm
    private final Set<JToggleButton> toppingButtons = new HashSet<>();
    private final Set<JToggleButton> sweetnessButtons = new HashSet<>();
    private final Set<JToggleButton> paymentMethodButtons = new HashSet<>();
    private final Set<JToggleButton> currencyButtons = new HashSet<>();

    private final ButtonGroup sweetnessGroup = new ButtonGroup();
    private final ButtonGroup paymentGroup = new ButtonGroup();
    private final ButtonGroup currencyGroup = new ButtonGroup();

    private JButton addonConfirmBtn;
    private JButton paymentConfirmBtn;

    private static RoundedButton.Orientation toRB(Orientation o) {
        switch (o) {
            case LEFT_RIGHT:
                return RoundedButton.Orientation.LEFT_RIGHT;
            case RIGHT_LEFT:
                return RoundedButton.Orientation.RIGHT_LEFT;
            default:
                return RoundedButton.Orientation.TOP_BOTTOM;
        }
    }

    private static RoundedToggleButton.Orientation toRT(Orientation o) {
        switch (o) {
            case LEFT_RIGHT:
                return RoundedToggleButton.Orientation.LEFT_RIGHT;
            case RIGHT_LEFT:
                return RoundedToggleButton.Orientation.RIGHT_LEFT;
            default:
                return RoundedToggleButton.Orientation.TOP_BOTTOM;
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Maeve Coffee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 820);
        frame.setResizable(false);
        frame.getContentPane().setBackground(BG);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setOpaque(false);

        MenuFromCSV("./gui/coffee_menus.csv");
        for (int p = 0; p < totalMenuPages; p++) {
            cards.add(buildMenuPage(p), "MENU" + (p + 1));
        }

        cards.add(buildAddonPage(), "ADDON");
        cards.add(buildPaymentPage(), "PAYMENT");

        frame.add(cards, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ============================ Jframes ============================

    // JframeCreator
    private void MenuFromCSV(String csvPath) {
        this.menuItems = loadMenuFromCSV(csvPath);
        if (this.menuItems == null)
            this.menuItems = new ArrayList<>();
        this.totalMenuPages = Math.max(1, (int) Math.ceil(this.menuItems.size() / (double) MENU_ITEMS_PER_PAGE));
    }

    private static List<MenuCoffee> loadMenuFromCSV(String filePath) {
        List<MenuCoffee> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    String desc = parts[2].trim();
                    String img = parts[3].trim();
                    list.add(new MenuCoffee(name, price, desc, img));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static class MenuCoffee {
        final String name;
        final double price;
        final String description;
        final String imagePath;

        MenuCoffee(String n, double p, String d, String img) {
            this.name = n;
            this.price = p;
            this.description = d;
            this.imagePath = img;
        }
    }

    private JPanel buildMenuPage(int pageIndex) {
        JPanel page = createHeaderOnlyPage("MAEVE COFFEE");

        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        RoundedBorderPanel content = new RoundedBorderPanel(PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP,
                PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        content.add(makeTitle("MENU", 36), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);

        int start = pageIndex * MENU_ITEMS_PER_PAGE;
        int end = Math.min(start + MENU_ITEMS_PER_PAGE, menuItems.size());

        for (int i = start; i < end; i++) {
            MenuCoffee mc = menuItems.get(i);
            grid.add(makeMenuSquare(mc.imagePath, mc.name, SQUARE_ITEM, () -> {
                show("ADDON");
            }));
        }

        for (int k = end; k < start + MENU_ITEMS_PER_PAGE; k++) {
            JPanel placeholder = new JPanel();
            placeholder.setOpaque(false);
            placeholder.setPreferredSize(new Dimension(SQUARE_ITEM, SQUARE_ITEM));
            grid.add(placeholder);
        }

        content.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 0, 0, 0));

        if (pageIndex > 0) {
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            left.setOpaque(false);
            JButton prev = makePrimaryButton("PREVIOUS", 120, 40);
            prev.addActionListener(e -> show("MENU" + (pageIndex)));
            left.add(prev);
            bottom.add(left, BorderLayout.WEST);
        }

        if (pageIndex < totalMenuPages - 1) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            JButton next = makePrimaryButton("NEXT", 120, 40);
            next.addActionListener(e -> show("MENU" + (pageIndex + 2)));
            right.add(next);
            bottom.add(right, BorderLayout.EAST);
        }

        content.add(bottom, BorderLayout.SOUTH);
        contentMargin.add(content, BorderLayout.CENTER);
        page.add(contentMargin, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildAddonPage() {
        JPanel page = createHeaderOnlyPage("ADDON");

        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        RoundedBorderPanel content = new RoundedBorderPanel(PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP,
                PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentMargin.add(content, BorderLayout.CENTER);

        // ===== TOPPINGS =====
        content.add(makeTitle("TOPPINGS", 28), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);

        JToggleButton t1 = makeImageSquareToggle(imagePath_topping1, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t2 = makeImageSquareToggle(imagePath_topping2, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t3 = makeImageSquareToggle(imagePath_topping3, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t4 = makeImageSquareToggle(imagePath_topping4, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t5 = makeImageSquareToggle(imagePath_topping5, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t6 = makeImageSquareToggle(imagePath_topping6, SQUARE_TOPPING, Orientation.TOP_BOTTOM);

        toppingButtons.add(t1);
        toppingButtons.add(t2);
        toppingButtons.add(t3);
        toppingButtons.add(t4);
        toppingButtons.add(t5);
        toppingButtons.add(t6);

        grid.add(t1);
        grid.add(t2);
        grid.add(t3);
        grid.add(t4);
        grid.add(t5);
        grid.add(t6);

        content.add(grid, BorderLayout.CENTER);

        // ===== SWEETNESS =====
        JPanel sweetWrap = new JPanel(new BorderLayout());
        sweetWrap.setOpaque(false);
        JLabel sTitle = makeTitle("SWEETNESS", 28);
        sTitle.setBorder(new EmptyBorder(18, 0, 8, 0));
        sweetWrap.add(sTitle, BorderLayout.NORTH);

        JPanel sweetBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
        sweetBtns.setOpaque(false);

        JToggleButton bsw0 = makeTextToggle("0%", 76, 40, Orientation.LEFT_RIGHT);
        JToggleButton bsw50 = makeTextToggle("50%", 76, 40, Orientation.TOP_BOTTOM);
        JToggleButton bsw100 = makeTextToggle("100%", 76, 40, Orientation.TOP_BOTTOM);
        JToggleButton bsw120 = makeTextToggle("120%", 76, 40, Orientation.RIGHT_LEFT);

        sweetnessGroup.add(bsw0);
        sweetnessGroup.add(bsw50);
        sweetnessGroup.add(bsw100);
        sweetnessGroup.add(bsw120);

        sweetnessButtons.add(bsw0);
        sweetnessButtons.add(bsw50);
        sweetnessButtons.add(bsw100);
        sweetnessButtons.add(bsw120);

        sweetBtns.add(bsw0);
        sweetBtns.add(bsw50);
        sweetBtns.add(bsw100);
        sweetBtns.add(bsw120);

        sweetWrap.add(sweetBtns, BorderLayout.CENTER);
        content.add(sweetWrap, BorderLayout.SOUTH);

        // CONFIRM, CANCEL
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        addonConfirmBtn = makeSecondaryButton("CONFIRM", 150, 44, Orientation.LEFT_RIGHT);
        addonConfirmBtn.addActionListener(e -> {
            boolean toppingSelected = toppingButtons.stream().anyMatch(AbstractButton::isSelected);
            boolean sweetSelected = sweetnessButtons.stream().anyMatch(AbstractButton::isSelected);
            if (!(toppingSelected && sweetSelected)) {
                JOptionPane.showMessageDialog(frame, "Please select Topping and Sweetness completely",
                        "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
            } else {
                show("PAYMENT");
                resetAddonSelections();
            }
        });

        JButton cancel = makeSecondaryButton("CANCEL", 150, 44, Orientation.RIGHT_LEFT);
        cancel.addActionListener(e -> {
            resetPaymentSelections();
            resetAddonSelections();
            updateAddonConfirmEnabled();
            show("MENU1");
        });

        bottomBar.add(addonConfirmBtn);
        bottomBar.add(cancel);

        ItemListener addonListener = e -> updateAddonConfirmEnabled();
        toppingButtons.forEach(b -> b.addItemListener(addonListener));
        sweetnessButtons.forEach(b -> b.addItemListener(addonListener));

        page.add(contentMargin, BorderLayout.CENTER);
        page.add(bottomBar, BorderLayout.SOUTH);
        return page;

    }

    private JPanel buildPaymentPage() {
        JPanel page = createHeaderOnlyPage("PAYMENT");

        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        RoundedBorderPanel content = new RoundedBorderPanel(PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP,
                PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentMargin.add(content, BorderLayout.CENTER);

        // ===== PAYMENT METHODS =====
        content.add(makeTitle("PAYMENT METHOD", 28), BorderLayout.NORTH);

        JPanel pmGrid = new JPanel(new GridLayout(2, 2, 18, 18));
        pmGrid.setOpaque(false);

        JToggleButton pm1 = makeImageSquareRadio(imagePath_payment_card, SQUARE_PM, Orientation.TOP_BOTTOM,
                paymentGroup);
        JToggleButton pm2 = makeImageSquareRadio(imagePath_payment_paypal, SQUARE_PM, Orientation.TOP_BOTTOM,
                paymentGroup);
        JToggleButton pm3 = makeImageSquareRadio(null, SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);
        JToggleButton pm4 = makeImageSquareRadio(null, SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);

        paymentMethodButtons.add(pm1);
        paymentMethodButtons.add(pm2);
        paymentMethodButtons.add(pm3);
        paymentMethodButtons.add(pm4);

        pmGrid.add(pm1);
        pmGrid.add(pm2);
        pmGrid.add(pm3);
        pmGrid.add(pm4);

        content.add(pmGrid, BorderLayout.CENTER);

        // ===== CURRENCY =====
        JPanel currencyWrap = new JPanel(new BorderLayout());
        currencyWrap.setOpaque(false);
        JLabel cTitle = makeTitle("CURRENCY", 28);
        cTitle.setBorder(new EmptyBorder(14, 0, 8, 0));
        currencyWrap.add(cTitle, BorderLayout.NORTH);

        JPanel curBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        curBtns.setOpaque(false);

        JToggleButton thb = makeTextRadio("THB", 76, 40, Orientation.LEFT_RIGHT, currencyGroup);
        JToggleButton usd = makeTextRadio("USD", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);
        JToggleButton eur = makeTextRadio("EUR", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);
        JToggleButton jpy = makeTextRadio("JPY", 76, 40, Orientation.RIGHT_LEFT, currencyGroup);

        currencyButtons.add(thb);
        currencyButtons.add(usd);
        currencyButtons.add(eur);
        currencyButtons.add(jpy);

        curBtns.add(thb);
        curBtns.add(usd);
        curBtns.add(eur);
        curBtns.add(jpy);

        currencyWrap.add(curBtns, BorderLayout.CENTER);
        content.add(currencyWrap, BorderLayout.SOUTH);

        // CONFIRM, CANCEL
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        paymentConfirmBtn = makeSecondaryButton("CONFIRM", 150, 44, Orientation.LEFT_RIGHT);
        paymentConfirmBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Please select Payment Method and Currency completely",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
        });

        JButton cancel = makeSecondaryButton("CANCEL", 150, 44, Orientation.RIGHT_LEFT);
        cancel.addActionListener(e -> {
            resetPaymentSelections();
            resetAddonSelections();
            updatePaymentConfirmEnabled();
            show("MENU1");
        });

        bottomBar.add(paymentConfirmBtn);
        bottomBar.add(cancel);

        ItemListener payListener = e -> updatePaymentConfirmEnabled();
        paymentMethodButtons.forEach(b -> b.addItemListener(payListener));
        currencyButtons.forEach(b -> b.addItemListener(payListener));

        page.add(contentMargin, BorderLayout.CENTER);
        page.add(bottomBar, BorderLayout.SOUTH);
        return page;
    }

    // ============================ Components ============================

    private enum Orientation {
        TOP_BOTTOM, LEFT_RIGHT, RIGHT_LEFT
    }

    private static class RoundedBorderPanel extends JPanel {
        private final Color fill;
        private final int arc;
        private final float stroke;
        private final Color c1, c2;
        private final Orientation ori;

        RoundedBorderPanel(Color fill, int arc, float stroke, Color c1, Color c2, Orientation ori) {
            this.fill = fill;
            this.arc = arc;
            this.stroke = stroke;
            this.c1 = c1;
            this.c2 = c2;
            this.ori = ori;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // fill
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            GradientPaint gp;

            if (ori == Orientation.TOP_BOTTOM) {
                gp = new GradientPaint(0, 0, c1, 0, (int) (h * 0.7), c2);
            } else {
                gp = new GradientPaint(0, 0, c1, w, 0, c2);
            }

            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(stroke));
            g2.draw(new RoundRectangle2D.Float(stroke / 2f, stroke / 2f, w - stroke, h - stroke, arc, arc));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JButton makePrimaryButton(String text, int w, int h) {
        RoundedButton btn = new RoundedButton(
                text,
                ARC, 0f, false,
                PRIMARY_FILL, SEL_FILL,
                SEL_FILL, SECONDARY_FILL,
                PRIMARY_TEXT, TITLE, TITLE, TITLE,
                PANEL_BORDER_TOP, PANEL_BORDER_BOT,
                RoundedButton.Orientation.TOP_BOTTOM);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

    private JButton makeSecondaryButton(String text, int w, int h, Orientation borderOri) {
        Orientation ori = borderOri;

        Color c1, c2;
        if (ori == Orientation.TOP_BOTTOM) {
            c1 = PANEL_BORDER_TOP;
            c2 = PANEL_BORDER_BOT;
        } else {
            c1 = SECONDARY_LEFT;
            c2 = SECONDARY_RIGHT;
        }

        Color hoverText;
        if (text.equalsIgnoreCase("PREVIOUS")) {
            hoverText = hex("#1e1e1e");
        } else {
            hoverText = SECONDARY_TEXT;
        }

        RoundedButton btn = new RoundedButton(text,
                ARC, 1f, true,
                SECONDARY_FILL, SEL_FILL, SEL_FILL, SECONDARY_FILL,
                SECONDARY_TEXT, hoverText, hoverText, SECONDARY_TEXT,
                c1, c2, toRB(ori));
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

    private JButton makeMenuSquare(String imagePath, String label, int size, Runnable onClick) {
        RoundedButton btn = new RoundedButton(
                label,
                ARC, BORDER_STROKE, true,
                ITEM_FILL, SEL_FILL, SEL_FILL, ITEM_FILL,
                TITLE, TITLE, TITLE, TITLE,
                ITEM_BORDER_TOP, ITEM_BORDER_BOT, RoundedButton.Orientation.TOP_BOTTOM) {
            @Override
            protected void paintContent(Graphics2D g2, Color textColor) {
                int inset = 12;
                int imageArea = (int) (getHeight() * 0.7);
                drawImageKeepRatio(g2, imagePath, inset, inset, getWidth() - inset * 2, imageArea - inset);

                g2.setFont(getFont());
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                int textY = getHeight() - fm.getDescent() - 6;
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                g2.drawString(getText(), textX, textY);
            }
        };
        btn.setPreferredSize(new Dimension(size, size));
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(TITLE);
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    private JToggleButton makeImageSquareToggle(String imagePath, int size, Orientation borderOri) {
        Color c1;
        Color c2;

        if (borderOri == Orientation.LEFT_RIGHT) {
            c1 = SECONDARY_LEFT;
            c2 = SECONDARY_RIGHT;
        } else {
            c1 = ITEM_BORDER_TOP;
            c2 = ITEM_BORDER_BOT;
        }

        RoundedToggleButton t = new RoundedToggleButton(
                "",
                ARC, BORDER_STROKE, true,
                ITEM_FILL,
                SEL_FILL,
                SEL_FILL,
                ITEM_FILL,
                SEL_FILL,
                PRIMARY_FILL,
                SEL_FILL,
                TITLE, TITLE, TITLE, TITLE,
                TITLE, PRIMARY_TEXT, TITLE,
                c1, c2, toRT(borderOri)) {
            @Override
            protected void paintContent(Graphics2D g2, Color textColor) {
                int inset = 12;
                drawImageKeepRatio(g2, imagePath, inset, inset, getWidth() - inset * 2, getHeight() - inset * 2);
            }
        };
        t.setPreferredSize(new Dimension(size, size));
        return t;
    }

    private JToggleButton makeTextToggle(String text, int w, int h, Orientation ori) {
        RoundedToggleButton t = new RoundedToggleButton(
                text,
                ARC, 1f, true,
                ITEM_FILL,
                SEL_FILL,
                SEL_FILL,
                ITEM_FILL,
                SEL_FILL,
                PRIMARY_FILL,
                SEL_FILL,
                TITLE, TITLE, TITLE, TITLE,
                TITLE, PRIMARY_TEXT, TITLE,
                ITEM_BORDER_TOP, ITEM_BORDER_BOT, toRT(ori));
        t.setPreferredSize(new Dimension(w, h));
        t.setFont(new Font("SansSerif", Font.BOLD, 16));
        return t;
    }

    private JToggleButton makeTextRadio(String text, int w, int h, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeTextToggle(text, w, h, ori);
        group.add(t);
        return t;
    }

    private JToggleButton makeImageSquareRadio(String imagePath, int size, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeImageSquareToggle(imagePath, size, ori);
        group.add(t);
        return t;
    }

    // ============================ Helpers ============================

    // Header
    private JPanel createHeaderOnlyPage(String headerText) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG);

        JLabel header = new JLabel(headerText, SwingConstants.LEFT);
        header.setForeground(TITLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 20, 6, 0));
        page.add(header, BorderLayout.NORTH);

        return page;
    }

    // Title
    private JLabel makeTitle(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TITLE);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        l.setBorder(new EmptyBorder(6, 0, 10, 0));
        return l;
    }

    private void show(String name) {
        cardLayout.show(cards, name);
    }

    private static Color hex(String h) {
        return Color.decode(h);
    }

    // Square
    private static void drawImageKeepRatio(Graphics2D g2, String path, int x, int y, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (path == null) {
            g2.setColor(new Color(70, 70, 70));
            g2.fillRoundRect(x, y, w, h, 12, 12);
            return;
        }
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) {
                g2.setColor(new Color(70, 70, 70));
                g2.fillRoundRect(x, y, w, h, 12, 12);
                return;
            }
            double scale = Math.min((double) w / img.getWidth(), (double) h / img.getHeight());
            int dw = (int) Math.round(img.getWidth() * scale);
            int dh = (int) Math.round(img.getHeight() * scale);
            int dx = x + (w - dw) / 2;
            int dy = y + (h - dh) / 2;
            g2.drawImage(img, dx, dy, dw, dh, null);
        } catch (Exception ex) {
            g2.setColor(new Color(70, 70, 70));
            g2.fillRoundRect(x, y, w, h, 12, 12);
        }
    }

    // ---- Enable/Disable Confirm buttons ----
    private void updateAddonConfirmEnabled() {
        boolean ok = toppingButtons.stream().anyMatch(AbstractButton::isSelected)
                && sweetnessButtons.stream().anyMatch(AbstractButton::isSelected);

        if (addonConfirmBtn != null) {
            JPanel parent = (JPanel) addonConfirmBtn.getParent();
            int index = parent.getComponentZOrder(addonConfirmBtn);
            parent.remove(addonConfirmBtn);

            if (ok) {
                addonConfirmBtn = makePrimaryButton("CONFIRM", 150, 44);
                addonConfirmBtn.addActionListener(ev -> {
                    show("PAYMENT");
                    resetAddonSelections();
                });
            } else {
                addonConfirmBtn = makeSecondaryButton("CONFIRM", 150, 44, Orientation.LEFT_RIGHT);
                addonConfirmBtn.addActionListener(ev -> {
                    JOptionPane.showMessageDialog(frame,
                            "Please select Topping and Sweetness completely",
                            "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
                });
            }

            parent.add(addonConfirmBtn, index);
            parent.revalidate();
            parent.repaint();
        }
    }

    // update payment confirm
    private void updatePaymentConfirmEnabled() {
        boolean ok = paymentMethodButtons.stream().anyMatch(AbstractButton::isSelected)
                && currencyButtons.stream().anyMatch(AbstractButton::isSelected);

        if (paymentConfirmBtn != null) {
            JPanel parent = (JPanel) paymentConfirmBtn.getParent();
            int index = parent.getComponentZOrder(paymentConfirmBtn);
            parent.remove(paymentConfirmBtn);

            if (ok) {
                paymentConfirmBtn = makePrimaryButton("CONFIRM", 150, 44);
                paymentConfirmBtn.addActionListener(ev -> {
                    JOptionPane.showMessageDialog(frame,
                            "Payment successful. Please wait for your coffee.",
                            "Payment", JOptionPane.INFORMATION_MESSAGE);
                    show("MENU1");
                    resetPaymentSelections();
                    resetAddonSelections();
                });
            } else {
                paymentConfirmBtn = makeSecondaryButton("CONFIRM", 150, 44, Orientation.LEFT_RIGHT);
                paymentConfirmBtn.addActionListener(ev -> {
                    JOptionPane.showMessageDialog(frame,
                            "Please select Payment Method and Currency completely",
                            "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
                });
            }

            parent.add(paymentConfirmBtn, index);
            parent.revalidate();
            parent.repaint();
        }
    }

    // ---- Reset selections ----
    private void resetAddonSelections() {
        toppingButtons.stream().forEach(b -> b.setSelected(false));
        sweetnessGroup.clearSelection();
        sweetnessButtons.stream().forEach(b -> b.setSelected(false));
    }

    private void resetPaymentSelections() {
        paymentMethodButtons.stream().forEach(b -> b.setSelected(false));
        paymentGroup.clearSelection();
        currencyButtons.stream().forEach(b -> b.setSelected(false));
        currencyGroup.clearSelection();
    }

    // Resize
    public static BufferedImage resizeImage(File in, int newW, int newH) {
        try {
            BufferedImage img = ImageIO.read(in);
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            return dimg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }
}
