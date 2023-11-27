package cn.learn.toys;

import cn.learn.toys.tabledesign.TableDesignFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        TableDesignFrame frame = new TableDesignFrame("小米-表设计");
        frame.setVisible(true);
        // 设置窗口的默认关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(1000, 1000);
    }
}