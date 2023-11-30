package cn.learn.toys.tabledesign;

import cn.learn.toys.Main;
import cn.learn.toys.utils.SqlUtil;
import cn.learn.toys.utils.StringUtil;
import cn.learn.toys.utils.SwingUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

public class TableDesignFrame extends JFrame {

    //private static final LinkedHashMap<String, TableCellEditor> headerToCellMap = new LinkedHashMap<>() {{
    //    put("列名", new DefaultCellEditor(new JTextField()));
    //    put("注释", new DefaultCellEditor(new JTextField()));
    //    put("数据类型", new DefaultCellEditor(new JComboBox<>()));
    //    put("默认值", new DefaultCellEditor(new JComboBox()));
    //    put("非空", new DefaultCellEditor(new JCheckBox()));
    //    put("主键", new DefaultCellEditor(new JComboBox<>()));
    //    put("On Update", new DefaultCellEditor(new JComboBox<>()));
    //}};

    private List<JPanel> panelList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JPanel root;
    private JPanel rightPanel;
    private JPanel bottom;
    private JScrollPane center;
    private JButton moveUpBtn;
    private JButton moveDownBtn;
    private JButton delRowBtn;
    private JButton addRowBtn;
    private JTable table;
    private JButton genBtn;
    private JButton genCreateSqlBtn;

    public TableDesignFrame(String title) {
        super(title);
        setContentPane(root);

        initTable();
        adjustStyle();
        bindActionListener();
        // 直接新增一行
        addRowBtn.doClick();
        //this.pack();
    }


    private void initTable() {
        tableModel = new DefaultTableModel(new Object[][]{},
                Arrays.stream(ColumnEnum.values()).map(ColumnEnum::getName).toList().toArray());
        table.setModel(tableModel);

        //根据定义的列格式初始化 JTable
        for (int idx = 0; idx < ColumnEnum.values().length; idx++) {
            ColumnEnum col = ColumnEnum.values()[idx];
            if (List.class.equals(col.getColumnClass())) {
                JComboBox<String> comboBox = new JComboBox<>(new Vector<>(col.getOptions()));
                comboBox.setEditable(true);
                table.getColumnModel().getColumn(idx).setCellEditor(new DefaultCellEditor(comboBox));
                table.getColumnModel().getColumn(idx).setCellRenderer(table.getDefaultRenderer(col.getColumnClass()));
            } else {
                table.getColumnModel().getColumn(idx).setCellEditor(table.getDefaultEditor(col.getColumnClass()));
                table.getColumnModel().getColumn(idx).setCellRenderer(table.getDefaultRenderer(col.getColumnClass()));
            }
        }
    }

    private void bindActionListener() {
        //增加一行按钮
        addRowBtn.addActionListener(event -> {
            tableModel.addRow(Arrays.stream(ColumnEnum.values())
                    .map(col -> col.getColumnClass() == Boolean.class
                            ? ColumnEnum.NOT_NULL.equals(col) : "").toArray());
            // 选中最后一行，并且使第一个单元格处于可编辑状态
            System.out.println("Now row count: " + table.getRowCount());
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.editCellAt(table.getRowCount() - 1, 0);
            //table.setCellEditor(new DefaultCellEditor(SwingUtil.getCellTextField()));
        });

        //生成 sql 按钮
        genCreateSqlBtn.addActionListener(event -> {
            List<String> rowSqlList = new ArrayList<>(table.getRowCount());
            for (int row = 0; row < table.getRowCount(); row++) {
                int colCount = ColumnEnum.values().length;
                List<String> keywords = new ArrayList<>(colCount);
                IntStream.range(0, colCount).forEach(i -> keywords.add(""));
                // 从 table 列中取数据
                for (int colIdx = 0; colIdx < colCount; colIdx++) {
                    ColumnEnum col = ColumnEnum.values()[colIdx];
                    keywords.add(col.getSqlSort(), col.getConvertor().apply(table.getValueAt(row, colIdx)));
                }
                keywords.removeIf(StringUtil::isEmpty);
                String oneRowSql = String.join(" ", keywords);
                rowSqlList.add("    " + oneRowSql + ",");
            }

            JDialog dialog = SwingUtil.createLabelDialog("SQL已复制到剪切板", SqlUtil.formatToCreateTableSql(rowSqlList));
            dialog.setVisible(true);
        });

        // 编辑表格时，进行数据联动
        table.addPropertyChangeListener("tableCellEditor", event -> {
            int column = table.getEditingColumn();
            int row = table.getEditingRow();

            // 如果选中了"主键"，自动填充整列
            if (ColumnEnum.PRIMARY_KEY.ordinal() == column && Boolean.TRUE.equals(tableModel.getValueAt(row, column))) {
                tableModel.setValueAt("id", row, ColumnEnum.NAME.ordinal());
                tableModel.setValueAt("自增主键", row, ColumnEnum.COMMENT.ordinal());
                tableModel.setValueAt("int", row, ColumnEnum.DATA_TYPE.ordinal());
            }
            // 如果 On Update 选中了"current_timestamp"，自动填充
            String currTimeKeyword = ColumnEnum.ON_UPDATE.getOptions().get(1);
            if (ColumnEnum.ON_UPDATE.ordinal() == column && currTimeKeyword.equals(tableModel.getValueAt(row, column))) {
                tableModel.setValueAt("update_time", row, ColumnEnum.NAME.ordinal());
                tableModel.setValueAt("更新时间", row, ColumnEnum.COMMENT.ordinal());
                tableModel.setValueAt("datetime", row, ColumnEnum.DATA_TYPE.ordinal());
                tableModel.setValueAt(currTimeKeyword, row, ColumnEnum.DEFAULT_VAL.ordinal());
            }
            // 如果注释以"是否"开头，自动填充数据格式和默认值
            if (ColumnEnum.COMMENT.ordinal() == column
                    && String.valueOf(tableModel.getValueAt(row, column)).startsWith("是否")) {
                tableModel.setValueAt("tinyint(1)", row, ColumnEnum.DATA_TYPE.ordinal());
                tableModel.setValueAt("0", row, ColumnEnum.DEFAULT_VAL.ordinal());
            }
        });

    }

    private void adjustStyle() {
        // 计算窗口大小，居中的 size 值
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.getWidth() * Main.SIZE_PERCENTAGE / 100);
        setSize(windowWidth, (int) (screenSize.getHeight() * Main.SIZE_PERCENTAGE / 100));
        // 居中显示
        setLocationRelativeTo(null);


        table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        table.setForeground(Color.darkGray);
        table.setRowHeight(36);
        table.getTableHeader().setFont(new Font("宋体", Font.BOLD, 18));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 42));

        // 表格边框
        table.setGridColor(Color.GRAY);
        // 被选中行背景色
        table.setSelectionBackground(new Color(115, 210, 250));
        // 根据配置的宽度比例设置每一列的宽度
        for (int idx = 0; idx < ColumnEnum.values().length; idx++) {
            ColumnEnum col = ColumnEnum.values()[idx];
            table.getColumnModel().getColumn(idx).setPreferredWidth(windowWidth * col.getWidthPercent() / 100);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}