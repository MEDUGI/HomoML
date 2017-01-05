package mlalgorithms;

import basicUtils.Matrix;
import dataInterface.DataProvider;
import dataInterface.ImageDataProvider;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * @author 李沅泽 on 2016/12/6.
 * 搭建一个层内含方法的前向感知网络，需要使用反向传播算法优化参数。
 * 属性layers 包含一系列层，层之间以单纯的输入输出关系链接，层的实现为两个单纯的函数。
 * 尝试在类内部赋予方法的写法。
 */
public class CNNetwork {
    private DataProvider imgs;
    private ArrayList<Layer> layers;
    private double tol = 1e-8;
    private int batchSize = 100;

    public CNNetwork() {
        layers = new ArrayList<Layer>();
    }

    public CNNetwork(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    /**
     * 这个方法用于初始化之后开始训练。
     * 首先检查layers不为空。
     * 然后开始前向循环。
     */
    public int train() throws Exception{
        int length = layers.size();
        Matrix output = new Matrix();
        Matrix input;
        Matrix error;
        boolean isContinued = true;
        int rank = 0;

        if (layers.size() == 0) {
            System.err.println("CNNetwork.java : layers为空，无法训练。");
            return -1;
        }
        while (isContinued) {
            for (int j = 0;j<batchSize;j++) {
                rank = (int) Math.floor(Math.random() * imgs.getDataMatrix().getHeight());
                input = imgs.getDataMatrix().get(rank);
                for (int i = 0; i < length; i++) {
                    Layer temp = layers.get(i);
                    output = temp.forwardPropagation(input);
                    input = output;
                }
                error = output.sub(imgs.getLabelMatrix().get(rank));
                for (int i = length - 1; i >= 0; i--) {
                    Layer temp = layers.get(i);
                    error = temp.backPropagation(error);
                }
            }
            for (int i = 0;i < length;i++) {
                Layer temp = layers.get(i);
                temp.updateWeights(batchSize);
            }
            isContinued = !isConvergence();
        }
        return 0;
    }

    public Matrix test(Matrix input) throws Exception{
        int length = layers.size();
        Matrix output = new Matrix();
        for (int i = 0;i<length;i++) {
            Layer temp = layers.get(i);
            output = temp.forwardPropagation(input);
            input = output;
        }
        return output.copy();
    }

    private boolean isConvergence() {
        for (int i = 0; i < layers.size();i++) {
            if (layers.get(i).isConvergence() > tol) {
                return false;
            }
        }
        return true;
    }

    public int insertLayer(Layer layer) {
        layers.add(layer);
        return 0;
    }

    public int insertLayer(int order,Layer layer) {
        layers.add(order,layer);
        return 0;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public double getTol() {
        return tol;
    }

    public void setTol(double tol) {
        this.tol = tol;
    }

    public DataProvider getImgs() {
        return imgs;
    }

    public void setImgs(DataProvider imgs) {
        this.imgs = imgs;
    }
}
