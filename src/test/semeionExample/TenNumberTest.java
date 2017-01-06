package test.semeionExample;

import basicUtils.BasicImageConvertor;
import basicUtils.featureExtractor.ContourExtractor;
import dataInterface.BasicDataProvider;
import dataInterface.semeionInterfaces.OneNumberSensitiveSemeion;
import dataInterface.semeionInterfaces.SemeionDataProvider;
import examples.RBFKernel;
import mlalgorithms.SupportVectorMachine;

/**
 * Created by Xiangxi on 2017/1/4.
 * Contact him on zxx_1996@qq.com
 */
public class TenNumberTest {
    public static void main(String[] args) {
        double[] gammaList = {2.78, 2.78, 2.78, 2.78, 2.78, 2.78, 2.78, 2.78, 2.78, 2.98};
        //                      0     1     2     3     4     5     6     7     8    9
        for(int i = 0; i <= 9; i++) {
            SemeionDataProvider semeionDataProvider = new OneNumberSensitiveSemeion("data/semeion.data", new ContourExtractor(16, 16), i);
            BasicDataProvider basicDataProvider = new BasicDataProvider(semeionDataProvider.getFeatureMatrix(), semeionDataProvider.getLabelMatrix());
            BasicImageConvertor basicImageConvertor = new BasicImageConvertor(16,16);
            SupportVectorMachine svm = new SupportVectorMachine(basicDataProvider,new RBFKernel(gammaList[i]));
            svm.train();
            svm.saveRbfModelToFile("data/semeionModel"+i+".data");
        }
    }

}
