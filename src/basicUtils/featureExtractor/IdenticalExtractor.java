package basicUtils.featureExtractor;

import basicUtils.Matrix;
import dataInterface.FeaturedDataProvider;

/**
 * Created by Xiangxi on 2017/1/6.
 * Contact him on zxx_1996@qq.com
 */
public class IdenticalExtractor implements FeatureExtractor{
    @Override
    public Matrix dataToFeature(Matrix rawData) {
        return rawData.copy();
    }
}
