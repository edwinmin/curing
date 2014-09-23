package com.edwin.curing.dialect.impl;

import com.edwin.curing.dialect.Dialect;
import com.google.common.base.Preconditions;

/**
 * mysql方言（limit语句）
 * 
 * @author jinming.wu
 * @date 2014-8-22
 */
public class MySqlDialect implements Dialect {

    private static final String SQL_END_DELIMITER = ";";

    private static final int    SQL_LENGTH        = 20;

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String generateLimitSql(String sql, int start, int limit) {

        Preconditions.checkArgument(sql != null && !sql.trim().isEmpty(), "Sql is illegal");

        sql = sql.trim();
        if (sql.endsWith(SQL_END_DELIMITER)) {
            sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
        }

        StringBuffer sb = new StringBuffer(sql.length() + SQL_LENGTH);
        sb.append(sql);

        int skip = start - 1;
        if (skip > 0) {
            sb.append(" LIMIT ").append(skip).append(',').append(limit);
        } else {
            sb.append(" LIMIT ").append(limit);
        }

        sb.append(SQL_END_DELIMITER);

        return sql;
    }

    @Override
    public boolean supportsCount() {
        return true;
    }

    @Override
    public String generateCountSql(String countSql, Object[] paramValues) {
        return null;
    }

}
