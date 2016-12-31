package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public abstract class ConvKernel {
    public int height;
    public int width;
    public abstract double convolution(Matrix section);
}
