package cn.learn.toys.tabledesign;

import cn.learn.toys.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 枚举的顺序决定列出现的顺序，sort 决定对应列在 sql 语句中出现的顺序
 * 全局的 sql 关键词都用小写
 */
public enum ColumnEnum {
    NAME("列名", Object.class, null, o -> "`" + o + "`", 20, 0),

    COMMENT("注释", Object.class, null, o -> "comment '" + o + "'", 30, 6),

    DATA_TYPE("数据类型", List.class,
            Arrays.asList("tinyint","smallint","int","varchar","char","datetime","decimal"), String::valueOf, 10, 1),

    NOT_NULL("非空", Boolean.class,null,
            obj -> Boolean.TRUE.equals(obj) ? "not null": "",8, 2),

    DEFAULT_VAL("默认值", List.class, Arrays.asList("", "''", "null", "0", "current_timestamp"),
            obj -> StringUtil.isEmpty(obj.toString()) ? "" : "default " + obj, 12, 3),

    PRIMARY_KEY("主键", Boolean.class,null,
            obj -> Boolean.TRUE.equals(obj) ? "auto_increment primary key" : "",8, 4),

    ON_UPDATE("On Update", List.class, Arrays.asList("","current_timestamp"),
            obj -> StringUtil.isEmpty(obj.toString()) ? "" : "on update " + obj, 12, 5),
    ;

    private String name;
    private Class<?> columnClass;
    private List<String> options;
    private Function<Object, String> convertor;
    private int widthPercent;
    private int sqlSort;

    ColumnEnum(String name, Class<?> columnClass, List<String> options,
               Function<Object, String> convertor, int widthPercent, int sort) {
        this.name = name;
        //this.defaultVal = defaultVal;
        this.columnClass = columnClass;
        this.options = options;
        this.convertor = convertor;
        this.widthPercent = widthPercent;
        this.sqlSort = sort;
    }

    public String getName() {
        return name;
    }
    public Class<?> getColumnClass() {
        return columnClass;
    }
    public int getSqlSort() {
        return sqlSort;
    }
    public List<String> getOptions() {
        return options;
    }
    public int getWidthPercent() {
        return widthPercent;
    }
    public Function<Object, String> getConvertor() {
        return convertor;
    }
}