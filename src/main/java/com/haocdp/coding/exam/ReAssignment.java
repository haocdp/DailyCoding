package com.haocdp.coding.exam;

import edu.princeton.cs.algs4.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReAssignment {

    public static void main(String[] args) throws IOException{
        int n,x;

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String line1 = bf.readLine();
        String line2 = bf.readLine();

        n =Integer.parseInt(line1.split(" ")[0]);
        x =Integer.parseInt(line1.split(" ")[1]);
        String[] array = line2.split(" ");

        //Scanner scanner = new Scanner(System.in);
        //n = scanner.nextInt();
        //x = scanner.nextInt();

        int a[] = new int[n];

        for (int i = 0; i < n ;i++) {
            a[i] = Integer.parseInt(array[i]);
        }

        boolean flag = true;
        int iter = x-1;
        int pn = 0;

        while(flag) {
            if (a[iter] == 0 && distance(n, x-1, iter) == pn % n) {
                //flag = false;
                a[iter] = pn;
                break;
            }
            a[iter] = a[iter] - 1;
            pn++;
//            if (a[iter] == 0 && distance(n, x-1, iter) == pn % n) {
//                flag = false;
//                a[iter] = pn;
//            }

            if (iter == 0)
                iter = n-1;
            else
                iter--;
        }

        for (int i = 0; i < n ;i++) {
            if (i != n-1)
                System.out.print(a[i] + " ");
            else
                System.out.print(a[i]);
        }
        //System.out.println();
    }

    private static int distance(int n, int x, int iter) {
        if (iter > x) {
            return n - (iter - x);
        }
        return x - iter;
    }
}
