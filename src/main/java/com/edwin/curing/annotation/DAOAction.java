package com.edwin.curing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.edwin.curing.enums.DAOActionType;

/**
 * dao操作
 * 
 * @author jinming.wu
 * @date 2014-8-18
 */
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DAOAction {

    /**
     * 方法名
     * 
     * @return
     */
    String method() default "";

    /**
     * 操作类型
     * 
     * @return
     */
    DAOActionType action() default DAOActionType.QUERY;
}
