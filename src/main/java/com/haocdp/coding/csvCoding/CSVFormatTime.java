package com.haocdp.coding.csvCoding;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CSVFormatTime {

    /**
     * 将数字时间转为标准格式
     * @param time
     * @return
     */
    private static String formatTime(int time) {
        String t = String.format("%06d", time);

        return t.substring(0,2) + ":"
                + t.substring(2, 4) + ":"
                + t.substring(4, 6);
    }

    /**
     * 将源目录下的所有csv文件进行格式化
     * @param srcDirectory
     * @param destDirectory
     */
    private static void formatter(String srcDirectory, String destDirectory)
            throws IOException{
        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            System.out.println("源目录不存在");
            return;
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer;
        String[] headers;
        String[] record;

        for (File srcFile : srcFiles) {
            reader = new CsvReader(srcFile.getAbsolutePath(), ',');
            if (reader.readRecord())
                headers = reader.getValues();
            else
                break;
            headers = Arrays.copyOf(headers, headers.length + 1);
            headers[headers.length - 1] = "time_format";

            writer = new CsvWriter(destDirectory + "/" + srcFile.getName());
            writer.writeRecord(headers);

            while (reader.readRecord()) {
                record = reader.getValues();
                record = Arrays.copyOf(record, record.length + 1);
                record[record.length - 1] = record[1].split(" ")[1];
                writer.writeRecord(record);
                writer.flush();
            }

            reader.close();
            writer.close();
        }

    }

    /**
     * 将源目录下的所有csv文件进行格式化到一个文件中
     * @param srcDirectory
     * @param destDirectory
     */
    private static void formatterToOneFile(String srcDirectory, String destDirectory)
            throws IOException{
        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            System.out.println("源目录不存在");
            return;
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer = new CsvWriter(destDirectory + "/point.csv");
        String[] headers = {"car_number","datetime_record","longitude","latitude","speed","direction","status","rn","time_format"};
        String[] record;

        for (File srcFile : srcFiles) {
            reader = new CsvReader(srcFile.getAbsolutePath(), ',');
            if (reader.readRecord())
                reader.getValues();
            else
                break;

            writer.writeRecord(headers);

            while (reader.readRecord()) {
                record = reader.getValues();
                record = Arrays.copyOf(record, record.length + 1);
                record[record.length - 1] = record[1].split(" ")[1];
                writer.writeRecord(record);
                writer.flush();
            }

            reader.close();
            //writer.close();
        }

    }

    /**
     * 将每个文件按照小时划分
     * @param srcDirectory
     * @param destDirectory
     */
    public static void divideByHour(String srcDirectory, String destDirectory)
            throws IOException{
        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            System.out.println("源目录不存在");
            return;
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer = null;
        String[] headers = {
                "car_number",
                "datetime_record",
                "longitude",
                "latitude",
                "speed",
                "direction",
                "status",
                "rn",
                "time_format"};
        String[] record;

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            reader = new CsvReader(srcFile.getAbsolutePath(), ',');
            if (reader.readRecord())
                reader.getValues();
            else
                break;

            //writer = new CsvWriter(destDirectory + "/" + srcFile.getName());

            String time = "";

            while (reader.readRecord()) {
                record = reader.getValues();

                if(!time.equals(record[record.length - 1].split(":")[0])) {
                    time = record[record.length - 1].split(":")[0];
                    writer = new CsvWriter(destDirectory +
                            "/" +
                            time +
                            "/" +
                            srcFile.getName());
                }
                if (writer == null) {
                    writer = new CsvWriter(destDirectory +
                            "/" +
                            record[record.length - 1].split(":")[0] +
                            "/" +
                            srcFile.getName());
                }
//                writer.writeRecord(headers);
                writer.writeRecord(record);
                writer.flush();
            }
            if (writer != null) {
                writer.close();
            }
            reader.close();

        }
    }

    /**
     * 将每个文件按照小时划分到一个文件
     * @param srcDirectory
     * @param destDirectory
     */
    public static void divideByHourToOneFile(String srcDirectory, String destDirectory)
            throws IOException{
        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            System.out.println("源目录不存在");
            return;
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer = null;
        String[] headers = {
                "car_number",
                "datetime_record",
                "longitude",
                "latitude",
                "speed",
                "direction",
                "status",
                "rn",
                "time_format"};
        String[] record;

        writer = new CsvWriter(destDirectory + "/00.csv" );
        writer.writeRecord(headers);
        writer.flush();

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            reader = new CsvReader(srcFile.getAbsolutePath(), ',');
            if (reader.readRecord())
                reader.getValues();
            else
                break;



            while (reader.readRecord()) {
                record = reader.getValues();

//                writer.writeRecord(headers);
                writer.writeRecord(record);
                writer.flush();

            }

            reader.close();

        }

        if (writer != null) {
            writer.close();
        }
    }


    public static void main(String[] args) throws IOException{
//        formatter(
//                "/Users/haoc_dp/Desktop/divideByCar",
//                "/Users/haoc_dp/Desktop/carTrajectory"
//        );

//        divideByHour(
////                "/Users/haoc_dp/Desktop/carTrajectory",
////                "/Users/haoc_dp/Desktop/carTrajectoryByHour"
//                args[0],
//                args[1]
//        );


        divideByHourToOneFile(
                "/Users/haoc_dp/Desktop/carTrajectoryByHour/00",
                "/Users/haoc_dp/Desktop/carTrajectoryByHour"
//                args[0],
//                args[1]
        );
    }
}
