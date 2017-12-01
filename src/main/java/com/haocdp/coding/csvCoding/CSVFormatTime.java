package com.haocdp.coding.csvCoding;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CSVFormatTime{


    private static final String[] HEADERS = {"car_number",
            "datetime_record",
            "longitude",
            "latitude",
            "speed",
            "direction",
            "status",
            "rn",
            "n_longitude",
            "n_latitude",
            "time_format"};

    /**
     * 将数字时间转为标准格式
     * 标准格式：HH:mm:ss
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
     * 将源目录下的所有csv文件时间进行格式化
     * 仅仅将时间进行格式化放到最后一列上
     * @param srcDirectory
     * @param destDirectory
     */
    private static void formatter(String srcDirectory, String destDirectory)
            throws IOException ,PathException, InterruptedException{
        File carTrajectoryDir = new File(destDirectory);
        if (!carTrajectoryDir.exists())
            carTrajectoryDir.mkdirs();

        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
        }

        File[] srcFiles = srcFD.listFiles();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (File srcFile : srcFiles) {

            executorService.execute(() -> {
                CsvReader reader = null;
                CsvWriter writer = null;
                String[] headers;
                String[] record;

                try {
                    reader = new CsvReader(srcFile.getAbsolutePath(), ',');
                    if (reader.readRecord())
                        headers = reader.getValues();
                    else
                        return;
                    headers = Arrays.copyOf(headers, headers.length + 1);
                    headers[headers.length - 1] = "time_format";

                    writer = new CsvWriter(
                            destDirectory + "/" + srcFile.getName());
                    writer.writeRecord(headers);

                    while (reader.readRecord()) {
                        record = reader.getValues();
                        record = Arrays.copyOf(record, record.length + 1);
                        record[record.length - 1] = record[1].split(" ")[1];
                        writer.writeRecord(record);
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null)
                        reader.close();
                    if (writer != null)
                        writer.close();
                }


            });

        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }

    }

    /**
     * 将源目录下的所有csv文件时间进行格式化
     * 把下一条记录中坐标添加其中
     * @param srcDirectory
     * @param destDirectory
     */
    private static void formatter_new(String srcDirectory, String destDirectory)
            throws IOException ,PathException, InterruptedException{
        File carTrajectoryDir = new File(destDirectory);
        if (!carTrajectoryDir.exists())
            carTrajectoryDir.mkdirs();

        File srcFD = new File(srcDirectory);

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
        }

        File[] srcFiles = srcFD.listFiles();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (File srcFile : srcFiles) {

            executorService.execute(() -> {
                CsvReader reader = null;
                CsvWriter writer = null;
                String[] record = null;
                String[] preRecord = null;

                try {
                    reader = new CsvReader(srcFile.getAbsolutePath(), ',');
                    if (reader.readRecord())
                        reader.getValues();
                    else
                        return;

                    writer = new CsvWriter(
                            destDirectory + "/" + srcFile.getName());
                    writer.writeRecord(HEADERS);

                    while (reader.readRecord()) {
                        record = reader.getValues();

                        if (preRecord == null) {
                            preRecord = record;
                            continue;
                        }

                        preRecord = Arrays.copyOf(preRecord, preRecord.length + 3);
                        preRecord[preRecord.length - 3] = record[2];
                        preRecord[preRecord.length - 2] = record[3];
                        preRecord[preRecord.length - 1] = record[1].split(" ")[1];
                        writer.writeRecord(preRecord);
                        writer.flush();

                        preRecord = record;
                    }

                    if (preRecord != null) {
                        preRecord = Arrays.copyOf(preRecord, preRecord.length + 3);
                        preRecord[preRecord.length - 3] = record[2];
                        preRecord[preRecord.length - 2] = record[3];
                        preRecord[preRecord.length - 1] = record[1].split(" ")[1];
                        writer.writeRecord(preRecord);
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null)
                        reader.close();
                    if (writer != null)
                        writer.close();
                }


            });

        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }

    }

    /**
     * 将源目录下的所有csv文件时间进行格式化并集成到一个文件中（ALL.csv）
     * @param srcDirectory
     * @param destDirectory
     */
    private static void formatterToOneFile(
            String srcDirectory,
            String destDirectory)
            throws IOException, PathException{
        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer = new CsvWriter(destDirectory + "/ALL.csv");
        String[] headers = {"car_number",
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
     * 将源目录中的所有文件集成到一个文件中(ALL.csv)，没有格式化过程
     * @param srcDirectory
     * @param destDirectory
     */
    private static void Integrate2OneFile(
            String srcDirectory,
            String destDirectory)
            throws IOException,PathException{
        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
        }

        File[] srcFiles = srcFD.listFiles();

        CsvReader reader;
        CsvWriter writer = new CsvWriter(destDirectory + "/ALL.csv");
        String[] headers = {"car_number",
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
            reader = new CsvReader(srcFile.getAbsolutePath(), ',');
            if (reader.readRecord())
                reader.getValues();
            else
                break;

            writer.writeRecord(headers);

            while (reader.readRecord()) {
                record = reader.getValues();
                writer.writeRecord(record);
                writer.flush();
            }

            reader.close();
        }
        writer.close();

    }

    /**
     * 将每个文件按照小时划分
     * @param srcDirectory
     * @param destDirectory
     */
    public static void divideByHour(
            String srcDirectory,
            String destDirectory)
            throws IOException, PathException, InterruptedException{
        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
        }

        File[] srcFiles = srcFD.listFiles();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            executorService.execute(() -> {
                CsvReader reader = null;
                CsvWriter writer = null;
                String[] record;

                try {
                    reader = new CsvReader(srcFile.getAbsolutePath(), ',');
                    if (reader.readRecord())
                        reader.getValues();
                    else
                        return;

                    //writer = new CsvWriter(destDirectory + "/" + srcFile.getName());

                    String time = "";
                    String dir = "";
                    File fDir = null;
                    while (reader.readRecord()) {
                        record = reader.getValues();
                        dir = destDirectory + "/" +
                                record[record.length - 1].split(":")[0];
                        fDir = new File(dir);
                        if (!fDir.exists())
                            fDir.mkdirs();

                        if(!time.equals(record[record.length - 1].split(":")[0])) {
                            time = record[record.length - 1].split(":")[0];
                            writer = new CsvWriter(dir+
                                    "/" +
                                    srcFile.getName());
                        }
                        if (writer == null) {
                            writer = new CsvWriter(dir +
                                    "/" +
                                    srcFile.getName());
                        }
//                writer.writeRecord(headers);
                        writer.writeRecord(record);
                        writer.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null)
                        reader.close();
                }

            });
        }
        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }
    }

    /**
     * 将文件按照5分钟间隔进行压缩
     * @param srcDirectory
     * @param destDirectory
     */
    public static void compressWith5Minute(
            String srcDirectory,
            String destDirectory)
            throws IOException, ParseException, PathException{

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
//            return;
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

            writer = new CsvWriter(destDirectory + "/" + srcFile.getName());
            writer.writeRecord(headers);

            Date firstDate = null;

            while (reader.readRecord()) {
                record = reader.getValues();

                if(firstDate == null) {
                    firstDate = sdf.parse(record[record.length - 1]);
                    //writer.writeRecord(record);
                } else {
                    if ((sdf.parse(
                            record[record.length - 1]).getTime() -
                            firstDate.getTime()) /
                            (1000*60) < 5) {
                        continue;
                    } else {
                        firstDate = sdf.parse(record[record.length - 1]);
                    }
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
     * 将文件按照5分钟间隔进行压缩
     * 把下一条记录中坐标添加其中
     * @param srcDirectory
     * @param destDirectory
     */
    public static void compressWith5Minute_new(
            String srcDirectory,
            String destDirectory)
            throws IOException, ParseException, PathException, InterruptedException{

        ThreadLocal<SimpleDateFormat> threadSafeSdf =
                ThreadLocal.withInitial(
                        () -> new SimpleDateFormat("HH:mm:ss")
                );

        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
//            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        File[] srcFiles = srcFD.listFiles();

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            executorService.execute(() -> {
                CsvWriter writer = null;
                CsvReader reader = null;
                String[] record = null;
                String[] preRecord = null;

                try {
                    reader = new CsvReader(srcFile.getAbsolutePath(), ',');
                    if (reader.readRecord())
                        reader.getValues();
                    else
                        return;

                    writer = new CsvWriter(destDirectory + "/" + srcFile.getName());
                    writer.writeRecord(HEADERS);

                    Date firstDate = null;

                    while (reader.readRecord()) {
                        record = reader.getValues();

                        if (preRecord == null) {
                            preRecord = record;
                            firstDate = threadSafeSdf.get().parse(record[record.length - 1]);
                            continue;
                        }

                        if ((threadSafeSdf.get().parse(
                                record[record.length - 1]).getTime() -
                                firstDate.getTime()) /
                                (1000 * 60) < 5) {
                            continue;
                        }

                        preRecord = Arrays.copyOf(preRecord, preRecord.length + 2);
                        preRecord[preRecord.length - 1] = preRecord[record.length - 1];
                        preRecord[preRecord.length - 3] = record[2];
                        preRecord[preRecord.length - 2] = record[3];
                        writer.writeRecord(preRecord);
                        writer.flush();

                        preRecord = record;
                        firstDate = threadSafeSdf.get().parse(record[record.length - 1]);
                    }

                    if (preRecord != null) {
                        preRecord = Arrays.copyOf(preRecord, preRecord.length + 2);
                        preRecord[preRecord.length - 1] = record[record.length - 1];
                        preRecord[preRecord.length - 3] = record[2];
                        preRecord[preRecord.length - 2] = record[3];
                        writer.writeRecord(preRecord);
                        writer.flush();
                    }
                    }catch (IOException e){
                        e.printStackTrace();
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (writer != null) {
                            writer.close();
                        }
                        if (reader != null) {
                            reader.close();
                        }
                    }
            });
        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }
    }

    /**
     * 压缩规则：对于速度连续为0的点只保留一个点，不连续为0的点进行5分钟间隔切分
     * @param srcDirectory
     * @param destDirectory
     * @throws IOException
     * @throws ParseException
     */
    public static void compressWith2thRule(
            String srcDirectory,
            String destDirectory)
            throws IOException,
            ParseException,
            PathException,
            InterruptedException{
        ThreadLocal<SimpleDateFormat> threadSafeSdf =
                ThreadLocal.withInitial(
                        () -> new SimpleDateFormat("HH:mm:ss")
                );

        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcFD.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
//            return;
        }

        File[] srcFiles = srcFD.listFiles();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

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

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            executorService.execute(() -> {
                CsvReader reader = null;
                CsvWriter writer = null;

                String[] record;

                try {
                    reader = new CsvReader(srcFile.getAbsolutePath(), ',');
                    if (reader.readRecord())
                        reader.getValues();
                    else
                        return;

                    writer = new CsvWriter(destDirectory + "/" + srcFile.getName());
                    writer.writeRecord(headers);

                    Date preDate = null;
                    String[] preRecord = null;

                    while (reader.readRecord()) {
                        record = reader.getValues();

                        if (!(Double.parseDouble(record[4]) > 0)) {
                            preRecord = record;
                            continue;
                        }

                        if (preRecord != null && preDate != null &&
                                (threadSafeSdf.get().
                                        parse(preRecord[preRecord.length - 1]).getTime() -
                                        preDate.getTime()
                                ) / (1000 * 60) >= 5) {
                            writer.writeRecord(preRecord);
                            writer.flush();
                            preDate = threadSafeSdf.get().parse(preRecord[preRecord.length - 1]);
                        }
                        preRecord = null;

                        if (preDate == null) {
                            preDate = threadSafeSdf.get().parse(record[record.length - 1]);
                            //writer.writeRecord(record);
                        } else {
                            if ((threadSafeSdf.get().
                                    parse(record[record.length - 1]).getTime() - preDate.getTime()
                            ) / (1000 * 60) < 5) {
                                continue;
                            } else {
                                preDate = threadSafeSdf.get().parse(record[record.length - 1]);
                            }
                        }
//                writer.writeRecord(headers);
                        writer.writeRecord(record);
                        writer.flush();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ParseException pe) {
                    pe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                }
            });

        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }
    }


    /**
     * 将每个文件按照小时划分到一个文件
     * @param srcDirectory String类型
     * @param destDirectory
     */
    public static void divideByHourToOneFile(String srcDirectory, String destDirectory)
            throws IOException ,PathException{
        File srcFD = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        divideByHourToOneFile(srcFD, destDirectory);
    }

    /**
     * 将每个文件按照小时划分到一个文件
     * @param srcDir File类型
     * @param destDirectory
     */
    public static void divideByHourToOneFile(File srcDir, String destDirectory)
            throws IOException, PathException{

        if (!srcDir.exists()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不存在");
//            return;
        }

        File[] srcFiles = srcDir.listFiles();

        CsvReader reader;
        CsvWriter writer = null;
        String[] record;

        writer = new CsvWriter(destDirectory + "/" +srcDir.getName() + ".csv" );
        writer.writeRecord(HEADERS);
        writer.flush();

        for (File srcFile : srcFiles) {
            if (!srcFile.getName().contains(".csv"))
                continue;

            reader = new CsvReader(srcFile.getAbsolutePath(), ',');

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

    /**
     * 将源目录中的各个子目录中的文件集成到相应的文件中
     * @param srcDirectory
     * @param destDirectory
     */
    public static void Integrate2OneFileWithDirectory (
            String srcDirectory,
            String destDirectory)
            throws IOException, InterruptedException, PathException{
        /**
         * 线程池
         */
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        File srcDir = new File(srcDirectory);

        if (!srcDir.isDirectory()) {
            throw new PathException("输出目录不存在");
//            System.out.println("源目录不是一个目录");
//            return;
        }

        File[] srcDirs = srcDir.listFiles();

        for (File dir : srcDirs) {
            if (!dir.isDirectory())
                continue;

            executorService.execute(() -> {
                try {
                    divideByHourToOneFile(dir, destDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (PathException pe) {
                    pe.printStackTrace();
                }
            });
        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }
    }

    /**
     * 将目录下的文件再按照每5分钟间隔划分成小文件
     * @param srcDirectory 所要进行分割的目录名
     * @param destDirectory 所要存放的目录
     * @throws IOException
     */
    public static void divideOneHour2FiveMinutesOneFile(
            String srcDirectory,
            String destDirectory) throws IOException, InterruptedException {
        /**
         * 线程池
         */
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        File srcDir = new File(srcDirectory);
        File destDir = new File(destDirectory);

        if (!destDir.exists())
            destDir.mkdirs();

        if (!srcDir.isDirectory())
            return;

        File[] srcFiles = srcDir.listFiles();
        for (File file : srcFiles) {
            if (file.isDirectory())
                continue;

            executorService.execute(() -> {
                try {
                    divideOneHour2FiveMinutesOneFile(file, destDirectory);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();

        /**
         * 确保任务执行完成后，主线程才往下执行
         */
        while (true) {
            if (executorService.isTerminated())
                break;
            Thread.sleep(1000);
        }
    }

    /**
     * 将文件进行分割
     * @param srcFile 源文件
     * @param destDirectory 目标目录
     * @throws IOException
     */
    public static void divideOneHour2FiveMinutesOneFile(
            File srcFile,
            String destDirectory) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        CsvReader reader = new CsvReader(srcFile.getAbsolutePath());
//        File file = new File(destDirectory + "\\" +srcFile.getName().split("[.]")[0] + "_5m");
        File file = new File(destDirectory + "\\" + "allFiles" + "_5m");
        if (!file.isDirectory())
            file.mkdir();

        /**
         * 新建12个写入
         */
        CsvWriter[] writers = new CsvWriter[12];
        CsvWriter csvWriter = null;
        for (int i = 0; i < 12; i++) {
            csvWriter = new CsvWriter(
                    file.getAbsolutePath() + "\\" +
                            srcFile.getName().split("[.]")[0] +
                            "_" + i + ".csv");
            csvWriter.writeRecord(HEADERS); //把头标题写入
            writers[i] = csvWriter;
        }


        Date startDate = sdf.parse(
                srcFile.getName().split("[.]")[0] + ":00:00");

        String[] record;

        /**
         * 忽略头标题
         */
        if (reader.readRecord())
            reader.getValues();

        while (reader.readRecord()) {
            record = reader.getValues();
            long number = (
                    sdf.parse(record[record.length - 1]).getTime() -
                            startDate.getTime())
                    /(60 * 1000 * 5);
            writers[(int)number].writeRecord(record);
            writers[(int)number].flush();
        }

        reader.close();
        for (int i = 0; i < 12; i++) {
            writers[i].close();
        }
    }


    public static void main(String[] args)
            throws IOException, ParseException,
            InterruptedException, PathException{

//        String srcDirectory = args[0];
//        String destDirectory = args[1];
//
//
//        formatter(
//                srcDirectory,
//                destDirectory + "/carTrajectory"
//        );
//
//        compressWith2thRule(
//                destDirectory + "/carTrajectory",
//                destDirectory + "carTrajectory_compress_5m_filter0speed"
//        );
//
//        divideByHour(
//                destDirectory + "carTrajectory_compress_5m_filter0speed",
//                destDirectory + "carTrajectoryByHour_compress_5m_filter0speed"
//        );
//
//        Integrate2OneFileWithDirectory(
//                destDirectory + "carTrajectoryByHour_compress_5m_filter0speed",
//                destDirectory + "carTrajectoryByHour_compress_5m_filter0speed"
//        );
//
//        divideOneHour2FiveMinutesOneFile(
//                destDirectory + "carTrajectoryByHour_compress_5m_filter0speed",
//                destDirectory + "carTrajectoryByHour_compress_5m_filter0speed"
//        );

    }

    public static boolean handle(String srcDirectory, String destDirectory)
            throws IOException, ParseException, InterruptedException, PathException {
        formatter(
                srcDirectory,
                destDirectory + "/carTrajectory"
        );

        compressWith5Minute_new(
                destDirectory + "/carTrajectory",
                destDirectory + "/carTrajectory_compress_5m_filter0speed"
        );

        divideByHour(
                destDirectory + "/carTrajectory_compress_5m_filter0speed",
                destDirectory + "/carTrajectoryByHour_compress_5m_filter0speed"
        );

        Integrate2OneFileWithDirectory(
                destDirectory + "/carTrajectoryByHour_compress_5m_filter0speed",
                destDirectory + "/carTrajectoryByHour_compress_5m_filter0speed"
        );

        divideOneHour2FiveMinutesOneFile(
                destDirectory + "/carTrajectoryByHour_compress_5m_filter0speed",
                destDirectory + "/carTrajectoryByHour_compress_5m_filter0speed"
        );

        return true;
    }
}
