package com.edwin.curing.ibatis;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

import com.edwin.curing.dialect.Dialect;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * 扩展ibatis executor实现物理分页（ibatis是逻辑分页，性能太差）
 * 
 * @author jinming.wu
 * @date 2014-8-22
 */
public class LimitSqlExecutor extends SqlExecutor {

    /** 数据库方言 */
    @Setter
    @Getter
    private Dialect dialect;

    /** 是否启用limit */
    @Setter
    @Getter
    private boolean enableLimit = true;

    @Override
    public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters,
                             int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {

        if (supportsLimit() && (skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS)) {

            sql = dialect.generateLimitSql(sql, skipResults + 1, maxResults);

            // Ibatis不需要继续逻辑分页了
            skipResults = NO_SKIPPED_RESULTS;
            maxResults = NO_MAXIMUM_RESULTS;
        }

        super.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
    }

    public boolean supportsLimit() {

        if (enableLimit && dialect != null) {
            return dialect.supportsLimit();
        }

        return false;
    }

}
