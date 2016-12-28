package SemeionExample;

import basicUtils.ContourExtractor;
import dataInterface.FeaturedDataProvider;
import dataInterface.SemeionDataProvider;

/**
 * Created by Xiangxi on 2016/12/27.
 * Contact him on zxx_1996@qq.com
 */
public class Test {
    public static void main(String[] args) {
        SemeionDataProvider semeionDataProvider = new SemeionDataProvider("C:\\Users\\zxx_1\\Desktop\\semeion.data", new ContourExtractor(16,16));
        System.out.println(1+1);
        // TODO: complete the Test on semeion.data
    }
}
