package test.semeionExample;

import basicUtils.featureExtractor.ContourExtractor;
import basicUtils.featureExtractor.FeatureExtractor;
import basicUtils.Matrix;
import basicUtils.featureExtractor.IdenticalExtractor;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;
import dataInterface.semeionInterfaces.MulticolumnDigitDataProvider;
import dataInterface.semeionInterfaces.SemeionDataProvider;
import examples.ReLUActivationFunction;
import examples.SigmoidActivationFunction;
import mlalgorithms.CNNetwork;
import mlalgorithms.FullConnectionLayer;

/**
 * Created by 李沅泽 on 2017/1/3.
 */
public class TestNN {
    public static void main(String[] args) throws Exception{
        FeatureExtractor featureExtractor = new IdenticalExtractor();
        SemeionDataProvider semeionDataProvider = new SemeionDataProvider("data\\semeion.data", featureExtractor);
        BasicDataProvider basicDataProvider = new BasicDataProvider(semeionDataProvider.getFeatureMatrix(), semeionDataProvider.getLabelMatrix());
        MulticolumnDigitDataProvider multicolumnDigitDataProvider = new MulticolumnDigitDataProvider(basicDataProvider);

        Matrix fullDataMatrix = multicolumnDigitDataProvider.getDataMatrix();
        int total = fullDataMatrix.getHeight();
        double sampleRate = 0.5;
        int trainNumber = (int)(total * sampleRate);
        int testNumber = trainNumber - 1;

        DataProvider trainDataProvider = new BasicDataProvider(fullDataMatrix.subMatrix(0, 0,
                fullDataMatrix.getWidth(), trainNumber), multicolumnDigitDataProvider.getLabelMatrix().subMatrix(0, 0,
                10, trainNumber));
        DataProvider testDataProvider = new BasicDataProvider(
                fullDataMatrix.subMatrix(0, trainNumber, fullDataMatrix.getWidth(), total),
                multicolumnDigitDataProvider.getLabelMatrix().subMatrix(0, trainNumber, 10, total)
        );

        CNNetwork NN = new CNNetwork();
        NN.insertLayer(new FullConnectionLayer(256,512, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(512,256, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(256,10,new SigmoidActivationFunction()));
        NN.setImgs(trainDataProvider);
        long startTime = System.currentTimeMillis();
        NN.train();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

        double[][] testResult = new double[testNumber][1];
        for(int i = 0; i < testNumber; i++) {
            Matrix oneResultVector = NN.test(testDataProvider.getDataMatrix().get(i));
            double tempMax = -10;
            int jTemp = 0;
            for(int j = 0; j < 10; j++) {
                if (oneResultVector.get(0,j)>tempMax) {
                    jTemp = j;
                    tempMax = oneResultVector.get(0,j);
                }
            }
            testResult[i][0] = jTemp;
            //testResult[i][0] = NN.test(testDataProvider.getDataMatrix().get(i));
        }
        System.out.println("The error rate is about " + testErrorRate(new Matrix(testResult), testDataProvider.getLabelMatrix()));
        // TODO: complete the Test on semeion.data
    }

    private static double testErrorRate(Matrix result, Matrix expected) {
        System.out.println(result.getSum());
        double sum = 0;
        int length = result.getHeight();
        for(int i = 0; i < length; i++) {
            if (expected.get(i, (int) Math.round(result.get(i, 0))) < 0.5)
                sum += 1;
        }
        return sum / length;
    }
}
