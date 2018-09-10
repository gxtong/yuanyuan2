package com.jhx.common.util.db.migrate.function;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 批量执行SQL的参数添加
 *
 * @author t.ch
 * @date 2018-03-30 09:41
 */
@FunctionalInterface
public interface BatchParamHandler<T> {
    void accept(PreparedStatement ps, T t) throws SQLException;
}
