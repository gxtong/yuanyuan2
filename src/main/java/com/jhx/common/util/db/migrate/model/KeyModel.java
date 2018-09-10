package com.jhx.common.util.db.migrate.model;

/**
 * 索引
 *
 * @author t.ch
 * @date 2018-03-10 15:50
 */
public class KeyModel {
    /** 表名 */
    public String Table;

    /** 是否是唯一约束 */
    public boolean Non_unique;

    /** 索引名称 */
    public String Key_name;

    /** 字段在索引中的顺序 */
    public int Seq_in_index;

    /** 字段名 */
    public String Column_name;
}
