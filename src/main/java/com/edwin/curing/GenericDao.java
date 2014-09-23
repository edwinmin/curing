package com.edwin.curing;

import java.util.List;

/**
 * 通用DAO接口
 * 
 * @author jinming.wu
 * @date 2014-8-21
 */
public interface GenericDao {

    /**
     * 批量查询
     * 
     * @param daoMethod
     * @return
     */
    List<Object> query(DAOMethod daoMethod);

    /**
     * 分页查询
     * 
     * @param daoMethod
     * @return
     */
    PageModel page(DAOMethod daoMethod);

    /**
     * 单条加载
     * 
     * @param daoMethod
     * @return
     */
    Object load(DAOMethod daoMethod);

    /**
     * limit 查询
     * 
     * @param daoMethod
     * @return
     */
    Object limit(DAOMethod daoMethod);

    /**
     * 新增
     * 
     * @param daoMethod
     * @return
     */
    Object insert(DAOMethod daoMethod);

    /**
     * 更新
     * 
     * @param daoMethod
     * @return
     */
    int update(DAOMethod daoMethod);

    /**
     * 删除
     * 
     * @param daoMethod
     * @return
     */
    int delete(DAOMethod daoMethod);
}
