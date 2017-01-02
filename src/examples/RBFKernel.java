package examples;
import java.lang.Math;
import basicUtils.Mathtools;
import basicUtils.Matrix;
import mlalgorithms.KernelFunction;

/**
 * Created by 李沅泽 on 2016/12/28.
 */
public class RBFKernel implements KernelFunction {
    private double gamma=0.0;

    public RBFKernel() {
    }
    public RBFKernel(double g) {
        gamma = g;
    }
    public double cal(Matrix x, Matrix y) {
        return Math.exp(-1*gamma*Mathtools.getL2(x, y));
    }

    public double getGamma() {
        return gamma;
    }

    public int setGamma(double gamma) {
        if (gamma > 0){
            this.gamma = gamma;
            return 1;
        }
        else return -1;
    }
}
