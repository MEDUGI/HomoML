package cunist;

import basicUtils.Matrix;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * Created by ZouKaifa on 2015/11/21.
 */
public class WriteCanvas extends Canvas{
    private static int widthAndHeight = Const.widthAndHeight;
    private static int canvasWidth = Const.canvasWidth;
    private CalculateByGPU calculateByGPU;
    private BasicStroke stroke;

    public int[] prePoints;
    public int[] nowPoints;
    private boolean first = true;
    private final CanvasListener canvasListener;
    DotMatrixCanvas dotMatrixCanvas;
    private final MainGraphic mainGra;


    public WriteCanvas(DotMatrixCanvas dotMatrixCanvas, MainGraphic mainGra){
        super();
        try {
            calculateByGPU = new CalculateByGPU();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prePoints = new int[2];
        nowPoints = new int[2];
        this.dotMatrixCanvas = dotMatrixCanvas;
        this.mainGra = mainGra;
        stroke = new BasicStroke(0xf);
        setBackground(Color.lightGray);
        canvasListener = new CanvasListener(this);
        addMouseListener(canvasListener);
        addMouseMotionListener(canvasListener);
        double[] r = new double[10];
        byte[] b = new byte[widthAndHeight*widthAndHeight];
        calculateByGPU.get(b, r);
    }
    public void clear(){
        Graphics2D graphics2D = (Graphics2D) getGraphics();
        graphics2D.clearRect(0, 0, canvasWidth, canvasWidth);
        if (canvasListener.newImage != null) {
            canvasListener.newImage.getGraphics().clearRect(0, 0, widthAndHeight, widthAndHeight);
            canvasListener.image.getGraphics().clearRect(0, 0, canvasWidth, canvasWidth);
            dotMatrixCanvas.clear();
        }
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(Color.red);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(8f));
        if (! first){
            graphics2D.drawLine(prePoints[0], prePoints[1], nowPoints[0], nowPoints[1]);
        }
        first = false;
    }

    class CanvasListener implements MouseMotionListener, MouseListener {

        private final WriteCanvas canvas;
        private boolean first;
        private BufferedImage image, newImage;

        public CanvasListener(WriteCanvas canvas) {
            this.canvas = canvas;
            this.first = true;
            this.image = new BufferedImage(canvasWidth, canvasWidth, BufferedImage.TYPE_BYTE_GRAY);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            canvas.nowPoints[0] = e.getX();
            canvas.nowPoints[1] = e.getY();
            if (!first) {
                canvas.paint(canvas.getGraphics());
                Graphics2D graphics2D = (Graphics2D) image.getGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setStroke(stroke);
                graphics2D.drawLine(canvas.prePoints[0], canvas.prePoints[1],
                        canvas.nowPoints[0], canvas.nowPoints[1]);
            }
            canvas.prePoints[0] = canvas.nowPoints[0];
            canvas.prePoints[1] = canvas.nowPoints[1];
            first = false;
            try {
                newImage = new BufferedImage(widthAndHeight, widthAndHeight, BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D graphics2D = (Graphics2D) newImage.getGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.drawImage(image, 0, 0, widthAndHeight, widthAndHeight,
                        null);
                Graphics2D graphics2D1 = (Graphics2D) canvas.getGraphics();
                graphics2D1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D1.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D1.drawImage(newImage, 0, 0, null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }


            byte[] grayByte = new byte[widthAndHeight*widthAndHeight];
            for (int i = 0; i < newImage.getHeight(); i++) {
                for (int j = 0; j < newImage.getWidth(); j++) {
                    int gr = new Color(newImage.getRGB(j, i)).getGreen();
                    if (gr != 0 ){
                        dotMatrixCanvas.madeRed(j+1, i+1);
                    }
                    grayByte[i*widthAndHeight+j] = (byte)gr;
                }
            }

            Matrix test;
            test = TurnGraphToMatrix();

            int max = 0;
            double[] pro = new double[10];
            int sta = calculateByGPU.get(test, pro);
            for (int i = 0; i < 10; i++) {
                Color c = new Color(255, 255 - (int) (255 * pro[i]), 255 - (int) (255 * pro[i]));
                mainGra.numberJLabels[i].setForeground(c);
                DecimalFormat df = new DecimalFormat("0.000000000000");
                mainGra.proJLabels[i].setText(df.format(pro[i]));
                if (pro[i] > pro[max]) {
                    max = i;
                }
            }
            mainGra.resultJLabel.setText(String.valueOf(max));
        }


        public Matrix TurnGraphToMatrix () {
            double[][] ans = new double[16][16];
            for (int i = 0; i < newImage.getHeight(); i++) {
                for (int j = 0; j < newImage.getWidth(); j++) {
                    int gr = new Color(newImage.getRGB(j, i)).getGreen();
                    ans[i][j] = gr == 0? 0 : 1;
                }
            }
            return new Matrix(ans);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            canvas.getGraphics().fillOval(x, y, 1, 1);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            first = true;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }
}
