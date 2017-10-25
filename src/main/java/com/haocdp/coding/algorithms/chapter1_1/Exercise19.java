package com.haocdp.coding.algorithms.chapter1_1;

public class Exercise19 {

    /**
     * 斐波那契数列递归实现
     * @param N
     * @return
     */
    public static long F(int N) {
        if (N == 0) return 0;
        if (N == 1) return 1;
        return F(N-1) + F(N-2);
    }

    /**
     * 使用数组实现的斐波那契数列
     * @param N
     * @return
     */
    public static long F2(int N) {
        if (N == 0) return 0;
        if (N == 1) return 1;

        long[] arr = new long[N + 1];
        arr[0] = 0;
        arr[1] = 1;

        for (int i = 2; i <= N ; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[N];
    }

    public static void main(String[] args) {
        for (int N = 0; N < 100; N++)
            System.out.println(N + " " + F2(N));
    }
}