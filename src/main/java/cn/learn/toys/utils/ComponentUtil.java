package cn.learn.toys.utils;

import java.awt.*;
import java.awt.event.KeyAdapter;

public class ComponentUtil {


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