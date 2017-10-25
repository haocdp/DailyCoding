package com.haocdp.coding.algorithms.chapter1_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Exercise4 {

    public static void main(String[] args) throws IOException{
        Stack<Character> s = new Stack<>();

        boolean flag = true;

        char[] array;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        while ((array = bf.readLine().toCharArray()).length != 0) {
            flag = true;
            for(char c : array) {
                switch (c) {
                    case '[':
                    case '(':
                    case '{':
                        s.push(c);
                        break;
                    case ']':
                        if (s.pop() != '['){
                            flag = false;
                            s.clear();
                        }
                        break;
                    case ')':
                        if (s.pop() != '('){
                            flag = false;
                            s.clear();
                        }
                        break;
                    case '}':
                        if (s.pop() != '{'){
                            flag = false;
                            s.clear();
                        }
                        break;
                }

                if (!flag)
                    break;
            }
            System.out.println(flag);
        }
    }
}
