package com.edwin.curing.ibatis;

import lombok.Setter;

import com.edwin.curing.dialect.Dialect;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;

/**
 * @author jinming.wu
 * @date 2014-8-29
 */
public class LimitSqlMapClientFactoryBean extends IbatisSqlMapClientFactoryBean {

    @Setter
    private SqlExecutor sqlExecutor = new LimitSqlExecutor();

    @Setter
    private Dialect     dialect;

    @Override
    protected SqlExecutor createSqlExecutor() {
        
        return this.sqlExecutor;
    }
}
