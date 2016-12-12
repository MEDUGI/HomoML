package mlalgorithms;

import basicUtils.Matrix;
import dataInterface.DataProvider;

import java.util.ArrayList;

/**
 * Created by 李沅泽 on 2016/12/12.
 */
public class SupportVectorMachine {
    /*
     * 一个简单的二分类SVM
     */
    private KernelFunction fKernel;  // Kernel函数，用于求高维空间中的向量内积，默认为线性核
    private DataProvider data;
    private Matrix productMatrix;
    private ArrayList<Double> alpha;
    private ArrayList<Matrix> supportVectors;
    private Matrix w;
    private double b;
    private double C=1.0f;
    private static final double eps=0.0001;
    private boolean isInit=false;
    SupportVectorMachine() {

    }

    public void run() {
        if (!data.isReady()) {
            System.err.println("Not enough Data");
            return ;
        }
        Init();
        SMO();
    }

    private double calE(int i) {
        // 计算f(x)=WT*x-b
        return w.reverse().multiply(data.getDataMatrix().get(i)).get(1,1)-b;
    }

    private double abs(double i) {
        if (i>0) return i;
        else return -i;
    }

    private void SMO() {
        int i = 0;
        int j = 0;
        while (!convergence()) {  // 未收敛就做
            // 选定第一个乘子，采用启发式算法，选定第一个处于0到C之间的乘子
            for (i=0;i<alpha.size();i++){
                double value=alpha.get(i).doubleValue();
                if (value>0 && value<C){
                    break;
                }
            }
            double Ei=calE(i);
            double maxEj=0.0;
            int maxj=0;  //采用启发式算法，确定和Ei差的绝对值最大的Ej
            for (j=0;j<alpha.size();j++) {
                if (i!=j){
                    double Ej = calE(j);
                    if (Ei*Ej<0){
                        if (abs(Ej)-maxEj>eps) {
                            maxEj=abs(Ej);
                            maxj=j;
                        }
                    }
                }
            }
            j=maxj;
            double Ej=calE(j);
            double yi = data.getLabelMatrix().get(i);
            double yj = data.getLabelMatrix().get(j);
            double oldalphai=alpha.get(i).doubleValue();
            double oldalphaj=alpha.get(j).doubleValue();
            double L, H;
            if (yi != yj) {
                L = Math.max(0, oldalphaj - oldalphai);
                H = Math.min(C, C - oldalphai + oldalphaj);
            } else {
                L = Math.max(0, oldalphai + oldalphaj - C);
                H = Math.min(0, oldalphai + oldalphaj);
            }

            double eta = 2 * productMatrix.get(i, j) - productMatrix.get(i, i) - productMatrix.get(j, j);
            double newAlphaj = oldalphaj-yj*(Ei-Ej)/eta;
            if (newAlphaj<L) alpha.set(j,L);
            else if (newAlphaj>H) alpha.set(j,H);
            else alpha.set(j,newAlphaj);
            double aj = alpha.get(j).doubleValue();
            alpha.set(i,oldalphai + yi * yj * (oldalphaj - aj);
            double ai = alpha.get(i).doubleValue();
            double b1 = b - Ei - yi * (ai - oldalphai) * productMatrix.get(i, i) - yj * (aj - oldalphaj) * productMatrix.get(i, j);
            double b2 = b - Ej - yi * (ai - oldalphai) * productMatrix.get(i, j) - yj * (aj - oldalphaj) * productMatrix.get(j, j);

            if (0 < ai && ai < C)
                b = b1;
            else if (0 < aj && aj < C)
                b = b2;
            else
                b = (b1 + b2) / 2;
        }
    }

    private void Init() {
        //ToDo 初始化alpha的权值，应该是全置0或者令alpha为0到C之间的某个随机数
        //ToDo 初始化b的值，可以考虑随机或者置默认值
        //ToDo 求Kernel矩阵
        isInit=true;
        return ;
    }

    public KernelFunction getfKernel() {
        return fKernel;
    }

    public void setfKernel(KernelFunction fKernel) {
        this.fKernel = fKernel;
    }

    public ArrayList<Matrix> getSupportVectors() {
        return supportVectors;
    }

    public Matrix getW() {
        return w;
    }

    public Matrix getB() {
        return b;
    }
}
