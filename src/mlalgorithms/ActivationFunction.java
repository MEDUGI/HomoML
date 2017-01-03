package mlalgorithms;

import basicUtils.Matrix;

/**
 * Created by 李沅泽 on 2017/1/2.
 */
public abstract class ActivationFunction {
    public abstract double cal(double param);
    public Matrix cal(Matrix param){
        Matrix result = new Matrix(param.getHeight(),param.getWidth());
        for (int i = 0;i < result.getHeight();i++) {
            for (int j = 0; j<result.getWidth();j++) {
                result.set(i,j,cal(param.get(i,j)));
            }
        }
        return result;
    }
    public abstract double derivation(double param);
    public Matrix derivation(Matrix param) {
        Matrix result = new Matrix(param.getHeight(),param.getWidth());
        for (int i = 0;i < result.getHeight();i++) {
            for (int j = 0; j<result.getWidth();j++) {
                result.set(i,j,derivation(param.get(i,j)));
            }
        }
        return result;
    }
}
