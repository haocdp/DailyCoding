package com.haocdp.coding.csvCoding;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/***
 * CSV文件操作和格式化
 * 主要功能是将出租车的od数据从两个文件拼接到一个文件中
 */
public class CSVFormatter {

    public static void merge(String inputFile1,
                             String inputFile2,
                             String outputFile) throws Exception{

            ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据

            CsvReader inputReader1 = new CsvReader(inputFile1, ',', Charset.forName("utf-8"));
            CsvReader inputReader2 = new CsvReader(inputFile2, ',', Charset.forName("utf-8"));
            CsvWriter outputWriter = new CsvWriter(outputFile, ',', Charset.forName("utf-8"));
        try {
            int id = 0;

            inputReader1.readRecord();
            while (inputReader2.readRecord()) {
                if (inputReader1.readRecord()) {
                    String[] tempRecord1 = inputReader1.getValues();
                    String[] tempRecord2 = inputReader2.getValues();

                    if (tempRecord1[1].equals(tempRecord2[1])) {
                        tempRecord2 = Arrays.copyOf(
                                tempRecord2,
                                tempRecord2.length + 3);
                        System.arraycopy(tempRecord1, 2, tempRecord2, 5, 3);
                        tempRecord2[0] = String.valueOf(id++);
                        csvList.add(tempRecord2);
                    }
                }
            }

            for (String[] strs : csvList) {
                System.out.println(strs[0] + "----" + strs[1]);
                outputWriter.writeRecord(strs);
            }
        } finally {
            inputReader1.close();
            inputReader2.close();
            outputWriter.close();
        }
    }
}
