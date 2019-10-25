package com.wenyu7980.statemachine;

/**
 *
 * @author:wenyu
 * @date:2019/10/16
 */
public interface StateContainer<T> {
    /**
     * 匹配
     * @param t
     * @return
     */
    boolean match(T t);
}
