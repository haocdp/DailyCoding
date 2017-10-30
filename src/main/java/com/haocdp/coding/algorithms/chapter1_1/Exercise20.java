package com.haocdp.coding.algorithms.chapter1_1;

import java.util.Date;

public class Exercise20 {

    /**
     * 阶乘的普通算法
     * @param N
     * @return
     */
    public static long factorial(int N) {
        if (N == 1) return 1;
        return N * factorial(N -1);
    }

    /**
     * 阶乘的复用栈帧递归算法
     * @param N
     * @param result
     * @return
     */
    public static long factorial2(int N, long result) {
        if (N == 1)
            return result;
        return factorial2(N - 1, N * result);
    }

    public static void main(String[] args) {
        long startTime=System.nanoTime();   //获取开始时间

//        System.out.println(factorial(100));
        System.out.println(factorial2(1000, 1));

        long endTime=System.nanoTime(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ns");
    }
}
