package mlalgorithms;

import basicUtils.Matrix;
import dataInterface.DataProvider;
import java.util.Collections;
import java.util.ArrayList;

public class SupportVectorMachine {
    /*
     * 一个简单的二分类SVM
     */
    private KernelFunction fKernel;  // Kernel函数，用于求高维空间中的向量内积，默认为线性核
    private DataProvider data;
    private Matrix productMatrix;
    private Matrix dataMatrix;
    private Matrix labelMatrix;
    private Matrix errorVector;
    private ArrayList<Double> alpha;
    private double b;
    private double C=1.0f;
    private double eps = 1e-2;
    private double tol = 1e-1; // 判断收敛的边界,重要参数
    private boolean isInit=false;

    public SupportVectorMachine(DataProvider dp) {
        data=dp;
        dataMatrix = data.getDataMatrix();
        labelMatrix = data.getLabelMatrix();
    }
    public SupportVectorMachine(DataProvider dp,KernelFunction k) {
        data=dp;
        dataMatrix = data.getDataMatrix();
        labelMatrix = data.getLabelMatrix();
        fKernel = k;
    }

    public void train() {
        if (!data.isReady()) {
            System.err.println("Not Enough Data");
            return ;
        }
        SMO();
        System.err.println("Train Has Finished!");
    }

    private double calE(int j) {
        return errorVector.get(j, 0) + b;
    }

    private boolean isKktToleranted(int i) {
        double expected = calE(i);
        double value=alpha.get(i);
        if (expected > 0 && Math.abs(value) > eps) {
            return false;
        }
        if (expected < 0 && Math.abs(value-C) > eps) {
            return false;
        }
        if (Math.abs(expected) < eps){
            if (value <= 0 || value >= C) {
                return false;
            }
        }
        return true;
    }

    private double calculateKktViolence(int i) {
        double expected = calE(i);
        double value=alpha.get(i).doubleValue();
        if (expected > 0 && Math.abs(value) > eps) {
            return Math.abs(value);
        }
        if (expected < 0 && Math.abs(value-C) > eps) {
            return Math.abs(value-C);
        }
        if (Math.abs(expected) < eps){
            if (value <= 0) {
                return Math.abs(value);
            }
            if (value >= C) {
                return Math.abs(value-C);
            }
        }
        return -1;
    }

    private void SMO() {
        ArrayList<Integer> kktViolatedEntries = new ArrayList<>();
        double[] kktViolence = new double[dataMatrix.getHeight()];
        if (!isInit) {
            Init();
        }
        int i = 0;
        int j = 0;
        int count = 0;
        double convergency = 10000;
        while (convergency > 0.1) {  // 未收敛就做

            if (count >= 0) {
                System.out.println("count:" + count + "; convergency:" + convergency);
            }
            count++;

            for(int index = 0; index < alpha.size(); index++) {
                kktViolence[index] = calculateKktViolence(index);
                if (kktViolence[index] >= 0)
                    kktViolatedEntries.add(index);
            }

            // 选定i，其中要i对应数据项不满足KKT条件且尽可能多地违背KKT条件
            double valueTemp = -0.01;
            int iTemp = -1;
            for(Integer index : kktViolatedEntries) {
                if (kktViolence[index] > valueTemp) {
                    iTemp = index;
                    valueTemp = kktViolence[index];
                }
            }
            i = iTemp;
            if (i == -1 || kktViolatedEntries.size() < 2)
                break;

            double Ei=calE(i);
            double maxEj=-1;
            int maxj=-1;  //采用启发式算法，确定和Ei差的绝对值最大的Ej
            for (Integer jIndex : kktViolatedEntries) {
                if (i == jIndex)
                    continue;
                double Ej = calE(jIndex);
                if (Math.abs(Ei - Ej) > maxEj) {
                    maxj = jIndex;
                    maxEj = Math.abs(Ei-Ej);
                }
            }
            j=maxj;
            double Ej=calE(j);

            double yi = labelMatrix.get(i, 0);
            double yj = labelMatrix.get(j, 0);

            double oldalphai=alpha.get(i);
            double oldalphaj=alpha.get(j);

            double L, H;
            if (yi != yj) {
                L = Math.max(0, oldalphaj - oldalphai);
                H = Math.min(C, C - oldalphai + oldalphaj);
            } else {
                L = Math.max(0, oldalphai + oldalphaj - C);
                H = Math.min(C, oldalphai + oldalphaj);
            }

            double eta = 2 * productMatrix.get(i, j) - productMatrix.get(i, i) - productMatrix.get(j, j);
            double derivationForAlphaJ = yj*(Ei-Ej)/eta;

            double newAlphaj = oldalphaj-derivationForAlphaJ;

            newAlphaj = Math.max(newAlphaj, L);
            newAlphaj = Math.min(newAlphaj, H);

            double newAlphai = oldalphai + yi * yj * (oldalphaj - newAlphaj);

            alpha.set(i, newAlphai);
            alpha.set(j, newAlphaj);

            updateErrorVector(i, j, newAlphai-oldalphai, newAlphaj - oldalphaj);

            double aj = newAlphaj;
            double ai = newAlphai;
            double b1 = b - Ei - yi * (ai - oldalphai) * productMatrix.get(i, i) - yj * (aj - oldalphaj) * productMatrix.get(i, j);
            double b2 = b - Ej - yi * (ai - oldalphai) * productMatrix.get(i, j) - yj * (aj - oldalphaj) * productMatrix.get(j, j);

            if (0 < ai && ai < C)
                b = b1;
            else if (0 < aj && aj < C)
                b = b2;
            else
                b = (b1 + b2) / 2;

            convergency = convergence();
        }
    }

    private void Init() {
        //初始化alpha的权值，应该是全置0或者令alpha为0到C之间的某个随机数
        alpha = new ArrayList<Double>(Collections.nCopies(dataMatrix.getHeight(), 0.0));
        for(int i = 0; i < alpha.size(); i++) {
            alpha.set(i,Math.random());
        }

        //初始化b的值，可以考虑随机或者置默认值
        //Todo 后续优化b参数取值
        b = 1;

        //求Kernel矩阵
        GetKernelMatrix();

        // initialize the errorVector
        errorVector = labelMatrix.multiply(-1.0);

        isInit=true;
    }

    private void updateErrorVector(int i, int j, double deltaAlphaI, double deltaAlphaJ) {
        Matrix iFixVector = productMatrix.get(i).reverse().multiply(deltaAlphaI * labelMatrix.get(i,0));
        Matrix jFixVector = productMatrix.get(j).reverse().multiply(deltaAlphaJ * labelMatrix.get(j,0));
        errorVector = errorVector.add(iFixVector);
        errorVector = errorVector.add(jFixVector);
    }

    private void GetKernelMatrix() {
        Matrix x=data.getDataMatrix();
        int height = x.getHeight();
        productMatrix = new Matrix(new double[height][height]);
        for (int i=0;i<height;i++) {
            for (int j=0; j <= i; j++) {
                double temp = fKernel.cal(x.get(i),x.get(j));
                productMatrix.set(i, j, temp);
                productMatrix.set(j, i, temp);
            }
        }
    }

    private double convergence() {
        // 采用KKT条件判别是否收敛，在alpha维数过多的时候会过于缓慢
        /*
        * KKT条件如下：
        * yi*Ei >=0 when alpha = 0
        * yi*Ei ==0 when 0<alpha<C
        * yi*Ei <=0 when alpha = C
        */
        /*
        int count = 0;
        int total = alpha.size();
        for (int i = 0;i<alpha.size();i++) {
            if ((labelMatrix.get(i,0)*calE(i)>=0 && Math.abs(alpha.get(i))<tol)
                ||(Math.abs(data.getLabelMatrix().get(i,0)*calE(i))<tol && alpha.get(i)>0 && alpha.get(i)-C < 0)
                ||(data.getLabelMatrix().get(i,0)*calE(i)>=0 && Math.abs(alpha.get(i)-C)<tol )) {
                count++;
            }
        }*/
        double error = -1.0;
        try {
            error = Matrix.vectorLength(errorVector.add(new Matrix(errorVector.getHeight(),1, b)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        error  = error / Math.sqrt(errorVector.getHeight());
        return error;
    }

    public int test(Matrix x) {
        // 在这里我发现，实际上还是要保留支持向量，因为测试的时候不可能单靠内积来进行计算。
        double f = 0.0;
        for (int i=0;i<alpha.size();i++) {
            f = f + alpha.get(i)*labelMatrix.get(i,0)*fKernel.cal(dataMatrix.get(i),x);
        }
        f += b;
        if (f > 0) return 1;
        else return -1;
    }

    public KernelFunction getfKernel() {
        return fKernel;
    }

    public void setfKernel(KernelFunction fKernel) {
        this.fKernel = fKernel;
    }

    public double getB() {
        return b;
    }
}
