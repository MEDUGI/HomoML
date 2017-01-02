package dataInterface;
import basicUtils.Matrix;
/**
 * Created by Xiangxi on 2016/12/6.
 * Contact him on zxx_1996@qq.com
 */
public interface DataProvider {
    Matrix getDataMatrix();
    Matrix getLabelMatrix();
    boolean isReady();
}
