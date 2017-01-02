package mlalgorithms;

/**
 * Created by 李沅泽 on 2017/1/2.
 */
public interface ActivationFunction {
    public abstract double cal(double param);
    public abstract double derivation(double param);
}
