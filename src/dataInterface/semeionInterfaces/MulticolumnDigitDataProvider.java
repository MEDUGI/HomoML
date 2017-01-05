package dataInterface.semeionInterfaces;

import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;

/**
 * Created by Xiangxi on 2017/1/5.
 * Contact him on zxx_1996@qq.com
 */
public class MulticolumnDigitDataProvider implements  DataProvider{
    Matrix dataMatrix;
    Matrix labelMatrix;
    boolean isReady;

    // assumes that the dataProvider has label matrix as a column vector ranging 0.0,1.0,...,9.0
    public MulticolumnDigitDataProvider(DataProvider dataProvider) {
        isReady = false;
        dataMatrix = dataProvider.getDataMatrix();
        labelMatrix = dataProvider.getLabelMatrix();
        double[][] newLabel = new double[dataMatrix.getHeight()][10];
        for(int i = 0; i < dataMatrix.getHeight(); i++) {
            newLabel[i][(int)Math.round(labelMatrix.get(i,0))] = 1.0;
        }
        labelMatrix = new Matrix(newLabel);
        isReady = true;
    }

    @Override
    public Matrix getLabelMatrix() {
        return labelMatrix;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public Matrix getDataMatrix() {
        return dataMatrix;
    }
}
