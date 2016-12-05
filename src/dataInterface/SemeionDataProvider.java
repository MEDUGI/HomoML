package dataInterface;

import basicUtils.FeatureExtractor;
import basicUtils.Matrix;

import java.io.File;

/**
 * Created by Xiangxi on 2016/12/6.
 * Contact him on zxx_1996@qq.com
 */
public class SemeionDataProvider implements FeaturedDataProvider{
    protected Matrix dataMatrix;
    protected Matrix labelMatrix;
    protected boolean isReady;
    protected boolean hasFeatured;
    protected Matrix featureMatrix;

    public SemeionDataProvider(String filepath) {
        isReady = false;
        hasFeatured = false;
        readFromFile(filepath);
        isReady = true;
    }
    public SemeionDataProvider(String filepath, FeatureExtractor featureExtractor) {
        isReady = false;
        hasFeatured = true;
        readFromFile(filepath);
        featureMatrix = featureExtractor.dataToFeature(dataMatrix);
        isReady = true;
    }
    protected void readFromFile(String filepath) {
        //TODO: readFromFile
    }
    @Override
    public Matrix getDataMatrix() {
        return dataMatrix;
    }

    @Override
    public Matrix getLabelMatrix() {
        return labelMatrix;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean hasFeatured() {
        return hasFeatured;
    }

    @Override
    public Matrix getFeatureMatrix() {
        return featureMatrix;
    }

    @Override
    public FeatureExtractor getFeatureExtractor() {
        return null;
    }
}
