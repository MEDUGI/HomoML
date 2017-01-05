package examples;

import mlalgorithms.ActivationFunction;

/**
 * Created by 李沅泽 on 2017/1/3.
 */
public class ReLUActivationFunction extends ActivationFunction{
    public ReLUActivationFunction() {

    }
    public double cal(double param) {
        if (param <= 0) {
            return 0.0;
        }
        else return param;
    }

    public double derivation(double param) {
        if (param <=0)  return 0.0;
        else return 1.0;
    }
}
