package com.haocdp.coding;

import com.haocdp.coding.csvCoding.CSVFormatter;

/**
 * 运行程序的入口
 */
public class Main {

    public static void main(String[] args) throws Exception{
        String inputFile1 = "/Users/haoc_dp/Desktop/tripsod20140227_1.mpos.csv";
        String inputFile2 = "/Users/haoc_dp/Desktop/tripsod20140227_2.mpos.csv";
        String outputFile = "/Users/haoc_dp/Desktop/od.csv";

        CSVFormatter.merge(inputFile1,inputFile2, outputFile);
    }
}
