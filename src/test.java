import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		//Matrix matrix = new Matrix();
		final Scanner inSys = new Scanner(System.in);
		int m = inSys.nextInt();
		int n = inSys.nextInt();
		double data[][] = new double[m][n];
		for (int i = 0; i < m; i ++)
			for (int j = 0; j < n; j ++)
				data[i][j] = inSys.nextDouble();
		Matrix matrix = new Matrix(data);
		//matrix.print();
		Matrix value = new Matrix(m,1);
		for (int i = 0; i < m; i ++)
			value.data[i][0] = inSys.nextDouble();
		Matrix ans = matrix.linearSolve(value);
		ans.print();
	}

}
