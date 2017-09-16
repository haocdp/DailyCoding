package com.haocdp.coding;

//import java.util.ArrayList;
import java.util.Scanner;

/***
 *
 */
public class Test {

    public static void main(String[] args) {
        int n = 0;

        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        if (n < 1 || n > 300000){
            System.out.println("n输入范围错误");
            System.exit(0);
        }

        int[] likes = new int[n];
        int min = Integer.MAX_VALUE;
        int max = 0;

        for (int i = 0 ; i < likes.length;i++) {
            likes[i] = scanner.nextInt();
            if (likes[i] < min)
                min = likes[i];
            if (likes[i] > max)
                max = likes[i];
        }

        int q = 0;
        q = scanner.nextInt();

        if (q < 1 || q > 300000){
            System.out.println("q输入范围错误");
            System.exit(0);
        }

        int[][] querys = new int[q][3];
        //ArrayList<int[]> arrayList = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            //int[] query = new int[3];
            for (int j = 0; j < 3; j++){
                querys[i][j] = scanner.nextInt();
            }
            //arrayList.add(query);
        }
        //System.out.println();

        int[] result = new int[q];

        for (int i = 0; i < q; i++) {
            int[] temp = querys[i];
            if (temp[2] < min || temp[2] > max)
                continue;
            for (int j = temp[0]; j <= temp[1]; j ++) {
                if (likes[j - 1] == temp[2]) {
                    result[i] += 1;
                }
            }
        }

        for (int i : result){
            System.out.println(i);
        }
    }
}
