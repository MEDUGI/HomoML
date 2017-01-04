package test.semeionExample;

import basicUtils.BasicImageConvertor;
import basicUtils.ContourExtractor;
import dataInterface.BasicDataProvider;
import dataInterface.OneNumberSensitiveSemeion;
import dataInterface.SemeionDataProvider;
import examples.RBFKernel;
import mlalgorithms.SupportVectorMachine;

/**
 * Created by Xiangxi on 2017/1/4.
 * Contact him on zxx_1996@qq.com
 */
public class TenNumberTest {
    public static void main(String[] args) {
        for(int i = 0; i <= 9; i++) {
            SemeionDataProvider semeionDataProvider = new OneNumberSensitiveSemeion("data\\semeion.data", new ContourExtractor(16, 16), i);
            BasicDataProvider basicDataProvider = new BasicDataProvider(semeionDataProvider.getFeatureMatrix(), semeionDataProvider.getLabelMatrix());
            BasicImageConvertor basicImageConvertor = new BasicImageConvertor(16,16);
            SupportVectorMachine svm = new SupportVectorMachine(basicDataProvider,new RBFKernel(2.73));
            svm.train();
            svm.saveRbfModelToFile("data\\semeionModel"+i+".data");
        }
    }

}
