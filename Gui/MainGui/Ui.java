package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import Gui.CustomGui.*;

public class Ui {

    // ===== Colors =====
    public static final Color BG = Color.decode("#1e1e1e");
    public static final Color TITLE = Color.decode("#d9d9d9");

    public static final Color PANEL_FILL = Color.decode("#2a2a2a");
    public static final Color PANEL_BORDER_TOP = Color.decode("#333333");
    public static final Color PANEL_BORDER_BOT = Color.decode("#2a2a2a");

    public static final Color ITEM_FILL = Color.decode("#333333");
    public static final Color ITEM_BORDER_TOP = Color.decode("#504e4e");
    public static final Color ITEM_BORDER_BOT = Color.decode("#333333");

    public static final Color SEL_FILL = Color.decode("#1e1e1e");

    public static final Color PRIMARY_FILL = Color.decode("#d9d9d9");
    public static final Color PRIMARY_TEXT = Color.decode("#1e1e1e");

    public static final Color SECONDARY_FILL = Color.decode("#333333");
    public static final Color SECONDARY_TEXT = TITLE;
    public static final Color SECONDARY_LEFT = Color.decode("#504e4e");
    public static final Color SECONDARY_RIGHT = Color.decode("#333333");

    // ===== Sizes =====
    public static final int ARC = 18;
    public static final float BORDER_STROKE = 2f;

    public static final int SQUARE_ITEM = 140;
    public static final int SQUARE_PM = 120;

    // ===== Orientation enum =====
    public enum Orientation {
        TOP_BOTTOM, LEFT_RIGHT, RIGHT_LEFT
    }

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

    // ===== RoundedBorderPanel (กรอบมีไล่เฉด) =====
    public static class RoundedBorderPanel extends JPanel {
        private final Color fill;
        private final int arc;
        private final float stroke;
        private final Color c1, c2;
        private final Orientation ori;

        public RoundedBorderPanel(Color fill, int arc, float stroke, Color c1, Color c2, Orientation ori) {
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

    // ===== Factory Methods =====

    public static JPanel makeTextFieldWithPadding(JTextField field, int w, int h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        field.setPreferredSize(new Dimension(w, h));
        panel.add(field, BorderLayout.CENTER);

        // ช่องว่าง dummy (ให้ขนาดเท่าปุ่มตา)
        JLabel dummy = new JLabel();
        dummy.setPreferredSize(new Dimension(40, h));
        panel.add(dummy, BorderLayout.EAST);

        return panel;
    }

    public static JPanel makePasswordFieldWithToggle(JPasswordField field, int w, int h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        field.setPreferredSize(new Dimension(w, h));
        field.setEchoChar('•');

        JButton toggleBtn = new JButton();
        toggleBtn.setPreferredSize(new Dimension(40, h));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorderPainted(false);

        // 👁 icon รูปตา
        ImageIcon eye = new ImageIcon("Imgs/icons/eye.png");
        ImageIcon eyeOff = new ImageIcon("Imgs/icons/eye_off.png");
        toggleBtn.setIcon(eyeOff);

        toggleBtn.addActionListener(e -> {
            if (field.getEchoChar() == (char) 0) {
                field.setEchoChar('•');
                toggleBtn.setIcon(eyeOff);
            } else {
                field.setEchoChar((char) 0);
                toggleBtn.setIcon(eye);
            }
        });

        panel.add(field, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);
        return panel;
    }

    public static JButton makePrimaryButton(String text, int w, int h) {
        RoundedButton btn = new RoundedButton(
                text,
                ARC, 0f, false,
                PRIMARY_FILL, SEL_FILL, SEL_FILL, SECONDARY_FILL,
                PRIMARY_TEXT, TITLE, TITLE, TITLE,
                PANEL_BORDER_TOP, PANEL_BORDER_BOT,
                RoundedButton.Orientation.TOP_BOTTOM);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

    public static JButton makeSecondaryButton(String text, int w, int h, Orientation borderOri) {
        Color c1, c2;
        if (borderOri == Orientation.TOP_BOTTOM) {
            c1 = PANEL_BORDER_TOP;
            c2 = PANEL_BORDER_BOT;
        } else {
            c1 = SECONDARY_LEFT;
            c2 = SECONDARY_RIGHT;
        }
        Color hoverText = text.equalsIgnoreCase("PREVIOUS") ? Color.decode("#1e1e1e") : SECONDARY_TEXT;

        RoundedButton btn = new RoundedButton(text,
                ARC, 1f, true,
                SECONDARY_FILL, SEL_FILL, SEL_FILL, SECONDARY_FILL,
                SECONDARY_TEXT, hoverText, hoverText, SECONDARY_TEXT,
                c1, c2, toRB(borderOri));
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

    public static JButton makeMenuSquare(String imagePath, String label, int size, Runnable onClick) {
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

    public static JToggleButton makeToppingToggle(String imagePath, int size, double price, Orientation borderOri) {
        Color c1 = (borderOri == Orientation.LEFT_RIGHT) ? SECONDARY_LEFT : ITEM_BORDER_TOP;
        Color c2 = (borderOri == Orientation.LEFT_RIGHT) ? SECONDARY_RIGHT : ITEM_BORDER_BOT;

        RoundedToggleButton t = new RoundedToggleButton(
                "",
                ARC, BORDER_STROKE, true,
                ITEM_FILL, SEL_FILL, SEL_FILL, ITEM_FILL,
                SEL_FILL, PRIMARY_FILL, SEL_FILL,
                TITLE, TITLE, TITLE, TITLE,
                TITLE, PRIMARY_TEXT, TITLE,
                c1, c2, toRT(borderOri)) {
            @Override
            protected void paintContent(Graphics2D g2, Color textColor) {
                int inset = 12;
                drawImageKeepRatio(g2, imagePath, inset, inset,
                        getWidth() - inset * 2, getHeight() - inset * 2 - 22);
                g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                g2.setColor(new Color(170, 170, 170));
                String priceText = "+ " + (int) price + " ฿";
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(priceText)) / 2;
                int ty = getHeight() - fm.getDescent() - 6;
                g2.drawString(priceText, tx, ty);
            }
        };
        t.setPreferredSize(new Dimension(size, size));
        return t;
    }

    public static JToggleButton makeTextToggle(String text, int w, int h, Orientation ori) {
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

    public static JToggleButton makeTextRadio(String text, int w, int h, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeTextToggle(text, w, h, ori);
        group.add(t);
        return t;
    }

    // ===== Image helper =====
    public static void drawImageKeepRatio(Graphics2D g2, String path, int x, int y, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (path == null) {
            g2.setColor(new Color(70, 70, 70));
            g2.fillRoundRect(x, y, w, h, 12, 12);
            return;
        }
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(path));
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
}
