// File: MaeveCoffeeUI.java
// Compile: javac MaeveCoffeeUI.java
// Run:     java MaeveCoffeeUI

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Maeve Coffee UI (single file, BorderLayout + EmptyBorder)
 *
 * สิ่งที่ทำตามสเปคล่าสุด:
 * - ใช้ BorderLayout + EmptyBorder คุมระยะทั้งหมด
 * - หน้า MENU:
 *    • กรอบมน (content) ห่างจากขอบ JFrame ซ้าย/ขวา/ล่าง = 20px และห่างจากหัว "MAEVE COFFEE" = 30px
 *    • ปุ่มกาแฟ สี่เหลี่ยมจัตุรัส, ข้อความอยู่ใต้รูป, ไม่ยืดรูป
 *    • ปุ่ม NEXT ไม่มีเส้นขอบ, hover = #1e1e1e, ห่างจากกริด 5px
 * - หน้า MENU2: เหมือนหน้าแรก + มี PREVIOUS (hover = #d9d9d9)
 * - หน้า ADDON:
 *    • กรอบมนเหมือนหน้า MENU
 *    • ปุ่ม Confirm/Cancel อยู่ในแถบล่าง ห่างจาก content 10px และห่างจากขอบล่าง JFrame 20px
 *    • ยังไม่เลือก topping + sweetness → ปุ่ม Confirm เป็นสีดำ (เหมือน Cancel) และ disabled
 *      เลือกครบแล้ว → Confirm เปลี่ยนเป็น #d9d9d9 และ enabled
 * - หน้า PAYMENT:
 *    • เพิ่มหัวข้อ "PAYMENT" ด้านบน
 *    • กรอบมน/ระยะห่างเหมือน ADDON
 *    • ปุ่มวิธีชำระเงินเป็นสี่เหลี่ยมจัตุรัส
 *    • ยังไม่เลือก payment method + currency → Confirm ดำ/disabled; เลือกครบแล้ว → #d9d9d9/enabled
 * - ขอบ gradient ทั้งหมดให้สีล่างขึ้นมาถึง 70% ของความสูง (h*0.7)
 * - ยังคงสไตล์, สี และ interaction อื่น ๆ ตามเวอร์ชันก่อนหน้า
 */
public class MaeveCoffeeUI {

    // ======= ใช้ placeholder ทั้งหมดเป็น "americano.png" ตามที่ขอ =======
    private static final String imagePath_menu1_item1 = "americano.png";
    private static final String imagePath_menu1_item2 = "americano.png";
    private static final String imagePath_menu1_item3 = "americano.png";
    private static final String imagePath_menu1_item4 = "americano.png";

    private static final String imagePath_menu2_item1 = "americano.png";
    private static final String imagePath_menu2_item2 = "americano.png";

    private static final String imagePath_topping1 = "americano.png";
    private static final String imagePath_topping2 = "americano.png";
    private static final String imagePath_topping3 = "americano.png";
    private static final String imagePath_topping4 = "americano.png";
    private static final String imagePath_topping5 = "americano.png";
    private static final String imagePath_topping6 = "americano.png";

    private static final String imagePath_payment_card   = "americano.png";
    private static final String imagePath_payment_paypal = "americano.png";

    // ======= Theme Colors =======
    private static final Color BG                = hex("#1e1e1e");
    private static final Color TITLE             = hex("#d9d9d9");

    // PANEL: พื้นทึบ + เส้นขอบ gradient (บน->ล่าง)
    private static final Color PANEL_FILL        = hex("#2a2a2a");
    private static final Color PANEL_BORDER_TOP  = hex("#333333");
    private static final Color PANEL_BORDER_BOT  = hex("#2a2a2a");

    // ปุ่มไอเทม (กาแฟ/ท็อปปิ้ง/วิธีจ่าย/สกุลเงิน) : พื้นทึบ + เส้นขอบ gradient (บน->ล่าง)
    private static final Color ITEM_FILL         = hex("#333333");
    private static final Color ITEM_BORDER_TOP   = hex("#504e4e");
    private static final Color ITEM_BORDER_BOT   = hex("#333333");

    // hover/selected
    private static final Color SEL_FILL          = hex("#1e1e1e");

    // ปุ่มหลัก (Next/Confirm) : ไม่มีเส้นขอบ
    private static final Color PRIMARY_FILL      = hex("#d9d9d9");
    private static final Color PRIMARY_TEXT      = hex("#1e1e1e");

    // ปุ่มรอง (Previous/Cancel) : พื้นทึบ + เส้นขอบ gradient ซ้าย->ขวา
    private static final Color SECONDARY_FILL    = hex("#333333");
    private static final Color SECONDARY_TEXT    = TITLE;
    private static final Color SECONDARY_LEFT    = hex("#504e4e");
    private static final Color SECONDARY_RIGHT   = hex("#333333");

    private static final int  ARC            = 18;
    private static final float BORDER_STROKE = 3f;

    // ขนาด "สี่เหลี่ยมจัตุรัส"
    private static final int SQUARE_ITEM    = 140; // menu
    private static final int SQUARE_TOPPING = 96;  // toppings
    private static final int SQUARE_PM      = 120; // payment methods

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    // states (ไว้ reset + enable/disable confirm)
    private final Set<JToggleButton> toppingButtons        = new HashSet<>();
    private final Set<JToggleButton> sweetnessButtons      = new HashSet<>();
    private final Set<JToggleButton> paymentMethodButtons  = new HashSet<>();
    private final Set<JToggleButton> currencyButtons       = new HashSet<>();

    private final ButtonGroup sweetnessGroup = new ButtonGroup(); // single-choice
    private final ButtonGroup paymentGroup   = new ButtonGroup(); // single-choice
    private final ButtonGroup currencyGroup  = new ButtonGroup(); // single-choice

    // ปุ่ม confirm (ต้องเอามาอัปเดต enable/disable + สี)
    private JButton addonConfirmBtn;
    private JButton paymentConfirmBtn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MaeveCoffeeUI().createAndShowGUI());
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

        cards.add(buildMenuPage1(), "MENU1");
        cards.add(buildMenuPage2(), "MENU2");
        cards.add(buildAddonPage(), "ADDON");
        cards.add(buildPaymentPage(), "PAYMENT");

        frame.add(cards, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ============================ Pages ============================

    private JPanel buildMenuPage1() {
        JPanel page = createHeaderOnlyPage("MAEVE COFFEE");

        // wrapper คุม "ช่องว่างจากหัว 30px" และ "ซ้าย/ขวา/ล่าง 20px"
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20)); // top, left, bottom, right

        RoundedBorderPanel content = new RoundedBorderPanel(
                PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP, PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        // ระยะในกล่อง (หายใจ): เบาๆ
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        content.add(makeTitle("MENU", 36), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);
        grid.add(makeMenuSquare(imagePath_menu1_item1, "Americano",     SQUARE_ITEM, () -> show("ADDON")));
        grid.add(makeMenuSquare(imagePath_menu1_item2, "Espresso Shot", SQUARE_ITEM, () -> show("ADDON")));
        grid.add(makeMenuSquare(imagePath_menu1_item3, "Caramel Coffee",SQUARE_ITEM, () -> show("ADDON")));
        grid.add(makeMenuSquare(imagePath_menu1_item4, "Ginseng Coffee",SQUARE_ITEM, () -> show("ADDON")));
        content.add(grid, BorderLayout.CENTER);

        // ปุ่ม NEXT: ห่างจากกริด 5px (ระยะด้านบนของแท่งปุ่ม)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(5, 0, 0, 0)); // 5px เหนือปุ่ม
        JButton next = makePrimaryButton("NEXT", 120, 40);
        next.addActionListener(e -> show("MENU2"));
        bottomBar.add(next);
        content.add(bottomBar, BorderLayout.SOUTH);

        contentMargin.add(content, BorderLayout.CENTER);
        page.add(contentMargin, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildMenuPage2() {
        JPanel page = createHeaderOnlyPage("MAEVE COFFEE");

        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20)); // top, left, bottom, right

        RoundedBorderPanel content = new RoundedBorderPanel(
                PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP, PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        content.add(makeTitle("MENU", 36), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 18, 18));
        grid.setOpaque(false);
        grid.add(makeMenuSquare(imagePath_menu2_item1, "Ice Mocha",     SQUARE_ITEM, () -> show("ADDON")));
        grid.add(makeMenuSquare(imagePath_menu2_item2, "Coming Soon...",SQUARE_ITEM, () -> {}));
        grid.add(makeMenuSquare(null, "", SQUARE_ITEM, () -> {}));
        grid.add(makeMenuSquare(null, "", SQUARE_ITEM, () -> {}));
        content.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 0, 0, 0)); // ห่างกริด 5px

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        JButton prev = makeSecondaryButton("PREVIOUS", 130, 40, Orientation.LEFT_RIGHT);
        prev.addActionListener(e -> show("MENU1"));
        left.add(prev);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        JButton next = makePrimaryButton("NEXT", 120, 40);
        next.addActionListener(e -> show("MENU1")); // loop back
        right.add(next);

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);
        content.add(bottom, BorderLayout.SOUTH);

        contentMargin.add(content, BorderLayout.CENTER);
        page.add(contentMargin, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildAddonPage() {
        JPanel page = createHeaderOnlyPage("ADDON");

        // content ห่างหัว 30px และซ้าย/ขวา/ล่าง 20px
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        RoundedBorderPanel content = new RoundedBorderPanel(
                PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP, PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentMargin.add(content, BorderLayout.CENTER);

        content.add(makeTitle("TOPPINGS", 28), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);
        JToggleButton t1 = makeImageSquareToggle(imagePath_topping1, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t2 = makeImageSquareToggle(imagePath_topping2, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t3 = makeImageSquareToggle(imagePath_topping3, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t4 = makeImageSquareToggle(imagePath_topping4, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t5 = makeImageSquareToggle(imagePath_topping5, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        JToggleButton t6 = makeImageSquareToggle(imagePath_topping6, SQUARE_TOPPING, Orientation.TOP_BOTTOM);
        toppingButtons.add(t1); toppingButtons.add(t2); toppingButtons.add(t3);
        toppingButtons.add(t4); toppingButtons.add(t5); toppingButtons.add(t6);
        grid.add(t1); grid.add(t2); grid.add(t3); grid.add(t4); grid.add(t5); grid.add(t6);
        content.add(grid, BorderLayout.CENTER);

        JPanel sweetWrap = new JPanel(new BorderLayout());
        sweetWrap.setOpaque(false);
        JLabel sTitle = makeTitle("SWEETNESS", 28);
        sTitle.setBorder(new EmptyBorder(18, 0, 8, 0));
        sweetWrap.add(sTitle, BorderLayout.NORTH);

        JPanel sweetBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
        sweetBtns.setOpaque(false);
        JToggleButton s0   = makeTextToggle("0%",   76, 40, Orientation.TOP_BOTTOM);
        JToggleButton s50  = makeTextToggle("50%",  76, 40, Orientation.TOP_BOTTOM);
        JToggleButton s100 = makeTextToggle("100%", 76, 40, Orientation.TOP_BOTTOM);
        JToggleButton s120 = makeTextToggle("120%", 76, 40, Orientation.TOP_BOTTOM);

        sweetnessGroup.add(s0); sweetnessGroup.add(s50); sweetnessGroup.add(s100); sweetnessGroup.add(s120);
        sweetnessButtons.add(s0); sweetnessButtons.add(s50); sweetnessButtons.add(s100); sweetnessButtons.add(s120);

        sweetBtns.add(s0); sweetBtns.add(s50); sweetBtns.add(s100); sweetBtns.add(s120);
        sweetWrap.add(sweetBtns, BorderLayout.CENTER);
        content.add(sweetWrap, BorderLayout.SOUTH);

        // แถบปุ่มล่าง: ห่าง content 10px และห่างขอบล่าง 20px
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        addonConfirmBtn = makePrimaryButton("CONFIRM", 150, 44);
        addonConfirmBtn.setEnabled(false);
        addonConfirmBtn.addActionListener(e ->{ 
            show("PAYMENT");
            resetAddonSelections();
        });



        JButton cancel = makeSecondaryButton("CANCEL", 150, 44, Orientation.LEFT_RIGHT);
        cancel.addActionListener(e -> {
            resetPaymentSelections();
            resetAddonSelections();
            updateAddonConfirmEnabled();
            show("MENU1");
        });

        bottomBar.add(addonConfirmBtn);
        bottomBar.add(cancel);

        // listener เพื่ออัปเดตสถานะปุ่ม Confirm (+ repaint ให้สีเปลี่ยน)
        ItemListener addonListener = e -> updateAddonConfirmEnabled();
        toppingButtons.forEach(b -> b.addItemListener(addonListener));
        sweetnessButtons.forEach(b -> b.addItemListener(addonListener));
        

        page.add(contentMargin, BorderLayout.CENTER);
        page.add(bottomBar,      BorderLayout.SOUTH);
        return page;
    }

    private JPanel buildPaymentPage() {
        JPanel page = createHeaderOnlyPage("PAYMENT");

        // content ห่างหัว 30px และซ้าย/ขวา/ล่าง 20px
        JPanel contentMargin = new JPanel(new BorderLayout());
        contentMargin.setOpaque(false);
        contentMargin.setBorder(new EmptyBorder(30, 20, 20, 20));

        RoundedBorderPanel content = new RoundedBorderPanel(
                PANEL_FILL, ARC, BORDER_STROKE, PANEL_BORDER_TOP, PANEL_BORDER_BOT, Orientation.TOP_BOTTOM);
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        contentMargin.add(content, BorderLayout.CENTER);

        content.add(makeTitle("PAYMENT METHOD", 28), BorderLayout.NORTH);

        JPanel pmGrid = new JPanel(new GridLayout(2, 2, 18, 18));
        pmGrid.setOpaque(false);
        JToggleButton pm1 = makeImageSquareRadio(imagePath_payment_card,   SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);
        JToggleButton pm2 = makeImageSquareRadio(imagePath_payment_paypal, SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);
        JToggleButton pm3 = makeImageSquareRadio(null, SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);
        JToggleButton pm4 = makeImageSquareRadio(null, SQUARE_PM, Orientation.TOP_BOTTOM, paymentGroup);
        paymentMethodButtons.add(pm1); paymentMethodButtons.add(pm2); paymentMethodButtons.add(pm3); paymentMethodButtons.add(pm4);
        pmGrid.add(pm1); pmGrid.add(pm2); pmGrid.add(pm3); pmGrid.add(pm4);
        content.add(pmGrid, BorderLayout.CENTER);

        JPanel currencyWrap = new JPanel(new BorderLayout());
        currencyWrap.setOpaque(false);
        JLabel cTitle = makeTitle("CURRENCY", 28);
        cTitle.setBorder(new EmptyBorder(14, 0, 8, 0));
        currencyWrap.add(cTitle, BorderLayout.NORTH);

        JPanel curBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        curBtns.setOpaque(false);
        JToggleButton thb = makeTextRadio("THB", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);
        JToggleButton usd = makeTextRadio("USD", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);
        JToggleButton eur = makeTextRadio("EUR", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);
        JToggleButton jpy = makeTextRadio("JPY", 76, 40, Orientation.TOP_BOTTOM, currencyGroup);

        currencyButtons.add(thb); currencyButtons.add(usd); currencyButtons.add(eur); currencyButtons.add(jpy);
        curBtns.add(thb); curBtns.add(usd); curBtns.add(eur); curBtns.add(jpy);
        currencyWrap.add(curBtns, BorderLayout.CENTER);

        content.add(currencyWrap, BorderLayout.SOUTH);

        // แถบปุ่มล่าง: ห่าง content 10px และห่างขอบล่าง 20px
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(10, 0, 20, 0));

        paymentConfirmBtn = makePrimaryButton("CONFIRM", 150, 44);
        paymentConfirmBtn.setEnabled(false);
        paymentConfirmBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Payment successful. Please wait for your coffee.",
                    "Payment", JOptionPane.INFORMATION_MESSAGE);
            show("MENU1");
            resetPaymentSelections();
            resetAddonSelections();
        });

        JButton cancel = makeSecondaryButton("CANCEL", 150, 44, Orientation.LEFT_RIGHT);
        cancel.addActionListener(e -> {
            resetPaymentSelections();
            resetAddonSelections();
            updatePaymentConfirmEnabled();
            show("MENU1");
        });

        bottomBar.add(paymentConfirmBtn);
        bottomBar.add(cancel);

        // listener เพื่ออัปเดตสถานะปุ่ม Confirm
        ItemListener payListener = e -> updatePaymentConfirmEnabled();
        paymentMethodButtons.forEach(b -> b.addItemListener(payListener));
        currencyButtons.forEach(b -> b.addItemListener(payListener));

        page.add(contentMargin, BorderLayout.CENTER);
        page.add(bottomBar,      BorderLayout.SOUTH);
        return page;
    }

    // ============================ Components ============================

    private enum Orientation { TOP_BOTTOM, LEFT_RIGHT }

    // Panel พื้นทึบ + ขอบ gradient
    private static class RoundedBorderPanel extends JPanel {
        private final Color fill;
        private final int arc;
        private final float stroke;
        private final Color c1, c2;
        private final Orientation ori;

        RoundedBorderPanel(Color fill, int arc, float stroke, Color c1, Color c2, Orientation ori) {
            this.fill = fill; this.arc = arc; this.stroke = stroke;
            this.c1 = c1; this.c2 = c2; this.ori = ori;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // fill
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // gradient border (สีล่างขึ้นมา 70%)
            GradientPaint gp = (ori == Orientation.TOP_BOTTOM)
                    ? new GradientPaint(0, 0, c1, 0, (int)(h * 0.7), c2)
                    : new GradientPaint(0, 0, c1, w, 0, c2);

            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(stroke));
            g2.draw(new RoundRectangle2D.Float(stroke/2f, stroke/2f, w - stroke, h - stroke, arc, arc));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // วาดพื้นทึบ + เส้นขอบ gradient (ใช้กับปุ่ม/ท็อกเกิล)
    private static void paintRounded(Graphics2D g2, int w, int h, int arc,
                                     Color fill, float stroke, Color c1, Color c2, Orientation ori) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fill);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
        GradientPaint gp = (ori == Orientation.TOP_BOTTOM)
                ? new GradientPaint(0, 0, c1, 0, (int)(h * 0.7), c2)
                : new GradientPaint(0, 0, c1, w, 0, c2);
        g2.setPaint(gp);
        g2.setStroke(new BasicStroke(stroke));
        g2.draw(new RoundRectangle2D.Float(stroke/2f, stroke/2f, w - stroke, h - stroke, arc, arc));
    }

    // ปุ่มหลัก (ไม่มีเส้นขอบ)
    // - ถ้า disabled → พื้น #333333 (เหมือน Cancel), ข้อความ #d9d9d9
    // - ถ้า enabled ปกติ → พื้น #d9d9d9, hover → #1e1e1e (SEL_FILL)
    private JButton makePrimaryButton(String text, int w, int h) {
        return new JButton(text) {
            { setPreferredSize(new Dimension(w, h));
              setFont(new Font("SansSerif", Font.PLAIN, 18));
              setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                boolean enabled = isEnabled();
                boolean hover = getModel().isRollover() && enabled;
                Color body = enabled ? (hover ? SEL_FILL : PRIMARY_FILL) : SECONDARY_FILL;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(body);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC, ARC));
                g2.dispose();

                g.setFont(getFont());
                g.setColor(enabled ? (hover ? TITLE : PRIMARY_TEXT) : TITLE);
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent()) / 2 - 4;
                g.drawString(getText(), tx, ty);
            }
        };
    }

    // ปุ่มรอง (มีเส้นขอบ gradient ซ้าย->ขวา, hover PREVIOUS=#d9d9d9 / อื่น=#1e1e1e)
    private JButton makeSecondaryButton(String text, int w, int h, Orientation borderOri) {
        return new JButton(text) {
            { setPreferredSize(new Dimension(w, h));
              setFont(new Font("SansSerif", Font.PLAIN, 18));
              setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                boolean isPrev = getText().equalsIgnoreCase("PREVIOUS");
                boolean hover  = getModel().isRollover();
                Color body = hover ? (isPrev ? hex("#d9d9d9") : SEL_FILL) : SECONDARY_FILL;
                Color left = (borderOri == Orientation.LEFT_RIGHT) ? SECONDARY_LEFT : PANEL_BORDER_TOP;
                Color right= (borderOri == Orientation.LEFT_RIGHT) ? SECONDARY_RIGHT: PANEL_BORDER_BOT;
                paintRounded(g2, getWidth(), getHeight(), ARC, body, BORDER_STROKE, left, right, borderOri);
                g2.dispose();

                g.setFont(getFont());
                g.setColor(isPrev && hover ? hex("#1e1e1e") : SECONDARY_TEXT);
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent()) / 2 - 4;
                g.drawString(getText(), tx, ty);
            }
        };
    }

    // ปุ่มเมนู "สี่เหลี่ยมจัตุรัส + ข้อความใต้รูป"
    private JButton makeMenuSquare(String imagePath, String label, int size, Runnable onClick) {
    return new JButton(label) {
        { 
            setPreferredSize(new Dimension(size, size));
            setFont(new Font("SansSerif", Font.PLAIN, 16));
            setForeground(TITLE);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM); // 👈 ช่วยจัดข้อความลงล่าง
            setFocusPainted(false); 
            setContentAreaFilled(false); 
            setBorderPainted(false); 
            setOpaque(false);
            addActionListener(e -> onClick.run());
        }

        @Override 
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            // วาดพื้นหลังสี่เหลี่ยมโค้งมน (เต็มปุ่ม)
            Color body = getModel().isRollover() ? SEL_FILL : ITEM_FILL;
            paintRounded(g2, getWidth(), getHeight(), ARC, body, BORDER_STROKE,
                    ITEM_BORDER_TOP, ITEM_BORDER_BOT, Orientation.TOP_BOTTOM);

            // --- วาดรูป ---
            int inset = 12;
            int imageArea = (int)(getHeight() * 0.7); // 70% บนไว้สำหรับรูป
            drawImageKeepRatio(g2, imagePath, inset, inset, getWidth() - inset*2, imageArea - inset);

            g2.dispose();

            // --- วาดข้อความ ---
            g.setFont(getFont());
            g.setColor(getForeground());
            FontMetrics fm = g.getFontMetrics();
            int textY = getHeight() - fm.getDescent() - 6; // เว้นจากล่าง 6px
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            g.drawString(getText(), textX, textY);
            }
        };
    }

    // Toggle รูปแบบ "สี่เหลี่ยมจัตุรัส" (หลายตัวเลือก)
    private JToggleButton makeImageSquareToggle(String imagePath, int size, Orientation borderOri) {
        return new JToggleButton() {
            { setPreferredSize(new Dimension(size, size));
              setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color body = isSelected() ? SEL_FILL : ITEM_FILL;
                Color c1 = (borderOri==Orientation.LEFT_RIGHT)? SECONDARY_LEFT : ITEM_BORDER_TOP;
                Color c2 = (borderOri==Orientation.LEFT_RIGHT)? SECONDARY_RIGHT: ITEM_BORDER_BOT;
                paintRounded(g2, getWidth(), getHeight(), ARC, body, BORDER_STROKE, c1, c2, borderOri);

                int inset = 12;
                drawImageKeepRatio(g2, imagePath, inset, inset, getWidth()-inset*2, getHeight()-inset*2);
                g2.dispose();
            }
        };
    }

    // Toggle รูปแบบ "สี่เหลี่ยมจัตุรัส + single choice"
    private JToggleButton makeImageSquareRadio(String imagePath, int size, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeImageSquareToggle(imagePath, size, ori);
        group.add(t);
        return t;
    }

    // Toggle ตัวหนังสือ
    private JToggleButton makeTextToggle(String text, int w, int h, Orientation ori) {
        return new JToggleButton(text) {
            { setPreferredSize(new Dimension(w, h));
              setFont(new Font("SansSerif", Font.PLAIN, 16));
              setForeground(TITLE);
              setFocusPainted(false); setContentAreaFilled(false); setBorderPainted(false); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color body = isSelected() ? SEL_FILL : ITEM_FILL;
                paintRounded(g2, getWidth(), getHeight(), ARC, body, BORDER_STROKE,
                        ITEM_BORDER_TOP, ITEM_BORDER_BOT, ori);
                g2.dispose();

                g.setFont(getFont());
                g.setColor(TITLE);
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent()) / 2 - 4;
                g.drawString(getText(), tx, ty);
            }
        };
    }

    private JToggleButton makeTextRadio(String text, int w, int h, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeTextToggle(text, w, h, ori);
        group.add(t);
        return t;
    }

    // ============================ Helpers ============================

    private JPanel createHeaderOnlyPage(String headerText) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG);

        JLabel header = new JLabel(headerText, SwingConstants.CENTER);
        header.setForeground(TITLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 52));
        header.setBorder(new EmptyBorder(22, 0, 6, 0));
        page.add(header, BorderLayout.NORTH);

        return page;
    }

    private JLabel makeTitle(String text, int size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TITLE);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        l.setBorder(new EmptyBorder(6, 0, 10, 0));
        return l;
    }

    private void show(String name) { cardLayout.show(cards, name); }

    private static Color hex(String h) { return Color.decode(h); }

    // วาดรูปแบบรักษาอัตราส่วน
    private static void drawImageKeepRatio(Graphics2D g2, String path, int x, int y, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (path == null) {
            g2.setColor(new Color(70, 70, 70));
            g2.fillRoundRect(x, y, w, h, 12, 12);
            return;
        }
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) { g2.setColor(new Color(70,70,70)); g2.fillRoundRect(x, y, w, h, 12, 12); return; }
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

    // ---- Enable/Disable logic for Confirm buttons ----
    private void updateAddonConfirmEnabled() {
        boolean toppingSelected = toppingButtons.stream().anyMatch(AbstractButton::isSelected);
        boolean sweetSelected   = sweetnessButtons.stream().anyMatch(AbstractButton::isSelected);
        boolean ok = toppingSelected && sweetSelected;
        if (addonConfirmBtn != null) {
            addonConfirmBtn.setEnabled(ok);
            addonConfirmBtn.repaint();
        }
    }

    private void updatePaymentConfirmEnabled() {
        boolean pmSel  = paymentMethodButtons.stream().anyMatch(AbstractButton::isSelected);
        boolean curSel = currencyButtons.stream().anyMatch(AbstractButton::isSelected);
        boolean ok = pmSel && curSel;
        if (paymentConfirmBtn != null) {
            paymentConfirmBtn.setEnabled(ok);
            paymentConfirmBtn.repaint();
        }
    }

    // ---- Reset selections ----
    private void resetAddonSelections() {
        for (JToggleButton b : toppingButtons)   b.setSelected(false);
        sweetnessGroup.clearSelection();
        for (JToggleButton b : sweetnessButtons) b.setSelected(false);
    }

    private void resetPaymentSelections() {
        paymentGroup.clearSelection();
        for (JToggleButton b : paymentMethodButtons) b.setSelected(false);
        currencyGroup.clearSelection();
        for (JToggleButton b : currencyButtons) b.setSelected(false);
    }

    // Optional: resize utility
    public static BufferedImage resizeImage(File in, int newW, int newH) {
        try {
            BufferedImage img = ImageIO.read(in);
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            return dimg;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }
}
