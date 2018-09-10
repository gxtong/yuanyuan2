
package com.jhx.common.util.dist.trade;

import com.jhx.common.util.AppPropUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 切换数据源用的，保存的数据源名
 *
 * @author t.ch
 * @time 2017-08-17 21:11:34
 * @description Description for this class
 */
public class DataSourceHolder {


    private final static ThreadLocal<String> DATASOURCE = new ThreadLocal<>();
    public static ArrayList<String> TRADE_DBS = new ArrayList<>();

    /**
     * 初始化,把配置添进去
     * 会从dataSourceNames中去掉cfgdb
     */
    public static void init() {
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            String db = "tradedb" + i;
            if (AppPropUtil.get("dbconfig." + db + ".url") != null) {
                TRADE_DBS.add(db);
            } else {
                break;
            }
        }
    }

    /**
     * 设置随机的交易数据源
     *
     * @return 数据源名称
     */
    public static String setRndTradeDataSourceName() {
        int index = RandomUtils.nextInt(TRADE_DBS.size());
        String dbName = TRADE_DBS.get(index);
        setDataSourceName(dbName);
        return dbName;
    }

    /**
     * 设置数据源名
     *
     * @param dataSourceName
     */
    public static void setDataSourceName(String dataSourceName) {
        if (!"sumdb".equals(dataSourceName) && !"cfgdb".equals(dataSourceName) && !TRADE_DBS.contains(dataSourceName)) {
            throw new RuntimeException("不存在的数据库");
        }
        clearDataSource();
        DATASOURCE.set(dataSourceName);
    }

    /**
     * 根据userId设置数据源名
     */
    public static void setTradeDataSourceName(int userId) {
        String db = UserDbMap.getDb(userId);
        setDataSourceName(db);
    }

    public static void setTradeDataSourceName(String userName) {
        String db = UserDbMap.getDb(userName);
        setDataSourceName(db);
    }


    public static void setSumDataSourceName() {
        setDataSourceName("sumdb");
    }

    /**
     * 将数据源设置为cfgdb
     */
    public static void setCfgDataSourceName() {
        setDataSourceName("cfgdb");
    }

    /**
     * 获取数据源，若没有设置则默认为cfgdb
     */
    public static String getDataSourceName() {
        if (StringUtils.isBlank(DATASOURCE.get())) {
            setCfgDataSourceName();
        }
        return DATASOURCE.get();
    }

    /**
     * 清空
     */
    public static void clearDataSource() {
        DATASOURCE.remove();
    }

    /**
     * desc 将一系列客户按db分组
     * author 钱智慧
     * date 2017/12/21 16:57
     *
     * @return key是dbname, value是userId data
     **/
    public static Map<String, List<Integer>> groupByDb(Collection<Integer> userIdList) {
        Map<String, List<Integer>> ret = new HashMap<>();

        for (Integer userId : userIdList) {
            String db = UserDbMap.getDb(userId);
            List<Integer> list = ret.computeIfAbsent(db, k -> new ArrayList<>());
            list.add(userId);
        }

        return ret;
    }


}
