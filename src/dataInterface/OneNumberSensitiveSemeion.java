package dataInterface;

import basicUtils.FeatureExtractor;
import basicUtils.Matrix;

/**
 * Created by Xiangxi on 2016/12/31.
 * Contact him on zxx_1996@qq.com
 */
public class OneNumberSensitiveSemeion extends SemeionDataProvider{
    public OneNumberSensitiveSemeion(String filepath, int digit) {
        super(filepath);
        if (digit < 0)
            digit = 0;
        if (digit > 9)
            digit = 9;
        this.digit = digit;
    }
    public OneNumberSensitiveSemeion(String filepath, FeatureExtractor featureExtractor, int digit) {
        super(filepath, featureExtractor);
        if (digit < 0)
            digit = 0;
        if (digit > 9)
            digit = 9;
        this.digit = digit;
    }

    public int digit;
    public static final double eps = 1e-1;

    @Override
    protected void readFromFile(String filepath) {
        super.readFromFile(filepath);
        double[][] data = labelMatrix.getData();
        for(int i = 0; i < labelMatrix.getHeight(); i++)
            for(int j = 0; j < labelMatrix.getWidth(); j++) {
                if (Math.abs(data[i][j]- digit)<eps)
                    data[i][j] = 1;
                else
                    data[i][j] = -1;
            }
        labelMatrix = new Matrix(data);
    }
}
