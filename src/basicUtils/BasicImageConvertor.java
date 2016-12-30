package basicUtils;

import dataInterface.ImageConvertor;

/**
 * Created by Xiangxi on 2016/12/31.
 * Contact him on zxx_1996@qq.com
 */
public class BasicImageConvertor implements ImageConvertor{
    public BasicImageConvertor(int height, int width) {
        this.height = height;
        this.width = width;
    }
    int height, width;

    @Override
    public Matrix toImageMatrix(Matrix rowMatrix) {
        double[][] row = rowMatrix.getData();
        double[][] image = new double[height][width];
        int k = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                image[i][j] = row[0][k];
                k++;
            }
        }
        return new Matrix(image);
    }
}
