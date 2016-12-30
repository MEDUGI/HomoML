package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public abstract class Layer {
    private Matrix weights;
    public abstract Matrix forwardPropagation(Matrix input);
    public abstract Matrix backPropagation(Matrix err);
}
