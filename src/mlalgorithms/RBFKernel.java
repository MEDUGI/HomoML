package mlalgorithms;
import java.lang.Math;
import basicUtils.Mathtools;
import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2016/12/28.
 */
public class RBFKernel implements KernelFunction{
    private double gamma=0.0;
    public double cal(Matrix x, Matrix y) {
        return Math.exp(-1*gamma*Mathtools.getL2(x.reverse(),y));
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
