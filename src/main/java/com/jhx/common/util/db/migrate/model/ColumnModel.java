package com.jhx.common.util.db.migrate.model;

import com.jhx.common.util.db.migrate.core.DataType;
import org.apache.commons.lang3.StringUtils;

/**
 * 字段
 *
 * @author t.ch
 */
public class ColumnModel {
    /**
     * 表名
     */
    public String TABLE_NAME;
    /**
     * 列名
     */
    public String COLUMN_NAME;
    /**
     * 默认值
     */
    public String COLUMN_DEFAULT;
    /**
     * 是否为空 YES | NO
     */
    public String IS_NULLABLE;
    /**
     * 长度 字符型
     */
    public long CHARACTER_MAXIMUM_LENGTH;
    /**
     * 长度 数字型
     */
    public int NUMERIC_PRECISION;
    /**
     * 精度 数字型
     */
    public int NUMERIC_SCALE;
    /**
     * 长度 DATETIME类型
     */
    public int DATETIME_PRECISION;
    /**
     * 列类型
     */
    public String COLUMN_TYPE;

    /**
     * 自增等 [ AUTO_INCREMENT ]
     */
    public String EXTRA;
    /**
     * 注释
     */
    public String COLUMN_COMMENT;
    /**
     * 旧列名, 判断重命名时用到
     */
    public String OLD_COLUMN_NAME;

    /**
     * 类型枚举，方便判断与使用
     */
    private DataType dataType;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("`").append(COLUMN_NAME).append("` ").append(COLUMN_TYPE);
        if(isPrimary()){
            sb.append(" NOT NULL");
        }else if ("NO".equals(IS_NULLABLE)) {
            sb.append(" NOT NULL");
            DataType type = this.getDataType();
            if (type.canSetDefaultValue) {
                if (type.defaultValue instanceof Integer) {
                    sb.append(" DEFAULT ").append(type.defaultValue);
                } else {
                    sb.append(" DEFAULT '").append(type.defaultValue).append("'");
                }
            }
        } else {
            sb.append(" DEFAULT NULL");
        }
        if (StringUtils.isNotBlank(EXTRA)) {
            sb.append(" ").append(EXTRA.toUpperCase());
        }
        if (StringUtils.isNotBlank(COLUMN_COMMENT)) {
            sb.append(" COMMENT '").append(COLUMN_COMMENT).append("'");
        }
        return sb.toString();
    }

    public String toTypeString() {
        StringBuilder sb = new StringBuilder();
        sb.append("`").append(COLUMN_NAME).append("` ").append(COLUMN_TYPE);
        if(isPrimary()){
            sb.append(" NOT NULL");
        }else if ("NO".equals(IS_NULLABLE)) {
            sb.append(" NOT NULL");
            DataType type = this.getDataType();
            if (type.canSetDefaultValue) {
                if (type.defaultValue instanceof Integer) {
                    sb.append(" DEFAULT ").append(type.defaultValue);
                } else {
                    sb.append(" DEFAULT '").append(type.defaultValue).append("'");
                }
            }
        } else {
            sb.append(" DEFAULT NULL");
        }
        if (StringUtils.isNotBlank(COLUMN_COMMENT)) {
            sb.append(" COMMENT '").append(COLUMN_COMMENT).append("'");
        }
        return sb.toString();
    }

    // 用于注解处理, 从数据库查出来的为null
    private TableModel tableModel;

    public ColumnModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * 判断是否是主键，主键不能有默认值
     * @return
     */
    public boolean isPrimary(){
       return tableModel.primaryKeys.contains(this.COLUMN_NAME);
    }

    /**
     * 获取TableModel
     *
     * @return
     */
    public TableModel getTableModel() {
        return tableModel;
    }

    public DataType getDataType() {
        if (this.dataType == null) {
            this.dataType = DataType.valueOfDefinition(this.COLUMN_TYPE);
        }
        return dataType;
    }
}
