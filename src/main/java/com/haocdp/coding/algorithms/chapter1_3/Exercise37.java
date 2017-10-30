package com.haocdp.coding.algorithms.chapter1_3;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Exercise37 {

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int M = Integer.parseInt(args[1]);

        Queue<Integer> queue = new ArrayBlockingQueue<>(N);
        for(int i = 0; i < N; i++)
            queue.add(i);

        int index = 1;
        while(true) {
            if (queue.size() != 0) {
                if (index != M) {
                    queue.add(queue.poll());
                    index++;
                    continue;
                }

                System.out.print(queue.poll() + " ");
                index = 1;
            }else {
                break;
            }

        }
    }
}
