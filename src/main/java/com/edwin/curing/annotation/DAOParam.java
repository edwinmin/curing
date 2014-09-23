package com.edwin.curing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * dao查询参数
 * 
 * @author jinming.wu
 * @date 2014-8-18
 */
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DAOParam {

    /**
     * 参数名称
     * 
     * @return
     */
    String value() default "";
}
