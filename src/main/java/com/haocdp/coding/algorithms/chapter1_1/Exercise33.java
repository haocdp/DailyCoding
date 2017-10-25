package com.haocdp.coding.algorithms.chapter1_1;

public class Exercise33 {

    /**
     * 向量点乘
     * @param x
     * @param y
     * @return
     */
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) {
            System.out.println("向量长度不同");
            System.exit(0);
        }

        double z = 0;
        for (int i = 0; i < x.length; i++) {
            z += x[i] * y[i];
        }
        return z;
    }

    /**
     * 矩阵相乘
     * @param a
     * @param b
     * @return
     */
    public static double[][] mult(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            System.out.println("矩阵不能相乘");
            System.exit(0);
        }

        double[][] m = new double[a.length][b[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = 0;
                for (int k = 0; k < b.length; k++) {
                    m[i][j] += a[i][k] * b[k][j];
                }
            }

        }
        return m;
    }

    /**
     * 转置
     * @param a
     * @return
     */
    public static double[][] transpose(double[][] a) {
        double[][] b = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++){
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    public static double[] mult(double[][] a, double[] b) {
        if (a[0].length != b.length) {
            System.out.println("矩阵不能相乘");
            System.exit(0);
        }

        double[] m = new double[a.length];
        for (int i = 0; i < m.length; i++) {
            m[i] = 0;
            for (int k = 0; k < b.length; k++) {
                m[i] += a[i][k] * b[k];
            }
        }
        return m;
    }

    public static double[] mult(double[] a, double[][] b) {
        if (a.length != b.length) {
            System.out.println("矩阵不能相乘");
            System.exit(0);
        }

        double[] m = new double[b[0].length];
        for (int i = 0; i < m.length; i++) {
            m[i] = 0;
            for (int k = 0; k < a.length; k++) {
                m[i] += a[k] * b[i][k];
            }
        }

        return m;
    }

    public static void main(String[] args) {
        double[] x = {1,2,3};
        double[] y = {2,3,4};
        double[][] a = {{1,2,3},
                        {2,3,4},
                        {3,4,5}};
        double[][] b = {{1,4},{2,5},{3,6}};

        System.out.println(dot(x, y));
        double[][] temp = mult(a, b);
        temp = transpose(b);
        double[] temp2 = mult(a, x);
        temp2 = mult(y, a);
    }
}
