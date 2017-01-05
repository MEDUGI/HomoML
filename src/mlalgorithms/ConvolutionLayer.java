package mlalgorithms;

import basicUtils.BasicImageConvertor;
import basicUtils.Matrix;

import java.util.ArrayList;

/**
 * Created by 李沅泽 on 2016/12/31.
 * @param eta 学习率
 * @param kernelNum 核数量
 * @param channelNum 通道数量，即上一层的特征图数目
 * @param inputSize 输入特征图大小
 * @param kernelSize 卷积核的大小
 * @param acFunc 激活函数
 * @param bias 偏置矩阵
 * @param kernelList 存储对应数目的卷积核列表
 */
public class ConvolutionLayer implements Layer {
    double eta = 0.5;
    int kernelNum;
    int channelNum;
    // 这里处理了一下，默认保证输入为方阵
    int inputSize;
    int kernelSize;
    int outputSize;
    Matrix output;
    Matrix input;
    ActivationFunction acFunc;
    ArrayList<ConvKernel> kernelList;

    public ConvolutionLayer(int channelNum, int inputSize, int kernelNum,
                            int kernelSize, ActivationFunction acFunc) {
        this.channelNum = channelNum;
        this.inputSize = inputSize;
        this.outputSize = inputSize-kernelSize+1;
        this.kernelNum = kernelNum;
        this.kernelSize = kernelSize;
        this.acFunc = acFunc;
        kernelList = new ArrayList<ConvKernel>();
        for (int i = 0;i<kernelNum;i++) {
            kernelList.add(new ConvKernel(kernelSize));
        }
    }

    public double isConvergence() {
        return 0.0;
    }

    public Matrix forwardPropagation(Matrix input) throws Exception {
        this.input = input;
        Matrix output = new Matrix(kernelSize,outputSize*outputSize);

        // 判断基本格式是否有误
        if (input.getHeight() != channelNum || input.getWidth() != inputSize*inputSize) {
            Exception e = new Exception("卷积层之中输入不匹配格式!");
            e.printStackTrace();
            throw e;
        }
        // 将Input里面存储的各个channel都提取出来，变化为若干个featureMap
        BasicImageConvertor inputConvertor = new BasicImageConvertor(inputSize, inputSize);
        BasicImageConvertor outputConvertor = new BasicImageConvertor(outputSize,outputSize);
        ArrayList<Matrix> featureMaps = new ArrayList<>();
        for (int i = 0;i<channelNum;i++) {
            featureMaps.add(inputConvertor.toImageMatrix(input.get(i)));
        }
        // 前向传播:遍历每个卷积核，在每一个featureMap上得到卷积结果
        for (int i = 0;i<kernelNum;i++) {
            Matrix outputTemp = new Matrix(outputSize,outputSize,kernelList.get(i).getBias());
            for (int j = 0;j<channelNum;j++) {
                // 卷积结果累加
                outputTemp.add(kernelList.get(i).convolutionValid(featureMaps.get(j)));
            }
            outputTemp = outputConvertor.toRowMatrix(acFunc.cal(outputTemp));
            output.set(i,outputTemp);
        }
        this.output = output;
        return output;
    }

    public Matrix backPropagation(Matrix err) throws Exception{
        Matrix errors = new Matrix(channelNum,inputSize*inputSize);
        // 判断基本格式是否有误
        if (err.getHeight() != kernelNum || err.getWidth() != outputSize*outputSize) {
            Exception e = new Exception("卷积层反向传播残差不匹配格式!");
            e.printStackTrace();
            throw e;
        }
        // 两个提取器
        BasicImageConvertor inputConvertor = new BasicImageConvertor(inputSize, inputSize);
        BasicImageConvertor outputConvertor = new BasicImageConvertor(outputSize, outputSize);
        ArrayList<Matrix> featureMaps = new ArrayList<>();
        for (int i = 0;i<kernelNum;i++) {
            featureMaps.add(outputConvertor.toImageMatrix(err.get(i)));
        }
        // 先求各层对应的前层残差，发现如果全连接的话，那么得到的残差应该是各图一致
        Matrix errorTemp = new Matrix(channelNum,inputSize*inputSize,0.0);
        Matrix thetas = acFunc.derivation(output).dotProduction(err);
        // 更新bias
        for (int i = 0; i<kernelNum;i++) {
            kernelList.get(i).updateBias(thetas.get(i));
        }
        // 计算weights变化量, 并且更新
        ArrayList<Matrix> imgs = new ArrayList<>();
        for (int i = 0;i<kernelNum;i++) {
            imgs.add(inputConvertor.toImageMatrix(input.get(i)));
        }
        for (int i = 0;i<kernelNum;i++) {
            Matrix dataWeights = new Matrix(kernelSize,kernelSize,0.0);
            ConvKernel tempKernel = new ConvKernel(outputSize, outputConvertor.toImageMatrix(thetas.get(i)).rot180());
            for (int j = 0;j < channelNum;j++) {
                dataWeights = dataWeights.add(tempKernel.convolutionValid(imgs.get(j)).rot180());
            }
            kernelList.get(i).updateWeights(dataWeights);
        }
        for (int i = 0;i<kernelNum;i++) {
            errorTemp.add(outputConvertor.toRowMatrix(kernelList.get(i).convolutionFullWithRotate(featureMaps.get(i))));
        }
        for (int i = 0;i<channelNum;i++) {
            errors.set(i,errorTemp);
        }
        return errors;
    }

    public void updateWeights(int batchSize) {

    }
}