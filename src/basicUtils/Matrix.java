package basicUtils;

/**
 * Created by tmy on 2016/12/10.
 */
public class Matrix {
    //TODO: all the methods in this class are remained for implementation
	public int m; // height
	public int n; // width
	public double data[][];
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
		try {
			for (int i = 0; i < m; i ++) 
				for (int j = 0; j < n; j ++) 
					data[i][j] += b.data[i][j];
		} catch (Exception e) {
			
		}
		return this;
	}
	public Matrix sub(Matrix b) {
		try {
			for (int i = 0; i < m; i ++) 
				for (int j = 0; j < n; j ++) 
					data[i][j] -= b.data[i][j];
		} catch (Exception e) {
			
		}
		return this;
	}
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
	
	// @return: 高斯消去后获得的上三角矩阵
	public Matrix reduce() {
		for (int h = 0; h < m; h++) {
			int maxRow = h;
			for (int i = h+1; i < m; i ++)
				if (cmp(Math.abs(data[i][h]),Math.abs(data[maxRow][h])) > 0) maxRow = i;
			if (maxRow != h) 
				swapRow(h, maxRow);
			
			if (cmp(data[maxRow][h], 0) == 0) { //all 0
				continue;
			}			
			for (int i = h+1; i < m; i ++) {
				double temp = data[i][h] / data[h][h];
				for (int j = h; j < n; j++)
					data[i][j] -= data[h][j] * temp;				
			}
			//System.out.println("case " + i + "maxj ");
			//print();
		}
		return this;
	}

	public Matrix inverse() {
		Matrix ans = new Matrix(m, n, 1);
		if (m != n) {
			//throw exception
			return ans;
		}
		
		for (int i = 0; i < n; i++) {
			int maxj = i;
			for (int j = i+1; j < n; j ++)
				if (cmp(Math.abs(data[j][i]),Math.abs(data[maxj][i])) > 0) maxj = j;
			if (cmp(data[maxj][i], 0) == 0) { //all 0
				//非满秩矩阵，no inverse
				return ans;
			}
			swapRow(i, maxj); 
			ans.swapRow(i, maxj);
			// change the 45du data to 1
			double temp = data[i][i];
			data[i][i] = 1;
			ans.data[i][i] /= temp; 
			// row 0->n, change data[j][i] to 0 
			for (int j = 0; j < n; j ++) 
				if (j != i) {
					temp = data[j][i];
					for (int k = 0; k < n; k++) {
						data[j][k] -= (temp*data[i][k]); 
						ans.data[j][k] -= (temp*ans.data[i][k]);
					}
			}
			//System.out.println("case " + i + "maxj ");
			print();
		}
		return ans;
	}
	// @return 高斯消元解线性方程组后的
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
		// change the 45du data to 1
		for (int i = 0; i < m; i ++) {
			double temp = augmentedMatrix.data[i][i];
			if (cmp(temp, 0) != 0) {				
				for (int j = i; j <= n; j++) {
					augmentedMatrix.data[i][j] /= temp;
				}
			}
		}		
		// back substitution
		Matrix solutionVector = new Matrix(m, 1, 0);
		for (int i = m-1; i > 0; i --)
			for (int j = i-1; j >= 0; j--) {
				double temp = augmentedMatrix.data[j][i];
				for (int k = i; k < n+1; k ++) {
					augmentedMatrix.data[j][k] -= augmentedMatrix.data[i][k] * temp;
				}
			}		
		// get solutionVector
		for (int i = 0; i < m; i ++) 
			solutionVector.data[i][0] = augmentedMatrix.data[i][n];
		return solutionVector;
	}
	
	
	
	private int cmp(double a, double b) {
		//System.out.println("cmp " + a + ", "+ b);
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
	
	public void print() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++)
				System.out.print(data[i][j] + " ");
			System.out.println();
		}
	}	

  
    public double[][] rawData() {
        return null;
    }
    public Matrix leastSquareSolve(Matrix y) {
        return null;
    }
    public Matrix transpose() {
        return null;
    }
    public double determinant() {
        return 0;
    }
    public Matrix[] svd() {
        return null;
    }
    public Matrix copy() {
        return null;
    }
}
