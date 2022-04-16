package it.unibo.oop.lab.workers02;

public class MultiThreadedSumMatrix implements SumMatrix {

    final int n;
    double result = 0;

    public MultiThreadedSumMatrix(final int n) {
        this.n = n;
    }

    @Override
    public double sum(double[][] matrix) {
        // TODO Auto-generated method stub

        Worker w;

        final int offset = matrix.length / this.n;
        for (int i = 0; i < this.n; i++) {

            final int endIndex;
            final int startIndex = offset * i;
            if (this.n - i == 1) {
                endIndex = startIndex + (matrix.length - startIndex) - 1;
            } else {
                endIndex = (offset * (i + 1)) - 1;
            }

            w = new Worker(startIndex, endIndex, matrix.length, matrix);
            w.start();
            try {
                w.join();

                result = result + w.getResult();

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return this.result;

    }

    private static class Worker extends Thread {

        final private int startIndex;
        final private int endIndex;
        final int columns;
        final double matrix[][];
        private double sum = 0;

        public Worker(final int startIndex, final int endIndex, final int columns, final double[][] matrix) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.columns = columns;
            this.matrix = matrix;
        }

        public double getResult() {
            return this.sum;
        }

        public void run() {

            for (int i = startIndex; i < endIndex + 1; i++) {
                for (int j = 0; j < columns; j++) {

                    sum = sum + matrix[i][j];

                }

            }

        }
    }

}
