package mlalgorithms;

import basicUtils.BasicImageConvertor;
import basicUtils.Matrix;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public abstract class ConvolutionLayer implements Layer {
    int inputLength;
    int outputLength;
    int filterNum;
    int filterHeight;
    int filterWidth;

    public abstract double isConvergence();

    public Matrix forwardPropagation(Matrix input) throws Exception{
        return null;
    }

    public abstract Matrix backPropagation(Matrix err);
}