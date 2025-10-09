package Gui.CustomGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.Shape;

public class SegmentedTab extends JToggleButton {
    private boolean hover = false;

    // โค้งรายมุม
    private boolean roundTopLeft;
    private boolean roundTopRight;
    private boolean roundBottomLeft;
    private boolean roundBottomRight;

    private int arc;

    // สีตามสเปค
    private final Color bgUnselected = Color.WHITE;          // พื้นปกติ: ขาว
    private final Color fgUnselected = Color.GRAY;           // ตัวอักษรปกติ: เทา
    private final Color bgSelected   = Color.decode("#333333"); // พื้นเลือกอยู่: เทาเข้ม
    private final Color fgSelected   = Color.WHITE;          // ตัวอักษรเลือกอยู่: ขาว

    // คอนสตรัคเตอร์เก่า (left/right) -> map เป็นบน+ล่างของซ้าย/ขวา เพื่อคงความเข้ากันได้
    public SegmentedTab(String text, boolean roundLeft, boolean roundRight, int arc) {
        this(text, roundLeft, roundRight, roundLeft, roundRight, arc);
    }

    // คอนสตรัคเตอร์ใหม่: ระบุโค้งได้รายมุม
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

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
        });
    }

    // เปลี่ยนรัศมีโค้ง runtime ได้
    public void setArc(int arc) {
        this.arc = arc;
        revalidate();
        repaint();
    }

    // เปลี่ยนมุมโค้ง runtime ได้
    public void setCorners(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        this.roundTopLeft = topLeft;
        this.roundTopRight = topRight;
        this.roundBottomLeft = bottomLeft;
        this.roundBottomRight = bottomRight;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // เลือกสีตามสถานะ
        boolean sel = isSelected();
        Color bg, fg;
        if (sel && hover) {
            // hover ขณะเลือก: พื้นขาว ตัวหนังสือเทา
            bg = bgUnselected;
            fg = fgUnselected;
        } else if (sel) {
            // เลือกอยู่: พื้นเทาเข้ม ตัวหนังสือขาว
            bg = bgSelected;
            fg = fgSelected;
        } else {
            // ไม่ได้เลือก: พื้นขาว ตัวหนังสือเทา (hover ก็เหมือนกันตามที่กำหนด)
            bg = bgUnselected;
            fg = fgUnselected;
        }

        Shape shape = makeSegmentShape(0, 0, w, h, arc,
                roundTopLeft, roundTopRight, roundBottomLeft, roundBottomRight);

        // พื้นหลังตามรูปทรง
        g2.setColor(bg);
        g2.fill(shape);

        // ข้อความตรงกลาง
        FontMetrics fm = g2.getFontMetrics(getFont());
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(fg);
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }

    // วาด path ที่ “โค้งเฉพาะมุมที่สั่ง” เท่านั้น
    private Shape makeSegmentShape(int x, int y, int w, int h, int r,
                                   boolean tl, boolean tr, boolean bl, boolean br) {
        Path2D.Double p = new Path2D.Double();

        // start at top-left
        if (tl) {
            p.moveTo(x + r, y);
        } else {
            p.moveTo(x, y);
        }

        // top edge -> top-right
        if (tr) {
            p.lineTo(x + w - r, y);
            p.quadTo(x + w, y, x + w, y + r);
        } else {
            p.lineTo(x + w, y);
        }

        // right edge -> bottom-right
        if (br) {
            p.lineTo(x + w, y + h - r);
            p.quadTo(x + w, y + h, x + w - r, y + h);
        } else {
            p.lineTo(x + w, y + h);
        }

        // bottom edge -> bottom-left
        if (bl) {
            p.lineTo(x + r, y + h);
            p.quadTo(x, y + h, x, y + h - r);
        } else {
            p.lineTo(x, y + h);
        }

        // left edge -> back to top-left
        if (tl) {
            p.lineTo(x, y + r);
            p.quadTo(x, y, x + r, y);
        } else {
            p.lineTo(x, y);
        }

        p.closePath();
        return p;
    }
}
