package cunist.semeion.cunistSVM;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Created by ZouKaifa on 2015/12/1.
 */
public class MainGraphic extends JFrame {
    private static int n = 10;
    private JPanel rootPanel;
    private WriteCanvas writeCanvas;  //手写
    private DotMatrixCanvas dotMatrixCanvas;  //点
    JLabel resultJLabel;  //结果
    JLabel[] numberJLabels;  //十个数字
    JLabel[] proJLabels;  //概率
    private JButton clearButton;  //清除

    public MainGraphic() {
        super("手写数字识别");
        this.initComponents();
        this.setAttr();
        this.addComponents();
        this.addListener();
        this.setContentPane(rootPanel);
        this.setBounds(200, 150, 600, 288);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

    }

    private void initComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rootPanel = new JPanel();
        dotMatrixCanvas = new DotMatrixCanvas();
        writeCanvas = new WriteCanvas(dotMatrixCanvas, this);
        resultJLabel = new JLabel("?", JLabel.CENTER);
        proJLabels = new JLabel[n];
        numberJLabels = new JLabel[n];
        for (int i = 0; i < n; i++) {
            numberJLabels[i] = new JLabel(String.valueOf(i), JLabel.CENTER);
            proJLabels[i] = new JLabel("0.000000000000", JLabel.LEFT);
        }
        clearButton = new JButton("清空");
    }

    private void setAttr() {
        rootPanel.setLayout(null);
        writeCanvas.setBounds(50, 50, Const.canvasWidth, Const.canvasWidth);
        dotMatrixCanvas.setBounds(286, 114, Const.dotConvaswidth, Const.dotConvaswidth);
        clearButton.setBounds(285, 30, 60, 45);
        clearButton.setFocusPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setFont(new Font("楷体", Font.BOLD, 15));
        clearButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        resultJLabel.setBounds(355, 20, 70, 70);
        resultJLabel.setFont(new Font("黑体", Font.BOLD, 60));
        resultJLabel.setBorder(BorderFactory.createLineBorder(Color.blue));
        for (int i = 0; i < n; i++) {
            numberJLabels[i].setBounds(450, 3+i*26, 24, 24);
            numberJLabels[i].setFont(new Font("黑体", Font.BOLD, 18));
            proJLabels[i].setBounds(478, 3+i*26, 100, 24);
            proJLabels[i].setFont(new Font("黑体", Font.PLAIN, 14));
        }
    }

    private void addComponents() {
        rootPanel.add(writeCanvas);
        rootPanel.add(dotMatrixCanvas);
        rootPanel.add(clearButton);
        rootPanel.add(resultJLabel);
        for (int i = 0; i < n; i++) {
            rootPanel.add(numberJLabels[i]);
            rootPanel.add(proJLabels[i]);
        }
    }

    private void addListener(){
        clearButton.addMouseListener(new Cursor());
        clearButton.addActionListener((ActionEvent e)->{
            resultJLabel.setText("?");
            writeCanvas.clear();
            for (int i = 0; i < n; i++) {
                numberJLabels[i].setForeground(Color.BLACK);
                proJLabels[i].setText(String.valueOf("0.000000000000"));
            }
        });
    }

    class Cursor extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent event){
            MainGraphic.this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MainGraphic.this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
}
