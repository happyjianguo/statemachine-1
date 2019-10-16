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
 * @param <S1>
 * @param <S2>
 * @param <E>
 */
public class StateMachine3d<T, S1 extends Enum<S1>, S2 extends Enum<S2>, S3 extends Enum<S3>, E extends Enum<E>, C>
        extends
        AbstractStateMachine<T, StateMachine3d.StateTriple<S1, S2, S3>, E, C> {

    /**
     * 构造函数
     * @param getState
     * @param setState
     * @param supplier
     */
    public StateMachine3d(Function<T, StateTriple<S1, S2, S3>> getState,
            BiConsumer<T, StateTriple<S1, S2, S3>> setState,
            StatemachineExceptionSupplier<T, StateTriple<S1, S2, S3>, E, ? extends RuntimeException> supplier) {
        super(getState, setState, supplier);
    }

    public static class StateTriple<S1 extends Enum<S1>, S2 extends Enum<S2>, S3 extends Enum<S3>>
            implements StateContainer<StateTriple<S1, S2, S3>> {
        private S1 s1;
        private S2 s2;
        private S3 s3;

        public StateTriple(S1 s1, S2 s2, S3 s3) {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
        }

        public S1 getS1() {
            return s1;
        }

        public S2 getS2() {
            return s2;
        }

        public S3 getS3() {
            return s3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StateTriple<?, ?, ?> that = (StateTriple<?, ?, ?>) o;
            return Objects.equals(s1, that.s1) && Objects.equals(s2, that.s2)
                    && Objects.equals(s3, that.s3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s1, s2, s3);
        }

        @Override
        public boolean match(StateTriple<S1, S2, S3> triple) {
            return (triple.s1 == null || this.s1 == null || Objects
                    .equals(this.s1, triple.s1)) && (triple.s2 == null
                    || this.s2 == null || Objects.equals(this.s2, triple.s2))
                    && (triple.s3 == null || this.s3 == null || Objects
                    .equals(this.s3, triple.s3));
        }

        @Override
        public boolean isNonNull() {
            return this.s1 != null || this.s2 != null || this.s3 != null;
        }

        @Override
        public boolean isSameFormat(StateTriple<S1, S2, S3> triple) {
            if (this.s1 == null && triple.s1 != null) {
                return false;
            }
            if (this.s1 != null && triple.s1 == null) {
                return false;
            }
            if (this.s2 == null && triple.s2 != null) {
                return false;
            }
            if (this.s2 != null && triple.s2 == null) {
                return false;
            }
            if (this.s3 == null && triple.s3 != null) {
                return false;
            }
            if (this.s3 != null && triple.s3 == null) {
                return false;
            }
            return true;
        }

        @Override
        public boolean nonContainNull() {
            return this.s1 != null && this.s2 != null && this.s3 != null;
        }
    }
}
