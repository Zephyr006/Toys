package cn.learn.toys.tabledesign;

import cn.learn.toys.Main;
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
                Arrays.stream(TableColumn.values()).map(TableColumn::getName).toList().toArray());
        table.setModel(tableModel);

        //根据定义的列格式初始化 JTable
        for (int idx = 0; idx < TableColumn.values().length; idx++) {
            TableColumn col = TableColumn.values()[idx];
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
        addRowBtn.addActionListener(event -> {
            tableModel.addRow(Arrays.stream(TableColumn.values())
                    .map(col -> col.getColumnClass() == Boolean.class
                            ? TableColumn.NOT_NULL.equals(col) : "").toArray());
            // 选中最后一行，并且使第一个单元格处于可编辑状态
            System.out.println("Now row count: " + table.getRowCount());
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.editCellAt(table.getRowCount() - 1, 0);
            //table.setCellEditor(new DefaultCellEditor(SwingUtil.getCellTextField()));
        });

        genCreateSqlBtn.addActionListener(event -> {
            List<String> rowSqlList = new ArrayList<>(table.getRowCount());
            for (int row = 0; row < table.getRowCount(); row++) {
                int colCount = TableColumn.values().length;
                List<String> keywords = new ArrayList<>(colCount);
                IntStream.range(0, colCount).forEach(i -> keywords.add(""));
                // 从 table 列中取数据
                for (int colIdx = 0; colIdx < colCount; colIdx++) {
                    TableColumn col = TableColumn.values()[colIdx];
                    keywords.add(col.getSqlSort(), col.getConvertor().apply(table.getValueAt(row, colIdx)));
                }
                keywords.removeIf(StringUtil::isEmpty);
                String oneRowSql = String.join(" ", keywords);
                rowSqlList.add(oneRowSql + ",");
            }
            JDialog dialog = SwingUtil.createLabelDialog("SQL已复制到剪切板",
                    String.join("\n", rowSqlList));
            dialog.setVisible(true);
        });
    }

    private void adjustStyle() {
        // 计算窗口大小，居中的 size 值
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.getWidth() * Main.SIZE_PERCENTAGE / 100);
        setSize(windowWidth, (int) (screenSize.getHeight() * Main.SIZE_PERCENTAGE / 100));
        // 居中显示
        setLocationRelativeTo(null);


        table.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        table.setForeground(Color.darkGray);
        table.setRowHeight(36);
        table.getTableHeader().setFont(new Font("宋体", Font.BOLD, 18));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 42));

        // 表格边框
        table.setGridColor(Color.GRAY);
        // 被选中行背景色
        table.setSelectionBackground(new Color(115, 210, 250));
        // 根据配置的宽度比例设置每一列的宽度
        for (int idx = 0; idx < TableColumn.values().length; idx++) {
            TableColumn col = TableColumn.values()[idx];
            table.getColumnModel().getColumn(idx).setPreferredWidth(windowWidth * col.getWidthPercent() / 100);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}