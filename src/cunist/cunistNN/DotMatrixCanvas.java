package cunist.cunistNN;

import java.awt.*;

/**
 * Created by ZouKaifa on 2015/12/1.
 */
public class DotMatrixCanvas extends Canvas {
    private static int width = 16;
    private int x;
    private int y;
    private boolean should = false;

    public DotMatrixCanvas() {
        super();
        setBackground(Color.WHITE);
    }

    public void madeRed(int x, int y) {
        this.x = x;
        this.y = y;
        paint(getGraphics());
    }

    public void clear() {
        Graphics2D graphics2D = (Graphics2D) getGraphics();
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                graphics2D.setColor(Color.WHITE);
                int startX = (int) (i * w * 1.0 / (width)) + 1;
                int startY = (int) (j * h * 1.0 / (width)) + 1;
                int wid = (int) (w * 1.0 / width) - 1;
                int hei = (int) (h * 1.0 / width) - 1;
                if (startX + wid >= w) {
                    wid -= 1;
                }
                if (startY + hei >= h) {
                    hei -= 1;
                }
                graphics2D.fillRect(startX, startY, wid, hei);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(0.5f));
        for (int i = 0; i < width + 1; i++) {
            int ww = (int) (i * w * 1.0 / (width));
            int hh = (int) (i * h * 1.0 / (width));
            graphics2D.drawLine(ww, 0, ww, h);
            graphics2D.drawLine(0, hh, w, hh);
        }
        graphics2D.drawLine(w - 1, 0, w - 1, h);
        graphics2D.drawLine(0, h - 1, w, h - 1);
        if (should) {
            graphics2D.setColor(Color.RED);
            int startX = (int) ((x - 1) * w * 1.0 / (width)) + 1;
            int startY = (int) ((y - 1) * h * 1.0 / (width)) + 1;
            int wid = (int) (w * 1.0 / width) - 1;
            int hei = (int) (h * 1.0 / width) - 1;
            if (startX + wid >= w) {
                wid -= 1;
            }
            if (startY + hei >= h) {
                hei -= 1;
            }
            graphics2D.fillRect(startX, startY, wid, hei);
        }
        should = true;
    }
}
