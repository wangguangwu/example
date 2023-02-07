package com.wangguangwu.map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * java 中的 Map 只允许有一个 key 和 一个 value 存在
 * guava 的 Table 允许一个 value 存在两个 key
 * Table 中的两个 key 分别被称为 rowKey 和 columnKey，即 双键 Map
 *
 * @author wangguangwu
 */
public class TableTest {

    private static Map<String, Map<String, Integer>> map;

    private static Table<String, String, Integer> table;

    /**
     * 记录员工每个月工作的天数
     * <p>
     * 分别使用 java 中的 Map 和 Guava 中的 Table 实现
     */
    @Before
    public void before() {
        // 使用 java 实现
        map = new HashMap<>();
        // 存放元素
        Map<String, Integer> workMap = new HashMap<>();
        workMap.put("Jan", 22);
        workMap.put("Feb", 24);
        map.put("wang", workMap);

        // 使用 Guava 实现
        table = HashBasedTable.create();
        // 存放元素
        table.put("wang", "Jan", 22);
        table.put("wang", "Feb", 24);
        table.put("gary", "Jan", 14);
        table.put("gary", "Feb", 22);
    }

    @Test
    public void get() {
        // java
        Integer javaCount = map.get("wang").get("Jan");
        // Guava
        Integer guavaCount = table.get("wang", "Jan");
        Assert.assertEquals(javaCount, guavaCount);
    }

    @Test
    public void getKeyValue() {
        // rowKey 或者 columnKey 的集合
        Set<String> rowKeys = table.rowKeySet();
        Set<String> columnKeys = table.columnKeySet();

        // value 集合
        Collection<Integer> values = table.values();

        Assert.assertNotNull("rowKeys 为 null", rowKeys);
        Assert.assertNotNull("columnKeys 为 null", columnKeys);
        Assert.assertNotNull("values 为 null", values);
        System.out.println(rowKeys);
        System.out.println(columnKeys);
        System.out.println(values);
    }

    @Test
    public void calculate() {
        // 计算 key 对应的所有 value 的和
        for (String key : table.rowKeySet()) {
            Set<Map.Entry<String, Integer>> rows = table.row(key).entrySet();
            int count = 0;
            for (Map.Entry<String, Integer> row : rows) {
                count += row.getValue();
            }
            System.out.println(key + ":" + count);
        }
    }

    @Test
    public void transfer() {
        // 转换 rowKey 和 columnKey
        Table<String, String, Integer> table2 = Tables.transpose(table);
        Set<Table.Cell<String, String, Integer>> cells = table2.cellSet();
        cells.forEach(cell -> System.out.println(cell.getRowKey() + ":" + cell.getColumnKey() + ":" + cell.getValue()));
    }

    @Test
    public void nesting() {
        // 将 Table 中存储的数据还原为嵌套 Map
        Map<String, Map<String, Integer>> rowMap = table.rowMap();
        Map<String, Map<String, Integer>> columnMap = table.columnMap();

        System.out.println("rowMap: " + rowMap);
        System.out.println("columnMap: " + columnMap);
    }
}
