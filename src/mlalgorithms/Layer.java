package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public interface Layer {
    public abstract double isConvergence();
    public abstract Matrix forwardPropagation(Matrix input) throws Exception;
    public abstract Matrix backPropagation(Matrix err) throws Exception;
    public abstract void updateWeights(int batchSize);
}
