package com.wenyu7980.statemachine;

/**
 * Copyright wenyu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
public class StateMachine3d<T, S1, S2, S3, E, C> extends
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

    public static class StateTriple<S1, S2, S3>
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
            return MatchUtil.matchOrNull(this.s1, triple.s1) && MatchUtil
                    .matchOrNull(this.s2, triple.s2) && MatchUtil
                    .matchOrNull(this.s3, triple.s3);
        }

        @Override
        public String toString() {
            return "{" + "s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + '}';
        }
    }
}
