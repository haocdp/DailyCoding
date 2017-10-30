package com.haocdp.coding.csvCoding;

public class CSVFormatTime {

    private static String formatTime(int time) {
        String t = String.format("%06d", time);

        return t.substring(0,2) + ":"
                + t.substring(2, 4) + ":"
                + t.substring(4, 6);
    }

    public static void main(String[] args) {
        System.out.println(formatTime(2));
    }
}
