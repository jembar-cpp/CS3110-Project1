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
        String filename = "matrices/matrix1.txt";
        int[][][] matrices = generateMatrixFromFile(filename);
        int[][] a = matrices[0];
        int[][] b = matrices[1];
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
}