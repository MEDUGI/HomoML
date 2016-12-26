package dataInterface;

import basicUtils.FeatureExtractor;
import basicUtils.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
    protected FeatureExtractor featureExtractor;

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
        this.featureExtractor = featureExtractor;
        featureMatrix = featureExtractor.dataToFeature(dataMatrix);
        isReady = true;
    }
    protected void readFromFile(String filepath) {
        try {
            File file = new File(filepath);
            ArrayList<ArrayList<Double>> rawData = new ArrayList<>();
            ArrayList<Double> rawLabel = new ArrayList<>();
            int lineNumber = 0;
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    String[] fragments = line.split(" ");
                    lineNumber++;
                    ArrayList<Double> oneLineData = new ArrayList<>();
                    for(int i = 0; i < 256; i++) {
                        oneLineData.add(Double.parseDouble(fragments[i]));
                    }
                    rawData.add(oneLineData);
                    for(int i = 256; i < 266; i++) {
                        if (Integer.parseInt(fragments[i]) == 1)
                            rawLabel.add(i-256.0);
                    }
                }
                double[][] data = new double[lineNumber][256];
                double[][] label = new double[lineNumber][1];
                for(int i = 0; i < lineNumber; i++) {
                    for(int j = 0; j < 256; j++)
                        data[i][j] = rawData.get(i).get(j);
                    label[i][0] = rawLabel.get(i);
                }
                dataMatrix = new Matrix(data);
                labelMatrix = new Matrix(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return featureExtractor;
    }
}
