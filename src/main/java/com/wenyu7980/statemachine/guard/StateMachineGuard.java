package com.wenyu7980.statemachine.guard;

/**
 * 守卫接口
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S>
 * @param <E>
 */
public interface StateMachineGuard<T, S, E> {
    /**
     * 守卫
     *
     * @param t
     * @param s
     * @param e
     * @param value
     * @return
     */
    boolean guard(T t, S s, E e, Object value);
}
