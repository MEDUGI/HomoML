package test.mnistExample;

import basicUtils.ContourExtractor;
import dataInterface.MnistDataProvider;
import dataInterface.SemeionDataProvider;

/**
 * Created by Xiangxi on 2016/12/28.
 * Contact him on zxx_1996@qq.com
 */
public class Test {
    public static void main(String[] args) {
        MnistDataProvider mnistTrainData = new MnistDataProvider("C:\\Users\\zxx_1\\Desktop\\train-images.idx3-ubyte",
                "C:\\Users\\zxx_1\\Desktop\\train-labels.idx1-ubyte");
        MnistDataProvider mnistTestData = new MnistDataProvider("C:\\Users\\zxx_1\\Desktop\\t10k-images.idx3-ubyte",
                "C:\\Users\\zxx_1\\Desktop\\t10k-labels.idx1-ubyte");
        System.out.println(1+1);
    }
}
