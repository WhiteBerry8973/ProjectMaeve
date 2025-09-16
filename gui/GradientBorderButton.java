package gui;
import javax.swing.*;
import java.awt.*;

public class GradientBorderButton extends JToggleButton {
    public GradientBorderButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;

        // background
        g2.setColor(new Color(30, 30, 30));
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        // gradient border
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(255, 255, 255, 150),
            0, (int)(h * 0.7), new Color(51, 51, 51)
        );
        g2.setPaint(gp);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
