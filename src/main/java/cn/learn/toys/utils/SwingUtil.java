package cn.learn.toys.utils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingUtil {

    public static JDialog createLabelDialog(String title, String text) {
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
        text.lines().forEach(line -> {
            oneRowMaxLen.set(Math.max(oneRowMaxLen.get(), line.length()));
            rowsCount.addAndGet(1);
        });
        dialog.setSize(oneRowMaxLen.get() * 10, rowsCount.get() * 32);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        return dialog;
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