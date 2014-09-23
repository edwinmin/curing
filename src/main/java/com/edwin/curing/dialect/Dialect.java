package com.edwin.curing.dialect;

/**
 * 数据库方言接口
 * 
 * @author jinming.wu
 * @date 2014-8-22
 */
public interface Dialect {

    /**
     * 是否支持物理limit
     * 
     * @return
     */
    boolean supportsLimit();

    /**
     * 生成limit语句
     * 
     * @param sql
     * @param start
     * @param limit
     * @return
     */
    String generateLimitSql(String sql, int start, int limit);

    /**
     * 是否支持count
     * 
     * @return
     */
    boolean supportsCount();

    /**
     * 生成count语句
     * 
     * @param countSql
     * @param paramValues
     * @return
     */
    String generateCountSql(String countSql, Object[] paramValues);
}
