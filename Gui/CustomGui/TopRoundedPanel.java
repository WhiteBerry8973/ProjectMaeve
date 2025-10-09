package Gui.CustomGui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class TopRoundedPanel extends JPanel {
    private int arc;

    public TopRoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight(), r = arc;

        Path2D p = new Path2D.Double();
        p.moveTo(r, 0);
        p.lineTo(w - r, 0);
        p.quadTo(w, 0, w, r);
        p.lineTo(w, h);
        p.lineTo(0, h);
        p.lineTo(0, r);
        p.quadTo(0, 0, r, 0);
        p.closePath();

        g2.setColor(getBackground());
        g2.fill(p);
        g2.dispose();
    }
}
