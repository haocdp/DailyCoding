package com.haocdp.coding.algorithms.chapter1_1;

public class Exercise15 {

    public static void main(String[] args){

    }

    /**
     * 接受一个整型数组a[]和一个整数M为参数并返回一个大小
     * 为M的数组，其中第i个元素的值为整数i在参数数组中出现的次数。
     * 如果a[]中的值均为0到M-1之间，返回数组中所有元素之和应该和a.length
     * 相等。
     * @param a 整型数组
     * @param M 整数
     * @return 一个大小为M的数组
     */
    public static int[] histogram(int[] a, int M) {
        int[] m = new int[M];

        for (int i : a) {
            if (0 <= i && i <= M - 1) {
                m[i]++;
            }
        }
        return m;
    }
}
