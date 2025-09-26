package Gui.CustomGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    public enum Orientation {
        TOP_BOTTOM, LEFT_RIGHT, RIGHT_LEFT
    }

    private boolean hover, pressed;
    private final int arc;
    private final float stroke;
    private final boolean drawBorder;

    private final Color fillNormal, fillHover, fillPressed, fillDisabled;
    private final Color textNormal, textHover, textPressed, textDisabled;

    private final Color borderC1, borderC2;
    private final Orientation orientation;

    public RoundedButton(
            String text,
            int arc, float stroke, boolean drawBorder,
            Color fillNormal, Color fillHover, Color fillPressed, Color fillDisabled,
            Color textNormal, Color textHover, Color textPressed, Color textDisabled,
            Color borderC1, Color borderC2, Orientation orientation) {
        super(text);
        this.arc = arc;
        this.stroke = stroke;
        this.drawBorder = drawBorder;

        this.fillNormal = fillNormal;
        this.fillHover = fillHover;
        this.fillPressed = fillPressed;
        this.fillDisabled = fillDisabled;

        this.textNormal = textNormal;
        this.textHover = textHover;
        this.textPressed = textPressed;
        this.textDisabled = textDisabled;

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
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color body, tcol;

        if (!isEnabled()) {
            body = fillDisabled;
            tcol = textDisabled;
        }
        else if ((getModel().isArmed() && getModel().isPressed() || getModel().isSelected()) && hover) {
            body = Color.decode("#2a2a2a");
            tcol = Color.decode("#d9d9d9");
        } else if (getModel().isArmed() && getModel().isPressed() || getModel().isSelected()) {
            body = fillPressed;
            tcol = textPressed;
        } else if (hover) {
            body = fillHover;
            tcol = textHover;
        } else {
            body = fillNormal;
            tcol = textNormal;
        }

        // background
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
