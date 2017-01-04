package cunist;

import basicUtils.BasicImageConvertor;
import basicUtils.ContourExtractor;
import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;
import dataInterface.OneNumberSensitiveSemeion;
import dataInterface.SemeionDataProvider;
import examples.RBFKernel;
import mlalgorithms.SupportVectorMachine;


/**
 * Created by ZouKaifa on 2015/12/2.
 */
public class CalculateByGPU {
    public native int inference(byte[] data, double[] result);

    SupportVectorMachine svm;
    SemeionDataProvider semeionDataProvider;
    BasicImageConvertor basicImageConvertor;


    public CalculateByGPU() {
        semeionDataProvider = new OneNumberSensitiveSemeion("semeion.data", new ContourExtractor(16, 16), 5);
        BasicDataProvider basicDataProvider = new BasicDataProvider(semeionDataProvider.getFeatureMatrix(), semeionDataProvider.getLabelMatrix());
        basicImageConvertor = new BasicImageConvertor(16,16);
        svm = new SupportVectorMachine(basicDataProvider,new RBFKernel(2.73));
        svm.train();
    }

    //static{
    //    System.loadLibrary("cuInfer");
    //}
    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){
        Matrix feature = semeionDataProvider.getFeatureExtractor().dataToFeature(basicImageConvertor.toRowMatrix(matrix));
        for (int i = 0; i<= 9;i++) {
            if (i != 5)
                pros[i] = 0;
            else {
                pros[i] = (svm.test(feature) + 1) / 2;
            }

        }
        return 1;

    }
}
