package com.learn.toys.tabledesign;

import com.learn.toys.Main;
import com.learn.toys.utils.SqlUtil;
import com.learn.toys.utils.StringUtil;
import com.learn.toys.utils.SwingUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

public class TableDesignFrame extends JFrame implements KeyListener{

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
        bindHotKey();

        // 设置为单选模式，以便只修改选中单元格的样式
        // table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 直接新增一行
        addRowBtn.doClick();

        setVisible(true);
    }

    private void bindHotKey() {
        // 生成sql的快捷键  meta+Enter
        root.registerKeyboardAction(e -> genCreateSqlBtn.doClick(),
                KeyStroke.getKeyStroke("meta ENTER"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // mac: 增加一行按钮的快捷键  meta+N
        root.registerKeyboardAction(e -> addRowBtn.doClick(),
                KeyStroke.getKeyStroke("meta " + (char) KeyEvent.VK_N), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // 删除一行按钮的快捷键  delete
        root.registerKeyboardAction(e -> delRowBtn.doClick(),
                KeyStroke.getKeyStroke("DELETE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // 向下移动选中行的快捷键 down
        root.registerKeyboardAction(e -> moveDownBtn.doClick(),
                KeyStroke.getKeyStroke("DOWN"), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // 向上移动选中行的快捷键 up
        root.registerKeyboardAction(e -> moveUpBtn.doClick(),
                KeyStroke.getKeyStroke("UP"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }


    private void initTable() {
        tableModel = new DefaultTableModel(new Object[][]{},
                Arrays.stream(ColumnEnum.values()).map(ColumnEnum::getName).toArray());
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
                    keywords.add(col.getSqlSort(), col.getSqlConvertor().apply(table.getValueAt(row, colIdx)));
                }
                keywords.removeIf(StringUtil::isEmpty);
                String oneRowSql = String.join(" ", keywords);
                rowSqlList.add("    " + oneRowSql + ",");
            }

            String createTableSql = SqlUtil.formatToCreateTableSql(rowSqlList);
            JDialog dialog = SwingUtil.createTextDialog("SQL已复制到剪切板", createTableSql);
            // 获取系统剪切板,并将sql复制到剪切板
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            clip.setContents(new StringSelection(createTableSql), null);
            dialog.setVisible(true);
        });

        // 编辑表格时，进行数据联动
        table.addPropertyChangeListener("tableCellEditor", event -> {
            int column = table.getEditingColumn();
            int row = table.getEditingRow();
            if (row < 0 || column < 0) {
                return;
            }

            // 如果选中了"主键"，自动填充整列
            Object tableValueAt = tableModel.getValueAt(row, column);
            if (ColumnEnum.PRIMARY_KEY.ordinal() == column && Boolean.TRUE.equals(tableValueAt)) {
                tableModel.setValueAt("id", row, ColumnEnum.NAME.ordinal());
                tableModel.setValueAt("自增主键", row, ColumnEnum.COMMENT.ordinal());
                tableModel.setValueAt("int", row, ColumnEnum.DATA_TYPE.ordinal());
            }
            // 如果 On Update 选中了"current_timestamp"，自动填充
            String currTimeKeyword = ColumnEnum.ON_UPDATE.getOptions().get(1);
            if (ColumnEnum.ON_UPDATE.ordinal() == column && currTimeKeyword.equals(tableValueAt)) {
                tableModel.setValueAt("update_time", row, ColumnEnum.NAME.ordinal());
                tableModel.setValueAt("更新时间", row, ColumnEnum.COMMENT.ordinal());
                tableModel.setValueAt("datetime", row, ColumnEnum.DATA_TYPE.ordinal());
                tableModel.setValueAt(currTimeKeyword, row, ColumnEnum.DEFAULT_VAL.ordinal());
            }
            // 如果注释以"是否"开头，自动填充数据格式和默认值
            if (ColumnEnum.COMMENT.ordinal() == column && String.valueOf(tableValueAt).startsWith("是否")) {
                tableModel.setValueAt("tinyint(1)", row, ColumnEnum.DATA_TYPE.ordinal());
                tableModel.setValueAt("0", row, ColumnEnum.DEFAULT_VAL.ordinal());
            }
            // 如果注释以"ID"结尾，自动填充数据格式
            if (ColumnEnum.COMMENT.ordinal() == column && String.valueOf(tableValueAt).toUpperCase().endsWith("ID")) {
                tableModel.setValueAt("int unsigned", row, ColumnEnum.DATA_TYPE.ordinal());
            }
            // 如果"非空"选中了"false"，自动填充默认值
            if (ColumnEnum.NOT_NULL.ordinal() == column) {
                if (Boolean.FALSE.equals(tableValueAt)) {
                    tableModel.setValueAt("null", row, ColumnEnum.DEFAULT_VAL.ordinal());
                } else {
                    tableModel.setValueAt("", row, ColumnEnum.DEFAULT_VAL.ordinal());
                }
            }
        });

        //删除一行按钮
        delRowBtn.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(table.getSelectedRow());
                if (table.getRowCount() > 0) {
                    table.setRowSelectionInterval(Math.max(0, selectedRow - 1), Math.max(0, selectedRow - 1));
                }
            }
        });
        //增加一行按钮
        addRowBtn.addActionListener(event -> {
            tableModel.addRow(Arrays.stream(ColumnEnum.values())
                    .map(col -> col.getColumnClass() == Boolean.class
                            ? ColumnEnum.NOT_NULL.equals(col) : "").toArray());
            // 选中最后一行，并且使第一个单元格处于可编辑状态
            System.out.println("Add row, now row count: " + table.getRowCount());
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.editCellAt(table.getRowCount() - 1, 0);
        });

        // 向下移动一行按钮
        moveDownBtn.addActionListener(event -> {
            if (SwingUtil.moveRow(tableModel, table.getSelectedRow(), table.getRowCount(), 1)) {
                table.setModel(tableModel);  // 刷新JTable
            }
        });
        // 向上移动一行按钮
        moveUpBtn.addActionListener(event -> {
            if (SwingUtil.moveRow(tableModel, table.getSelectedRow(), table.getRowCount(), -1)) {
                table.setModel(tableModel);  // 刷新JTable
            }
        });
    }

    // public static void main(String[] args) throws IOException {
    //     InputStream inputStream = TableDesignFrame.class.getResourceAsStream("/com/learn/toys/tabledesign/icons8-tool-96.ico");
    //     Image image = new ImageIcon("/com/learn/toys/tabledesign/icons8-tool-96.ico").getImage();
    //     ImageProducer source = image.getSource();
    //     System.out.println(inputStream.available());
    // }
    private void adjustStyle() {
        // 计算窗口大小，居中的 size 值
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.getWidth() * Main.SIZE_PERCENTAGE / 100);
        setSize(windowWidth, (int) (screenSize.getHeight() * Main.SIZE_PERCENTAGE / 100));
        // 居中显示
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icons8-tool-96.ico").getImage());


        table.setRowHeight(32);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        table.setForeground(Color.darkGray);
        table.getTableHeader().setFont(new Font("宋体", Font.BOLD, 18));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 36));

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

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(e);
    }
}