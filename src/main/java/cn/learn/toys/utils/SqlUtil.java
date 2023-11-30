package cn.learn.toys.utils;

import java.util.List;
import java.util.StringJoiner;

public class SqlUtil {

    public static String formatToCreateTableSql(final List<String> rowSqlList) {
        StringJoiner stringJoiner = new StringJoiner("\n",
                "\ncreate table if not exist `t_name` (\n",
                "\n) engine=InnoDB default charset=utf8 comment='表名'; ");

        for (int i = 0; i < rowSqlList.size(); i++) {
            if (i == rowSqlList.size() - 1) {
                stringJoiner.add(rowSqlList.get(i).substring(0, rowSqlList.get(i).length() - 1));
            } else {
                stringJoiner.add(rowSqlList.get(i));
            }
        }
        return stringJoiner.toString();
    }
}