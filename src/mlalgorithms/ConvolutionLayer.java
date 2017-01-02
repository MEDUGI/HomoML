package mlalgorithms;

import basicUtils.BasicImageConvertor;
import basicUtils.Matrix;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by 李沅泽 on 2016/12/31.
 */
public abstract class ConvolutionLayer implements Layer {
    int channelNum;
    int filterNum;
    int filterHeight;
    int filterWidth;

    public abstract double isConvergence();

    public abstract Matrix forwardPropagation(Matrix input) throws Exception;

    public abstract Matrix backPropagation(Matrix err);
}