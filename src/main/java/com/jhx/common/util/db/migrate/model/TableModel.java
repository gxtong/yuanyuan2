package com.jhx.common.util.db.migrate.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 表
 *
 * @author t.ch
 * @date 2018-03-10 15:49
 */
public class TableModel {
    /** 表名 */
    public String TABLE_NAME;

    /** 旧表, 判断重命名时用到 */
    public String OLD_TABLE_NAME;

    public LinkedHashMap<String, ColumnModel> columns = new LinkedHashMap<>();
    public LinkedHashMap<String, ColumnModel> oldColumns = new LinkedHashMap<>();
    public LinkedHashSet<String> primaryKeys = new LinkedHashSet<>();
    public LinkedHashMap<String, String> uniqueKeys = new LinkedHashMap<>();
    public LinkedHashMap<String, String> keys = new LinkedHashMap<>();

    // 是联合索引时查询判断用, 只在本类中使用
    private HashMap<String, String> keyMap = new HashMap<>();

    public void addKey(String keyName, boolean unique, String... columnNames) {
        boolean union = StringUtils.isNotBlank(keyName);
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = String.format("`%s`", columnNames[i]);
        }
        List<String> cols = new ArrayList<>(Arrays.asList(columnNames));
        if(union && keyMap.containsKey(keyName)){
            String keyStr = keyMap.get(keyName);
            cols.addAll(Arrays.asList(keyStr.split(",")));
            if(unique){
                uniqueKeys.remove(keyStr);
            }else {
                keys.remove(keyStr);
            }
        }
        cols.sort(String::compareToIgnoreCase);
        String keyStr = StringUtils.join(cols.toArray(), ",");
        if(unique){
            uniqueKeys.put(keyStr, keyName);
        }else {
            keys.put(keyStr, keyName);
        }
        if(union){
            keyMap.put(keyName, keyStr);
        }
    }

    public void addPrimaryKey(String... columnNames) {
        primaryKeys.addAll(Arrays.asList(columnNames));
    }

    public String joinPrimaryKeys(){
        StringJoiner sj = new StringJoiner(",");
        sj.setEmptyValue("");
        primaryKeys.forEach(key -> sj.add(String.format("`%s`", key)));
        return sj.toString();
    }

    public void sortColumn() {
        LinkedHashMap<String, ColumnModel> sorted = new LinkedHashMap<>();
        primaryKeys.forEach(key -> sorted.put(key, columns.get(key)));
        columns.forEach((key, column) -> {
            if (!primaryKeys.contains(key)) {
                sorted.put(key, column);
            }
        });
        columns = sorted;
    }
}
