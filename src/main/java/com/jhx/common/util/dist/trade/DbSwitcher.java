package com.jhx.common.util.dist.trade;

/**
* @author 钱智慧
* @date 2017年9月2日 上午10:57:05
*/
public class DbSwitcher {
	
	/**
	 * 设置随机的交易数据源
	 * @return 数据源名称
	 */
	public static String useRndTradeDb() {
		String dbName = DataSourceHolder.setRndTradeDataSourceName();
		return dbName;
	}

	public static void useTradeDb(){
		DataSourceHolder.setDataSourceName(DataSourceHolder.TRADE_DBS.get(0));
	}

	public static void useTradeDb(int userId) {
		DataSourceHolder.setTradeDataSourceName(userId);
	}

	public static void useTradeDb(String userName){
		DataSourceHolder.setTradeDataSourceName(userName);
	}

	/**
	 * 判断两个user是否在同一个tradedb中
	 * @param userId1
	 * @param userId2
	 * @return
	 */
	public static boolean isSameDb(int userId1,int userId2){
		return UserDbMap.isSameDb(userId1,userId2);
	}

	/**
	 * 遍历所有数据库时会用到
	 * @param dataSourceName 示例：tradedb0
	 */
	public static void useDb(String dataSourceName) {
		DataSourceHolder.setDataSourceName(dataSourceName);
	}

	/**
	 * 将数据源设置为cfgdb
	 */
	public static void useCfgDb() {
		DataSourceHolder.setCfgDataSourceName();
	}
	
	/**
	 * 将数据源设置为sumdb
	 */
	public static void useSumDb() {
		DataSourceHolder.setSumDataSourceName();
	}
}
