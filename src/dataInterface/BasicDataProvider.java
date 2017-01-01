package dataInterface;

import basicUtils.Matrix;

/**
 * Created by Xiangxi on 2016/12/31.
 * Contact him on zxx_1996@qq.com
 */
public class BasicDataProvider implements DataProvider{
    Matrix dataMatrix;
    Matrix labelMatrix;
    boolean isReady;

    public BasicDataProvider(Matrix dataMatrix, Matrix labelMatrix) {
        isReady = false;
        this.dataMatrix = dataMatrix;
        this.labelMatrix = labelMatrix;
        isReady = true;
    }

    @Override
    public Matrix getDataMatrix() {
        return dataMatrix;
    }

    @Override
    public Matrix getLabelMatrix() {
        return labelMatrix;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }
}
