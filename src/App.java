/**
 * Project 1 - Matrix multiplication
 * One-file program which implements different algorithms for multiplying matrices.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws Exception {
        // Initialize matrices
        for(int i = 1; i <= 10; i++) {
            System.out.printf("----- TEST %d -----\n", i);
            String filename = "matrices/matrix" + i + ".txt";
            int[][][] matrices = generateMatrixFromFile(filename);
            int[][] a = matrices[0];
            int[][] b = matrices[1];
            int[][] c = strassenMult(a, b);
            printResults(a, b, c);
        }
    }

    /**
     * Reads a file and writes to two matrices.
     * The file is assumed to be of the following format (and will fail if it isn't):
     *  First line: the number of rows/columns of each matrix
     * Second line: the first row of matrix 1, separated by spaces (ex. 1 2 3 4)
     *  Third line: the second row of matrix 1
     *    ...
     *    nth line: the last row of matrix 2
     * 
     * @param filename    String  The relative path to the file to read the matrix from
     * @return            both matrices in an array
     * @throws            FileNotFoundException
     */
    public static int[][][] generateMatrixFromFile(String filename) throws FileNotFoundException {
        File f = new File(filename);
        Scanner sc = new Scanner(f);
        int n = Integer.parseInt(sc.nextLine());

        // Initialize a
        int[][] a = new int[n][n];
        for(int i = 0; i < n; i++) {
            int[] line = Stream.of(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray(); // Get the line as an integer array
            for(int j = 0; j < n; j++) {
                a[i][j] = line[j];
            }
        }

        // Initialize b
        int[][] b = new int[n][n];
        for(int i = 0; i < n; i++) {
            int[] line = Stream.of(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray(); // Get the line as an integer array
            for(int j = 0; j < n; j++) {
                b[i][j] = line[j];
            }
        }
        sc.close();
        return new int[][][] {a, b};
    }

    /**
     * Multiplies two matrices using classical matrix multiplication.
     * 
     * @param a   The first matrix to multiply
     * @param b   The first matrix to multiply
     * @return    int[][] the resultant matrix
     */
    public static int[][] classicalMult(int[][] a, int[][] b) {
        int n = a.length;
        int c[][] = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                c[i][j] = 0;
                for(int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    /**
     * Multiplies two matrices using divide and conquer algorithm.
     * 
     * @param a   The first matrix to multiply
     * @param b   The first matrix to multiply
     * @return    int[][] the resultant matrix
     */
    public static int[][] divAndConquerMult(int[][] a, int[][] b) {
        int n = a.length;
        int c[][] = new int[n][n];
        if(n == 1) {
            c[0][0] = a[0][0] * b[0][0];
        }
        else {
            // Split the matrices into four sub-matrices
            int half_n = n/2;
            int a11[][] = new int[half_n][half_n];
            int a12[][] = new int[half_n][half_n];
            int a21[][] = new int[half_n][half_n];
            int a22[][] = new int[half_n][half_n];
            int b11[][] = new int[half_n][half_n];
            int b12[][] = new int[half_n][half_n];
            int b21[][] = new int[half_n][half_n];
            int b22[][] = new int[half_n][half_n];
            int c11[][] = new int[half_n][half_n];
            int c12[][] = new int[half_n][half_n];
            int c21[][] = new int[half_n][half_n];
            int c22[][] = new int[half_n][half_n];

            // Initialize a and b
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(i < half_n) {
                        if(j < half_n) {
                            a11[i][j] = a[i][j];
                            b11[i][j] = b[i][j];
                        }
                        else {
                            a12[i][j-half_n] = a[i][j];
                            b12[i][j-half_n] = b[i][j];
                        }
                    }
                    else {
                        if(j < half_n) {
                            a21[i-half_n][j] = a[i][j];
                            b21[i-half_n][j] = b[i][j];
                        }
                        else {
                            a22[i-half_n][j-half_n] = a[i][j];
                            b22[i-half_n][j-half_n] = b[i][j];
                        }
                    }
                }
            }
            
            // Recursive calls for C
            c11 = addMatrices(divAndConquerMult(a11, b11), divAndConquerMult(a12, b21));
            c12 = addMatrices(divAndConquerMult(a11, b12), divAndConquerMult(a12, b22));
            c21 = addMatrices(divAndConquerMult(a21, b11), divAndConquerMult(a22, b21));
            c22 = addMatrices(divAndConquerMult(a21, b12), divAndConquerMult(a22, b22));

            // Reconstruct C
            for(int i = 0; i < half_n; i++) {
                for(int j = 0; j < half_n; j++) {
                    c[i][j] = c11[i][j];
                    c[i][j + half_n] = c12[i][j];
                    c[i + half_n][j] = c21[i][j];
                    c[i + half_n][j + half_n] = c22[i][j];
                }
            }
        }

        return c;
    }

    /**
     * Multiplies two matrices using Strassen's algorithm.
     * 
     * @param a   The first matrix to multiply
     * @param b   The first matrix to multiply
     * @return    int[][] the resultant matrix
     */
    public static int[][] strassenMult(int[][] a, int[][] b) {
        int n = a.length;
        int c[][] = new int[n][n];
        if(n == 1) {
            c[0][0] = a[0][0] * b[0][0];
        }
        else {
            // Split the matrices into four sub-matrices
            int half_n = n/2;
            int a11[][] = new int[half_n][half_n];
            int a12[][] = new int[half_n][half_n];
            int a21[][] = new int[half_n][half_n];
            int a22[][] = new int[half_n][half_n];
            int b11[][] = new int[half_n][half_n];
            int b12[][] = new int[half_n][half_n];
            int b21[][] = new int[half_n][half_n];
            int b22[][] = new int[half_n][half_n];
            int c11[][] = new int[half_n][half_n];
            int c12[][] = new int[half_n][half_n];
            int c21[][] = new int[half_n][half_n];
            int c22[][] = new int[half_n][half_n];

            // Initialize a and b
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(i < half_n) {
                        if(j < half_n) {
                            a11[i][j] = a[i][j];
                            b11[i][j] = b[i][j];
                        }
                        else {
                            a12[i][j-half_n] = a[i][j];
                            b12[i][j-half_n] = b[i][j];
                        }
                    }
                    else {
                        if(j < half_n) {
                            a21[i-half_n][j] = a[i][j];
                            b21[i-half_n][j] = b[i][j];
                        }
                        else {
                            a22[i-half_n][j-half_n] = a[i][j];
                            b22[i-half_n][j-half_n] = b[i][j];
                        }
                    }
                }
            }

            // Recursive calls for some letter-named variables
            int[][] p = strassenMult(addMatrices(a11, a22), addMatrices(b11, b22));
            int[][] q = strassenMult(addMatrices(a21, a22), b11);
            int[][] r = strassenMult(a11, subtractMatrices(b12, b22));
            int[][] s = strassenMult(a22, subtractMatrices(b21, b11));
            int[][] t = strassenMult(addMatrices(a11, a12), b22);
            int[][] u = strassenMult(subtractMatrices(a21, a11), addMatrices(b11, b22));
            int[][] v = strassenMult(subtractMatrices(a12, a22), addMatrices(b21, b22));

            // This is why operator overloading is useful
            c11 = addMatrices(p, s);
            c11 = subtractMatrices(c11, t);
            c11 = addMatrices(c11, v);
            c12 = addMatrices(r, t);
            c21 = addMatrices(q, s);
            c22 = addMatrices(p, r);
            c22 = subtractMatrices(c22, q);
            c22 = addMatrices(c22, u);

            // Reconstruct C
            for(int i = 0; i < half_n; i++) {
                for(int j = 0; j < half_n; j++) {
                    c[i][j] = c11[i][j];
                    c[i][j + half_n] = c12[i][j];
                    c[i + half_n][j] = c21[i][j];
                    c[i + half_n][j + half_n] = c22[i][j];
                }
            }
        }

        return c;
    }

    /**
     * Prints a matrix in human-readable format
     * @param matrix int[][]   The matrix to print
     */
    public static void printMatrix(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Prints the results of matrix multiplication
     * 
     * @param a    The first matrix
     * @param b    The second matrix
     * @param c    The resultant matrix
     */
    public static void printResults(int[][] a, int[][] b, int[][] c) {
        System.out.println("Matrix A:");
        printMatrix(a);
        System.out.println("Matrix B:");
        printMatrix(b);
        System.out.println("Resultant matrix C:");
        printMatrix(c);
    }

    // Helper function to add matrices since Java doesn't have a built-in function...
    public static int[][] addMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int c[][] = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    // Another helper function to subtract matrices
    public static int[][] subtractMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int c[][] = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }
}