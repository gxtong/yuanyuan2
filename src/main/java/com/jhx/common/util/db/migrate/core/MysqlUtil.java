package com.jhx.common.util.db.migrate.core;

import com.jhx.common.util.LogUtil;
import com.jhx.common.util.db.migrate.function.BatchParamHandler;
import com.jhx.common.util.db.migrate.function.ResultSetHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建连接, 执行sql语句等
 *
 * @author t.ch
 * @date 2018-03-30 10:19
 */
public class MysqlUtil implements AutoCloseable{
    private String url;
    private String username;
    private String password; // 密码

    private Connection connection; // 连接

    /**
     * 创建个执行sql的工具类
     *
     * @param url      连接串
     * @param username 用户名
     * @param password 密码
     * @param autoCreateDatabase   是否自动创建数据库, 连接串里必须有数据库名, true: 如果数据库不存在会自动创建
     * @return MysqlUtil
     */
    public static MysqlUtil createUtil(String url, String username, String password, boolean autoCreateDatabase) {
        MysqlUtil util = new MysqlUtil();
        util.url = url;
        util.username = username;
        util.password = password;
        if (autoCreateDatabase) {
            util.createDatabase();
        }
        return util;
    }

    /**
     * 查询单个
     * @param sql SQL语句
     * @param handler ResultSet处理
     * @param args SQL语句的参数
     * @param <R> 返回类型
     * @return <R>
     * @see ResultSetHandler
     */
    public <R> R queryForObject(String sql, ResultSetHandler<R> handler, Object... args) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = getStatement(sql, args);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return handler.accept(resultSet);
            }
            return null;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 查询列表
     * @param sql SQL语句
     * @param handler ResultSet处理
     * @param args SQL语句的参数
     * @param <R> 返回类型
     * @return ArrayList&lt;R&gt;
     * @see ResultSetHandler
     */
    public <R> ArrayList<R> queryForList(String sql, ResultSetHandler<R> handler, Object... args) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = getStatement(sql, args);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<R> resultList = new ArrayList<>();
            while (resultSet.next()) {
                resultList.add(handler.accept(resultSet));
            }
            return resultList;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    /**
     * 执行SQL语句
     * @param sql SQL语句
     * @param args SQL语句的参数
     */
    public void execute(String sql, Object... args) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = getStatement(sql, args);
            statement.execute();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }


    /**
     * 批量执行SQL
     * @param sql SQL语句
     * @param list 参数
     * @param handler 参数处理
     * @see BatchParamHandler
     */
    public <T> void executeBatch(String sql, List<T> list, BatchParamHandler<T> handler) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            if (list != null && list.size() > 0) {
                for (T t : list) {
                    handler.accept(statement, t);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                LogUtil.err(MysqlUtil.class, e);
            }
        }
    }

    /**
     * 获取连接
     */
    private Connection getConnection() throws SQLException {
        if (this.connection == null) {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        }
        return this.connection;
    }

    /**
     * 获取PreparedStatement
     *
     * @param sql  要执行的sql
     * @param args 参数
     * @return PreparedStatement
     */
    private PreparedStatement getStatement(String sql, Object[] args) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(sql);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
        }
        return statement;
    }

    /**
     * 创建数据库
     */
    private void createDatabase() {
        Matcher matcher = Pattern.compile("jdbc:mysql://[^/]+/([^?]+).*").matcher(this.url);
        String dbName = "";
        if (matcher.find()) {
            dbName = matcher.group(1);
        }
        String url = this.url.replace("/" + dbName, "");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(url, this.username, this.password);
            preparedStatement = connection.prepareStatement(String.format("CREATE DATABASE IF NOT EXISTS %s DEFAULT CHARSET utf8 COLLATE utf8_bin", dbName));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LogUtil.err(MysqlUtil.class, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LogUtil.err(MysqlUtil.class, e);
            }
        }
    }
}
