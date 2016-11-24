**纪要**

算法： SVM, 卷积神经网络

SVM --> 坐标下降，Kernel函数集（是否能实现用户自己定义的Kernel类）

CNN --> 卷积操作（不同卷积核，同样的，是否能实现基本的扩展），前向传播，后向传播，全连接（基本的神经网络，感知器）,非线性输出（sigmod等函数，同样的是否可以拓展）

基本数学工具实现
>* 矩阵类（定义，数据结构，基本操作——加、减、乘、row reduction，determinant，SVD，（PCA）——部分多线程）
>* 梯度下降（尝试找到统一的最小的函数，否则舍弃，多线程操作）
>* 卷积操作（多线程的）
>* Kernel函数求值（多线程，实际上是矩阵操作）
>* 前向传播和反向传播（尝试找统一的格式，否则舍弃）

我们要做的事情：
>1. 实现算法库统一的输入输出接口。
>2. 算法库内部的函数可以单独被调用，也可以相互共享变量。
>3. 需要利用算法库实现一个实例，候选为CNN手写识别、验证码识别

分工：
>1. 库内外的沟通，统一的输入输出接口，调用GPU和多线程、多进程的接口。   --zxx
>2. 基本数学工具实现part1：矩阵类和矩阵基本操作，kernel核抽象实现，卷积操作，卷积核。    --tmy
>3. 基本数学工具实现part2：机器学习基本算法（CNN和SVM），梯度下降，坐标下降，传播算法。    --lyz
>4. 利用算法库实现一个实例。自己选手写识别和验证码。手写识别需要GUI（实时传输数据，处理画笔的粗细，将图片的边缘处理好，得到较好的样例，避免算法因为样例的问题导致误差），验证码验证需要（自己生成验证码算法，GUI展示验证码，展示的时候生成验证码并同时输出判断结果）
  -- zqy

Matrix类
>* 构造函数：double[][] data
>* 构造函数: int m, int n, double initial.
>* 加法: Matrix Matrix.add(Matrix)
>* 减法： Matrix Matrix.sub(Matrix)
>* 乘法: Matrix Matrix.multiply(Matrix) throws Exception
>* 数乘: Matrix Matrix.multiply(double scalar)
>* row reduction: Matrix Matrix.reduce()
>* linearSolve: Matrix Matrix.linearSolve(Matrix y): return null when no solutions
>* leastSquareSolve: Matrix Matrix.leastSquareSolve(Matrix y): return exacts solution when possible, apporx. solution otherwise.
>* 转置: Matrix matrix.reverse()
>* determininant: double Matrix.determinant() throws Exception
>* 求逆： Matrix Matrix.inverse() throws Exception
>* SVD: Matrix[] Matrix.SVD() throws Exception. The returned list contains 3 entries, represents U Σ and V'
>* copy: Matrix Matrix.copy()
>* subMatrix: Matrix Matrix.subMatrix(int lx, int ly, int rx, int ry) assert(rx > lx; ry > ly) throws Exception
>* columnUnion: Matrix Matrix.columnUnion(Matrix rhm) throws Exception
>* convolution: Matrix Matrix.convolution(ConvolutionFunction function) throws Exception
>* kernel: Matrix Matrix.kernel(KernelFunction function) throws Exception

MatrixException类 extends Exception

PCA类
>* 构造函数：Matrix dataMatrix, int newDimension
>* 压缩数据：Matrix PCA.encode(Matirx) throws Exception

ConvolutionFunction接口
>* double convolutionOf(Matrix m)
>* int getXSize()
>* int getYSize()

KernelFunction接口
>* double kernelOf(Matrix x1, Matrix x2) throws Exception
 
DataMatrix接口
>* Matrix dataMatrix();

