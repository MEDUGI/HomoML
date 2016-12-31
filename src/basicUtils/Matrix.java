package basicUtils;

/**
 * Created by tmy on 2016/12/10.
 */
public class Matrix {
    //TODO: all the methods in this class are remained for implementation
	private int m; // height
	private int n; // width
	private double data[][];
	private static double eps = 1e-8;
	
	public Matrix() {}
	public Matrix( double[][] data) {
		m = data.length;
		n = data[0].length;
		this.data = new double[m][n];
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j ++)
				this.data[i][j] = data[i][j];
	}
	public Matrix(int m, int n, double initial) {
		this.m = m;
		this.n = n;
		this.data = new double[m][n];
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j ++)
				this.data[i][j] = initial;
	}
	public Matrix(int m, int n) {
		this.m = m;
		this.n = n;
 		this.data = new double[m][n];
	}	
	public Matrix(Matrix mat) {
		m = mat.m;
		n = mat.n;
		data = new double[m][n];
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j++)
				data[i][j] = mat.data[i][j];
	}
	
	public Matrix add(Matrix b) {
		if (b.n != n || b.m != m) return null;
		Matrix ans = new Matrix(m,n,0);
		for (int i = 0; i < m; i ++) 
			for (int j = 0; j < n; j ++) 
				ans.data[i][j] = data[i][j] + b.data[i][j];
		return ans;
	}
	public Matrix sub(Matrix b) {
		if (b.n != n || b.m != m) return null;
		Matrix ans = new Matrix(n,m,0);
		for (int i = 0; i < m; i ++) 
			for (int j = 0; j < n; j ++) 
				ans.data[i][j] = data[i][j] - b.data[i][j];
		return ans;
	}
	// this*b
	public Matrix multiply(Matrix b) throws Exception {
		if (n != b.m)
			throw new Exception("Matrix A's width doesn't match Matrix B's height.");
		Matrix c = new Matrix(m,b.n,0);
		for (int i = 0; i < m; i ++) 
			for (int j = 0; j < b.n; j ++) 
				for (int k = 0; k <n; k ++)
					c.data[i][j] += this.data[i][k] * b.data[k][j];
		return c;
	}
	public Matrix multiply(double x) {
		Matrix ans = new Matrix(this);
		for (int i = 0; i < m; i ++) 
			for (int j = 0; j < n; j ++) 
				ans.data[i][j] *= x;
		return ans;
	}	
	public Matrix reverse() {
		Matrix y = new Matrix(n,m);
		for (int i = 0; i < m; i ++) 
			for (int j = 0; j < n; j++) {
				y.data[j][i] = this.data[i][j];
			}	
		return y;
	}
	public Matrix columnUnion(Matrix rightMatrix) throws Exception {
		if (m != rightMatrix.m) 
			throw new Exception("Matrix heights don't match.");
		Matrix ans = new Matrix(m, n + rightMatrix.n);
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < ans.n; j ++)
				if (j < n)
					ans.data[i][j] = this.data[i][j];
				else 
					ans.data[i][j] = rightMatrix.data[i][j-n];
		return ans;
	}
	
	// @return: 将矩阵�?�过高斯消去变成上三角矩阵，返回交换行次�?
	public int reduce() {
		int swapTimes = 0;
		for (int h = 0; h < m; h++) {
			int maxRow = h;
			for (int i = h+1; i < m; i ++)
				if (cmp(Math.abs(data[i][h]),Math.abs(data[maxRow][h])) > 0) maxRow = i;
			if (maxRow != h) { 
				swapRow(h, maxRow);
				swapTimes ++;
			}
			if (cmp(data[maxRow][h], 0) == 0) { //all 0
				continue;
			}			
			for (int i = h+1; i < m; i ++) {
				double temp = data[i][h] / data[h][h];
				for (int j = h; j < n; j++)
					data[i][j] -= data[h][j] * temp;				
			}
		}
		return swapTimes;
	}

	// change the 45du data to 1
	public boolean normalization() {
		for (int i = 0; i < m; i ++) {
			double temp = data[i][i];
			if (cmp(temp, 0) != 0) {				
				for (int j = i; j < n; j++) 
					data[i][j] /= temp;
			}
		}			
		return true;
	}
	private boolean backSubstitution() {
		for (int i = m-1; i > 0; i --)
			for (int j = i-1; j >= 0; j--) {
				double temp = data[j][i];
				for (int k = i; k < n; k ++) {
					data[j][k] -= data[i][k] * temp;
				}
			}			
		return true;
	}
	// @return 高斯消元解线性方程组后的向量
	public Matrix linearSolve(Matrix valueVector) {
		if (valueVector.m != m)
			return null;
		Matrix augmentedMatrix;
		// get upper triangular augmentedMatrix
		try {
			augmentedMatrix = columnUnion(valueVector);
			augmentedMatrix.reduce();
		} catch (Exception e) {
			return null;
		}
		augmentedMatrix.normalization();
		augmentedMatrix.backSubstitution();		
		// get solutionVector
		Matrix solutionVector = new Matrix(m, 1, 0);
		for (int i = 0; i < m; i ++) 
			solutionVector.data[i][0] = augmentedMatrix.data[i][n];
		return solutionVector;
	}
	// @return 行列�?
	public double determinant() throws Exception {
		if (m != n)
		    throw new Exception("It's not a square matrix.");
		Matrix matrix = new Matrix(this);
		int swapTimes = matrix.reduce();
		double result = 1.0;
		for(int i = 0; i < matrix.n; i++) {
		    result *= matrix.data[i][i];
		}
		if (swapTimes % 2 == 1)
		    result *= -1;
		return result;		
	}
	public int getRank() {
		Matrix mat = this.copy();
		mat.reduce();
		int i = 0;
		while (i < m && cmp(data[i][i],0)!=0) i++;
		return i;
	}
	public Matrix inverse() throws Exception{
		if (m != n)
			throw new Exception("It's not a square matrix.");
		Matrix rightMat = new Matrix(m,n,0);
		for (int i = 0; i < m; i ++)
			rightMat.data[i][i] = 1;
		Matrix augmentedMatrix = columnUnion(rightMat);
		
		augmentedMatrix.reduce();
		int rank = 0;
		while (rank < m && cmp(data[rank][rank],0)!=0) rank++;
		if (rank != m) 
			throw new Exception("The rank of matrix is not equal its height.");
		augmentedMatrix.normalization();
		augmentedMatrix.backSubstitution();
		// get inverse matrix
		double[][] newdata = new double[m][n];		
		for (int i = 0; i < m; i ++)
			for (int j = n; j < n+n; j++)
				newdata[i][j-n] = augmentedMatrix.data[i][j];
		return new Matrix(newdata);
	}
	public Matrix leastSquareSolve(Matrix y) {
	    return null;
	}
	
	public Matrix[] svd() {
	    return null;
	}
	
	
	public void unification() {
		for (int j = 0; j < n; j ++) {
			double sum = 0.0;
			for (int i = 0; i < m; i ++)
				sum += data[i][j];
			for (int i = 0; i < m; i++)
				if (cmp(sum,0)!=0)
					data[i][j] /= sum;
		}
	}
	// @return the max eigenvalue of Matrix, and the eigenvector changed
	public double getMAXEigenvalue(Matrix eigenvector) {		
		Matrix W = copy();
		W.unification();
		double[][] newdata = new double[m][1];
		for (int i = 0; i < m; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++)
				sum += W.data[i][j];
			newdata[i][0] = sum;
		}
		Matrix WW = new Matrix(newdata);
		WW.unification();
		eigenvector = WW.copy();
		double ans = 0.0; 
		try {
			Matrix ANS = this.multiply(WW);
			for (int i = 0; i < m; i ++)
				ans += ANS.data[i][0] / WW.data[i][0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans/m;
	}
	// @return norm1: the max abs_sum of lines
	public double getNorm1() {
		double norm1 = 0.0;
		for (int j = 0; j < n; j++) {
			double sum = 0.0;
			for (int i = 0; i < m; i++)
				sum += Math.abs(data[i][j]);
			if (cmp(sum, norm1) > 1) norm1 = sum;
		}
		return norm1;
	}
	// @return norm2: the sqrt of the max eigenvalue of AT*A 
	public double getNorm2() {
		Matrix T = this.reverse();
		try {
			Matrix mat = T.multiply(this);
			Matrix eigenvector = new Matrix();
			double eigenvalue = mat.getMAXEigenvalue(eigenvector);
			return Math.sqrt(eigenvalue);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return 0.0;
	}
	// @return norm: the max abs_sum of rows
	public double getNormInfinity() {
		double norm = 0.0;
		for (int i = 0; i < m; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++)
				sum += Math.abs(data[i][j]);
			if (cmp(sum, norm) > 0)
				norm = sum;
		}
		return norm;
	}
	// @return normF: the Frobenius norm
	public double getNormF() {
		double normF = 0.0;
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j++)
				normF += data[i][j] * data[i][j];
		normF = Math.sqrt(normF);
		return normF;
	}
	// @return norm2: the norm2 for vector
	public double getNorm2Vector() {
		double norm2 = 0.0;
		for (int i = 0; i < m; i++)
			norm2 += data[i][0];
		norm2 = Math.sqrt(norm2);
		return norm2;
	}
	// @return norm: the min abs_sum of rows
	public double getNormInfinitesimalVector() {
		double norm = 0.0;
		for (int i = 0; i < m; i++) {
			double sum = 0.0;
			for (int j = 0; j < n; j++)
				sum += Math.abs(data[i][j]);
			if (cmp(sum, norm) < 0)
				norm = sum;
		}
		return norm;
	}
	public double getNormPVector(double p) {
		double norm = 0.0;
		for (int i = 0; i < m; i ++)
			norm += Math.pow(Math.abs(data[i][0]), p) / p;
		return norm;
	}
	
	private int cmp(double a, double b) {
		if (a-b > eps) return 1;
		if (b-a > eps) return -1;
		return 0;
	}
	private void swapRow(int a, int b) {
		if (a == b) return;
		double x;
		for (int i = 0; i < n; i ++) {
			x = data[a][i];
			data[a][i] = data[b][i];
			data[b][i] = x;
		}
	}
	
	public Matrix subMatrix(int left, int top, int right, int bottom) {
		int mm = bottom-top, nn = right-left;
	    double[][] newdata = new double[mm][nn];
	    for (int i = top; i < bottom; i ++)
	    	for (int j = left; j < right; j++)
	    		newdata[i-top][j-left] = data[i][j];
	    return new Matrix(newdata);
	}

	public double get(int x, int y) {
		return data[x][y];
	}	
	public boolean set(int x, int y, double value) {
		if (x >= m || x < 0) return false;
		if (y >= n || y < 0) return false;
		data[x][y] = value;
		return true;
	}
	// @return Matrix for data[x][] (1*width)
	public Matrix get(int x) {
		double[][] newrow = new double[1][n];
		for (int j = 0; j < n; j++)
			newrow[0][j] = data[x][j];		
		return new Matrix(newrow);
	}
	public int getHeight() {
		return m;
	}
	public void setHeight(int height) {
		m = height;
	}
	public int getWidth() {
		return n;
	}
	public void setWidth(int width) {
		n = width;
	}

	public double vectorLength(Matrix vector) throws Exception {
	    if (vector.n != 1)
	        throw new Exception("The inputed matrix is not a vector");
	    double result = 0;
	    for(int i = 0; i < vector.m; i++)
	        result += vector.data[i][0] * vector.data[i][0];
	    return Math.sqrt(result);		
	}
	// @return data[][]
	public double[][] getData() {
		double[][] newdata = new double[m][n];
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j ++)
				newdata[i][j] = data[i][j];
	    return newdata;
	}

	public Matrix copy() {
		return new Matrix(this);
	}
	public void print() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++)
				System.out.print(data[i][j] + " ");
			System.out.println();
		}
	}
}