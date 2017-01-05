package cunist;

import basicUtils.BasicImageConvertor;
import basicUtils.ContourExtractor;
import basicUtils.FeatureExtractor;
import basicUtils.Matrix;
import dataInterface.BasicDataProvider;
import dataInterface.DataProvider;
import dataInterface.semeionInterfaces.OneNumberSensitiveSemeion;
import dataInterface.semeionInterfaces.SemeionDataProvider;
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


    public CalculateByGPU() {
        featureExtractor = new ContourExtractor(16,16);
        svms = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            svms.add(new SupportVectorMachine("data/semeionModel"+i+".data"));
        }
        basicImageConvertor = new BasicImageConvertor(16,16);
    }

    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){
        Matrix feature = featureExtractor.dataToFeature(basicImageConvertor.toRowMatrix(matrix));
        double top = -1;
        for (int i = 0; i<= 9;i++) {
            pros[i] = svms.get(i).test(feature);
            if (pros[i] < 0) {
                pros[i] = 0;
                System.out.println("Minus!");
                continue;
            }
            if (pros[i] > top)
                top = pros[i];
        }

        if (top > 0) {
            for(int i = 0; i <= 9; i++)
                /*pros[i] = pros[i] / top*/;
        }
        return 1;
    }
}
