package cunist.mnist.cunistNN;

import basicUtils.BasicImageConvertor;
import basicUtils.featureExtractor.ContourExtractor;
import basicUtils.featureExtractor.FeatureExtractor;
import basicUtils.Matrix;
import basicUtils.featureExtractor.IdenticalExtractor;
import dataInterface.BasicDataProvider;
import dataInterface.mnistInterfaces.MnistDataProvider;
import dataInterface.semeionInterfaces.MulticolumnDigitDataProvider;
import dataInterface.semeionInterfaces.SemeionDataProvider;
import examples.ReLUActivationFunction;
import examples.SigmoidActivationFunction;
import mlalgorithms.CNNetwork;
import mlalgorithms.FullConnectionLayer;

/**
 * Created by Xiangxi on 2017/1/6.
 * Contact him on zxx_1996@qq.com
 */
public class CalculateByGPU {
    CNNetwork NN;
    FeatureExtractor featureExtractor = new IdenticalExtractor();
    public CalculateByGPU(){
        MnistDataProvider mnistTrainData = new MnistDataProvider("data\\train-images.idx3-ubyte",
                "data\\train-labels.idx1-ubyte");
        BasicDataProvider basicTrain = new BasicDataProvider(mnistTrainData);
        MulticolumnDigitDataProvider multiTrain = new MulticolumnDigitDataProvider(basicTrain);

        NN = new CNNetwork();
        NN.insertLayer(new FullConnectionLayer(784,392, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(392,184, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(184,10,new SigmoidActivationFunction()));
        NN.setImgs(multiTrain);
        NN.train();
    }

    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){
        BasicImageConvertor basicImageConvertor = new BasicImageConvertor(28,28);
        Matrix feature = featureExtractor.dataToFeature(basicImageConvertor.toRowMatrix(matrix));
        for (int i = 0; i<= 9;i++) {
            pros[i] = NN.test(feature).get(0,i);
        }
        return 1;
    }
}
