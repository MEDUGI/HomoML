package basicUtils;

/**
 * Created by 李沅泽 on 2016/12/28.
 */
public class Mathtools {
    public static double getL2(Matrix x,Matrix y) {
        return x.sub(y).reverse().getNorm2Vector();
    }
}
