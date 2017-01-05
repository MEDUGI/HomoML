package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2017/1/2.
 */
public class FullConnectionLayer implements Layer{
    int inputNum;
    int outputNum;
    double eta = 0.00001;
    ActivationFunction acFunc;
    Matrix weights;
    Matrix bias;
    Matrix output;
    Matrix input;
    double convergency;
    Matrix deltaW;
    Matrix deltab;

    public FullConnectionLayer(int inputNum, int outputNum, ActivationFunction func) {
        this.inputNum = inputNum;
        this.outputNum = outputNum;
        acFunc = func;
        weights = new Matrix(inputNum,outputNum);
        deltab = new Matrix(1,outputNum,0.0);
        deltaW = new Matrix(inputNum,outputNum,0.0);
        double coefficient = Math.sqrt(6.0)/Math.sqrt(inputNum+outputNum);
        for (int i = 0;i<inputNum;i++) {
            for (int j = 0;j<outputNum;j++) {
                weights.set(i,j,(Math.random()-0.5)/0.5*coefficient);
            }
        }
        bias = new Matrix(1,outputNum,0.0);
        output = new Matrix(1, outputNum,0.0);
    }

    public double isConvergence() {
        return convergency;
    }

    public Matrix forwardPropagation(Matrix input) throws Exception{
        this.input = input.copy();
        output = new Matrix(1,outputNum);
        if (input.getWidth() != inputNum) {
            Exception e = new Exception("全连接层输入参数不匹配！");
            e.printStackTrace();
            throw e;
        }
        for (int i = 0;i<outputNum;i++) {
            double temp = 0;
            for (int j = 0;j < inputNum;j++) {
                temp += input.get(0,j)*weights.get(j,i);
                temp += bias.get(0,i);
            }
            output.set(0,i,acFunc.cal(temp));
        }
        return output;
    }
    public Matrix backPropagation(Matrix err) throws Exception{
        Matrix errors = new Matrix(1, inputNum);
        Matrix thetas = new Matrix(1, outputNum);
        convergency = 0.0;
        for (int i = 0;i < outputNum;i++) {
            thetas.set(0,i,acFunc.derivation(output.get(0,i))*err.get(0,i));
            double deltabTMP = deltab.get(0,i) + thetas.get(0,i);
            deltab.set(0,i,deltabTMP);
        }
        for (int i = 0;i < inputNum;i++) {
            double temp = 0.0;
            for (int j = 0;j < outputNum;j++) {
                temp += thetas.get(0,j) * weights.get(i,j);
                double deltaWTMP = deltaW.get(i,j) + deltab.get(0,j)*input.get(0,i);
                deltaW.set(i,j,deltaWTMP);
            }
            errors.set(0,i,temp);
        }
        return errors;
    }

    public void updateWeights(int batchSzie){
        convergency = 0;
        for (int i = 0;i<outputNum;i++) {
            bias.set(0,i,bias.get(0,i)+deltab.get(0,i)/batchSzie);
            for (int j = 0;j<inputNum;j++) {
                weights.set(j,i,weights.get(j,i)+deltaW.get(j,i)/batchSzie);
                convergency += deltaW.get(j,i);
            }
        }
        deltab = new Matrix(1,outputNum,0.0);
        deltaW = new Matrix(inputNum,outputNum,0.0);
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
