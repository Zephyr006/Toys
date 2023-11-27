package cn.learn.toys.tabledesign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TableDesignFrame extends JFrame {

    private List<JPanel> panelList = new ArrayList<>();
    private JPanel motherPanel;
    private JTextField columnNameField;
    private JTextField commentField;
    private JComboBox dataTypeField;
    private JComboBox defaultField;
    private JRadioButton notNullField;
    private JCheckBox autoIncField;
    private JComboBox opUpdateField;
    private JButton genCreateSqlBtn;
    private JPanel mainPanel;
    private JButton addRowBtn;

    public TableDesignFrame(String title) {
        super(title);
        //add(genCreateSqlBtn);
        //add(motherPanel);
        setContentPane(mainPanel);

    }

    private void createUIComponents() {
        //columnNameField.setBounds(20, 20, 50, 20);

        // TODO: place custom component creation code here
        //motherPanel.add(columnNameField);
        //motherPanel.add(commentField);
        //motherPanel.add(dataTypeField);
        //motherPanel.add(defaultField);
        //motherPanel.add(notNullField);
        //motherPanel.add(autoIncField);
        //motherPanel.add(opUpdateField);
    }

    private void bindActionListener() {
        addRowBtn.addActionListener(event -> {
            JPanel newPanel = new JPanel();
            for (Component componentInPanel : motherPanel.getComponents()) {
                //componentInPanel.clone();
            }
        });
    }
}