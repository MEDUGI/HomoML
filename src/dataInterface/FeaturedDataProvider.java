package dataInterface;

import basicUtils.FeatureExtractor;
import basicUtils.Matrix;

/**
 * Created by Xiangxi on 2016/12/6.
 * Contact him on zxx_1996@qq.com
 */
public interface FeaturedDataProvider extends DataProvider{
    Matrix getFeatureMatrix();
    boolean hasFeatured();
    FeatureExtractor getFeatureExtractor();
}
