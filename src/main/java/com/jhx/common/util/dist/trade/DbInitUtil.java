package com.jhx.common.util.dist.trade;

import com.google.common.collect.ImmutableMap;
import com.jhx.common.util.LogUtil;
import com.jhx.common.util.db.DbUtil;
import com.jhx.common.util.db.SqlHelper;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * db初始化，主要是完成这样的一种逻辑：
 * 检测所有的tradedb，看看User表的autoincrement（主键自增）起始值有没有按照配置文件中的进行设置，如果没有，设置上正确的起始值
 */
public class DbInitUtil {

    @Data
    public static class TableStatus {
        private int Auto_increment;
    }

    /**
     * 检查并给User表做正确的AutoIncrement设置：确保主键不是自增的
     *
     * @LastChanger t.ch
     * @time 2017-11-11 08:34
     */
    public static void init() {
        ArrayList<String> arrayList = DataSourceHolder.TRADE_DBS;
        arrayList.forEach(dbname -> {
            DbSwitcher.useDb(dbname);
            SqlHelper<TableStatus> sqlHelper = DbUtil.getHelper(TableStatus.class);
            BigInteger increment = (BigInteger) sqlHelper.getSimple("SHOW TABLE STATUS LIKE 'User' ", ImmutableMap.of()).get("Auto_increment");
            if (increment != null) {
                LogUtil.err("发现自增设置不正确, 数据库 : " + dbname + ", 开始初始化设置");
                try {
                    sqlHelper.doSql("ALTER TABLE `User` modify column id int", ImmutableMap.of());
                } catch (RuntimeException e) {
                    if (!"更新失败".equals(e.getMessage())) {
                        LogUtil.err("初始化主键属性为非自增失败：" + e);
                    }
                }
            }
        });
    }
}
