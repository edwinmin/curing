package com.edwin.curing.enums;

/**
 * Dao操作类型
 * 
 * @author jinming.wu
 * @date 2014-8-18
 */
public enum DAOActionType {

    /** 加载一条 */
    LOAD,

    /** 批量查询 */
    QUERY,

    /** limit查询 */
    LIMIT,

    /** 分页查询 */
    PAGE,

    /** 插入 */
    INSERT,

    /** 更新 */
    UPDATE,

    /** 删除 */
    DELETE
}
