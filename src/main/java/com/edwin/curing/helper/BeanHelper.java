package com.edwin.curing.helper;

import java.lang.reflect.Field;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * bean工具类
 * 
 * @author jinming.wu
 * @date 2014-8-27
 */
public class BeanHelper {

    public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(propertyName));
        Preconditions.checkNotNull(clazz);

        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {

            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    /**
     * 给object注入属性
     * 
     * @param object
     * @param propertyName
     * @param newValue
     * @throws NoSuchFieldException
     */
    public static void setDeclaredFieldValue(Object object, String propertyName, Object newValue)
                                                                                                 throws NoSuchFieldException {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(propertyName));
        Preconditions.checkNotNull(object);

        Field field = getDeclaredField(object.getClass(), propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            throw new NoSuchFieldException("No such field: " + object.getClass() + '.' + propertyName);
        } finally {
            field.setAccessible(accessible);
        }
    }
}
