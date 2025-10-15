package Gui.CustomGui;

import javax.swing.*;

import Gui.MainGui.Ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedToggleButton extends JToggleButton {
    public enum Orientation {
        TOP_BOTTOM, LEFT_RIGHT, RIGHT_LEFT
    }

    private boolean hover, pressed;
    private final int arc;
    private final float stroke;
    private final boolean drawBorder;

    private final Color fillNormal, fillHover, fillPressed, fillDisabled;
    private final Color fillSelected, fillSelectedHover, fillSelectedPressed;

    private final Color textNormal, textHover, textPressed, textDisabled;
    private final Color textSelected, textSelectedHover, textSelectedPressed;

    private final Color borderC1, borderC2;
    private final Orientation orientation;

    public RoundedToggleButton(
            String text,
            int arc, float stroke, boolean drawBorder,
            Color fillNormal, Color fillHover, Color fillPressed, Color fillDisabled,
            Color fillSelected, Color fillSelectedHover, Color fillSelectedPressed,
            Color textNormal, Color textHover, Color textPressed, Color textDisabled,
            Color textSelected, Color textSelectedHover, Color textSelectedPressed,
            Color borderC1, Color borderC2, Orientation orientation) {
        super(text);
        this.arc = arc;
        this.stroke = stroke;
        this.drawBorder = drawBorder;

        this.fillNormal = fillNormal;
        this.fillHover = fillHover;
        this.fillPressed = fillPressed;
        this.fillDisabled = fillDisabled;

        this.fillSelected = fillSelected;
        this.fillSelectedHover = fillSelectedHover;
        this.fillSelectedPressed = fillSelectedPressed;

        this.textNormal = textNormal;
        this.textHover = textHover;
        this.textPressed = textPressed;
        this.textDisabled = textDisabled;

        this.textSelected = textSelected;
        this.textSelectedHover = textSelectedHover;
        this.textSelectedPressed = textSelectedPressed;

        this.borderC1 = borderC1;
        this.borderC2 = borderC2;
        this.orientation = orientation;

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean sel = isSelected();
        Color body, tcol;

        if (!isEnabled()) {
            body = fillDisabled;
            tcol = textDisabled;
        } else if (sel) {
            body = Ui.BROWN_DARK;
            tcol = Ui.WHITE;
        } else if (pressed) {
            body = Ui.BROWN_DARK;
            tcol = Ui.WHITE;
        } else if (hover) {
            body = Ui.BROWN;
            tcol = Ui.WHITE;
        } else {
            body = fillNormal;
            tcol = textNormal;
        }

        g2.setColor(body);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

        if (drawBorder && stroke > 0f) {
            Paint p;
            if (orientation == Orientation.TOP_BOTTOM) {
                p = new GradientPaint(0, 0, borderC1, 0, (int) (h * 0.7f), borderC2);
            } else {
                boolean reverse = (orientation == Orientation.RIGHT_LEFT);
                Color cStart;
                Color cEnd;

                if (reverse) {
                    cStart = borderC2;
                    cEnd = borderC1;
                } else {
                    cStart = borderC1;
                    cEnd = borderC2;
                }

                p = new GradientPaint(0, 0, cStart, w, 0, cEnd);
            }
            g2.setPaint(p);
            g2.setStroke(new BasicStroke(stroke));
            g2.draw(new RoundRectangle2D.Float(stroke / 2f, stroke / 2f, w - stroke, h - stroke, arc, arc));
        }

        paintContent(g2, tcol);
        g2.dispose();
    }

    protected void paintContent(Graphics2D g2, Color textColor) {
        g2.setFont(getFont());
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(getText())) / 2;
        int ty = (getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(getText(), tx, ty);
    }
}
