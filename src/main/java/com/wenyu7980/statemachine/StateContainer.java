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

    /**
     * 是否非全空 (断言使用)
     * @return
     */
    default boolean isNonNull() {
        return true;
    }

    /**
     * 格式相同(断言使用)
     * @param t
     * @return
     */
    default boolean isSameFormat(T t) {
        return true;
    }

    /**
     * 不包含null(断言使用)
     * @return
     */
    default boolean nonContainNull() {
        return true;
    }
}
