package Gui.CustomGui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class TopRoundedPanel extends JPanel {
    private int arc;

    public TopRoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false); // เราจะระบายพื้นเองตามทรงโค้ง
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

        // โค้งเฉพาะ "มุมบนซ้าย/บนขวา" ด้านล่างเป็นเส้นตรงสี่เหลี่ยม
        Path2D p = new Path2D.Double();
        p.moveTo(r, 0);
        p.lineTo(w - r, 0);
        p.quadTo(w, 0, w, r); // top-right arc
        p.lineTo(w, h); // right edge (ตรง)
        p.lineTo(0, h); // bottom edge (ตรง)
        p.lineTo(0, r);
        p.quadTo(0, 0, r, 0); // top-left arc
        p.closePath();

        g2.setColor(getBackground());
        g2.fill(p);
        g2.dispose();
    }
}
