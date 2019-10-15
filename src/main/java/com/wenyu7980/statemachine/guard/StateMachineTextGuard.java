package com.wenyu7980.statemachine.guard;

import com.wenyu7980.statemachine.guard.textguard.StateMachineValue;

/**
 * 动态语言守卫
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S>
 * @param <E>
 */
public class StateMachineTextGuard<T, S, E extends Enum<E>>
        implements StateMachineGuard<T, S, E> {

    /** 动态语言 */
    private String text;

    /**
     *
     * @param text
     *            以";"结尾的动态语言，例如："test"=="test";
     */
    public StateMachineTextGuard(String text) {
        this.text = text;
    }

    @Override
    public boolean guard(T t, S s, E e, Object value) {
        return (Boolean) StateMachineValue.get(text, t, value);
    }

}
