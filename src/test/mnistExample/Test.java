package test.mnistExample;

import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.mnistInterfaces.MnistDataProvider;
import dataInterface.semeionInterfaces.MulticolumnDigitDataProvider;
import examples.ReLUActivationFunction;
import examples.SigmoidActivationFunction;
import mlalgorithms.CNNetwork;
import mlalgorithms.FullConnectionLayer;

/**
 * Created by Xiangxi on 2016/12/28.
 * Contact him on zxx_1996@qq.com
 */
public class Test {
    public static void main(String[] args) {
        MnistDataProvider mnistTrainData = new MnistDataProvider("data\\train-images.idx3-ubyte",
                "data\\train-labels.idx1-ubyte");
        MnistDataProvider mnistTestData = new MnistDataProvider("data\\t10k-images.idx3-ubyte",
                "data\\t10k-labels.idx1-ubyte");
        BasicDataProvider basicTrain = new BasicDataProvider(mnistTrainData);
        BasicDataProvider basicTest = new BasicDataProvider(mnistTestData);
        MulticolumnDigitDataProvider multiTrain = new MulticolumnDigitDataProvider(basicTrain);
        MulticolumnDigitDataProvider multiTest = new MulticolumnDigitDataProvider(basicTest);

        CNNetwork NN = new CNNetwork();
        NN.insertLayer(new FullConnectionLayer(784,196, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(196,200, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(200,10,new SigmoidActivationFunction()));
        NN.setImgs(multiTrain);
        NN.train();

        int testNumber = multiTest.getLabelMatrix().getHeight();
        double[][] testResult = new double[testNumber][1];
        for(int i = 0; i < testNumber; i++) {
            Matrix oneResultVector = NN.test(multiTest.getDataMatrix().get(i));
            double tempMax = -10;
            int jTemp = 0;
            for(int j = 0; j < 10; j++) {
                if (oneResultVector.get(0,j)>tempMax) {
                    jTemp = j;
                    tempMax = oneResultVector.get(0,j);
                }
            }
            testResult[i][0] = jTemp;
        }
        System.out.println("The error rate is about " + testErrorRate(new Matrix(testResult), multiTest.getLabelMatrix()));
    }

    private static double testErrorRate(Matrix result, Matrix expected) {
        double sum = 0;
        int length = result.getHeight();
        for(int i = 0; i < length; i++) {
            if (expected.get(i, (int) Math.round(result.get(i, 0))) < 0.5)
                sum += 1;
        }
        return sum / length;
    }
}
