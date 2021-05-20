package processor;

import java.util.Locale;
import java.util.Scanner;

public class Processor {
    private static double det;
    private final static int index = 0;
    private static Matrix firstMatrix;
    private static Matrix secondMatrix;
    private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

    public static void main(String[] args) {
        menu();
    }

    private static void menu () {
        int choice;
        do {
            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix by a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Transpose matrix");
            System.out.println("5. Calculate a determinant");
            System.out.println("6. Calculate a determinant");
            System.out.println("0. Exit");
            System.out.printf("Your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1: printer(sum());
                    break;
                case 2: printer(multiplicationByNumber());
                    break;
                case 3: printer(multiplication());
                    break;
                case 4: printer(transpose());
                    break;
                case 5: printer(determinant(firstMatrixGetter()));
                    break;
                case 6: printer(inverseMatrix());
                    break;
                case 0: return;
                default: System.out.println("Invalid number!");
            }
        } while (choice != 0);
    }

    private static void transposeMenu () {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.printf("Your choice: ");
    }

    private static double[][] sum () {
        double[][] fM = firstMatrixGetter();
        double[][] sM = secondMatrixGetter();
        double[][] result = new double[fM.length][fM[0].length];

        if (fM.length != sM.length || fM[0].length != sM[0].length) {
            System.out.println("The operation cannot be performed.");
            return null;
        }

        for (int i = 0; i < fM.length; i++) {
            for (int j = 0; j < fM[0].length; j++) {
                result[i][j] = fM[i][j] + sM[i][j];
            }
        }
        return result;
    }

    private static double[][] multiplicationByNumber () {
        double[][] matrix = firstMatrixGetter();
        double[][] result = new double[matrix.length][matrix[0].length];

        System.out.printf("Enter constant: ");
        double number = scanner.nextDouble();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = number * matrix[i][j];
            }
        }

        return result;
    }

    private static double[][] multiplicationByNumber (double[][] matrix, double constant) {
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = constant * matrix[i][j];
            }
        }
        return result;
    }

    private static double[][] multiplication () {
        double sum;
        double[][] fM = firstMatrixGetter();
        double[][] sM = secondMatrixGetter();
        double[][] result = new double[fM.length][sM[0].length];

        if (fM[0].length != sM.length) {
            System.out.println("The operation cannot be performed.");
            return null;
        }

        for (int i = 0; i < fM.length; i++) {
            for (int j = 0; j < sM[0].length; j++) {
                sum = 0;
                for (int k = 0; k < sM.length; k++) {
                    sum += fM[i][k] * sM[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    private static double[][] transpose() {
        int choice;
        int k = 0;
        int m = 0;
        transposeMenu();
        choice = scanner.nextInt();
        double[][] matrix = firstMatrixGetter();
        double[][] result;

        switch (choice) {
            case 1: result = new double[matrix[0].length][matrix.length];
                for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    result[i][j] = matrix[j][i];
                }
            }
                return result;
            case 2: result = new double[matrix[0].length][matrix.length];
                for (int i = matrix[0].length - 1; i >= 0; i--) {
                    for (int j = matrix.length - 1; j >= 0; j--) {
                        result[k][m] = matrix[j][i];
                        m++;
                    }
                    k++;
                    m = 0;
                }
                return result;
            case 3: result = new double[matrix.length][matrix[0].length];
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = matrix[0].length - 1; j >= 0; j--) {
                        result[k][m] = matrix[i][j];
                        m++;
                    }
                    k++;
                    m = 0;
                }
                return result;
            case 4: result = new double[matrix.length][matrix[0].length];
                for (int i = matrix.length - 1; i >= 0; i--) {
                    for (int j = 0; j < matrix[0].length; j++) {
                        result[k][m] = matrix[i][j];
                        m++;
                    }
                    k++;
                    m = 0;
                }
                return result;
            default: break;
        }
        return null;
    }

    private static double[][] transposeByMainDiagonal(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[j][i];
            }
        }
        return result;
    }

    private static double[][] inverseMatrix () {
        double[][] matrix = firstMatrixGetter();
        double det = determinant(matrix);
        double coefficient;

        if (det == 0) {
            System.out.println("This matrix doesn't have an inverse.");
            return null;
        }

        coefficient = 1.0 / det;
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = Math.pow(-1, 2 + i + j) * determinant(minorMaker(matrix, i, j));
            }
        }

        return multiplicationByNumber(transposeByMainDiagonal(result), coefficient);
    }

    private static double[][] firstMatrixGetter() {
        System.out.printf("Enter matrix size: ");
        firstMatrix = new Matrix(scanner.nextInt(), scanner.nextInt());

        System.out.printf("Enter matrix: \n");
        firstMatrix.setMatrix();

        return firstMatrix.getMatrix();
    }

    private static double[][] secondMatrixGetter() {
        System.out.printf("Enter matrix size: ");
        secondMatrix = new Matrix(scanner.nextInt(), scanner.nextInt());

        System.out.printf("Enter matrix: \n");
        secondMatrix.setMatrix();

        return secondMatrix.getMatrix();
    }

    private static void printer (double[][] matrix) {
        if (matrix == null) {
            return;
        }
        System.out.printf("\nThe result is: \n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%f ", matrix[i][j]);
            }
            System.out.printf("\n");
        }
    }

    private static void printer (double determinant) {
        System.out.printf("The result is: \n%f\n", determinant);
    }

    private static double determinant (double[][] matrix) {
        if (matrix.length == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else {
            det = 0;
            for (int i = 0; i < matrix.length; i++) {
                det += Math.pow(-1, i) * matrix[index][i] * determinant(minorMaker(matrix, index, i));
            }
        }
        return det;
    }

    private static double[][] minorMaker (double[][] matrix, int m, int i) {
        int x = 0;
        int y = 0;
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];
        for (int j = 0; j < minor.length; j++) {
            for (int k = 0; k < minor.length; k++) {
                if (y == i) {
                    y++;
                }
                if (x == m) {
                    x++;
                }
                minor[j][k] = matrix[x][y];
                y++;
            }
            y = 0;
            x++;
        }
        return minor;
        /*int x = 1;
        int y = 0;
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];
        for (int j = 0; j < minor.length; j++) {
            for (int k = 0; k < minor.length; k++) {
                if (y == i) {
                    y++;
                }
                minor[j][k] = matrix[x][y];
                y++;
            }
            y = 0;
            x++;
        }
        return minor;*/
    }
}

class Matrix {
    private final Scanner scanner = new Scanner(System.in);
    private int rows;
    private int columns;
    private double[][] matrix;

    public Matrix(int rows, int columns) {
        this.matrix = new double[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.matrix[i][j] = scanner.nextDouble();
            }
        }
    }
}

