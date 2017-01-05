package cunist;

import basicUtils.BasicImageConvertor;
import basicUtils.ContourExtractor;
import basicUtils.FeatureExtractor;
import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;
import dataInterface.OneNumberSensitiveSemeion;
import dataInterface.SemeionDataProvider;
import examples.RBFKernel;
import mlalgorithms.SupportVectorMachine;

import java.util.ArrayList;


/**
 * Created by ZouKaifa on 2015/12/2.
 */
public class CalculateByGPU {
    ArrayList<SupportVectorMachine> svms;
    FeatureExtractor featureExtractor;
    BasicImageConvertor basicImageConvertor;
    private double sumTemp=-1.0;


    public CalculateByGPU() {
        featureExtractor = new ContourExtractor(16,16);
        svms = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            svms.add(new SupportVectorMachine("data\\semeionModel"+i+".data"));
        }
        basicImageConvertor = new BasicImageConvertor(16,16);
    }

    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){
        Matrix feature = featureExtractor.dataToFeature(basicImageConvertor.toRowMatrix(matrix));
        if (sumTemp < 0)
            sumTemp = feature.getSum();
        else
            if (Math.abs(sumTemp - feature.getSum()) > 0.5)
                System.out.println("Not Equal!");
        double top = -1;
        for (int i = 0; i<= 9;i++) {
            pros[i] = svms.get(i).test(feature);
            if (pros[i] < 0) {
                pros[i] = 0;
                continue;
            }
            if (pros[i] > top)
                top = pros[i];
        }

        if (top > 0) {
            for(int i = 0; i <= 9; i++)
                pros[i] = pros[i] / top;
        }
        return 1;
    }
}
