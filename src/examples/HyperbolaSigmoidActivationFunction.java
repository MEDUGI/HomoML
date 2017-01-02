package examples;

import mlalgorithms.ActivationFunction;

/**
 * Created by 李沅泽 on 2017/1/3.
 */
public class HyperbolaSigmoidActivationFunction  implements ActivationFunction{
    public HyperbolaSigmoidActivationFunction() {

    }
    public double cal(double param) {
        double expX = Math.exp(param);
        double expMinorX = Math.exp(-1*param);
        return (expX-expMinorX)/(expX+expMinorX);
    }

    public double derivation(double param) {
        double temp = cal(param);
        return 1-temp*temp;
    }
}
