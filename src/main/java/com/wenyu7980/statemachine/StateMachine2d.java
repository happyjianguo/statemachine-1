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
import com.wenyu7980.statemachine.util.MatchUtil;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 状态机
 *
 * @author wenyu
 *
 * @param <T>
 * @param <S1>
 * @param <S2>
 * @param <E>
 */
public class StateMachine2d<T, S1, S2, E, C> extends
        AbstractStateMachine<T, StateMachine2d.StatePair<S1, S2>, E, C> {
    /**
     * 构造函数
     * @param getState
     * @param setState
     * @param supplier
     */
    public StateMachine2d(Function<T, StatePair<S1, S2>> getState,
            BiConsumer<T, StatePair<S1, S2>> setState,
            StatemachineExceptionSupplier<T, StatePair<S1, S2>, E, ? extends RuntimeException> supplier) {
        super(getState, setState, supplier);
    }

    public static class StatePair<S1, S2>
            implements StateContainer<StatePair<S1, S2>> {
        private S1 s1;
        private S2 s2;

        public StatePair(S1 s1, S2 s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        public S1 getS1() {
            return s1;
        }

        public S2 getS2() {
            return s2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StatePair<?, ?> statePair = (StatePair<?, ?>) o;
            return Objects.equals(s1, statePair.s1) && Objects
                    .equals(s2, statePair.s2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s1, s2);
        }

        @Override
        public boolean match(StatePair<S1, S2> pair) {
            return MatchUtil.matchOrNull(pair.s1, this.s1) && MatchUtil
                    .matchOrNull(this.s2, pair.s2);
        }

        @Override
        public String toString() {
            return "{" + "s1=" + s1 + ", s2=" + s2 + '}';
        }
    }
}
