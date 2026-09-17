import java.util.Random;

class MatrixWorker extends Thread {

    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int row;
    private int col;

    MatrixWorker(int[][] A, int[][] B, int[][] C, int row, int col) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.row = row;
        this.col = col;
    }

    public void run() {

        int sum = 0;

        for (int k = 0; k < 100; k++) {
            sum = sum + A[row][k] * B[k][col];
        }

        C[row][col] = sum;
    }
}

public class MatrixMultiplicationThreads {

    static final int SIZE = 100;

    public static void main(String[] args) {

        int[][] A = new int[SIZE][SIZE];
        int[][] B = new int[SIZE][SIZE];
        int[][] C = new int[SIZE][SIZE];

        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                A[i][j] = random.nextInt(10);
                B[i][j] = random.nextInt(10);
            }
        }

        System.out.println("   MATRIX MULTIPLICATION USING THREADS");
        System.out.println("Matrix A : 100 x 100");
        System.out.println("Matrix B : 100 x 100");
        System.out.println("Result   : 100 x 100");
        System.out.println();

        Thread[] threads = new Thread[SIZE * SIZE];

        int index = 0;

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                threads[index] =
                    new MatrixWorker(A, B, C, i, j);

                threads[index].setName("T-" + index);

                threads[index].start();

                index++;
            }

            System.out.println("Processing row " + (i + 1) + " / 100");
        }

        for (int i = 0; i < threads.length; i++) {

            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println();
        System.out.println("==========================================");
        System.out.println("All threads completed!");
        System.out.println("Total threads used: " + (SIZE * SIZE));
        System.out.println("==========================================");

        System.out.println();
        System.out.println("MATRIX A (100 x 100)");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {
                System.out.print(A[i][j] + "\t");
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("MATRIX B (100 x 100)");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {
                System.out.print(B[i][j] + "\t");
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("RESULT MATRIX C = A x B (100 x 100)");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {
                System.out.print(C[i][j] + "\t");
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("=");
        System.out.println("Matrix Multiplication Completed!");
        System.out.println("==");
    }
}
