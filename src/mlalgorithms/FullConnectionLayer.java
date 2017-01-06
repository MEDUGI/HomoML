package mlalgorithms;

import basicUtils.ConcurenceRunner.*;
import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2017/1/2.
 */
public class FullConnectionLayer implements Layer{
    int inputNum;
    int outputNum;
    double eta = 0.01;
    Matrix gradient;
    Matrix biasGradient;
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
        gradient = new Matrix(inputNum,outputNum,0.0);
        biasGradient = new Matrix(1,outputNum,0.0);
    }

    public double isConvergence() {
        return convergency;
    }

    public Matrix forwardPropagation(Matrix input) throws Exception{
        this.input = input.copy();
        Matrix result = new Matrix(1,outputNum);
        if (input.getWidth() != inputNum) {
            Exception e = new Exception("全连接层输入参数不匹配！");
            e.printStackTrace();
            throw e;
        }
        new TaskManager(outputNum) {
            @Override
            public void process(int start, int end) {
                for (int i = start;i<end;i++) {
                    double temp = 0;
                    for (int j = 0;j < inputNum;j++) {
                        temp += input.get(0,j)*weights.get(j,i);
                    }
                    temp+=bias.get(0,i);
                    output.set(0,i,temp);
                    result.set(0,i,acFunc.cal(temp));
                }
            }
        }.start();
        /*for (int i = 0;i<outputNum;i++) {
            double temp = 0;
            for (int j = 0;j < inputNum;j++) {
                temp += input.get(0,j)*weights.get(j,i);
            }
            temp+=bias.get(0,i);
            output.set(0,i,temp);
            result.set(0,i,acFunc.cal(temp));
        }*/
        return result;
    }
    public Matrix backPropagation(Matrix err) throws Exception{
        Matrix errors = new Matrix(1, inputNum);
        Matrix thetas = new Matrix(1, outputNum);
        convergency = 0.0;
        new TaskManager(outputNum) {
            @Override
            public void process(int start, int end) {
                for (int i = start;i < end;i++) {
                    thetas.set(0,i,acFunc.derivation(output.get(0,i))*err.get(0,i));
                    biasGradient.set(0,i,biasGradient.get(0,i)+Math.pow(thetas.get(0,i),2));
                    double deltabTMP = deltab.get(0,i) + calEta(biasGradient.get(0,i))*thetas.get(0,i);
                    deltab.set(0,i,deltabTMP);
                }
            }
        }.start();
        /*for (int i = 0;i < outputNum;i++) {
            thetas.set(0,i,acFunc.derivation(output.get(0,i))*err.get(0,i));
            biasGradient.set(0,i,biasGradient.get(0,i)+Math.pow(thetas.get(0,i),2));
            double deltabTMP = deltab.get(0,i) + calEta(biasGradient.get(0,i))*thetas.get(0,i);
            deltab.set(0,i,deltabTMP);
        }*/
        new TaskManager(inputNum) {
            @Override
            public void process(int start, int end) {
                for (int i = start;i < end;i++) {
                    double temp = 0.0;
                    for (int j = 0;j < outputNum;j++) {
                        temp += thetas.get(0,j) * weights.get(i,j);
                        gradient.set(i, j, gradient.get(i, j) + Math.pow(input.get(0,i)*thetas.get(0,j),2));
                        double deltaWTMP = deltaW.get(i,j) + calEta(gradient.get(i,j))*thetas.get(0,j)*input.get(0,i);
                        deltaW.set(i,j,deltaWTMP);
                    }
                    errors.set(0,i,temp);
                }
            }
        }.start();
        /*for (int i = 0;i < inputNum;i++) {
            double temp = 0.0;
            for (int j = 0;j < outputNum;j++) {
                temp += thetas.get(0,j) * weights.get(i,j);
                gradient.set(i, j, gradient.get(i, j) + Math.pow(input.get(0,i)*thetas.get(0,j),2));
                double deltaWTMP = deltaW.get(i,j) + calEta(gradient.get(i,j))*thetas.get(0,j)*input.get(0,i);
                deltaW.set(i,j,deltaWTMP);
            }
            errors.set(0,i,temp);
        }*/
        return errors;
    }

    public void updateWeights(int batchSize){
        convergency = 0;
        for (int i = 0;i<outputNum;i++) {
            bias.set(0,i,bias.get(0,i)-deltab.get(0,i)/batchSize);
            for (int j = 0;j<inputNum;j++) {
                weights.set(j,i,weights.get(j,i)-deltaW.get(j,i)/batchSize);
                convergency += deltaW.get(j,i);
            }
        }
        deltab = new Matrix(1,outputNum,0.0);
        deltaW = new Matrix(inputNum,outputNum,0.0);
    }

    public double calEta(double gradientSum) {
        double temp = Math.sqrt(gradientSum+0.001);
        double see = eta/temp;
        return see;
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
