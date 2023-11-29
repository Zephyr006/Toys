package cn.learn.toys.utils;

import javax.swing.*;
import java.awt.*;

public class RadioButtonEditor extends DefaultCellEditor{

    private JRadioButton radioButton;

    public RadioButtonEditor() {
        super(new JCheckBox());
        radioButton = new JRadioButton();
        radioButton.setHorizontalAlignment(SwingConstants.CENTER);
        radioButton.setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        radioButton.setText(value.toString());
        radioButton.setSelected(isSelected);
        return radioButton;
    }

    @Override
    public Object getCellEditorValue() {
        return radioButton.isSelected();
    }

}