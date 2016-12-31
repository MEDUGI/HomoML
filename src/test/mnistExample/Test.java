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
        MnistDataProvider mnistTrainData = new MnistDataProvider("data\\train-images.idx3-ubyte",
                "data\\train-labels.idx1-ubyte");
        MnistDataProvider mnistTestData = new MnistDataProvider("data\\t10k-images.idx3-ubyte",
                "data\\t10k-labels.idx1-ubyte");
        System.out.println(1+1);
    }
}
