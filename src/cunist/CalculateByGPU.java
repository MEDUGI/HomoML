package cunist;

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


    public CalculateByGPU() {

    }

    //static{
    //    System.loadLibrary("cuInfer");
    //}
    public int get(byte[] b, double[] pros){
        int sta = 1;//inference(b,pros);

        return sta;
    }

    public int get(Matrix matrix, double[] pros){


        for (int i = 0; i<= 9;i++) {
            SemeionDataProvider semeionDataProvider = new OneNumberSensitiveSemeion("semeion.data", new ContourExtractor(16, 16), i);

            Matrix fullDataMatrix = semeionDataProvider.getDataMatrix();
            int total = fullDataMatrix.getHeight();
            int trainNumber = total;

            DataProvider trainDataProvider = new BasicDataProvider(fullDataMatrix.subMatrix(0, 0,
                    fullDataMatrix.getWidth(), trainNumber), semeionDataProvider.getLabelMatrix().subMatrix(0, 0,
                    1, trainNumber));
            svm = new SupportVectorMachine(trainDataProvider,new RBFKernel(1.0/100));
            pros[i] = svm.test(matrix);

        }

        return 1;

    }
}
