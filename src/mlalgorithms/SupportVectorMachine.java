package mlalgorithms;

import basicUtils.Matrix;
import dataInterface.DataProvider;
import examples.RBFKernel;

import java.io.*;
import java.util.*;

public class SupportVectorMachine {
    /*
     * 一个简单的二分类SVM
     */
    private RBFKernel fKernel;  // Kernel函数，用于求高维空间中的向量内积，默认为线性核
    private DataProvider data;
    private Matrix productMatrix;
    private Matrix dataMatrix;
    private Matrix labelMatrix;
    private Matrix errorVector;
    private Matrix centralizingVector;
    private ArrayList<Double> alpha;
    private HashSet<Integer> boundaryPoints;
    private double b;
    private double C=1.0f;
    private double eps = 1e-3;
    private double tol = 1e-1; // 判断收敛的边界,重要参数
    private double referencePoint = 0.0;
    private boolean isInit=false;
    private final int maxPasses = 5;

    public SupportVectorMachine(DataProvider dp) {
        data=dp;
        dataMatrix = data.getDataMatrix();
        labelMatrix = data.getLabelMatrix();
    }

    public SupportVectorMachine(DataProvider dp,RBFKernel k) {
        data=dp;
        dataMatrix = data.getDataMatrix();
        labelMatrix = data.getLabelMatrix();
        fKernel = k;
    }

    /**
     *
     * @param filepath : the data file
     * the data is formatted as that in saveRbfModelToFile
     * this version of SVM supports only rbf kernel now
     */
    public SupportVectorMachine(String filepath) {
        File file = new File(filepath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
            if (dataInputStream.readInt() != 0x1086)
                throw new IOException("File's magic number not matches");


            int count = dataInputStream.readInt();              // number of data
            int width = dataInputStream.readInt();              // width of data
            b = dataInputStream.readDouble();                   // bias
            double gamma = dataInputStream.readDouble();        // rbf_gamma
            referencePoint = dataInputStream.readDouble();      // referencePoint

            centralizingVector = new Matrix(1, width);
            alpha = new ArrayList<>(Collections.nCopies(count, 0.0));
            labelMatrix = new Matrix(count, 1, 1.0);
            dataMatrix = new Matrix(count, width);
            fKernel = new RBFKernel(gamma);

            for(int i = 0; i < width; i++)
                centralizingVector.set(0, i, dataInputStream.readDouble());

            for(int i = 0; i < count; i++) {
                alpha.set(i, dataInputStream.readDouble());
            }

            for(int i = 0; i < count; i++) {
                for(int j = 0; j < width; j++)
                    dataMatrix.set(i, j, dataInputStream.readDouble());
            }
        }catch (EOFException e) {
            //nothing need to be done here.
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void train() {
        if (!data.isReady()) {
            System.err.println("Not Enough Data");
            return;
        }
        centralizingVector = new Matrix(1, dataMatrix.getWidth(), 0.0);
        for(int i = 0; i < dataMatrix.getWidth(); i++) {
            for(int j = 0; j < dataMatrix.getHeight(); j++)
                centralizingVector.set(0, i, centralizingVector.get(0, i) + dataMatrix.get(j, i));
        }
        centralizingVector = centralizingVector.multiply(1.0 / dataMatrix.getHeight());
        for(int i = 0; i < dataMatrix.getHeight(); i++) {
            for(int j = 0; j < dataMatrix.getWidth(); j++)
                dataMatrix.set(i, j, dataMatrix.get(i, j) - centralizingVector.get(0, j));
        }
        SMO();
        // 下一步将所有正例提取出来，然后计算其输出的均值，作为参考量。
        referencePoint = 0.0;
        int counter = 0;
        for (int i = 0;i < dataMatrix.getHeight();i++) {
            if (labelMatrix.get(i,0) == 1) {
                counter++;
                double result = test(dataMatrix.get(i));
                referencePoint += result;
            }
        }
        referencePoint /= counter;
    }

    private double calE(int i) {
        return errorVector.get(i, 0) + b;
    }

    private void SMO() {
        if (!isInit) {
            Init();
        }
        int passes = 0;
        int count = 0;
        while(passes < maxPasses) {
            int alphaChanges = 0;
            for(int i = 0; i < alpha.size(); i++) {
                int j = 0;

                // 选定i，其中要i对应数据项不满足KKT条件且尽可能多地违背KKT条件
                double Ei = calE(i);
                double yi = labelMatrix.get(i,0);
                if (!(yi * Ei < -tol && alpha.get(i) < C) &&
                        !(yi * Ei > tol && alpha.get(i) > 0))
                    continue;

                // determine the j index
                j = selectJ(i);

                // maintain the structure of boundaryPoints
                boundaryPoints.remove(i);
                boundaryPoints.remove(j);

                double Ej = calE(j);
                double yj = labelMatrix.get(j, 0);

                double oldalphai = alpha.get(i);
                double oldalphaj = alpha.get(j);

                double L, H;
                if (yi * yj < 0) {
                    L = Math.max(0, oldalphaj - oldalphai);
                    H = Math.min(C, C - oldalphai + oldalphaj);
                } else {
                    L = Math.max(0, oldalphai + oldalphaj - C);
                    H = Math.min(C, oldalphai + oldalphaj);
                }

                double eta = 2 * productMatrix.get(i, j) - productMatrix.get(i, i) - productMatrix.get(j, j);

                if (eta >= 0)
                    continue;

                double derivationForAlphaJ = yj * (Ei - Ej) / eta;

                double newAlphaj = oldalphaj - derivationForAlphaJ;

                newAlphaj = Math.max(newAlphaj, L);
                newAlphaj = Math.min(newAlphaj, H);

                if (Math.abs(newAlphaj - oldalphaj) < eps)
                    continue;

                double newAlphai = oldalphai + yi * yj * (oldalphaj - newAlphaj);

                if (newAlphaj > 0 && newAlphaj < C)
                    boundaryPoints.add(j);
                if (newAlphai > 0 && newAlphai < C)
                    boundaryPoints.add(i);

                alpha.set(i, newAlphai);
                alpha.set(j, newAlphaj);

                updateErrorVector(i, j, newAlphai - oldalphai, newAlphaj - oldalphaj);

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

                alphaChanges++;
            }
            if (count >= 0) {
                //System.out.println("count:"+count + ";alphaChanges = " + alphaChanges);
            }
            count++;
            if (alphaChanges == 0)
                passes++;
            else
                passes = 0;
        }
    }

    private void Init() {
        //初始化alpha的权值，应该是全置0或者令alpha为0到C之间的某个随机数
        alpha = new ArrayList<Double>(Collections.nCopies(dataMatrix.getHeight(), 0.0));
        boundaryPoints = new HashSet<>();
        for(int i = 0; i < alpha.size(); i++) {
            double randomReal = Math.random();
            alpha.set(i, randomReal);
            if (randomReal > 0 && randomReal < C)
                boundaryPoints.add(i);
        }

        //初始化b的值，可以考虑随机或者置默认值
        //Todo 后续优化b参数取值
        b = 0;

        //求Kernel矩阵
        GetKernelMatrix();

        // initialize the errorVector
        double[][] alphaByY = labelMatrix.getData();
        for(int i = 0; i < labelMatrix.getHeight(); i++) {
            alphaByY[i][0] *= alpha.get(i);
        } try {
            errorVector = productMatrix.multiply(new Matrix(alphaByY));
            errorVector = errorVector.sub(labelMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            isInit = false;
        }
        isInit=true;
    }

    private void updateErrorVector(int i, int j, double deltaAlphaI, double deltaAlphaJ) {
        Matrix iFixVector = productMatrix.get(i).reverse().multiply(deltaAlphaI * labelMatrix.get(i,0));
        Matrix jFixVector = productMatrix.get(j).reverse().multiply(deltaAlphaJ * labelMatrix.get(j, 0));
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

    private int selectJ(int i) {
        int result;
        if (boundaryPoints.size() == 0 || (boundaryPoints.size() == 1 && boundaryPoints.contains(i))) {
            Random random = new Random();
            do {
                result = random.nextInt(alpha.size());
            } while (result == i);
            return result;
        }
        double Ei = calE(i);
        double maxValue = -1;
        int j = -1;
        for(Integer jIndex : boundaryPoints) {
            double Ej = calE(jIndex);
            if (Math.abs(Ei - Ej) > maxValue) {
                j = jIndex;
                maxValue = Math.abs(Ei - Ej);
            }
        }
        return j;
    }

    public double test(Matrix x) {
        // 在这里我发现，实际上还是要保留支持向量，因为测试的时候不可能单靠内积来进行计算。
        Matrix result = x.sub(centralizingVector);
        double f = 0.0;
        for (int i=0;i<alpha.size();i++) {
            f = f + alpha.get(i)*labelMatrix.get(i,0)*fKernel.cal(dataMatrix.get(i),result);
        }
        f += b;
        return f;
    }

    /**
     *
     * @param filepath
     *
     * @return whether the model is saved properly
     * file format:
     * int 0x1086 : magic number for rbf-kernel svm model
     * int count : number of data
     * int width : length of each data
     * double b : the bias of model
     * double rbf_gamma : the gamma parameter of rbf kernel
     * double referencePoint : the mean of all positive example's result.
     * double[width]: the centralizing row vector.
     * double[count] alpha: the alpha vector multiplies by the label vector
     * double[count][width] data : the data matrix
     */
    public boolean saveRbfModelToFile(String filepath) {
        File file = new File(filepath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            dataOutputStream.writeInt(0x1086);                  // magic number

            int count = 0;
            for(double i : alpha)
                if (Math.abs(i) > eps)
                    count++;

            dataOutputStream.writeInt(count);                   // height of data
            dataOutputStream.writeInt(dataMatrix.getWidth());   // width of data
            dataOutputStream.writeDouble(b);                    // bias
            dataOutputStream.writeDouble(fKernel.getGamma());   // rbf_gamma
            dataOutputStream.writeDouble(referencePoint);       // referencePoint

            for(int i = 0; i < dataMatrix.getWidth(); i++)
                dataOutputStream.writeDouble(centralizingVector.get(0, i));

            int countTemp = 0;
            for(int i = 0; i < alpha.size(); i++) {
                if (Math.abs(alpha.get(i)) > eps) {
                    dataOutputStream.writeDouble(alpha.get(i) * labelMatrix.get(i, 0));
                    countTemp++;
                }
            }
            if (countTemp != count)
                throw new Exception("Error: count not matches.");

            countTemp = 0;
            for(int i = 0; i < alpha.size(); i++)
                if (Math.abs(alpha.get(i)) > eps) {
                    countTemp++;
                    for(int j = 0; j < dataMatrix.getWidth(); j++)
                        dataOutputStream.writeDouble(dataMatrix.get(i,j));
                                                                // data matrix
                }

            if (countTemp != count)
                throw new Exception("Error: count not matches.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public KernelFunction getfKernel() {
        return fKernel;
    }

    public void setfKernel(RBFKernel fKernel) {
        this.fKernel = fKernel;
    }

    public double getB() {
        return b;
    }

    public double getReferencePoint() {
        return referencePoint;
    }
}
