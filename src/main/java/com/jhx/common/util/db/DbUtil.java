package com.jhx.common.util.db;


import com.jhx.common.util.LogUtil;
import com.jhx.common.util.dto.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Supplier;

/**
 * SqlHelper 工厂类，用于创建具体的SqlHelper
 *
 * @author 钱智慧
 * @date 2017年9月2日 下午3:46:31
 */
public class DbUtil {

    private DbUtil() {
    }

    private static JdbcTemplate jt;

    public static void setJt(JdbcTemplate jdbcTemplate) {
        jt = jdbcTemplate;
    }

    /**
     * 获取一个MySqlHelper，针对MySql数据库使用
     *
     * @param clazz 实体class
     * @return
     * @author 钱智慧
     * @date 2017年9月2日 下午3:48:53
     */
    public static <T> SqlHelper<T> getHelper(Class<T> clazz) {
        return new MySqlHelper<T>(clazz, jt);
    }

    /**
     * 获取一个OracleSqlHelper，针对Oracle数据库使用（未实现，日后有需要再说）
     *
     * @param clazz
     * @return
     * @author 钱智慧
     * @date 2017年9月2日 下午3:48:53
     */
    public static <T> SqlHelper<T> getOracleHelper(Class<T> clazz) {
        return null;
    }


    /**
     * 处理事务, 发生异常会回滚事务 <br/>
     * 示例：
     * <pre>
     *  Result result  = transaction(()->{
     *      // 数据库操作
     *      ....
     *
     *      // 返回false示例
     *      if( ... ){
     *          return new Result().setOk(false).setMsg("条件不符合");
     *      }
     *
     *      return new Result().setOk(true);
     *  });
     *
     * </pre>
     *
     * @param function 数据库操作
     * @return Result 未发生异常 {@code isOk = true} , 发生异常 {@code isOk = false}
     */
    public static Result transaction(PlatformTransactionManager transactionManager, Supplier<Result> function) {
        TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
        Result ret = new Result();
        try {
            ret = function.get();
            if (!ret.isOk()) {
                transactionManager.rollback(ts);
            } else {
                transactionManager.commit(ts);
            }
        } catch (Throwable e) {
            transactionManager.rollback(ts);
            ret.setOk(false).setMsg(DbConstant.DbFailMsg);
            LogUtil.err(DbUtil.class, e);
            throw new RuntimeException(e);
        }
        return ret;
    }
}