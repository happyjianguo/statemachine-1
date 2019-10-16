package com.wenyu7980.statemachine;

/**
 * Copyright 2019 WenYu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.wenyu7980.statemachine.exception.StatemachineExceptionSupplier;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 状态机
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S>
 * @param <E>
 */
public class StateMachine<T, S extends Enum<S>, E extends Enum<E>, C>
        extends AbstractStateMachine<T, StateMachine.StateSingle<S>, E, C> {

    /**
     * 构造函数
     * @param getState
     * @param setState
     * @param supplier
     */
    public StateMachine(Function<T, StateSingle<S>> getState,
            BiConsumer<T, StateSingle<S>> setState,
            StatemachineExceptionSupplier<T, StateSingle<S>, E, ? extends RuntimeException> supplier) {
        super(getState, setState, supplier);
    }

    public static class StateSingle<S extends Enum<S>>
            implements StateContainer<StateSingle<S>> {
        private S s;

        public StateSingle(S s) {
            this.s = s;
        }

        public S getS() {
            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StateSingle<?> statePair = (StateSingle<?>) o;
            return Objects.equals(s, statePair.s);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s);
        }

        @Override
        public boolean match(StateSingle<S> single) {
            return Objects.equals(single.s, this.s);
        }

        @Override
        public boolean isNonNull() {
            return this.s != null;
        }

        @Override
        public boolean isSameFormat(StateSingle<S> single) {
            if (this.s == null && single.s != null) {
                return false;
            }
            if (this.s != null && single.s == null) {
                return false;
            }
            return true;
        }

        @Override
        public boolean nonContainNull() {
            return this.s != null;
        }
    }
}
