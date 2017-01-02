package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2017/1/2.
 */
public class FullConnectionLayer implements Layer{
    private int inputNum;
    private int outputNum;
    private double eta = 0.5;
    private ActivationFunction acFunc;
    private Matrix weights;
    private Matrix bias;
    private Matrix output;
    private Matrix errors;

    FullConnectionLayer(int inputNum,int outputNum, ActivationFunction func) {
        this.inputNum = inputNum;
        this.outputNum = outputNum;
        acFunc = func;
        weights = new Matrix(inputNum,outputNum,1.0);
        bias = new Matrix(1,outputNum,0.0);
        output = new Matrix(1, outputNum,0.0);
    }

    public double isConvergence() {

    }
    public Matrix forwardPropagation(Matrix input) throws Exception{
        output = new Matrix(1,outputNum);
        if (input.getWidth() != inputNum) {
            Exception e = new Exception("全连接层输入参数不匹配！");
            e.printStackTrace();
            throw e;
        }
        for (int i = 0;i<outputNum;i++) {
            double temp = 0;
            for (int j = 0;j < inputNum;j++) {
                temp += input.get(1,j)*weights.get(j,i);
                temp += bias.get(1,i);
            }
            output.set(1,i,acFunc.cal(temp));
        }
        return output;
    }
    public Matrix backPropagation(Matrix err){
        errors = new Matrix(1, inputNum);
        Matrix thetas = new Matrix(1, outputNum);
        for (int i = 0;i < outputNum;i++) {
            thetas.set(1,i,acFunc.derivation(errors.get(1,i)));
        }
        for (int i = 0;i < inputNum;i++) {
            double temp = 0.0;
            for (int j = 0;j < outputNum;i++) {
                temp += thetas.get(1,j) * weights.get(i,j);
            }
            errors.set(1,i,temp);
        }
        return errors.copy();
    }

    public void changeEta(int param) {
        eta = param;  // param as new eta
        // eta = 1.0/param; param as k iterations.
    }

    public Matrix getWeights() {
        return weights;
    }

    public Matrix getBias() {
        return bias;
    }

    public int getInputNum() {
        return inputNum;
    }

    public void setInputNum(int inputNum) {
        this.inputNum = inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(int outputNum) {
        this.outputNum = outputNum;
    }

    public ActivationFunction getAcFunc() {
        return acFunc;
    }

    public void setAcFunc(ActivationFunction acFunc) {
        this.acFunc = acFunc;
    }
}
