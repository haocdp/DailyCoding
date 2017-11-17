package com.haocdp.coding.csvCoding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;


public class CSVHandler {
    private JTextField inputDir;
    private JLabel inputDirLabel;
    private JButton inputDirSelectButton;
    private JLabel outputDirLabel;
    private JTextField outputDir;
    private JButton outputDirSelectButton;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel MainPanel;
    // 得到显示器屏幕的宽高
    private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;


    public CSVHandler() {
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        inputDirSelectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if (file != null)
                    inputDir.setText(file.getAbsolutePath());
            }
        });

        outputDirSelectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
                jfc.showDialog(new JLabel(), "选择");


                File file=jfc.getSelectedFile();
                if (file != null)
                    outputDir.setText(file.getAbsolutePath());
            }
        });
    }

    private void onOK() {
        // add your code here
        if (!"".equals(this.inputDir.getText())
                        && !"".equals(this.outputDir.getText())) {
            /**
             * 开辟新的线程。防止将主线程阻塞、点击关闭按钮不能窗体
             */
            new Thread(() -> {
                try {
                    if (CSVFormatTime.handle(this.inputDir.getText(), this.outputDir.getText()))
                        System.exit(0);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this.MainPanel, ioe.getMessage());
                } catch (ParseException pse) {
                    JOptionPane.showMessageDialog(this.MainPanel, pse.getMessage());
                } catch (InterruptedException ie) {
                    JOptionPane.showMessageDialog(this.MainPanel, ie.getMessage());
                } catch (PathException phe) {
                    JOptionPane.showMessageDialog(this.MainPanel, phe.getMessage());
                }
            }).start();

            this.buttonOK.setEnabled(false);
        }

    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("CSVHandler");

        CSVHandler csvHandler = new CSVHandler();
        JPanel jPanel = csvHandler.MainPanel;
        frame.setContentPane(jPanel);
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(jPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation((width - 350)/2,
                (height - 200)/2);
    }
}
