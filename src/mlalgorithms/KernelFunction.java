package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2016/12/28.
 */
public interface KernelFunction {
    public abstract double cal(Matrix x, Matrix y);
}
