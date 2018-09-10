package com.jhx.common.util.db.migrate.function;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询结果集处理
 *
 * @author t.ch
 * @date 2018-03-30 09:41
 */
@FunctionalInterface
public interface ResultSetHandler<R> {
    R accept(ResultSet rs) throws SQLException;
}
