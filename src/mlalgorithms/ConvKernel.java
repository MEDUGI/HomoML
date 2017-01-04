package mlalgorithms;

import basicUtils.Matrix;
import java.lang.Math;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public class ConvKernel {
    public int size;
    private Matrix weights;
    private double bias;
    public ConvKernel(int length) {
        size = length;
        weights = new Matrix(size,size);
        for (int i = 0;i<size;i++) {
            for (int j = 0;j<size;j++) {
                weights.set(i,j,Math.random());
            }
        }
        bias = Math.random();
    }
    public ConvKernel(int length, Matrix weights) {
        size = length;
        this.weights = weights;
        bias = Math.random();
    }

    public void updateBias(Matrix mat) {
        bias += mat.getSum();
    }

    public Matrix convolutionFull(Matrix mat) {
        Matrix result = new Matrix();
        return result;
    }
    public Matrix convolutionValid(Matrix mat) {
        Matrix result = new Matrix();
        return result;
    }

    public Matrix convolutionFullWithRotate(Matrix mat) {
        weights.rot180();
        Matrix result = convolutionFull(mat);
        weights.rot180();
        return result;
    }

    public void updateWeights(Matrix theta) {
        weights.sub(theta);
    }

    public double getBias() {
        return bias;
    }
}
