package com.edwin.curing.dialect.impl;

import com.edwin.curing.dialect.Dialect;

/**
 * SQL Server方言
 * 
 * @author jinming.wu
 * @date 2014-8-22
 */
public class SQLServerDialect implements Dialect {

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String generateLimitSql(String sql, int start, int limit) {
        return null;
    }

    @Override
    public boolean supportsCount() {
        return false;
    }

    @Override
    public String generateCountSql(String countSql, Object[] paramValues) {
        return null;
    }
}
