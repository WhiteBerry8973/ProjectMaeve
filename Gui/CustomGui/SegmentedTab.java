package Gui.CustomGui;

import javax.swing.*;

import Gui.MainGui.*;

import java.awt.*;
import java.awt.geom.Path2D;

public class SegmentedTab extends JToggleButton {
    private boolean hover = false;

    private boolean roundTopLeft;
    private boolean roundTopRight;
    private boolean roundBottomLeft;
    private boolean roundBottomRight;

    private int arc;

    private Color bgUnselected = Ui.WHITE;
    private Color fgUnselected = Ui.BROWN;
    private Color bgSelected = Ui.WHITE_DARK;
    private Color fgSelected = Ui.BROWN;
    private Color disabledBg = Ui.BROWN_DARK;
    private Color disabledFg = Ui.BROWN_LIGHT;

    public SegmentedTab(String text, boolean roundLeft, boolean roundRight, int arc) {
        this(text, roundLeft, roundRight, roundLeft, roundRight, arc);
    }

    public SegmentedTab(String text,
            boolean roundTopLeft,
            boolean roundTopRight,
            boolean roundBottomLeft,
            boolean roundBottomRight,
            int arc) {
        super(text);
        this.roundTopLeft = roundTopLeft;
        this.roundTopRight = roundTopRight;
        this.roundBottomLeft = roundBottomLeft;
        this.roundBottomRight = roundBottomRight;
        this.arc = arc;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
    }

    // ==== Arc ====
    public void setArc(int arc) {
        this.arc = arc;
        revalidate();
        repaint();
    }

    // ==== Corner ====
    public void setCorners(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        this.roundTopLeft = topLeft;
        this.roundTopRight = topRight;
        this.roundBottomLeft = bottomLeft;
        this.roundBottomRight = bottomRight;
        revalidate();
        repaint();
    }

    public void setDisabledColors(Color bg, Color fg) {
        this.disabledBg = bg;
        this.disabledFg = fg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        boolean sel = isSelected();
        Color bg, fg;

        if (!isEnabled()) {
            bg = disabledBg;
            fg = disabledFg;
        } else if (sel && hover) {
            bg = bgUnselected;
            fg = fgUnselected;
        } else if (sel) {
            bg = bgSelected;
            fg = fgSelected;
        } else {
            bg = bgUnselected;
            fg = fgUnselected;
        }

        Shape shape = makeSegmentShape(0, 0, w, h, arc,
                roundTopLeft, roundTopRight, roundBottomLeft, roundBottomRight);
        g2.setColor(bg);
        g2.fill(shape);

        FontMetrics fm = g2.getFontMetrics(getFont());
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(fg);
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }

    private Shape makeSegmentShape(int x, int y, int w, int h, int r,
            boolean tl, boolean tr, boolean bl, boolean br) {
        Path2D.Double p = new Path2D.Double();

        if (tl) {
            p.moveTo(x + r, y);
        } else {
            p.moveTo(x, y);
        }

        if (tr) {
            p.lineTo(x + w - r, y);
            p.quadTo(x + w, y, x + w, y + r);
        } else {
            p.lineTo(x + w, y);
        }

        if (br) {
            p.lineTo(x + w, y + h - r);
            p.quadTo(x + w, y + h, x + w - r, y + h);
        } else {
            p.lineTo(x + w, y + h);
        }

        if (bl) {
            p.lineTo(x + r, y + h);
            p.quadTo(x, y + h, x, y + h - r);
        } else {
            p.lineTo(x, y + h);
        }

        if (tl) {
            p.lineTo(x, y + r);
            p.quadTo(x, y, x + r, y);
        } else {
            p.lineTo(x, y);
        }

        p.closePath();
        return p;
    }

    public void setSelectedColors(Color bg, Color fg) {
        this.bgSelected = bg;
        this.fgSelected = fg;
        repaint();
    }

    public void setUnselectedColors(Color bg, Color fg) {
        this.bgUnselected = bg;
        this.fgUnselected = fg;
        repaint();
    }
}
