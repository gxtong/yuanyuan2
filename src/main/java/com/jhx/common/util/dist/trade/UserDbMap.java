package com.jhx.common.util.dist.trade;

import com.google.common.collect.ImmutableMap;
import com.jhx.common.util.db.DbUtil;
import com.jhx.common.util.db.SqlHelper;
import com.jhx.common.util.dist.trade.dto.UserIdNameModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author 钱智慧
 * date 2017/11/29 19:05
 */
public class UserDbMap {


    private static Map<Integer, String> idDbMap = new HashMap<>();
    private static Map<String, String> nameDbMap = new HashMap<>();

    public static void addUser(int userId, String userName, String tradeDbName) {
        idDbMap.put(userId, tradeDbName);
        nameDbMap.put(userName, tradeDbName);
    }

    public static boolean contains(String userName) {
        return nameDbMap.containsKey(userName);
    }

    public static boolean contains(int userId) {
        return idDbMap.containsKey(userId);
    }

    public static void init() {
        DataSourceHolder.TRADE_DBS.forEach(dbName -> {
            DbSwitcher.useDb(dbName);
            SqlHelper<UserIdNameModel> helper = DbUtil.getHelper(UserIdNameModel.class);
            List<UserIdNameModel> list = helper.list("select id,userName from User", null);
            list.forEach(item -> {
                idDbMap.put(item.getId(), dbName);
                nameDbMap.put(item.getUserName(), dbName);
            });
        });
    }

    public static boolean isSameDb(int userId1, int userId2) {
        return idDbMap.containsKey(userId1) && idDbMap.containsKey(userId2) && idDbMap.get(userId1).equals(idDbMap.get(userId2));
    }

    public static String getDb(int userId) {
        String ret=idDbMap.get(userId);
        if(ret==null){
            reloadDb(userId);
        }
        return idDbMap.get(userId);
    }

    public static String getDb(String userName) {
        String ret= nameDbMap.get(userName);
        if(ret==null){
            reloadDb(userName);
        }
        return nameDbMap.get(userName);
    }

    private static void reloadDb(int userId) {
        for (String dbName : DataSourceHolder.TRADE_DBS) {
            DbSwitcher.useDb(dbName);
            SqlHelper<UserIdNameModel> helper = DbUtil.getHelper(UserIdNameModel.class);
            UserIdNameModel user = helper.get("select id,userName from User where id=:id", ImmutableMap.of("id", userId));
            if (user != null) {
                UserDbMap.addUser(user.getId(), user.getUserName(), dbName);
                break;
            }
        }
    }

    private static void reloadDb(String userName) {
        for (String dbName : DataSourceHolder.TRADE_DBS) {
            DbSwitcher.useDb(dbName);
            SqlHelper<UserIdNameModel> helper = DbUtil.getHelper(UserIdNameModel.class);
            UserIdNameModel user = helper.get("select id,userName from User where userName=:userName", ImmutableMap.of("userName", userName));
            if (user != null) {
                UserDbMap.addUser(user.getId(), user.getUserName(), dbName);
                break;
            }
        }
    }
}
