**��Ҫ**

�㷨�� SVM, ���������

SVM --> �����½���Kernel���������Ƿ���ʵ���û��Լ������Kernel�ࣩ

CNN --> �����������ͬ����ˣ�ͬ���ģ��Ƿ���ʵ�ֻ�������չ����ǰ�򴫲������򴫲���ȫ���ӣ������������磬��֪����,�����������sigmod�Ⱥ�����ͬ�����Ƿ������չ��

������ѧ����ʵ��
>* �����ࣨ���壬���ݽṹ���������������ӡ������ˡ�row reduction��determinant��SVD����PCA���������ֶ��̣߳�
>* �ݶ��½��������ҵ�ͳһ����С�ĺ������������������̲߳�����
>* ������������̵߳ģ�
>* Kernel������ֵ�����̣߳�ʵ�����Ǿ��������
>* ǰ�򴫲��ͷ��򴫲���������ͳһ�ĸ�ʽ������������

����Ҫ�������飺
>1. ʵ���㷨��ͳһ����������ӿڡ�
>2. �㷨���ڲ��ĺ������Ե��������ã�Ҳ�����໥���������
>3. ��Ҫ�����㷨��ʵ��һ��ʵ������ѡΪCNN��дʶ����֤��ʶ��

�ֹ���
>1. ������Ĺ�ͨ��ͳһ����������ӿڣ�����GPU�Ͷ��̡߳�����̵Ľӿڡ�   --zxx
>2. ������ѧ����ʵ��part1��������;������������kernel�˳���ʵ�֣��������������ˡ�    --tmy
>3. ������ѧ����ʵ��part2������ѧϰ�����㷨��CNN��SVM�����ݶ��½��������½��������㷨��    --lyz
>4. �����㷨��ʵ��һ��ʵ�����Լ�ѡ��дʶ�����֤�롣��дʶ����ҪGUI��ʵʱ�������ݣ������ʵĴ�ϸ����ͼƬ�ı�Ե����ã��õ��Ϻõ������������㷨��Ϊ���������⵼��������֤����֤��Ҫ���Լ�������֤���㷨��GUIչʾ��֤�룬չʾ��ʱ��������֤�벢ͬʱ����жϽ����
  -- zqy

Matrix��
>* ���캯����double[][] data
>* ���캯��: int m, int n, double initial.
>* �ӷ�: Matrix Matrix.add(Matrix)
>* ������ Matrix Matrix.sub(Matrix)
>* �˷�: Matrix Matrix.multiply(Matrix) throws Exception
>* ����: Matrix Matrix.multiply(double scalar)
>* row reduction: Matrix Matrix.reduce()
>* linearSolve: Matrix Matrix.linearSolve(Matrix y): return null when no solutions
>* leastSquareSolve: Matrix Matrix.leastSquareSolve(Matrix y): return exacts solution when possible, apporx. solution otherwise.
>* ת��: Matrix matrix.reverse()
>* determinant: double Matrix.determinant() throws Exception
>* ���棺 Matrix Matrix.inverse() throws Exception
>* SVD: Matrix[] Matrix.SVD() throws Exception. The returned list contains 3 entries, represents U �� and V'
>* copy: Matrix Matrix.copy()
>* subMatrix: Matrix Matrix.subMatrix(int lx, int ly, int rx, int ry) assert(rx > lx; ry > ly) throws Exception
>* columnUnion: Matrix Matrix.columnUnion(Matrix rhm) throws Exception

MatrixException�� extends Exception

PCA��
>* ���캯����Matrix dataMatrix, int newDimension
>* ѹ�����ݣ�Matrix PCA.encode(Matirx) throws Exception

ConvolutionFunction�ӿ�
>* Matrix convolutionOf(Matrix m)

PoolingFunction�ӿ�
>* Matrix poolingOf(Matrix m)

ConvolutionLayerʵ��ConvolutionFunction��PoolingFunction
>* Matrix dataMatrix;
>* Matrix weightVector;
>* Matrix outputOf(Matrix inputVector);
>* getter & setter

FullConnectedLayer��
>* Matrix weightVector;
>* getter & setter

KernelFunction�ӿ�
>* double kernelOf(Matrix x1, Matrix x2) throws Exception
 
DataProvider�ӿ�
>* Matrix dataMatrix();
>* Matrix labelMatrix();
>* boolean isReady();

ClassificationMethodö���ࣺ
>* {KSVM, CNN}

Classifier���ࣺ
>* protected ClassificationMethod classificationMethod
>* protected boolean isTrained
>* protected Map<String, Object> setting;
>* public boolean trainClassifier(Matrix dataMatrix, Matrix labelMatrix);
>* public double classify(Matrix dataVector)
>* getter & setter

SvmClassifier�̳�Classifier
>* protected KernelFunction kernelFunction;
>* protected Matrix supportVectors;
>* protected Matrix alphaVector;
>* protected double bias;
>* protected boolean applySetting();
>* getter & setter

CnnClassifier�̳�Classifier
>* protected ArrayList<ConvolutionLayer> convolutionLayers