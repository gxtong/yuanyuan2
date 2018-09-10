package com.jhx.common.util.dist.trade;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * author 钱智慧
 * date 2017/11/19 下午8:47
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceName();
    }
}
