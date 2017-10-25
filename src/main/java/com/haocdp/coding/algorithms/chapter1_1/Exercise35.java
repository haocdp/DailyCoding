package com.haocdp.coding.algorithms.chapter1_1;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Random;

public class Exercise35 {

    public static double[] dist() {
        int SIDES = 6;
        double[] dist = new double[2 * SIDES + 1];
        for(int i = 1; i <= SIDES; i++)
            for(int j = 1; j <= SIDES; j++)
                dist[i + j] += 1.0;

        for (int k = 2; k <= 2 * SIDES; k++)
            dist[k] /= 36.0;

        return dist;
    }

    public static void main(String[] args) {
        double[] dist = dist();

        double N = 0;
        Random random = new Random();
        double[] real = new double[13];
        while(true) {
            N++;
            int a = random.nextInt(6) + 1;
            int b = random.nextInt(6) + 1;
            real[a + b] += 1.0;

            boolean flag = true;
            for (int k = 2; k <= 12; k++){
                if (!(Math.abs(real[k] / N - dist[k]) < 0.001))
                    flag = false;
            }
            if (flag)
                break;
        }
        System.out.println(N);
    }
}
