package com.jhx.common.util.dist.trade;

import com.alibaba.druid.pool.DruidDataSource;
import com.jhx.common.util.AppPropUtil;
import com.jhx.common.util.db.DbUtil;
import com.jhx.common.util.db.migrate.DbMigrateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * date 2017/11/19 下午8:40
 */
@Configuration
public class DataSourceConfig {


    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void init(){
        BeanContext.setApplicationContext(context);
        DbUtil.setJt(jdbcTemplate());
        UserDbMap.init();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DynamicDataSource dataSource(){
        DataSourceHolder.init();
        String dbCfg = "dbconfig";
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();


        ArrayList<String> dbList = new ArrayList<String>(DataSourceHolder.TRADE_DBS);
        dbList.add("cfgdb");

        if(StringUtils.isNotBlank(AppPropUtil.get(dbCfg + ".sumdb.url"))) {
            dbList.add("sumdb");
        }

        for (String db : dbList) {

            String url = AppPropUtil.get(dbCfg + "." + db + ".url");
            String user = AppPropUtil.get(dbCfg + "." + db + ".user");
            String password = AppPropUtil.get(dbCfg + "." + db + ".password");

            String entity = db.split("db")[0];
            String packageName = String.format("com.jhx.dist.trade.entity.%s.entity",entity);
            DbMigrateUtil.migrate(url, user, password, packageName);

            DataSource ds=DataSourceBuilder.create().type(DruidDataSource.class)
                    .url(url).driverClassName("com.mysql.jdbc.Driver")
                    .username(user).password(password).build();

            targetDataSources.put(db, ds);
        }

        dataSource.setTargetDataSources(targetDataSources);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }


}
