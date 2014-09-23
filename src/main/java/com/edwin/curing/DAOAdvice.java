package com.edwin.curing;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import com.edwin.curing.annotation.DAOAction;
import com.edwin.curing.enums.DAOActionType;
import com.edwin.curing.exception.DAOException;

/**
 * IbatisGenericDaoImpl 功能扩展拦截器
 * 
 * @author jinming.wu
 * @date 2014-8-29
 */
public class DAOAdvice implements IntroductionInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        if (!implementsInterface(invocation.getMethod().getDeclaringClass())) {
            return invocation.proceed();
        }

        Annotation daoAction = invocation.getMethod().getAnnotation(DAOAction.class);

        String methodName = invocation.getMethod().getName();

        if (daoAction == null) {
            throw new DAOException("You must announced the method " + methodName);
        }

        DAOMethod daoMethod = new DAOMethod();

        GenericDao genericDao = (GenericDao) invocation.getThis();

        if (daoMethod.getActionType() == DAOActionType.LOAD) {
            return genericDao.load(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.QUERY) {
            return genericDao.load(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.LIMIT) {
            return genericDao.limit(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.PAGE) {
            return genericDao.page(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.INSERT) {
            return genericDao.insert(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.UPDATE) {
            return genericDao.update(daoMethod);
        }

        if (daoMethod.getActionType() == DAOActionType.DELETE) {
            return genericDao.delete(daoMethod);
        }

        return invocation.proceed();
    }

    @Override
    public boolean implementsInterface(Class<?> clazz) {
        return clazz.isInterface() && GenericDao.class.isAssignableFrom(clazz);
    }
}
