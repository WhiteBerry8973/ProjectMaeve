package Gui.MainGui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import Gui.CustomGui.*;

public class Ui {

    public static final Color WHITE = new Color(0xE8E3D5);
    public static final Color WHITE_DARK = new Color(0xCFC8B4);
    public static final Color BROWN = new Color(0x633322);
    public static final Color BROWN_LIGHT = new Color(0x9d715c);
    public static final Color BROWN_DARK = new Color(0x35190f);
    public static final Color YELLOW = new Color(0xffcc80);
    public static final Color YELLOW_DARK = new Color(0x8a4010);

    public static final int ARC = 18;
    public static final float BORDER_STROKE = 1f;

    public static final int SQUARE_ITEM = 140;
    public static final int SQUARE_PM = 120;

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

    // ===== ROUNDED BORDER PANEL =====
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

            g2.setColor(c1);
            g2.setStroke(new BasicStroke(stroke));
            g2.draw(new RoundRectangle2D.Float(stroke / 2f, stroke / 2f, w - stroke, h - stroke, arc, arc));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== ROUNDED PANEL =====
    public static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bgColor;

        public RoundedPanel(int arc, Color bgColor) {
            this.arc = arc;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== ROUNDED TEXT FIELD =====
    public static class RoundedTextField extends JTextField {
        private int arc = 12;
        
        public RoundedTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10));
            setFont(new Font("SansSerif", Font.PLAIN, 18));
            setForeground(BROWN_DARK);
            setCaretColor(BROWN_DARK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== ROUNDED PASSWORD FIELD =====
    public static class RoundedPasswordField extends JPasswordField {
        private int arc = 12;

        public RoundedPasswordField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10));
            setFont(new Font("SansSerif", Font.PLAIN, 18));
            setForeground(BROWN_DARK);
            setCaretColor(BROWN_DARK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(WHITE_DARK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== ROUNDED BOTTOM PANEL =====
    public static class BottomRoundedBorderPanel extends JPanel {
        private final Color fill;
        private final int arc;
        private final float stroke;
        private final Color c1, c2;
        private final Orientation ori;

        public BottomRoundedBorderPanel(Color fill, int arc, float stroke,
                Color c1, Color c2, Orientation ori) {
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - 1;
            int h = getHeight() - 1;
            int r = arc;

            Shape shape = makeBottomRoundedRect(0, 0, w, h, r);

            g2.setColor(fill);
            g2.fill(shape);

            g2.setColor(c1);

            g2.setStroke(new BasicStroke(stroke));
            g2.draw(shape);

            g2.dispose();
        }

        private Shape makeBottomRoundedRect(int x, int y, int w, int h, int r) {
            Path2D.Double p = new Path2D.Double();

            p.moveTo(x, y);
            p.lineTo(x + w, y);

            p.lineTo(x + w, y + h - r);
            p.quadTo(x + w, y + h, x + w - r, y + h);

            p.lineTo(x + r, y + h);
            p.quadTo(x, y + h, x, y + h - r);

            p.lineTo(x, y);
            p.closePath();
            return p;
        }
    }

    // ===== EYE ICON =====
    public static class EyeIcon implements Icon {
        private final Color color;
        private final int size;
        private final boolean open;

        public EyeIcon(Color color, int size, boolean open) {
            this.color = color;
            this.size = size;
            this.open = open;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = size;
            int h = size / 2;
            int cx = x + w / 2;
            int cy = y + h / 2 + 4;

            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            g2.drawArc(x + 2, cy - h / 2, w - 4, h, 0, 180);
            g2.drawArc(x + 2, cy - h / 2, w - 4, h, 180, 180);

            if (open) {
                g2.fillOval(cx - 4, cy - 4, 8, 8);
            } else {
                g2.drawLine(cx - 6, cy + 2, cx + 6, cy - 6);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    // ===== METHOD =====
    public static JPanel makeTextFieldWithPadding(JTextField field, int w, int h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        field.setPreferredSize(new Dimension(w, h));
        panel.add(field, BorderLayout.CENTER);

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

        panel.add(field, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);
        return panel;
    }

    public static JButton makePrimaryButton(String text, int w, int h) {
        RoundedButton btn = new RoundedButton(
                text,
                ARC, 0f, false,
                BROWN, BROWN_LIGHT, BROWN_DARK, WHITE,
                WHITE, WHITE_DARK, BROWN_LIGHT, BROWN_DARK,
                WHITE, WHITE,
                RoundedButton.Orientation.TOP_BOTTOM);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

    public static JButton makeSecondaryButton(String text, int w, int h, Orientation borderOri) {
        Color c1 = (borderOri == Orientation.TOP_BOTTOM) ? WHITE : BROWN_DARK;
        Color c2 = (borderOri == Orientation.TOP_BOTTOM) ? WHITE : BROWN_DARK;

        Color hoverText = text.equalsIgnoreCase("PREVIOUS") ? WHITE : BROWN_DARK;

        RoundedButton btn = new RoundedButton(text,
                ARC, 1f, true,
                WHITE, WHITE_DARK, WHITE_DARK, WHITE,
                BROWN_DARK, hoverText, hoverText, BROWN_DARK,
                c1, c2, toRB(borderOri));
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return btn;
    }

 public static RoundedButton makeLightCapsuleButton(String text, int w, int h) {
        RoundedButton b = new RoundedButton(
                text,
                18, 1f, true,
                WHITE,
                WHITE_DARK,
                BROWN,
                WHITE,
                BROWN,
                BROWN_DARK,
                WHITE_DARK,
                WHITE,
                BROWN, BROWN,
                RoundedButton.Orientation.LEFT_RIGHT
        );
        b.setPreferredSize(new Dimension(w, h));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 20f));
        return b;
    }

    public static JButton makeMenuSquare(String imagePath, String label, int size, Runnable onClick) {
        RoundedButton btn = new RoundedButton(
                label,
                ARC, BORDER_STROKE, true,
                BROWN_DARK, WHITE_DARK, WHITE_DARK, BROWN_DARK,
                BROWN_DARK, BROWN_DARK, BROWN_DARK, BROWN_DARK,
                WHITE, WHITE, RoundedButton.Orientation.TOP_BOTTOM) {
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
        btn.setForeground(BROWN_DARK);
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    public static JToggleButton makeToppingToggle(String imagePath, int size, double price, Orientation borderOri) {
        Color c1 = (borderOri == Orientation.LEFT_RIGHT) ? BROWN_DARK : WHITE;
        Color c2 = (borderOri == Orientation.LEFT_RIGHT) ? BROWN_DARK : WHITE;

        RoundedToggleButton t = new RoundedToggleButton(
                "", ARC, BORDER_STROKE, true, BROWN, WHITE_DARK, WHITE_DARK, BROWN, BROWN,
                BROWN,
                BROWN, BROWN, BROWN, BROWN, BROWN, BROWN, WHITE, BROWN, c1, c2, toRT(borderOri)) {
            @Override
            protected void paintContent(Graphics2D g2, Color textColor) {
                int inset = 12;
                drawImageKeepRatio(g2, imagePath, inset, inset,
                        getWidth() - inset * 2, getHeight() - inset * 2 - 22);
                g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                g2.setColor(WHITE_DARK);
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
                WHITE, WHITE_DARK, WHITE_DARK, BROWN, BROWN, BROWN_DARK, BROWN, BROWN, BROWN, BROWN,
                BROWN, BROWN, BROWN, BROWN, BROWN, BROWN, toRT(ori));
        t.setPreferredSize(new Dimension(w, h));
        t.setFont(new Font("SansSerif", Font.BOLD, 16));
        return t;
    }

    public static JToggleButton makeTextRadio(String text, int w, int h, Orientation ori, ButtonGroup group) {
        JToggleButton t = makeTextToggle(text, w, h, ori);
        group.add(t);
        return t;
    }

    // ===== IMAGE =====
    public static void drawImageKeepRatio(Graphics2D g2, String path, int x, int y, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (path == null) {
            g2.setColor(BROWN_DARK);
            g2.fillRoundRect(x, y, w, h, 12, 12);
            return;
        }
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File(path));
            if (img == null) {
                g2.setColor(BROWN_DARK);
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
            g2.setColor(BROWN_DARK);
            g2.fillRoundRect(x, y, w, h, 12, 12);
        }
    }
}
