package basicUtils.featureExtractor;

import basicUtils.Matrix;

/**
 * Created by Xiangxi on 2016/12/27.
 * Contact him on zxx_1996@qq.com
 */
public class ContourExtractor implements FeatureExtractor{
    private int imageHeight;
    private int imageWidth;
    private static final double eps = 1e-6;
    public ContourExtractor(int imageHeight, int imageWidth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    @Override
    public Matrix dataToFeature(Matrix rawData) {
        int dataLength = rawData.getWidth();
        int dataNumber = rawData.getHeight();
        try {
            Matrix temp = transformOnOneEntry(rawData.subMatrix(0, 0, dataLength, 1));
            Matrix result = new Matrix(dataNumber, temp.getWidth());
            result.set(0, temp);
            for (int i = 1; i < dataNumber; i++) {
                Matrix transferedResult = transformOnOneEntry(rawData.subMatrix(0, i, dataLength, i + 1));
                result.set(i, transferedResult);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix transformOnOneEntry(Matrix line) throws Exception{
        if (line.getWidth() != imageHeight * imageWidth)
            throw new Exception("Image size doesn't match.");
        double[][] rawData = line.getData();
        double[][] imageData = new double[imageHeight][imageWidth];
        int k = 0;
        for(int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                imageData[i][j] = rawData[0][k];
                k++;
            }
        }
        double[][] featureData = new double[1][2 * imageHeight + 2 * imageWidth];
        k = 0;
        for(int i = 0; i < imageHeight; i++) {
            int count = 0;
            for(int j = 0; j < imageWidth; j++) {
                if (Math.abs(imageData[i][j]) > eps)
                    break;
                count++;
            }
            featureData[0][k] = count / 16.0;
            k++;
            count = 0;
            for(int j = imageWidth-1; j >= 0; j--) {
                if (Math.abs(imageData[i][j]) > eps)
                    break;
                count++;
            }
            featureData[0][k] = count / 16.0;
            k++;
        }
        for(int i = 0; i < imageWidth; i++) {
            int count = 0;
            for(int j = 0; j < imageHeight; j++) {
                if (Math.abs(imageData[j][i]) > eps)
                    break;
                count++;
            }
            featureData[0][k] = count / 16.0;
            k++;
            count = 0;
            for(int j = imageHeight-1; j >= 0; j--) {
                if (Math.abs(imageData[j][i]) > eps)
                    break;
                count++;
            }
            featureData[0][k] = count / 16.0;
            k++;
        }
        if (k != 2 * imageHeight + 2 * imageWidth)
            throw new Exception("The k is added wrong times!");
        return new Matrix(featureData);
    }
}
