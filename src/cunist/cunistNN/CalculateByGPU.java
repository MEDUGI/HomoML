package cunist.cunistNN;

import basicUtils.BasicImageConvertor;
import basicUtils.featureExtractor.ContourExtractor;
import basicUtils.featureExtractor.FeatureExtractor;
import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
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

    public CalculateByGPU(){
        SemeionDataProvider semeionDataProvider = new SemeionDataProvider("data\\semeion.data", new ContourExtractor(16,16));
        BasicDataProvider basicDataProvider = new BasicDataProvider(semeionDataProvider.getFeatureMatrix(), semeionDataProvider.getLabelMatrix());
        MulticolumnDigitDataProvider multicolumnDigitDataProvider = new MulticolumnDigitDataProvider(basicDataProvider);
        NN = new CNNetwork();
        NN.insertLayer(new FullConnectionLayer(64,128, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(128,64, new ReLUActivationFunction()));
        NN.insertLayer(new FullConnectionLayer(64,10,new SigmoidActivationFunction()));
        NN.setImgs(multicolumnDigitDataProvider);
        try {
            NN.train();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){
        FeatureExtractor featureExtractor = new ContourExtractor(16,16);
        BasicImageConvertor basicImageConvertor = new BasicImageConvertor(16,16);
        Matrix feature = featureExtractor.dataToFeature(basicImageConvertor.toRowMatrix(matrix));
        for (int i = 0; i<= 9;i++) {
            pros[i] = NN.test(feature).get(0,i);
        }
        return 1;
    }
}
