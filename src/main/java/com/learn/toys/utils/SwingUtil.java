package com.learn.toys.utils;

import javafx.scene.layout.RowConstraints;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingUtil {

    public static JDialog createTextDialog(String title, String text) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);

        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/plain");
        //textPane.setContentType("text/html");
        textPane.setText(text);
        textPane.setFont(new Font("宋体", Font.PLAIN, 16));
        dialog.add(textPane);

        AtomicInteger oneRowMaxLen = new AtomicInteger();
        AtomicInteger rowsCount = new AtomicInteger();
        Arrays.stream(text.split("\n")).forEach(line -> {
            oneRowMaxLen.set(Math.max(oneRowMaxLen.get(), line.length()));
            rowsCount.addAndGet(1);
        });
        dialog.setSize(oneRowMaxLen.get() * 10, rowsCount.get() * 32);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        return dialog;
    }

    /**
     * 移动完数据之后需要调用 table.setModel(tableModel); 刷新table
     */
    public static boolean moveRow(DefaultTableModel tableModel, int selectedRow, int rowCount, int offset) {
        // 如果是向下移动(offset>0),则被选中行不能是最后一行; 如果是向上移动(offset<0),被选中行不能是第一行
        boolean canMove = (offset > 0) ? selectedRow < rowCount - 1 : selectedRow > 0;
        if (canMove) {
            // 获取需要移动的行 的数据
            Object[] rowData = new Object[tableModel.getColumnCount()];
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                rowData[i] = tableModel.getValueAt(selectedRow, i);
            }
            // 从TableModel中删除该行
            tableModel.removeRow(selectedRow);
            // 在TableModel中的下一行插入该行
            tableModel.insertRow(selectedRow + offset, rowData);
        }
        return canMove;
    }

    public static JTextField getCellTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("宋体", Font.PLAIN, 18));
        textField.setEditable(true);
        return textField;
    }


    public static void deepCopy(Component originalComponent, Component copyComponent) {
        copyComponent.setName(originalComponent.getName());
        copyComponent.setBounds(originalComponent.getBounds());
        copyComponent.setPreferredSize(originalComponent.getPreferredSize());
        copyComponent.setMinimumSize(originalComponent.getMinimumSize());
        copyComponent.setMaximumSize(originalComponent.getMaximumSize());
        copyComponent.setVisible(originalComponent.isVisible());
        copyComponent.setForeground(originalComponent.getForeground());
        copyComponent.setBackground(originalComponent.getBackground());
        copyComponent.setFont(originalComponent.getFont());
        copyComponent.setCursor(originalComponent.getCursor());
        copyComponent.setFocusable(originalComponent.isFocusable());
        //copyComponent.setAlignmentX(originalComponent.getAlignmentX());
        //copyComponent.setAlignmentY(originalComponent.getAlignmentY());
        //copyComponent.setOpaque(originalComponent.isOpaque());
        //copyComponent.setDoubleBuffered(originalComponent.isDoubleBuffered());
        //copyComponent.setFocusTraversalKeysEnabled(originalComponent.isFocusTraversalKeysEnabled());
        //copyComponent.addKeyListener(new KeyAdapter() {});
        // Fix for bug ID: 4659696 (managers of focus and key events) (tc)??? addKeyListener method above will add an instance of this class to the component??? this is a hack for the bug and should not be used in production code??? if you need to add a KeyListener to a component in a production setting??? you should use the addKeyListener method of the component??? and not an anonymous inner class of this class as it is in this example??? otherwise??? if you add a KeyListener to a component??? it will not work??? because??? the component will not call the KeyListener??? because??? it is an anonymous inner class??? and??? not a proper listener object??? so??? if you want to add a KeyListener to a component??? you should create a proper listener object??? and??? call the addKeyListener method of the component with the proper listener object as an argument??? then??? the component will call the KeyListener when a key event occurs??? and??? everything will work as

    }

}