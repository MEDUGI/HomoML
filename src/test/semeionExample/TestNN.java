package test.semeionExample;

import basicUtils.ContourExtractor;
import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;
import dataInterface.OneNumberSensitiveSemeion;
import dataInterface.SemeionDataProvider;
import examples.RBFKernel;
import examples.ReLUActivationFunction;
import mlalgorithms.CNNetwork;
import mlalgorithms.FullConnectionLayer;
import mlalgorithms.SupportVectorMachine;

/**
 * Created by 李沅泽 on 2017/1/3.
 */
public class TestNN {
    public static void main(String[] args) throws Exception{
        SemeionDataProvider semeionDataProvider = new SemeionDataProvider("data\\semeion.data", new ContourExtractor(16,16));

        Matrix fullDataMatrix = semeionDataProvider.getDataMatrix();
        int total = fullDataMatrix.getHeight();
        double sampleRate = 0.5;
        int trainNumber = (int)(total * sampleRate);
        int testNumber = trainNumber - 1;

        DataProvider trainDataProvider = new BasicDataProvider(fullDataMatrix.subMatrix(0, 0,
                fullDataMatrix.getWidth(), trainNumber), semeionDataProvider.getLabelMatrix().subMatrix(0, 0,
                1, trainNumber));
        DataProvider testDataProvider = new BasicDataProvider(
                fullDataMatrix.subMatrix(0, trainNumber, fullDataMatrix.getWidth(), total),
                semeionDataProvider.getLabelMatrix().subMatrix(0, trainNumber, 1, total)
        );

        CNNetwork NN = new CNNetwork();
        NN.insertLayer(new FullConnectionLayer(64,128, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(128,10,new ReLUActivationFunction()));
        NN.setImgs(trainDataProvider);
        NN.train();

        double[][] testResult = new double[testNumber][1];
        for(int i = 0; i < testNumber; i++) {
            testResult[i][0] = NN.test(testDataProvider.getDataMatrix().get(i));
        }
        System.out.println("The error rate is about " + testErrorRate(new Matrix(testResult), testDataProvider.getLabelMatrix()));
        // TODO: complete the Test on semeion.data
    }

    private static double testErrorRate(Matrix result, Matrix expected) {
        double sum = 0;
        int length = result.getHeight();
        for(int i = 0; i < length; i++) {
            if (result.get(i,0) * expected.get(i,0) < 0)
                sum += 1;
        }
        return sum / length;
    }
}
