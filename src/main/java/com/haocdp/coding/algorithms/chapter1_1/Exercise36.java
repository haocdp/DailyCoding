package com.haocdp.coding.algorithms.chapter1_1;

import edu.princeton.cs.algs4.StdRandom;

public class Exercise36 {

    public static void shuffle(double[] a) {
        int N = a.length;
        for(int i = 0; i < N; i++) {
            int r = i + StdRandom.uniform(N - i);
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void main(String[] args){
        double[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        shuffle(a);
    }
}
