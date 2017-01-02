package examples;

import mlalgorithms.ActivationFunction;
import java.lang.Math;

/**
 * Created by 李沅泽 on 2017/1/3.
 * 作为激活函数构建的样例，实现了对数Sigmoid函数
 */
public class SigmoidActivationFunction implements ActivationFunction{

    public SigmoidActivationFunction() {

    }
    public double cal(double param) {
        return 1/(1+Math.exp(-1*param));
    }

    public double derivation(double param) {
        double temp = cal(param);
        return temp*(1-temp);
    }
}
