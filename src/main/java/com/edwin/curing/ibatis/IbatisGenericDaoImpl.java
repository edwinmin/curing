package com.edwin.curing.ibatis;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.ReflectionUtils;

import com.edwin.curing.DAOMethod;
import com.edwin.curing.GenericDao;
import com.edwin.curing.PageModel;
import com.edwin.curing.constant.IbatisConstant;
import com.edwin.curing.dialect.Dialect;
import com.edwin.curing.exception.DAOException;
import com.edwin.curing.helper.SqlHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * 通用dao的ibatis实现
 * 
 * @author jinming.wu
 * @date 2014-8-21
 */
public class IbatisGenericDaoImpl extends SqlMapClientDaoSupport implements GenericDao {

    @Setter
    private String              namespace;

    private Map<String, String> countSqlCache = Maps.newConcurrentMap();

    public IbatisGenericDaoImpl(String namespace) {
        this.namespace = namespace;
    }

    @SuppressWarnings("unchecked")
    public List<Object> query(DAOMethod daoMethod) throws DataAccessException {

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().queryForList(statementName, daoMethod.getParams());
    }

    public PageModel page(DAOMethod daoMethod) {

        PageModel pageModel = new PageModel();

        int recordCount = queryTotalCount(daoMethod);

        if (recordCount > 0) {

            Map<String, Object> daoParams = daoMethod.getParams();

            Integer max = (Integer) daoParams.get(IbatisConstant.PAGE_MAX);
            Integer pageNo = (Integer) daoParams.get(IbatisConstant.PAGE_NO);
            Integer skip = (Integer) daoParams.get(IbatisConstant.PAGE_SKIP);

            if (max == null) {
                throw new DAOException("The max parameter must be set.");
            }

            if (skip == null) {
                if (pageNo != null) {
                    pageModel.setPage(pageNo);
                    int pageIndex = pageNo - 1;
                    skip = pageIndex >= 0 ? pageIndex * max : 0;
                    daoParams.put(IbatisConstant.PAGE_SKIP, skip);
                } else {
                    throw new DAOException("The page parameter must be set.");
                }
            }

            // setting PageModel
            pageModel.setRecordCount(recordCount);
            pageModel.setRecords((List<?>) limit(daoMethod));
            pageModel.setPageSize(max);
        }

        return pageModel;
    }

    @Override
    public Object limit(DAOMethod daoMethod) {

        Map<String, Object> daoParams = daoMethod.getParams();
        Integer skip = (Integer) daoParams.get(IbatisConstant.PAGE_SKIP);
        Integer max = (Integer) daoParams.get(IbatisConstant.PAGE_MAX);

        if (skip == null && max == null) {
            throw new DAOException("Must define skip or max parameter as least.");
        }

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().queryForList(statementName, daoMethod.getParams(),
                                                      skip != null ? skip : SqlExecutor.NO_SKIPPED_RESULTS,
                                                      max != null ? max : SqlExecutor.NO_MAXIMUM_RESULTS);
    }

    public Object load(DAOMethod daoMethod) throws DataAccessException {

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().queryForObject(statementName, daoMethod.getParams());
    }

    public Object insert(DAOMethod daoMethod) throws DataAccessException {

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().insert(statementName, daoMethod.getParams());
    }

    public int update(DAOMethod daoMethod) throws DataAccessException {

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().update(statementName, daoMethod.getParams());
    }

    public int delete(DAOMethod daoMethod) throws DataAccessException {

        String statementName = getStatementName(daoMethod);

        return getSqlMapClientTemplate().delete(statementName, daoMethod.getParams());
    }

    /**
     * 分页查询数据总数
     * 
     * @param daoMethod
     * @return
     */
    private int queryTotalCount(DAOMethod daoMethod) {

        String statementName = getStatementName(daoMethod) + IbatisConstant.DEFAULT_SUFFIX;

        // 获取分页查询的count sql
        MappedStatement statment = ((SqlMapClientImpl) getSqlMapClient()).getMappedStatement(statementName);

        if (statment != null) {

            Object result = getSqlMapClientTemplate().queryForObject(statementName, daoMethod.getParams());

            return result == null ? 0 : (Integer) result;
        }

        // 目前不支持自动生成count sql，只是个简单框架
        return autoCalTotalCount(daoMethod);
    }

    /**
     * 截取SQL计算数据总数
     * 
     * @param daoMethod
     * @return
     */
    private int autoCalTotalCount(DAOMethod daoMethod) {

        String statementName = getStatementName(daoMethod);

        String countSql = countSqlCache.get(statementName);

        // 执行期动态获取sql语句
        MappedStatement statement = ((SqlMapClientImpl) getSqlMapClient()).getMappedStatement(statementName);
        Sql sql = statement.getSql();

        SessionScope sessionScope = new SessionScope();
        sessionScope.setSqlMapClient(getSqlMapClient());
        sessionScope.setSqlMapExecutor(getSqlMapClient());
        sessionScope.setSqlMapTxMgr(getSqlMapClient());

        StatementScope statementScope = new StatementScope(sessionScope);
        statementScope.setStatement(statement);

        if (Strings.isNullOrEmpty(countSql)) {

            String sqlText = sql.getSql(statementScope, daoMethod.getParams());

            // 组装count sql
            countSql = SqlHelper.gernerateCountSql(sqlText);
            countSqlCache.put(statementName, countSql);
        }

        ParameterMap paramMap = sql.getParameterMap(statementScope, daoMethod.getParams());
        Object[] paramValues = paramMap.getParameterObjectValues(statementScope, daoMethod.getParams());

        SqlMapClient sqlMapClient = this.getSqlMapClient();

        if (sqlMapClient instanceof SqlMapClientImpl) {

            SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sqlMapClient).getDelegate();

            Field field = ReflectionUtils.findField(SqlMapExecutorDelegate.class, "sqlExecutor", SqlExecutor.class);

            Object v = ReflectionUtils.getField(field, delegate);

            if (v instanceof LimitSqlExecutor) {
                Dialect dialect = ((LimitSqlExecutor) v).getDialect();
                if (dialect.supportsCount()) {

                    // 处理该语句
                    dialect.generateCountSql(countSql, paramValues);
                    // processSql();
                }
            }
        }

        return -1;
    }

    /**
     * 定位sql语句
     * 
     * @param daoMethod
     * @return
     */
    private String getStatementName(DAOMethod daoMethod) {
        return namespace + "." + daoMethod.getName();
    }
}
