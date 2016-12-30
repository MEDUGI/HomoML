package dataInterface;

import basicUtils.BasicImageConvertor;
import basicUtils.Matrix;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Xiangxi on 2016/12/28.
 * Contact him on zxx_1996@qq.com
 */
public class MnistDataProvider extends ImageDataProvider{
    protected Matrix dataMatrix;
    protected Matrix labelMatrix;
    protected boolean isReady;
    int imageWidth;
    int imageHeight;

    public MnistDataProvider(String imageFilePath, String labelFilePath) {
        isReady = false;
        try {
            readLabelFile(labelFilePath);
            readImageFile(imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        isReady = true;
    }

    private void readLabelFile(String filepath) throws IOException{
        File file = new File(filepath);

        if (file.isFile() && file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
            if (dataInputStream.readInt() != 0x00000801)
                throw new IOException("File's magic number not matches");
            int dataNumber = dataInputStream.readInt();
            double[][] label = new double[dataNumber][1];

            for(int i = 0; i < dataNumber; i++) {
                label[i][0] = dataInputStream.readUnsignedByte();
            }
            labelMatrix = new Matrix(label);
        }
    }

    private void readImageFile(String filepath) throws IOException{
        File file = new File(filepath);

        if (file.isFile() && file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
            if (dataInputStream.readInt() != 0x00000803)
                throw new IOException("File's magic number not matches");
            int dataNumber = dataInputStream.readInt();
            imageHeight = dataInputStream.readInt();
            imageWidth = dataInputStream.readInt();
            int dataLength = imageHeight * imageWidth;
            double[][] rawData = new double[dataNumber][dataLength];

            for(int i = 0; i < dataNumber; i++) {
                for(int j = 0; j < dataLength; j++)
                    rawData[i][j] = dataInputStream.readUnsignedByte();
            }
            dataMatrix = new Matrix(rawData);
        }
    }

    @Override
    public Matrix toImageMatrix(Matrix rowData) {
        return new BasicImageConvertor(imageHeight, imageWidth).toImageMatrix(rowData);
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

}
