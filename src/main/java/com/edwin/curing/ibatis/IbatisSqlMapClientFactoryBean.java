package com.edwin.curing.ibatis;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import lombok.Setter;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.util.Assert;

import com.edwin.curing.helper.BeanHelper;
import com.google.common.collect.Lists;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

/**
 * Ibatis扩展工厂类（注入LimitSqlExecutor）
 * 
 * @author jinming.wu
 * @date 2014-8-27
 */
public abstract class IbatisSqlMapClientFactoryBean extends SqlMapClientFactoryBean {

    @Override
    protected SqlMapClient buildSqlMapClient(Resource[] configLocations, Resource[] mappingLocations,
                                             Properties properties) throws IOException {

        SqlMapClient sqlMapClient = super.buildSqlMapClient(configLocations, mappingLocations, properties);

        Assert.isInstanceOf(SqlMapClientImpl.class, sqlMapClient,
                            "SqlMapClient is not type of SqlMapClientImpl, SqlMapClientImpl interface may be removed!");

        SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sqlMapClient).getDelegate();

        try {
            BeanHelper.setDeclaredFieldValue(delegate, "sqlExecutor", createSqlExecutor());
        } catch (NoSuchFieldException e) {
            throw new IOException(e);
        }
        return sqlMapClient;
    }

    /**
     * 可对sqlExecutor代理，统计SQL执行性能 
     * ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
     * proxyFactoryBean.setTarget(new SqlExecutor()); 
     * 强制cgilb代理 proxyFactoryBean.setProxyTargetClass(true);
     * 
     * @return
     */
    protected abstract SqlExecutor createSqlExecutor();

}
